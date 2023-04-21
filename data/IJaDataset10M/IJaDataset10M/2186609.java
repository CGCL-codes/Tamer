package com.be.vo;

import com.be.bo.GlobalParameter;

public class ResourceVO {

    private long id;

    private long type;

    private String locale;

    private String key;

    private String value;

    private java.sql.Timestamp modified;

    private java.sql.Date validFrom;

    private java.sql.Date validTo;

    public void setId(long id) {
        this.id = id;
    }

    public void setType(long type) {
        this.type = type;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setModified(java.sql.Timestamp modified) {
        this.modified = modified;
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

    public long getType() {
        return type;
    }

    public String getLocale() {
        return locale;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public java.sql.Timestamp getModified() {
        return modified;
    }

    public java.sql.Date getValidFrom() {
        return validFrom;
    }

    public java.sql.Date getValidTo() {
        return validTo;
    }

    public String toString() {
        return id + ";" + type + ";" + locale + ";" + key + ";" + value + ";" + modified + ";" + validFrom + ";" + validTo;
    }

    public void init() {
        id = 0;
        type = 0;
        locale = "";
        key = "";
        value = "";
        validFrom = new java.sql.Date(System.currentTimeMillis());
        validTo = new java.sql.Date(GlobalParameter.defaultValidTo);
    }
}
