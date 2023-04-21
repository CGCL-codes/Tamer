package org.wicketrad.service.mock;

import java.io.Serializable;

public class TestUser implements Serializable {

    private String username;

    private String firstName;

    private String lastName;

    private String emailAddress;

    private String phone;

    private String mobilePhone;

    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /** default constructor */
    public TestUser() {
    }

    public TestUser(String string, String string2) {
        this.username = string;
    }

    public String getId() {
        return username;
    }

    public String getName() {
        return username;
    }
}
