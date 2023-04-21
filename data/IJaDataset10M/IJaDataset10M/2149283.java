package com.volantis.mcs.protocols.widgets.attributes;

import com.volantis.mcs.protocols.widgets.PropertyReference;

/**
 * Holds properties specific to select element
 */
public class SelectAttributes extends WidgetAttributes {

    private String mode;

    private PropertyReference propertyReference;

    /**
     * @return Returns the mode.
     */
    public String getMode() {
        return mode;
    }

    /**
     * @param mode The mode to set.
     */
    public void setMode(String mode) {
        this.mode = mode;
    }

    /**
     * @return Returns the propertyReference.
     */
    public PropertyReference getPropertyReference() {
        return propertyReference;
    }

    /**
     * @param propertyReference The propertyReference to set.
     */
    public void setPropertyReference(PropertyReference propertyReference) {
        this.propertyReference = propertyReference;
    }
}
