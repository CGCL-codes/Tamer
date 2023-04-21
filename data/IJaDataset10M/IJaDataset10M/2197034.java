package com.easyframe.system.domain;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class User {

    private Integer id;

    private String number;

    private String password;

    private String name;

    private String gender;

    private Integer age;

    private String birthday;

    private String phone;

    private List<Role> roles;

    private List<Message> receivedMessages;

    private List<Message> sentMessages;

    public static User getInstance(ResultSet rs) throws SQLException {
        User user = new User();
        user.id = rs.getInt("ID");
        user.number = rs.getString("NUMBER");
        user.password = rs.getString("PASSWORD");
        user.name = rs.getString("NAME");
        user.gender = rs.getString("GENDER");
        user.age = rs.getInt("AGE");
        user.birthday = rs.getString("BIRTHDAY");
        user.phone = rs.getString("PHONE");
        return user;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public List<Message> getReceivedMessages() {
        return receivedMessages;
    }

    public void setReceivedMessages(List<Message> receivedMessages) {
        this.receivedMessages = receivedMessages;
    }

    public List<Message> getSentMessages() {
        return sentMessages;
    }

    public void setSentMessages(List<Message> sentMessages) {
        this.sentMessages = sentMessages;
    }
}
