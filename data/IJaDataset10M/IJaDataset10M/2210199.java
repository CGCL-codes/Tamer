package org.openamf.examples;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import org.openamf.examples.spring.IhaveName;

/**
 * @author Jason Calabrese <jasonc@missionvi.com>
 * @version $Revision: 1.4 $, $Date: 2005/07/05 22:04:06 $
 */
public class Person implements Serializable, IhaveName {

    private String firstName;

    private String lastName;

    private String address;

    private String city;

    private String state;

    private int zipCode;

    private Date birthDate;

    private List extraInfo;

    public Person() {
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

    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public int getZipCode() {
        return zipCode;
    }

    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public List getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(List list) {
        extraInfo = list;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(firstName);
        sb.append(" ");
        sb.append(lastName);
        sb.append(", zipcode = ");
        sb.append(zipCode);
        return sb.toString();
    }

    public String getName() {
        return this.getFullName();
    }

    public void setName(String newName) {
        this.firstName = newName;
    }
}
