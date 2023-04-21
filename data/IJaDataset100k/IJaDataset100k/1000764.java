package org.akrogen.tkui.core.internal.converters;

import org.akrogen.tkui.core.converters.ITkuiConverter;

public class StringConverter implements ITkuiConverter {

    private static StringConverter instance;

    public static StringConverter getInstance() {
        if (instance == null) instance = new StringConverter();
        return instance;
    }

    public Class getId() {
        return String.class;
    }

    public String toString(Object value) {
        if (value != null) return value.toString();
        return "";
    }

    public Object parse(String value) {
        return value;
    }
}
