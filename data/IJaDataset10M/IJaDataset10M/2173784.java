package org.dllearner.core.config;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.beans.PropertyChangeListener;
import java.beans.PropertyEditor;

public class DoubleEditor implements PropertyEditor {

    private Double value;

    @Override
    public void addPropertyChangeListener(PropertyChangeListener arg0) {
    }

    @Override
    public String getAsText() {
        return value.toString();
    }

    @Override
    public Component getCustomEditor() {
        return null;
    }

    @Override
    public String getJavaInitializationString() {
        return null;
    }

    @Override
    public String[] getTags() {
        return null;
    }

    @Override
    public Object getValue() {
        return null;
    }

    @Override
    public boolean isPaintable() {
        return false;
    }

    @Override
    public void paintValue(Graphics arg0, Rectangle arg1) {
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener arg0) {
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        value = Double.valueOf(text);
    }

    @Override
    public void setValue(Object arg0) {
    }

    @Override
    public boolean supportsCustomEditor() {
        return false;
    }
}
