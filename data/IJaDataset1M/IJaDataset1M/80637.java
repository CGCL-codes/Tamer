package com.fmi.weprom.api;

import java.io.Serializable;
import java.util.List;

public abstract class User implements Serializable {

    protected int id;

    protected String nick;

    protected String password;

    protected String name;

    protected String lastName;

    protected String email;

    protected String info;

    protected String phone;

    /**
   * Note that this persist the newly create User and also creates its corressponding
   * UserWorkspace. 
   */
    public abstract void save() throws WEPROMException;

    public abstract void update();

    public abstract void delete();

    public abstract UserWorkspace getUserWorkspace();

    public abstract List getCommunities();

    public abstract List getProjects();

    public String getNick() {
        return nick;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public int getID() {
        return id;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("User[").append(id);
        sb.append(',').append(nick).append(']');
        return sb.toString();
    }

    public String getInformation() {
        return info;
    }

    public void setInformation(String information) {
        this.info = information;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int hashCode() {
        return nick.hashCode();
    }

    public boolean equals(Object anObject) {
        if (this == anObject) return true;
        if (anObject instanceof User) {
            User u = (User) anObject;
            return u.nick.equals(nick);
        }
        return false;
    }
}
