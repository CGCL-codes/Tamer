package com.nflakes.petrol.model;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import java.util.HashMap;
import java.util.Map;

@JsonAutoDetect
public class PetrolStation {

    private Address address;

    private String name;

    private Map<PetrolType, PetrolPrice> priceMap = new HashMap<PetrolType, PetrolPrice>();

    private PetrolStation() {
    }

    private PetrolStation(String name, Address address) {
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Address getAddress() {
        return address;
    }

    public static PetrolStation fromTable(String[] tableRows) {
        return new PetrolStation(tableRows[0], new Address(tableRows[1], tableRows[3]));
    }
}
