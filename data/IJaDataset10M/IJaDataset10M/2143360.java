package org.escapek.core.nodes.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Address extends ContactData implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1627716120692626616L;

    private String street;

    private String postcode;

    private String city;

    private String state;

    private RegistryNodeValue countryCode;

    public Address() {
        super();
    }

    @Column(name = "CITY")
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @ManyToOne
    @JoinColumn(name = "COUNTRYCODE_ID")
    public RegistryNodeValue getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(RegistryNodeValue countryCode) {
        this.countryCode = countryCode;
    }

    @Column(name = "POSTCODE")
    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    @Column(name = "STATE")
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Column(name = "STREET")
    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }
}
