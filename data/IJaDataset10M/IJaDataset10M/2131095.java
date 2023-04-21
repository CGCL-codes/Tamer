package com.objectwave.persist.bcel.examples;

public class Company {

    protected Employee[] employees;

    protected Employee ceo;

    protected transient int currentHeadCount;

    public Employee getCeo() {
        return ceo;
    }

    public void setCeo(Employee value) {
        ceo = value;
    }

    public Employee[] getEmployees() {
        Object obj = employees;
        return (Employee[]) obj;
    }

    public void setEmployees(Employee[] args) {
        employees = args;
    }

    public Company() {
    }
}
