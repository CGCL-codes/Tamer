package net.videgro.oma.web;

import net.videgro.oma.managers.SettingManager;

public class AddMemberBean {

    private String location;

    private String verify;

    private String birthdayDay_0;

    private String birthdayMonth_0;

    private String birthdayYear_0;

    private String city;

    private String country;

    private String email;

    private String gender = "";

    private String nameFirst;

    private String nameInsertion;

    private String nameLast;

    private String nameTitle;

    private String postcode;

    private String street;

    private String telephone1;

    private String telephone2;

    private String studyInstitution;

    private String studyStudy;

    private String department;

    private String password;

    private String message;

    private String remoteAddr;

    public AddMemberBean() {
        location = SettingManager.getSettings().getApplicationUrl();
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getNameFirst() {
        return nameFirst;
    }

    public void setNameFirst(String nameFirst) {
        this.nameFirst = nameFirst;
    }

    public String getNameInsertion() {
        return nameInsertion;
    }

    public void setNameInsertion(String nameInsertion) {
        this.nameInsertion = nameInsertion;
    }

    public String getNameLast() {
        return nameLast;
    }

    public void setNameLast(String nameLast) {
        this.nameLast = nameLast;
    }

    public String getNameTitle() {
        return nameTitle;
    }

    public void setNameTitle(String nameTitle) {
        this.nameTitle = nameTitle;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getTelephone1() {
        return telephone1;
    }

    public void setTelephone1(String telephone1) {
        this.telephone1 = telephone1;
    }

    public String getTelephone2() {
        return telephone2;
    }

    public void setTelephone2(String telephone2) {
        this.telephone2 = telephone2;
    }

    public String getStudyInstitution() {
        return studyInstitution;
    }

    public void setStudyInstitution(String studyInstitution) {
        this.studyInstitution = studyInstitution;
    }

    public String getStudyStudy() {
        return studyStudy;
    }

    public void setStudyStudy(String studyStudy) {
        this.studyStudy = studyStudy;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getBirthdayDay_0() {
        return birthdayDay_0;
    }

    public void setBirthdayDay_0(String birthdayDay_0) {
        this.birthdayDay_0 = birthdayDay_0;
    }

    public String getBirthdayMonth_0() {
        return birthdayMonth_0;
    }

    public void setBirthdayMonth_0(String birthdayMonth_0) {
        this.birthdayMonth_0 = birthdayMonth_0;
    }

    public String getBirthdayYear_0() {
        return birthdayYear_0;
    }

    public void setBirthdayYear_0(String birthdayYear_0) {
        this.birthdayYear_0 = birthdayYear_0;
    }

    public String getVerify() {
        return verify;
    }

    public void setVerify(String verify) {
        this.verify = verify;
    }

    public String getRemoteAddr() {
        return remoteAddr;
    }

    public void setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

    public String getNameComplete() {
        return nameFirst + " " + (nameInsertion.isEmpty() ? "" : nameInsertion + " ") + nameLast;
    }

    public String getBirthday() {
        return birthdayYear_0 + "-" + birthdayMonth_0 + "-" + birthdayDay_0;
    }
}
