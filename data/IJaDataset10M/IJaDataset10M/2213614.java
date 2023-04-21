package com.servengine.pim;

public class PIMContactFieldEntry {

    Integer id;

    PIMContactField contactfield;

    String value;

    public PIMContactFieldEntry(PIMContactField contactfield, String value) {
        this.contactfield = contactfield;
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
