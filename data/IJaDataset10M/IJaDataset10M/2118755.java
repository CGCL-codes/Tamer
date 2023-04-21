package com.peterhi.util;

public class Account {

    private String name;

    private String password;

    private int age;

    private boolean happy;

    private String email;

    public Account() {
    }

    public Account(String name, String password, int age, boolean happy, String email) {
        this.name = name;
        this.password = password;
        this.age = age;
        this.happy = happy;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isHappy() {
        return happy;
    }

    public void setHappy(boolean happy) {
        this.happy = happy;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
