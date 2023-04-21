package org.learnaholic.application.model;

public class ItemProperty {

    private String value;

    public ItemProperty(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String toString() {
        return getValue();
    }
}
