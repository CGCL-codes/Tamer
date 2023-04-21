package org.springframework.samples.jpetstore.domain;

/**
 *
 */
public class Address implements java.io.Serializable {

    private String addr1;

    private String addr2;

    private String city;

    private String state;

    private String zipcode;

    private String country;

    /** default constructor */
    public Address() {
    }

    public Address(String addr1, String addr2, String city, String state, String zipcode, String country) {
        this.addr1 = addr1;
        this.addr2 = addr2;
        this.city = city;
        this.state = state;
        this.zipcode = zipcode;
        this.country = country;
    }

    /**
   * �������캯��
   */
    public Address(Address other) {
        this.addr1 = other.addr1;
        this.addr2 = other.addr2;
        this.city = other.city;
        this.state = other.state;
        this.zipcode = other.zipcode;
        this.country = other.country;
    }

    public String getAddr1() {
        return addr1;
    }

    public void setAddr1(String addr1) {
        this.addr1 = addr1;
    }

    public String getAddr2() {
        return addr2;
    }

    public void setAddr2(String addr2) {
        this.addr2 = addr2;
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

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Address)) return false;
        final Address address = (Address) o;
        if (!addr1.equals(address.addr1)) return false;
        if (!addr2.equals(address.addr2)) return false;
        if (!city.equals(address.city)) return false;
        if (!city.equals(address.state)) return false;
        if (!zipcode.equals(address.zipcode)) return false;
        if (!country.equals(address.country)) return false;
        return true;
    }

    public int hashCode() {
        int result;
        result = addr1.hashCode();
        result = 31 * result + addr2.hashCode();
        result = 31 * result + city.hashCode();
        result = 31 * result + state.hashCode();
        result = 31 * result + zipcode.hashCode();
        result = 31 * result + country.hashCode();
        return result;
    }

    public String toString() {
        return "Street: \t" + getAddr1() + ", " + getAddr2() + "\n" + "City: \t" + getCity() + ", \n" + "State: \t" + getState() + ", \n" + "Zipcode: \t" + getZipcode() + ", \n" + "Country: \t" + getCountry();
    }
}
