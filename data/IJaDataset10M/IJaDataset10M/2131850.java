package org.jazzteam.edu.oop.hotelComplex;

public class Human extends MyID {

    private int age;

    private String name;

    private String sex;

    public int getAge() {
        return age;
    }

    public void vozrastPlus() {
        setAge(getAge() + 1);
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void deadPeople() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Human(int id, int age, String name, String sex) {
        super(id);
        this.age = age;
        this.name = name;
        this.sex = sex;
    }
}
