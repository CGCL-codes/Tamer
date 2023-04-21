package org.archive.spring;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.beans.PropertyChangeListener;
import java.beans.PropertyEditor;

/**
 * PropertyEditor allowing Strings to become ConfigPath instances.
 * 
 */
public class ConfigPathEditor<T> implements PropertyEditor {

    Object value;

    public void addPropertyChangeListener(PropertyChangeListener listener) {
    }

    public String getAsText() {
        return null;
    }

    public Component getCustomEditor() {
        return null;
    }

    public String getJavaInitializationString() {
        return null;
    }

    public String[] getTags() {
        return null;
    }

    public Object getValue() {
        ConfigPath c = new ConfigPath(null, value.toString());
        return c;
    }

    public boolean isPaintable() {
        return false;
    }

    public void paintValue(Graphics gfx, Rectangle box) {
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
    }

    public void setAsText(String text) throws IllegalArgumentException {
        setValue(text);
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public boolean supportsCustomEditor() {
        return false;
    }
}
