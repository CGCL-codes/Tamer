package budgeteventplanner.client.entity;

import java.io.Serializable;
import com.googlecode.objectify.annotation.Entity;

@SuppressWarnings("serial")
@Entity
public class Address implements Serializable {

    private String firstName;

    private String lastName;

    private String street;

    private String city;

    private String state;

    private String zipcode;

    private String country;

    private String rawAddress;

    public Address() {
    }

    public static class Builder {

        private Address address = new Address();

        public Builder(Address address) {
            this.address = address;
        }

        public Builder(String rawAddress) {
            this.address.rawAddress = rawAddress;
        }

        public Builder(String firstName, String lastName) {
            this.address.firstName = firstName;
            this.address.lastName = lastName;
        }

        public Builder SetStreet(String street) {
            this.address.street = street;
            return this;
        }

        public Builder setCity(String city) {
            this.address.city = city;
            return this;
        }

        public Builder setState(String state) {
            this.address.state = state;
            return this;
        }

        public Builder setZipcode(String zipcode) {
            this.address.zipcode = zipcode;
            return this;
        }

        public Builder setCountry(String country) {
            this.address.country = country;
            return this;
        }

        public Address build() {
            return this.address;
        }
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZipcode() {
        return zipcode;
    }

    public String getCountry() {
        return country;
    }

    public boolean validate() {
        return false;
    }

    public String toString() {
        return firstName + " " + lastName + " " + street + " " + city + " " + state + " " + zipcode + " " + country;
    }

    public String getRawAddress() {
        return rawAddress;
    }
}
