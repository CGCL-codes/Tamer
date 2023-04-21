package org.vqwiki;

import org.vqwiki.PropertyType;

/**
 * Represents a VQWiki property.
 */
public class Property {

    private String name = null;

    private String defaultvalue = null;

    private String value = null;

    private PropertyType type = PropertyType.STRING;

    private String[] options = null;

    private String maxsize = "250";

    /**
     * TODO: document
     */
    public Property(String name, String defvalue, PropertyType type, String maxsize) {
        this.name = name;
        this.value = value;
        this.defaultvalue = value;
        this.type = type;
        this.maxsize = maxsize;
    }

    /**
     * TODO: document
     */
    public Property(String name, String value, PropertyType type) {
        this.name = name;
        this.value = value;
        this.defaultvalue = value;
        this.type = type;
    }

    /**
     * TODO: document
     */
    public Property(String name, String value, PropertyType type, String[] options) {
        this.name = name;
        this.value = value;
        this.defaultvalue = value;
        this.options = options;
        this.type = type;
    }

    /**
     * TODO: document
     */
    public Property(String name, String value) {
        this.name = name;
        this.value = value;
        this.defaultvalue = value;
    }

    /**
     * TODO: document
     */
    public String getName() {
        return name;
    }

    /**
     * TODO: document
     */
    public String getValue() {
        return value;
    }

    /**
     * TODO: document
     */
    public String getDefaultValue() {
        return defaultvalue;
    }

    /**
     * TODO: document
     */
    public PropertyType getType() {
        return type;
    }

    /**
     * TODO: document
     */
    public String[] getOptions() {
        return options;
    }

    /**
     * TODO: document
     */
    public String getMaxsize() {
        return maxsize;
    }

    /**
     * TODO: document
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * TODO: document
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * TODO: document
     */
    public void setDefaultValue(String value) {
        this.defaultvalue = value;
    }

    /**
     * TODO: document
     */
    public void setType(PropertyType type) {
        this.type = type;
    }

    /**
     * TODO: document
     */
    public void setOptions(String[] options) {
        this.options = options;
    }

    /**
     * TODO: document
     */
    public void setMaxsize(String maxsize) {
        this.maxsize = maxsize;
    }
}
