package com.titan.domain;

import javax.persistence.*;
import java.util.*;

@Entity
public class CreditCompany implements java.io.Serializable {

    private int id;

    private String name;

    private Address address;

    @Id
    @GeneratedValue
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

    @OneToOne(cascade = { CascadeType.ALL })
    @JoinColumn(name = "ADDRESS_ID")
    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
