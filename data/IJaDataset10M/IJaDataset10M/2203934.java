package com.phil.philreports.asynccelltablesearch.client.data;

import java.io.Serializable;

public class ClientPerson implements Serializable {

    private static final long serialVersionUID = 1L;

    private String firstname;

    private String lastname;

    private String email;

    public ClientPerson() {
        firstname = null;
        lastname = null;
        email = null;
    }

    public ClientPerson(String firstname, String lastname, String email) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
    }

    public String getFirstname() {
        return this.firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return this.lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
