package de.mpiwg.vspace.navigation.util.preferences.editors;

import de.mpiwg.vspace.navigation.fieldeditor.IProperty;

public class SimpleProperty implements IProperty {

    private String propertyLabel;

    private String propertyName;

    public SimpleProperty(String propertyLabel, String propertyName) {
        this.propertyLabel = propertyLabel;
        this.propertyName = propertyName;
    }

    public String getPropertyLabel() {
        return propertyLabel;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public String getPropertyUseDefaultLabel() {
        return null;
    }

    public String getPropertyUseDefaultName() {
        return null;
    }
}
