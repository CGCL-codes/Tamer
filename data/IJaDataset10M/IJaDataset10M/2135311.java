package aml.ramava.data.entities;

public class Invitation {

    private int id = -1;

    private InvitationType type;

    private String clasification;

    private String name;

    private String surname;

    private String text;

    private String company;

    private String street;

    private String city;

    private String province;

    private String district;

    private String country;

    private String zipCode;

    private String parish;

    private String workPhone;

    private String homePhone;

    private String mobilePhone;

    private String fax;

    private String email;

    private String compoundAddress;

    private int lastVisitCount;

    public int getLastVisitCount() {
        return lastVisitCount;
    }

    public void setLastVisitCount(int lastVisitCount) {
        this.lastVisitCount = lastVisitCount;
    }

    public String getCompoundAddress() {
        return compoundAddress;
    }

    public void setCompoundAddress(String compoundAddress) {
        this.compoundAddress = compoundAddress;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getHomePhone() {
        return homePhone;
    }

    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getWorkPhone() {
        return workPhone;
    }

    public void setWorkPhone(String workPhone) {
        this.workPhone = workPhone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getClasification() {
        return clasification;
    }

    public void setClasification(String clasification) {
        this.clasification = clasification;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public InvitationType getType() {
        return type;
    }

    public void setType(InvitationType type) {
        this.type = type;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getParish() {
        return this.parish;
    }

    public void setParish(String parish) {
        this.parish = parish;
    }
}
