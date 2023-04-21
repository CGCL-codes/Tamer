package org.vastenhouw.swing.fld;

import javax.swing.JPasswordField;

public class PasswordField extends SingleTextField {

    public PasswordField(String labelText) {
        this(labelText, "");
    }

    public PasswordField(String labelText, String dataText) {
        super(labelText, new JPasswordField(dataText));
    }
}
