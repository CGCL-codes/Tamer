package com.google.code.ptrends.Application.DAL.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "APP.SUPPLIERS")
public class SupplierEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;

    private String name;

    private String website;

    private String country;

    private String city;

    private String address;

    private String line1, line2, line3;

    private String fax1, fax2;

    private String email;

    private String workingHours;

    public void setId(int id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "NAME", nullable = false, unique = true, length = 50)
    public String getName() {
        return name;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    @Column(name = "WEBSITE", unique = true, length = 256)
    public String getWebsite() {
        return website;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Column(name = "COUNTRY", length = 128)
    public String getCountry() {
        return country;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Column(name = "CITY", length = 128)
    public String getCity() {
        return city;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Column(name = "ADDRESS", length = 512)
    public String getAddress() {
        return address;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    @Column(name = "LINE1", length = 50)
    public String getLine1() {
        return line1;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
    }

    @Column(name = "LINE2", length = 50)
    public String getLine2() {
        return line2;
    }

    public void setLine3(String line3) {
        this.line3 = line3;
    }

    @Column(name = "LINE3", length = 50)
    public String getLine3() {
        return line3;
    }

    public void setFax1(String fax) {
        this.fax1 = fax;
    }

    @Column(name = "FAX", length = 50)
    public String getFax1() {
        return fax1;
    }

    public void setFax2(String fax) {
        this.fax2 = fax;
    }

    @Column(name = "FAX2", length = 50)
    public String getFax2() {
        return fax2;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "EMAIL", unique = true, length = 128)
    public String getEmail() {
        return email;
    }

    public void setWorkingHours(String workingHours) {
        this.workingHours = workingHours;
    }

    @Column(name = "WORKING_HOURS", length = 512)
    public String getWorkingHours() {
        return workingHours;
    }
}
