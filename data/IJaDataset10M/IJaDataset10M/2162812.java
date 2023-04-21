package org.sgodden.echo.ext20;

import nextapp.echo.app.Component;

/**
 * A wrapper for a form component and its label.
 * @author sgodden
 */
public class FormField {

    private Component field;

    private String label;

    /**
     * Returns the form field (the input component).
     * @return the form field.
     */
    public Component getField() {
        return field;
    }

    /**
     * Sets the form field (the input component).
     * @param field the form field.
     */
    public void setField(Component field) {
        this.field = field;
    }

    /**
     * Returns the field label.
     * @return the field label.
     */
    public String getLabel() {
        return label;
    }

    /**
     * Sets the field label.
     * @param label the field label.
     */
    public void setLabel(String label) {
        this.label = label;
    }
}
