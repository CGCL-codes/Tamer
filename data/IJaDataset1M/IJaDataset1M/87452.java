package org.jazzteam.edu.oop.technikumDenis;

public class Curator extends Teacher {

    private Group group;

    private int bonusSalary;

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public int getBonusSalary() {
        return bonusSalary;
    }

    public void setBonusSalary(int bonusSalary) {
        this.bonusSalary = bonusSalary;
    }

    public Curator(String name, String surName, String sex, int salary, Group group, int bonusSalary) {
        super(name, surName, sex, salary);
        this.group = group;
        this.bonusSalary = bonusSalary;
    }

    @Override
    public String toString() {
        return super.getName() + "   " + super.getSurName() + "  " + super.getSex() + "   " + super.getTabelNomber() + "    " + super.getSalary() + "    " + group + "   " + bonusSalary;
    }

    @Override
    public String getDiscription() {
        return group + " " + super.getSalary() + " " + bonusSalary + " = �������";
    }
}
