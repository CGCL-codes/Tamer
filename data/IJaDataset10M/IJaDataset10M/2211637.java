package com.be.vo;

import com.be.bo.GlobalParameter;

public class SystemParameterVO {

    private long id;

    private String name;

    private String value;

    private java.sql.Date validFrom;

    private java.sql.Date validTo;

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setValidFrom(java.sql.Date validFrom) {
        this.validFrom = validFrom;
    }

    public void setValidTo(java.sql.Date validTo) {
        this.validTo = validTo;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public java.sql.Date getValidFrom() {
        return validFrom;
    }

    public java.sql.Date getValidTo() {
        return validTo;
    }

    public String toString() {
        return id + ";" + name + ";" + value + ";" + validFrom + ";" + validTo;
    }

    public void init() {
        id = 0;
        name = "";
        value = "";
        validFrom = new java.sql.Date(System.currentTimeMillis());
        validTo = new java.sql.Date(GlobalParameter.defaultValidTo);
    }
}
