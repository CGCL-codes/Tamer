package com.jsoft.linkbuild.listenerAndServerLibrary;

import com.jsoft.linkbuild.utility.*;

class Rule implements RegistrationRule {

    String[] fields;

    String favorite_address;

    int max_fields = 2;

    public boolean checkLength(String[] fields) {
        return (fields.length == max_fields);
    }

    public String[] getFields() {
        return fields;
    }

    public void setFields(String[] fields) {
        this.fields = fields;
    }

    public boolean acceptRegistration(String address, String[] remote_fields) {
        if (checkLength(remote_fields) && address.equals(favorite_address)) return true;
        return false;
    }
}
