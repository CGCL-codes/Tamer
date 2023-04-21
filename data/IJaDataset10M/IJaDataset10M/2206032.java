package org.atlantal.impl.app.user;

import java.util.Map;
import org.atlantal.api.app.resource.ResourceException;
import org.atlantal.api.app.user.User;
import org.atlantal.api.app.user.UserManager;

/**
 * Titre :        Atlantal Framework
 * Description :
 * Copyright :    Copyright (c) 2002
 * Soci�t� :      Mably Multimedia
 * @author <a href="mailto:masurel@mably.com">Francois MASUREL</a>
 * @version 1.0
 */
public class UserInstance implements User {

    private UserManager service = null;

    private String id;

    private String login;

    private String password;

    private String prefix;

    private String name;

    private String firstname;

    private String address;

    private String zipcode;

    private String city;

    private String country;

    private String email;

    private String phone;

    private Integer groupid;

    private Map groups;

    /**
     * Constructeur
     */
    public UserInstance() {
    }

    /**
     * getId
     * @return id
     */
    public String getId() {
        return this.id;
    }

    /**
     * setId
     * @param id id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * getLogin
     * @return login
     */
    public String getLogin() {
        return this.login;
    }

    /**
     * setLogin
     * @param login login
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * getPassword
     * @return password
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * setPassword
     * @param password password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * getPrefix
     * @return prefix
     */
    public String getPrefix() {
        return this.prefix;
    }

    /**
     * setPrefix
     * @param prefix prefix
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     * getName
     * @return name
     */
    public String getName() {
        return this.name;
    }

    /**
     * setName
     * @param name name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * getFirstName
     * @return First name
     */
    public String getFirstName() {
        return this.firstname;
    }

    /**
     * setFirstName
     * @param firstname First name
     */
    public void setFirstName(String firstname) {
        this.firstname = firstname;
    }

    /**
     * getAddress
     * @return Address
     */
    public String getAddress() {
        return this.address;
    }

    /**
     * setAddress
     * @param address Address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * getZipCode
     * @return Zip code
     */
    public String getZipCode() {
        return this.zipcode;
    }

    /**
     * setZipCode
     * @param zipcode Zip code
     */
    public void setZipCode(String zipcode) {
        this.zipcode = zipcode;
    }

    /**
     * getCity
     * @return City
     */
    public String getCity() {
        return this.city;
    }

    /**
     * setCity
     * @param city City
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * getCountry
     * @return Country
     */
    public String getCountry() {
        return this.country;
    }

    /**
     * setCountry
     * @param country country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * getEmail
     * @return Email
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * setEmail
     * @param email Email address
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * getPhone
     * @return Phone number
     */
    public String getPhone() {
        return this.phone;
    }

    /**
     * setPhone
     * @param phone Phone number
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * setGroupId
     * @return Group Id
     */
    public Integer getGroupId() {
        return this.groupid;
    }

    /**
     * setGroupId
     * @param groupid groupid
     */
    public void setGroupId(Integer groupid) {
        this.groupid = groupid;
    }

    /**
     * @param groups groups
     */
    public void setGroups(Map groups) {
        this.groups = groups;
    }

    /**
     * @return groups
     */
    public Map getGroups() {
        return this.groups;
    }

    /**
     * @param groupid groupid
     * @return boolean
     */
    public boolean isInGroup(Integer groupid) {
        boolean result;
        if (this.groups == null) {
            result = false;
        } else {
            result = groups.containsKey(groupid);
        }
        return result;
    }

    /**
     * @param service The service to set.
     */
    public void setService(UserManager service) {
        this.service = service;
    }

    /**
     * Save user resource
     * @throws ResourceException ResourceException
     */
    public void saveResource() throws ResourceException {
        this.service.saveResource(this);
    }
}
