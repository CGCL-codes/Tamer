package com.excilys.sugadroid.beans;

import java.io.Serializable;

/**
 * A bean representing a contact, as given by SugarCRM
 * 
 * @author Pierre-Yves Ricau
 * 
 */
public class ContactBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String firstName;

    private String lastName;

    private String phoneWork;

    private String phoneMobile;

    private String accountName;

    private String accountId;

    private String email1;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getPhoneWork() {
        return phoneWork;
    }

    public void setPhoneWork(String phoneWork) {
        this.phoneWork = phoneWork;
    }

    public String getPhoneMobile() {
        return phoneMobile;
    }

    public void setPhoneMobile(String phoneMobile) {
        this.phoneMobile = phoneMobile;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getEmail1() {
        return email1;
    }

    public void setEmail1(String email1) {
        this.email1 = email1;
    }

    /**
	 * Returns the first name, a space and the last name if available, and if
	 * not, only the firstName or only the lastName (or an empty string if
	 * nothing available)
	 */
    @Override
    public String toString() {
        if (firstName == null && lastName == null) {
            return "";
        }
        if (firstName == null) {
            return lastName;
        }
        if (lastName == null) {
            return firstName;
        }
        return firstName + " " + lastName;
    }
}
