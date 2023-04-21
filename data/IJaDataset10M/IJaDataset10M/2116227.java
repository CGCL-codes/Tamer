package net.sourceforge.yagsbook.encyclopedia.ant;

import org.apache.tools.ant.*;

/**
 * Handles nested property elements within the Encyclopedia task.
 * Each property has a name and a value, and is passed to the XSLT
 * to control the processing of the Encyclopedia.
 */
public class PropertyTask extends Task {

    private String name = null;

    private String value = null;

    /**
     * The name of the property to be set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * The value of this property.
     */
    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
