package com.agimatec.commons.config;

public class BooleanNode extends Node {

    protected boolean value;

    public BooleanNode() {
    }

    public Object getObjectValue() {
        return Boolean.valueOf(value);
    }

    public boolean getValue() {
        return value;
    }

    public void setValue(final boolean aValue) {
        value = aValue;
    }
}
