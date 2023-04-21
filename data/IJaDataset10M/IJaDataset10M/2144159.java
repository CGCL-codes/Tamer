package org.griffante.session.entity;

/**
 * @author giuliano
 */
public class User {

    private int uid;

    private String userName;

    private String fullName;

    private String password;

    private String domain;

    public User() {
    }

    public User(int id, String un, String fn, String p, String d) {
        uid = id;
        userName = un;
        fullName = fn;
        password = p;
        domain = d;
    }

    public User(String un, String fn, String p, String d) {
        userName = un;
        fullName = fn;
        password = p;
        domain = d;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String name) {
        this.userName = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String name) {
        this.fullName = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String d) {
        domain = d;
    }

    public String toString() {
        return fullName;
    }
}
