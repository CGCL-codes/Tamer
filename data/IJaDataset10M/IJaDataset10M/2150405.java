package com.bashizip.andromed.data;

import java.io.Serializable;

public class User implements Serializable {

    private static final long serialVersionUID = 7390103290165670089L;

    private Long id;

    private String firstname;

    private String lastname;

    private String mail;

    private String phone;

    private String categorie;

    private int since;

    private boolean underTreatment;

    private int age;

    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getMail() {
        return mail;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public boolean isUnderTreatment() {
        return underTreatment;
    }

    public void setUnderTreatment(boolean underTreatment) {
        this.underTreatment = underTreatment;
    }
}
