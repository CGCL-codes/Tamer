package org.jazzteam.edu.oop.technikumDenis;

public class Director extends Teacher implements Cloneable {

    private int bonusSalary;

    public int getBonusSalary() {
        return bonusSalary;
    }

    public void setBonusSalary(int bonusSalary) {
        this.bonusSalary = bonusSalary;
    }

    public Director(String name, String surName, String sex, int salary, int bonusSalary) {
        super(name, surName, sex, salary);
        this.bonusSalary = bonusSalary;
    }

    @Override
    public String toString() {
        return super.getName() + "   " + super.getSurName() + "      " + super.getSex() + "      " + super.getTabelNomber() + "     " + getSalary() + "         " + bonusSalary;
    }

    @Override
    public Object clone() {
        try {
            Director cloned = (Director) super.clone();
            return cloned;
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    @Override
    public String getDiscription() {
        return "��������= " + super.getSalary() + " " + bonusSalary;
    }

    @Override
    public boolean equals(Object other) {
        if (getClass() == other.getClass()) {
            return true;
        } else return false;
    }
}
