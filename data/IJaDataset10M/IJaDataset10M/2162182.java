package aml.ramava.data.entities;

import java.util.Vector;

public class ReportMultipleExibitionParticipantDetail {

    private int id;

    private String name;

    private String email;

    private String web;

    private String address;

    private String city;

    private String state;

    private String country;

    private String postalCode;

    private Vector<Exibition> exibitions = new Vector<Exibition>();

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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Vector<Exibition> getExibitions() {
        return exibitions;
    }

    public void setExibitions(Vector<Exibition> exibitions) {
        this.exibitions = exibitions;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }
}
