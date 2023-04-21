package edu.nus.iss.ejava.team4.model;

import java.io.Serializable;

public class SessionStore implements Serializable {

    private String userid;

    private String username;

    private String role;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
