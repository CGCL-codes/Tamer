package com.jae.wwwmidwestayurveda.client.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * This class is used to communicate between client/server [SignUp] entity.
 * This stores data using Google App Engine DataStore (Bigtable).
 * Entity Relationships: SignUp
 * 
 * @author gsubu
 * @since  Sept 03 2009
 * 
 */
public class SignUp implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long signupId;

    private String signupEmail;

    private String firstName;

    private String lastName;

    private String address;

    private String city;

    private String state;

    private String zip;

    private String phone;

    private String gender;

    private String hlthcarePractnr;

    private String intrstAyurveda;

    private String intrstEnergyWork;

    private String intrstMassage;

    private String intrstHolisticHealth;

    private String intrstReading;

    private String intrstPhilosophy;

    private String intrstMeditation;

    private String intrstYoga;

    private String intrstAlternativeMedicine;

    private String intrstIncreasingVitality;

    private String intrstWorkLifeBalance;

    private String intrstWeightLossManagement;

    private String intrstTeachingCoaching;

    private String hearAboutUs;

    private String hearAboutUsOther;

    private Date submitDate;

    public Long getSignUpId() {
        return this.signupId;
    }

    public void setSignUpId(Long signupId) {
        this.signupId = signupId;
    }

    public String getSignUpEmail() {
        return this.signupEmail;
    }

    public void setSignUpEmail(String signupEmail) {
        this.signupEmail = signupEmail;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return this.zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return this.gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHealthcarePractitioner() {
        return this.hlthcarePractnr;
    }

    public void setHealthcarePractitioner(String hlthcarePractnr) {
        this.hlthcarePractnr = hlthcarePractnr;
    }

    public String getIntrstAyurveda() {
        return this.intrstAyurveda;
    }

    public void setIntrstAyurveda(String intrstAyurveda) {
        this.intrstAyurveda = intrstAyurveda;
    }

    public String getIntrstEnergyWork() {
        return this.intrstEnergyWork;
    }

    public void setIntrstEnergyWork(String intrstEnergyWork) {
        this.intrstEnergyWork = intrstEnergyWork;
    }

    public String getIntrstMassage() {
        return this.intrstMassage;
    }

    public void setIntrstMassage(String intrstMassage) {
        this.intrstMassage = intrstMassage;
    }

    public String getIntrstHolisticHealth() {
        return this.intrstHolisticHealth;
    }

    public void setIntrstHolisticHealth(String intrstHolisticHealth) {
        this.intrstHolisticHealth = intrstHolisticHealth;
    }

    public String getIntrstReading() {
        return this.intrstReading;
    }

    public void setIntrstReading(String intrstReading) {
        this.intrstReading = intrstReading;
    }

    public String getIntrstPhilosophy() {
        return this.intrstPhilosophy;
    }

    public void setIntrstPhilosophy(String intrstPhilosophy) {
        this.intrstPhilosophy = intrstPhilosophy;
    }

    public String getIntrstMeditation() {
        return this.intrstMeditation;
    }

    public void setIntrstMeditation(String intrstMeditation) {
        this.intrstMeditation = intrstMeditation;
    }

    public String getIntrstYoga() {
        return this.intrstYoga;
    }

    public void setIntrstYoga(String intrstYoga) {
        this.intrstYoga = intrstYoga;
    }

    public String getIntrstAlternativeMedicine() {
        return this.intrstAlternativeMedicine;
    }

    public void setIntrstAlternativeMedicine(String intrstAlternativeMedicine) {
        this.intrstAlternativeMedicine = intrstAlternativeMedicine;
    }

    public String getIntrstIncreasingVitality() {
        return this.intrstIncreasingVitality;
    }

    public void setIntrstIncreasingVitality(String intrstIncreasingVitality) {
        this.intrstIncreasingVitality = intrstIncreasingVitality;
    }

    public String getIntrstWorkLifeBalance() {
        return this.intrstWorkLifeBalance;
    }

    public void setIntrstWorkLifeBalance(String intrstWorkLifeBalance) {
        this.intrstWorkLifeBalance = intrstWorkLifeBalance;
    }

    public String getIntrstWeightLossManagement() {
        return this.intrstWeightLossManagement;
    }

    public void setIntrstWeightLossManagement(String intrstWeightLossManagement) {
        this.intrstWeightLossManagement = intrstWeightLossManagement;
    }

    public String getIntrstTeachingCoaching() {
        return this.intrstTeachingCoaching;
    }

    public void setIntrstTeachingCoaching(String intrstTeachingCoaching) {
        this.intrstTeachingCoaching = intrstTeachingCoaching;
    }

    public String getHearAboutUs() {
        return this.hearAboutUs;
    }

    public void setHearAboutUs(String hearAboutUs) {
        this.hearAboutUs = hearAboutUs;
    }

    public String getHearAboutUsOther() {
        return this.hearAboutUsOther;
    }

    public void setHearAboutUsOther(String hearAboutUsOther) {
        this.hearAboutUsOther = hearAboutUsOther;
    }

    public Date getSubmitDate() {
        return this.submitDate;
    }

    public void setSubmitDate(Date submitDate) {
        this.submitDate = submitDate;
    }

    public SignUp() {
    }
}
