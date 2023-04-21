package com.bestattempt.utility.bind;

import java.util.List;
import java.util.Map;

/**
 * TODO: Document me
 *
 * @author Ganeshji Marwaha
 * @since Oct 17, 2006
 */
public class User {

    private String firstName;

    private String lastName;

    private Address address;

    private List<User> friends;

    Map<String, Address> addressMap;

    public Map<String, Address> getAddressMap() {
        return addressMap;
    }

    public void setAddressMap(Map<String, Address> addressMap) {
        this.addressMap = addressMap;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<User> getFriends() {
        return friends;
    }

    public void setFriends(List<User> friends) {
        this.friends = friends;
    }

    public String toString() {
        return "User{" + "firstName='" + firstName + '\'' + "\n" + ", lastName='" + lastName + '\'' + "\n" + ", address=" + address + "\n" + ", friends=" + friends + "\n" + ", addressMap=" + addressMap + "\n" + '}';
    }
}
