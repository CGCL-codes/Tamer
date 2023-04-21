package com.ericpol.lab.student1;

import java.util.Date;

/**
 * 
 * @author Victoria Sadko
 *
 */
public class Student {

    private int id;

    private String familyName;

    private String lastName;

    private String middleName;

    private Date date;

    private String adress;

    private int phone;

    private String faculty;

    private int course;

    private String group;

    private static int nextID = 0;

    public int getId() {
        return id;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public int getCourse() {
        return course;
    }

    public void setCourse(int course) {
        this.course = course;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Student() {
        id = nextID;
        nextID++;
    }

    public Student(String fn, String ln, String mn, String adr, int ph, String fac, int c, String g) {
        id = nextID;
        nextID++;
        familyName = fn;
        lastName = ln;
        middleName = mn;
        adress = adr;
        phone = ph;
        faculty = fac;
        course = c;
        group = g;
    }

    public void print() {
        System.out.println(id + "\t" + familyName + "\t" + lastName + "\t" + middleName + "\t");
        System.out.println("Adress: " + adress + "\t Phone: " + phone + "\t group " + group + " \tfaculty " + faculty);
    }
}
