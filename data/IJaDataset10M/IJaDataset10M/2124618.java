package ru.yep.forum.core;

import java.util.Date;

/**
 * Info about user
 * 
 * @author Oleg Orlov
 */
public class User {

    public static final String EMPTY_PASSWD = "";

    private int id;

    private String login;

    /** password's md5 hash */
    private String passwd;

    private String name;

    private String email;

    private Date lastLoginDate;

    public User() {
    }

    public User(int id, String login, String passwd, String name, String email, Date lastLogin) {
        this.id = id;
        this.login = login;
        this.passwd = passwd != null ? passwd : EMPTY_PASSWD;
        this.name = name;
        this.lastLoginDate = lastLogin;
        this.email = email;
    }

    public String toString() {
        return login + " uid=" + id;
    }

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getPasswd() {
        return passwd;
    }

    public String getName() {
        return name;
    }

    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public UserGroup getDefaultGroup() {
        return Users.getDefault().getDefaultGroup(this);
    }
}
