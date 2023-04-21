package org.plazmaforge.framework.uwt.widgets;

public class CheckBox extends Control {

    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        fireChangeProperty(PROPERTY_TEXT, text);
    }
}
