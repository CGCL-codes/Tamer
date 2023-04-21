package com.volantis.mcs.build.themes.definitions.values;

/**
 * Representation of a list of values.
 */
public interface ListValue extends Value {

    /**
     * Sets the next child value.
     * @param next The next value
     */
    public void setNext(Value next);
}
