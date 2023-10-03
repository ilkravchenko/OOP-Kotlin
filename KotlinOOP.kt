abstract class Employee(
    var name: String,
    var surname: String,
    var baseSalary: Int,
    var experience: Int
) {
    open val countedSalary: Double
        get() {
            var salary = baseSalary.toDouble()
			
            if (experience > 5) {
                salary = salary * 1.2 + 500.0
            } else if (experience > 2) {
                salary += 200.0
            }

            return salary
        }
}

class Developer(
    name: String,
    surname: String,
    baseSalary: Int,
    experience: Int
) : Employee(name, surname, baseSalary, experience) {
}

class Designer(
    name: String,
    surname: String,
    baseSalary: Int,
    experience: Int,
    effCoef: Double
) : Employee(name, surname, baseSalary, experience) {
    var effCoef = checkCoef(effCoef)
        set(value) {
            field = checkCoef(value)
        }

    private fun checkCoef(coef: Double): Double {
        if (coef <= 1 && coef >= 0) {
            return coef
        } else if (coef > 1) {
            return 1.0
        } else {
            return 0.0
        }
    }

    override val countedSalary: Double
        get() {
            return super.countedSalary * effCoef
        }
}

class Manager(
    name: String,
    surname: String,
    baseSalary: Int,
    experience: Int,
    val team: MutableList<Employee> = mutableListOf()
) : Employee(name, surname, baseSalary, experience) {
    override val countedSalary: Double
        get() {
            var salary = super.countedSalary

            if (team.size > 5) {
                salary += 200.0
                if (team.size > 10) {
                    salary += 100.0
                }
            }

            val developerCount = team.count { it is Developer }
            if (developerCount > team.size / 2) {
                salary *= 1.1
            }
			
            val roundedNumber = String.format("%.2f", salary).toDouble()
            return roundedNumber
        }
}

class Department(
    val managers: MutableList<Manager> = mutableListOf()
) {
    fun giveSalary() {
        for (manager in managers) {
            manager.team.forEach { employee ->
                val salary = employee.countedSalary
                println("${employee.name} ${employee.surname} отримав $salary шекелів")
            }
        }
        
        for (manager in managers) {
            val salary = manager.countedSalary
            println("Менеджер ${manager.name} ${manager.surname} отримав $salary шекелів")
        }
    }
}

fun main() {
    // Створення департаменту, різних працівників, додавання до команд тощо
    val department = Department()
    val manager1 = Manager("John", "Doe", 50000, 5)
    val manager2 = Manager("Alice", "Smith", 60000, 7)
    val developer1 = Developer("Bob", "Johnson", 55000, 3)
    val developer2 = Developer("Eva", "Williams", 60000, 6)
    val designer1 = Designer("Cindy", "Brown", 55000, 4, -1.0)
    val designer2 = Designer("David", "Davis", 52000, 2, 1.2)

    manager1.team.add(developer1)
    manager1.team.add(developer2)
    manager2.team.add(designer1)
    manager2.team.add(designer2)

    department.managers.add(manager1)
    department.managers.add(manager2)

    // Виплата зарплати
    department.giveSalary()
}