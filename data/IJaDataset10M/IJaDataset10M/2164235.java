package org.proteored.miapeapi.xml.miapeproject;

import java.util.Date;
import org.proteored.miapeapi.interfaces.User;

public class UserImpl implements User {

    private final String ownerName;

    private final String userName;

    private final String password;

    public UserImpl(String ownerName, String userName, String password) {
        this.ownerName = ownerName;
        this.userName = userName;
        this.password = password;
    }

    @Override
    public String getName() {
        return ownerName;
    }

    @Override
    public String getLastName() {
        return null;
    }

    @Override
    public String getTelephoneNumber() {
        return null;
    }

    @Override
    public String getFaxNumber() {
        return null;
    }

    @Override
    public String getEmail() {
        return null;
    }

    @Override
    public String getAddress() {
        return null;
    }

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getStatus() {
        return null;
    }

    @Override
    public String getWeb() {
        return null;
    }

    @Override
    public Integer getContactPerson() {
        return null;
    }

    @Override
    public Integer getManager() {
        return null;
    }

    @Override
    public String getManagerRole() {
        return null;
    }

    @Override
    public String getCode() {
        return null;
    }

    @Override
    public String getCity() {
        return null;
    }

    @Override
    public String getCountry() {
        return null;
    }

    @Override
    public String getPostCode() {
        return null;
    }

    @Override
    public String getDepartment() {
        return null;
    }

    @Override
    public String getInstitution() {
        return null;
    }

    @Override
    public String getInstitutionName() {
        return null;
    }

    @Override
    public String getNif() {
        return null;
    }

    @Override
    public String getAccount() {
        return null;
    }

    @Override
    public Date getDateSignedUp() {
        return null;
    }

    @Override
    public String getMainResearcher() {
        return null;
    }
}
