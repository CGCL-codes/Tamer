package jpa.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User: Pavlo
 * Date: 31.01.12
 * Time: 11:32
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "Contacts")
public class Contact extends ID {

    private String name;

    private String surname;

    private String fatherName;

    private String phoneNumOne;

    private String phoneNumTwo;

    private String address;

    @Column(name = "name", unique = false, nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "surname", unique = false, nullable = false)
    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    @Column(name = "fatherName", unique = false, nullable = true)
    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    @Column(name = "phoneNumOne", unique = false, nullable = false)
    public String getPhoneNumOne() {
        return phoneNumOne;
    }

    public void setPhoneNumOne(String phoneNumOne) {
        this.phoneNumOne = phoneNumOne;
    }

    @Column(name = "phoneNumTwo", unique = false, nullable = true)
    public String getPhoneNumTwo() {
        return phoneNumTwo;
    }

    public void setPhoneNumTwo(String phoneNumTwo) {
        this.phoneNumTwo = phoneNumTwo;
    }

    @Column(name = "address", unique = false, nullable = true)
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
