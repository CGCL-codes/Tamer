package com.narirelays.ems.persistence.orm;

/**
 * UserInfo entity. @author MyEclipse Persistence Tools
 */
public class UserInfo implements java.io.Serializable {

    private String userId;

    private Users users;

    private String phone;

    private String EMail;

    private String department;

    private String position;

    private String description;

    /** default constructor */
    public UserInfo() {
    }

    /** minimal constructor */
    public UserInfo(String userId, Users users) {
        this.userId = userId;
        this.users = users;
    }

    /** full constructor */
    public UserInfo(String userId, Users users, String phone, String EMail, String department, String position, String description) {
        this.userId = userId;
        this.users = users;
        this.phone = phone;
        this.EMail = EMail;
        this.department = department;
        this.position = position;
        this.description = description;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Users getUsers() {
        return this.users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEMail() {
        return this.EMail;
    }

    public void setEMail(String EMail) {
        this.EMail = EMail;
    }

    public String getDepartment() {
        return this.department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPosition() {
        return this.position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
