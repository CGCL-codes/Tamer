package com.rapidgwt.client.components;

import com.google.gwt.user.client.ui.*;

public class RapidCheckBox extends CheckBox implements IRapidComponent {

    public RapidCheckBox(String pName) {
        super(pName);
    }

    public String getComponentValue() {
        if (super.isChecked()) {
            return (com.rapidgwt.client.Messages.ON);
        } else {
            return (com.rapidgwt.client.Messages.EMPTY);
        }
    }
}
