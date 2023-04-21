package com.ncs.crm.domain;

public class User {

    private String id;

    private String name;

    public String sayHello() {
        return "Hello";
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
}
