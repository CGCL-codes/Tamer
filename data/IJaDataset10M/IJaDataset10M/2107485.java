package com.acv.webapp.taglib;

public class AttributeBean implements Attribute {

    private String name;

    private String value;

    public AttributeBean(String n, String v) {
        name = n;
        value = v;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String toString() {
        return name + " : " + value;
    }
}
