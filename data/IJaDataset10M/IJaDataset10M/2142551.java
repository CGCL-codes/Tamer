package de.mogwai.kias.example.bo;

import java.util.List;
import java.util.Vector;

public class Customer extends PersistentObject {

    private String name1;

    private String name2;

    private String company;

    private String street;

    private String country;

    private String plz;

    private String city;

    private String comments;

    private boolean contactforbidden;

    private List<CustomerContact> contacts = new Vector<CustomerContact>();

    private List<CustomerHistory> history = new Vector<CustomerHistory>();

    public Customer() {
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public boolean isContactforbidden() {
        return contactforbidden;
    }

    public void setContactforbidden(boolean contactforbidden) {
        this.contactforbidden = contactforbidden;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<CustomerContact> getContacts() {
        return contacts;
    }

    public void setContacts(List<CustomerContact> kontakte) {
        this.contacts = kontakte;
    }

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public String getPlz() {
        return plz;
    }

    public void setPlz(String plz) {
        this.plz = plz;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public List<CustomerHistory> getHistory() {
        return history;
    }

    public void setHistory(List<CustomerHistory> history) {
        this.history = history;
    }
}
