package com.googlecode.sarasvati.env;

public abstract class AbstractStringValueOfAttributeConverter implements AttributeConverter {

    @Override
    public String objectToString(final Object object) {
        return String.valueOf(object);
    }
}
