package com.volantis.xml.pipeline.sax.impl.drivers.webservice;

/**
 * A Part of a Message. This consists of a value name and the value itself
 * represented as an object.
 */
public class Part {

    /**
     * The name of the Part.
     */
    private String name;

    /**
     * The value.
     */
    private Object value;

    /**
     * Get the value name.
     * @return The value name.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the value name.
     * @param name The value name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the value.
     * @return The value.
     */
    public Object getValue() {
        return value;
    }

    /**
     * Set the value.
     * @param value The value.
     */
    public void setValue(Object value) {
        this.value = value;
    }
}
