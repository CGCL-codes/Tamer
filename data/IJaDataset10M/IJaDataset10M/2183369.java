package com.jawise.serviceadapter.core;

import com.jawise.serviceadapter.ValidationErrors;

/**
 * Define the location of this instance of the service
 * 
 * @author sathyan
 * 
 */
public class Service {

    String name;

    String userid;

    String password;

    String permittedIPAddresses;

    Port port;

    Adapter adapter;

    String namespace;

    public Adapter getAdapter() {
        return adapter;
    }

    public void setAdapter(Adapter adapter) {
        this.adapter = adapter;
    }

    public Service() {
        port = new Port();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Port getPort() {
        return port;
    }

    public void setPort(Port port) {
        this.port = port;
    }

    public ValidationErrors validate() {
        ValidationErrors errors = new ValidationErrors();
        if (name == null || name.isEmpty()) {
            errors.add("service name is required");
        }
        return errors;
    }

    public String getPermittedIPAddresses() {
        return permittedIPAddresses;
    }

    public void setPermittedIPAddresses(String permittedIPAddresses) {
        this.permittedIPAddresses = permittedIPAddresses;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }
}
