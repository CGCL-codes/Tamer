package com.mobfee.dao.hibernate;

import java.util.Date;

public class UserProfile {

    private long userId;

    private String username;

    private String password;

    private int type;

    private Date regdate;

    private int points;

    public UserProfile() {
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Date getRegdate() {
        return regdate;
    }

    public void setRegdate(Date regdate) {
        this.regdate = regdate;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public com.mobfee.domain.UserProfile createDomainObject() {
        com.mobfee.domain.UserProfile up = new com.mobfee.domain.UserProfile();
        up.setUserId(userId);
        up.setUsername(username);
        up.setPassword(password);
        up.setType(type);
        up.setRegdate(regdate);
        up.setPoints(points);
        return up;
    }
}
