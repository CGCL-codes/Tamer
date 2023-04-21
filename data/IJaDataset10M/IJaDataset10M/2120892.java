package net.sf.balm.common.beanutils;

public class Person {

    private String name;

    private String address;

    private String teacher;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress(int i) {
        return address;
    }

    public void setAddress(int i, String address) {
        this.address = address;
    }

    public String getTeacher(String name) {
        return teacher;
    }

    public void setTeacher(String name, String teacher) {
        this.teacher = teacher;
    }
}
