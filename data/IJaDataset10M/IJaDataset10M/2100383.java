package edu.upmc.opi.caBIG.caTIES.connector.pojos;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.GenericGenerator;

/**
 * The Class AddressImpl.
 @see edu.upmc.opi.caBIG.caTIES.database.domain.Address 
 */
@Entity
@Table(name = "ADDRESS")
@org.hibernate.annotations.Entity(selectBeforeUpdate = true, dynamicInsert = true, dynamicUpdate = true)
@org.hibernate.annotations.Proxy(lazy = true)
@BatchSize(size = 5)
public class AddressImpl implements java.io.Serializable {

    /**
     * The id.
     */
    @Id
    @Column(name = "ID")
    @GenericGenerator(name = "hibseq", strategy = "edu.upmc.opi.caBIG.caTIES.database.ExistingIDPreservingTableHiLoGenerator")
    @GeneratedValue(generator = "hibseq")
    protected java.lang.Long id;

    /**
     * Gets the id.
     * 
     * @return the id
     */
    public java.lang.Long getId() {
        return id;
    }

    /**
     * Sets the id.
     * 
     * @param id
     *            the id
     */
    public void setId(java.lang.Long id) {
        this.id = id;
    }

    /**
     * The uuid.
     */
    @Column(name = "UUID", length = 50)
    protected java.lang.String uuid;

    /**
     * Gets the uuid.
     * 
     * @return the uuid
     */
    public java.lang.String getUuid() {
        return uuid;
    }

    /**
     * Sets the uuid.
     * 
     * @param uuid
     *            the uuid
     */
    public void setUuid(java.lang.String uuid) {
        this.uuid = uuid;
    }

    /**
     * The street.
     */
    @Column(name = "STREET", length = 255)
    protected java.lang.String street;

    /**
     * Gets the street.
     * 
     * @return the street
     */
    public java.lang.String getStreet() {
        return street;
    }

    /**
     * Sets the street.
     * 
     * @param street
     *            the street
     */
    public void setStreet(java.lang.String street) {
        this.street = street;
    }

    /**
     * The city.
     */
    @Column(name = "CITY", length = 50)
    protected java.lang.String city;

    /**
     * Gets the city.
     * 
     * @return the city
     */
    public java.lang.String getCity() {
        return city;
    }

    /**
     * Sets the city.
     * 
     * @param city
     *            the city
     */
    public void setCity(java.lang.String city) {
        this.city = city;
    }

    /**
     * The state.
     */
    @Column(name = "STATE", length = 50)
    protected java.lang.String state;

    /**
     * Gets the state.
     * 
     * @return the state
     */
    public java.lang.String getState() {
        return state;
    }

    /**
     * Sets the state.
     * 
     * @param state
     *            the state
     */
    public void setState(java.lang.String state) {
        this.state = state;
    }

    /**
     * The country.
     */
    @Column(name = "COUNTRY", length = 50)
    protected java.lang.String country;

    /**
     * Gets the country.
     * 
     * @return the country
     */
    public java.lang.String getCountry() {
        return country;
    }

    /**
     * Sets the country.
     * 
     * @param country
     *            the country
     */
    public void setCountry(java.lang.String country) {
        this.country = country;
    }

    /**
     * The zip code.
     */
    @Column(name = "ZIP_CODE", length = 10)
    protected java.lang.String zipCode;

    /**
     * Gets the zip code.
     * 
     * @return the zip code
     */
    public java.lang.String getZipCode() {
        return zipCode;
    }

    /**
     * Sets the zip code.
     * 
     * @param zipCode
     *            the zip code
     */
    public void setZipCode(java.lang.String zipCode) {
        this.zipCode = zipCode;
    }

    /**
     * The phone number.
     */
    @Column(name = "PHONE_NUMBER", length = 20)
    protected java.lang.String phoneNumber;

    /**
     * Gets the phone number.
     * 
     * @return the phone number
     */
    public java.lang.String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the phone number.
     * 
     * @param phoneNumber
     *            the phone number
     */
    public void setPhoneNumber(java.lang.String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * The fax number.
     */
    @Column(name = "FAX_NUMBER", length = 20)
    protected java.lang.String faxNumber;

    /**
     * Gets the fax number.
     * 
     * @return the fax number
     */
    public java.lang.String getFaxNumber() {
        return faxNumber;
    }

    /**
     * Sets the fax number.
     * 
     * @param faxNumber
     *            the fax number
     */
    public void setFaxNumber(java.lang.String faxNumber) {
        this.faxNumber = faxNumber;
    }

    /**
     * The email address.
     */
    @Column(name = "EMAIL_ADDRESS", length = 50)
    protected java.lang.String emailAddress;

    /**
     * Gets the email address.
     * 
     * @return the email address
     */
    public java.lang.String getEmailAddress() {
        return emailAddress;
    }

    /**
     * Sets the email address.
     * 
     * @param emailAddress
     *            the email address
     */
    public void setEmailAddress(java.lang.String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.getClass().getName() + "\n");
        sb.append("ID ==> " + this.id + "\n");
        sb.append("UUID ==> " + this.uuid + "\n");
        sb.append("STREET ==> " + this.street + "\n");
        sb.append("CITY ==> " + this.city + "\n");
        sb.append("STATE ==> " + this.state + "\n");
        sb.append("COUNTRY ==> " + this.country + "\n");
        sb.append("ZIP_CODE ==> " + this.zipCode + "\n");
        sb.append("PHONE_NUMBER ==> " + this.phoneNumber + "\n");
        sb.append("FAX_NUMBER ==> " + this.faxNumber + "\n");
        sb.append("EMAIL_ADDRESS ==> " + this.emailAddress + "\n");
        return sb.toString();
    }
}
