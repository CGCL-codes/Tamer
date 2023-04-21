package com.googlecode.openmpis.dto;

/**
 * The Relative class is used to represent a person's relative.
 * 
 * @author  <a href="mailto:rvbabilonia@gmail.com">Rey Vincent Babilonia</a>
 * @version 1.0
 */
public class Relative {

    /**
     * The relative ID
     */
    private int id;

    /**
     * The first name of the relative
     */
    private String firstName;

    /**
     * The last name of the relative
     */
    private String lastName;

    /**
     * The middle name of the relative
     */
    private String middleName;

    /**
     * The email address of the relative
     */
    private String email;

    /**
     * The telephone number of the relative
     */
    private String number;

    /**
     * The street address of the relative
     */
    private String street;

    /**
     * The home city of the relative
     */
    private String city;

    /**
     * The home province of the relative
     */
    private String province;

    /**
     * The home country of the relative
     */
    private String country;

    /**
     * Gets the ID of the relative.
     *
     * @return               the ID of the relative
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the ID of the relative.
     *
     * @param id            the ID of the relative
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the first name of the relative.
     *
     * @return               the first name of the relative
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the relative.
     *
     * @param firstName     the first name of the relative
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the last name of the relative.
     *
     * @return               the last name of the relative
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of the relative.
     *
     * @param lastName      the last name of the relative
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the middle name of the relative.
     *
     * @return               the middle name of the relative
     */
    public String getMiddleName() {
        return middleName;
    }

    /**
     * Sets the middle name of the relative.
     *
     * @param middleName    the middle name of the relative
     */
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    /**
     * Gets the email address of the relative.
     *
     * @return              the email address of the relative
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the relative.
     *
     * @param email         the email address of the relative
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the telephone number of the relative.
     *
     * @return              the telephone number of the relative
     */
    public String getNumber() {
        return number;
    }

    /**
     * Sets the telephone number of the relative.
     *
     * @param number        the telephone number of the relative
     */
    public void setNumber(String number) {
        this.number = number;
    }

    /**
     * Gets the street address of the relative.
     *
     * @return              the street address of the relative
     */
    public String getStreet() {
        return street;
    }

    /**
     * Sets the street address of the relative.
     *
     * @param street        the street address of the relative
     */
    public void setStreet(String street) {
        this.street = street;
    }

    /**
     * Sets the home city of the relative.
     *
     * @return              the home city of the relative
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets the home city of the relative.
     *
     * @param city          the home city of the relative
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Gets the home province of the relative.
     *
     * @return              the home province of the relative
     */
    public String getProvince() {
        return province;
    }

    /**
     * Sets the home province of the relative.
     *
     * @param province      the home province of the relative
     */
    public void setProvince(String province) {
        this.province = province;
    }

    /**
     * Gets the home country of the relative.
     *
     * @return              the home country of the relative
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets the home country of the relative.
     * 
     * @param country       the home country of the relative
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Returns a String representation of this data transfer object.
     *
     * @return              the String representation of this data transfer object
     */
    @Override
    public String toString() {
        String content = "";
        content += "\nID: " + id;
        content += "\nFirst Name: " + firstName;
        content += "\nMiddle Name: " + middleName;
        content += "\nLast Name: " + lastName;
        content += "\nEmail: " + email;
        content += "\nNumber: " + number;
        content += "\nStreet: " + street;
        content += "\nCity: " + city;
        content += "\nProvince: " + province;
        content += "\nCountry: " + country;
        return content;
    }
}
