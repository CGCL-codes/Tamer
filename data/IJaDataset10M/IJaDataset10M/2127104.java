package org.promotego.api.geocoder.beans;

public class Address {

    private String m_streetAddress;

    private String m_city;

    private String m_state;

    private String m_zip;

    private String m_country;

    public Address(String streetAddress, String city, String state, String zip, String country) {
        m_streetAddress = streetAddress;
        m_city = city;
        m_state = state;
        m_zip = zip;
        m_country = country;
    }

    public String getCity() {
        return m_city;
    }

    public String getCountry() {
        return m_country;
    }

    public String getState() {
        return m_state;
    }

    public String getStreetAddress() {
        return m_streetAddress;
    }

    public String getZip() {
        return m_zip;
    }
}
