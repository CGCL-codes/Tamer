package net.lshift.jsp.taglib.yatl.demo;

public class TestBean {

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    protected String firstName;

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public String getSurName() {
        return surName;
    }

    protected String surName;

    public TestBean() {
    }

    public TestBean(String firstName, String surName) {
        this.firstName = firstName;
        this.surName = surName;
    }

    public String toString() {
        return super.toString() + ", firstName=" + firstName + ", surName=" + surName;
    }
}
