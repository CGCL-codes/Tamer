package de.molimo.client.wings.components;

import org.wings.STextField;

public class SChooserTextField extends SChooser {

    public SChooserTextField() {
        super();
        setComponent(new STextField());
    }

    public void setText(String text) {
        ((STextField) getComponent()).setText(text);
    }

    public String getText() {
        return ((STextField) getComponent()).getText();
    }
}
