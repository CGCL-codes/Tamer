package com.nybbleware.tag4j.value;

import com.nybbleware.tag4j.TagValue;

/**
 *
 * @author NACHIKET PATEL
 */
public class StringTagValue implements TagValue {

    private String value;

    public StringTagValue() {
    }

    public StringTagValue(String value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        if (!(value instanceof String)) {
            throw new IllegalArgumentException("value must be instance of Number");
        }
        this.value = (String) value;
    }
}
