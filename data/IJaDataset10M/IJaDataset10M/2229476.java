package com.ken.dgking.model;

import com.ken.dgking.common.ValidationManager;

public class User {

    private String userId;

    private String userPwd;

    private String realname;

    private String power;

    private String status;

    public User() {
        super();
    }

    public User(String userName, String password, String name, String power, String st) {
        super();
        this.userId = userName;
        this.userPwd = password;
        this.realname = name;
        this.power = power;
        this.status = status;
    }

    public String getUserName() {
        return userId;
    }

    public void setUserName(String userName) {
        this.userId = userName;
    }

    public String getPassword() {
        return userPwd;
    }

    public void setPassword(String password) {
        this.userPwd = password;
    }

    public String getName() {
        return realname;
    }

    public void setName(String name) {
        this.realname = name;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getOperatorValue(int col) {
        switch(col) {
            case 0:
                return ValidationManager.changeNull(getUserName());
            case 1:
                return ValidationManager.changeNull(getName());
            case 2:
                return ValidationManager.changeNull(getPower());
            default:
                return "";
        }
    }
}
