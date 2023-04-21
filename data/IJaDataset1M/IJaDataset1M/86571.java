package com.simpletasks.domain;

/**
 * Created by IntelliJ IDEA. User: Mario Arias Date: 30/05/2008 Time: 08:27:36 PM
 */
public class User {

    private int id;

    private String email;

    private String password;

    private String name;

    private String lastName;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public String toString() {
        return "User{" + "email='" + email + '\'' + ", id=" + id + ", password='" + password + '\'' + ", name='" + name + '\'' + ", lastName='" + lastName + '\'' + '}';
    }
}
