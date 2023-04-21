package com.wwwc.index.servlet;

import java.io.*;
import java.util.*;
import com.wwwc.util.web.*;

public class UserProfile implements Serializable {

    private String userName = null;

    private String passWord = null;

    private int accessLevel = 0;

    private String userEmail = null;

    private String firstName = null;

    private String lastName = null;

    private String photoUrl = null;

    private int userAge = 0;

    private String userSex = null;

    private String education = null;

    private String maritalStatus = null;

    private String industry = null;

    private String jobTitle = null;

    private String phoneAreaCode = null;

    private String userZip = null;

    private String country = null;

    private String accountStatus = null;

    private String lastSignIn = null;

    public UserProfile(String name, String password, int level, String email, String fname, String lname, String photo, Date birth_day, String sex, String edu, String marital_status, String industry, String job_title, String phone_area_code, String zip, String country, String account_status, String last_sign_in) {
        System.out.println("brth_day=" + birth_day);
        this.userName = name;
        this.passWord = password;
        this.accessLevel = level;
        this.userEmail = email;
        this.firstName = fname;
        this.lastName = lname;
        this.photoUrl = photo;
        this.userAge = Integer.parseInt(MyDateAndTime.getUserAge(birth_day.toString()));
        this.userSex = sex;
        this.education = edu;
        this.maritalStatus = marital_status;
        this.industry = industry;
        this.jobTitle = job_title;
        this.phoneAreaCode = phone_area_code;
        this.userZip = zip;
        this.country = country;
        this.accountStatus = account_status;
        this.lastSignIn = last_sign_in;
    }

    public String getUserName() {
        return this.userName;
    }

    public int getUserAge() {
        return this.userAge;
    }

    public String getUserZip() {
        return this.userZip;
    }

    public int getUserLevel() {
        return this.accessLevel;
    }

    public String getUserPassword() {
        return this.passWord;
    }

    public String getAccountStatus() {
        return this.accountStatus;
    }
}
