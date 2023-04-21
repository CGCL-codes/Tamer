package com.jungbo.servlet.centric.dto;

import java.io.Serializable;

public class CustUserDto implements Serializable {

    private static final long serialVersionUID = -8653059912970844675L;

    private String id;

    private String name;

    private String address;

    public CustUserDto() {
    }

    public CustUserDto(String id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String toString() {
        return "[" + id + "/" + name + "/" + address + "]";
    }
}
