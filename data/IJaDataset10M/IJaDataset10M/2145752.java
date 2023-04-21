package net.rmanager.employees;

import net.rmanager.environment.Restaurant.DepartmentType;

public class Assistant extends Person {

    public Assistant(String name, int age, int quality, int power, int monthlyCosts) {
        super(name, age, quality, power, monthlyCosts);
    }

    @Override
    public DepartmentType getDepartment() {
        return DepartmentType.Reefer;
    }
}
