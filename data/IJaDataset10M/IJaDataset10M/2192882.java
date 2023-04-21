package org.j2eebuilder.license.ejb;

/**
 * 
 * @(#)LicenseEJB.java 1.350 01/12/03
 * 
 * @version 1.350, 01/12/03
 * 
 * @see org.j2eebuilder.view.ValueObjectFactory#getDataVO(java.lang.Object,
 *      java.lang.Class)
 * 
 * @since OEC1.2
 */
public class LicenseBean extends org.j2eebuilder.model.ejb.SignatureImpl {

    public LicenseBean() {
    }

    public Integer licenseID;

    public String contactName;

    public String licenseeName;

    public String addressLine1;

    public String addressLine2;

    public String city;

    public String state;

    public String country;

    public String zip;

    public String email;

    public String url;

    public String fullPhone;

    public java.sql.Date expirationDate;

    public Integer numberOfOrganizations;

    public Integer numberOfUsers;

    public String licenseKey;

    public String signature;

    public Integer getLicenseID() {
        return licenseID;
    }

    public void setLicenseID(Integer licenseID) {
        this.licenseID = licenseID;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getLicenseeName() {
        return licenseeName;
    }

    public void setLicenseeName(String licenseeName) {
        this.licenseeName = licenseeName;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFullPhone() {
        return fullPhone;
    }

    public void setFullPhone(String fullPhone) {
        this.fullPhone = fullPhone;
    }

    public java.sql.Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(java.sql.Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Integer getNumberOfOrganizations() {
        return numberOfOrganizations;
    }

    public void setNumberOfOrganizations(Integer numberOfOrganizations) {
        this.numberOfOrganizations = numberOfOrganizations;
    }

    public Integer getNumberOfUsers() {
        return numberOfUsers;
    }

    public void setNumberOfUsers(Integer numberOfUsers) {
        this.numberOfUsers = numberOfUsers;
    }

    public String getLicenseKey() {
        return licenseKey;
    }

    public void setLicenseKey(String licenseKey) {
        this.licenseKey = licenseKey;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
