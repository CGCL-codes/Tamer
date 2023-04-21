package com.loribel.commons.abstraction;

/**
 * Abstraction of a Enum String Selector.
 */
public interface GB_EnumStringSelectorSet extends GB_SelectorSet, GB_EnumStringSelector {

    /**
     * Sets the values to include or to exclude.
     */
    void setValues(String[] a_values);
}
