package org.jazzteam.edu.lang.equalsMethod;

class Manager extends EmployeeEq {

    private double bonus;

    public Manager(String n, double s) {
        super(n, s);
        bonus = 0;
    }

    @Override
    public String getDescription() {
        return "a manager with a salary of " + super.getSalary() + " and bonus of" + bonus;
    }

    public void setBonus(double b) {
        bonus = b;
    }

    @Override
    public double getSalary() {
        double s = super.getSalary();
        return s += bonus;
    }

    @Override
    public boolean equals(Object other) {
        if (!super.equals(other)) {
            return false;
        }
        Manager m = (Manager) other;
        return bonus == m.bonus;
    }
}
