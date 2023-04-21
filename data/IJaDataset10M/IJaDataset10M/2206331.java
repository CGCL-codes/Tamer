package com.google.code.jtracert.model;

/**
 * Distributed under GNU GENERAL PUBLIC LICENSE Version 3
 *
 * @author Dmitry.Bedrin@gmail.com
 */
public class SimpleObjectDump extends ObjectDump {

    /**
     *
     */
    private String value;

    /**
     *
     * @return
     */
    public String getValue() {
        return value;
    }

    /**
     *
     * @param value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return "SimpleObjectDump{" + "value='" + value + '\'' + "} " + super.toString();
    }
}
