package net.sf.gilead.test.domain.proxy;

import java.io.Serializable;
import net.sf.gilead.test.domain.interfaces.IAddress;
import net.sf.gilead.test.domain.interfaces.ICountry;

/**
 * Embedded test class for address (depends on User)
 * @author bruno.marchesson
 *
 */
public class Address implements Serializable, IAddress {

    /**
	 * Serialization ID
	 */
    private static final long serialVersionUID = -6914495957511158133L;

    private String street;

    private String city;

    private ICountry country;

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public ICountry getCountry() {
        return country;
    }

    public void setCountry(ICountry country) {
        this.country = country;
    }
}
