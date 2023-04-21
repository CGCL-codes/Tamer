package com.javaeedev.test.util;

import java.util.Date;

/**
 * Copyright_2006, Liao Xuefeng
 * Created on 2006-3-3
 */
public class Account {

    private int id;

    private String username;

    private String email;

    private Date updateTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date d) {
        this.updateTime = d;
    }

    public void validate() {
        if (!username.matches("[a-zA-Z0-9_]{6,20}")) {
            throw new IllegalArgumentException("Invalid username");
        }
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid id");
        }
    }
}
