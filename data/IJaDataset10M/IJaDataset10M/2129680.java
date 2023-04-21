package model;

import java.text.*;

public class Student {

    private String userId;

    private String password;

    private String name;

    private String school;

    private double eDollar;

    /**
     * initialize the student constructor
     * @param userId the student's user id
     * @param password the student's password
     * @param name the name of the student
     * @param school the school of the student
     * @param eDollar the e-dollars of the student
     */
    public Student(String userId, String password, String name, String school, double eDollar) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.school = school;
        this.eDollar = eDollar;
    }

    /**
     * sets the edollar of the student
     * @param eDollar the edollars of the student
     */
    public void setEdollar(double eDollar) {
        this.eDollar = eDollar;
    }

    /**
     * sets the name of the student
     * @param name the name of the student
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * sets the password of the student
     * @param password the password of the student
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * sets the school of the student
     * @param school the school of the student
     */
    public void setSchool(String school) {
        this.school = school;
    }

    /**
     * sets the user id of the student
     * @param userId the user id of the student
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * retrieve the edollars in 2 decimal places
     * @return the edollars in string
     */
    public String getEdollarTwoDecimal() {
        DecimalFormat dmt = new DecimalFormat("#,##0.00");
        return dmt.format(eDollar);
    }

    /**
     * retrieve the edollars of the student
     * @return the edollars of the student
     */
    public double getEdollar() {
        return eDollar;
    }

    /**
     * retrieve the student's name
     * @return the student's name
     */
    public String getName() {
        return name;
    }

    /**
     * retrieve the password of the student
     * @return the password of the student
     */
    public String getPassword() {
        return password;
    }

    /**
     * retrieve the school of the student
     * @return the school of the student
     */
    public String getSchool() {
        return school;
    }

    /**
     * retrieve the student's user id
     * @return the student user id
     */
    public String getUserId() {
        return userId;
    }
}
