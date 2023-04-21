package org.jazzteam.edu.lang.toStringMethod;

class ManagerTS extends EmployeeTS {

    private double bonus;

    public ManagerTS(String n, double s) {
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
    public String toString() {
        return super.toString() + "[bonus = " + bonus + " ]";
    }
}
