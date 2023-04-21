package com.threerings.antidote.property;

import com.threerings.antidote.field.Field;

/**
 * A {@link BaseProperty} that holds an {@link Integer} object.
 */
public class IntegerProperty extends BaseProperty<Integer> {

    public IntegerProperty(String name, Field field) {
        super(name, field);
    }

    public IntegerProperty(String name, Field field, int defaultValue) {
        super(name, field, defaultValue);
    }

    @Override
    protected Integer validateProperty() {
        try {
            return Integer.parseInt(getRawValue());
        } catch (final NumberFormatException nfe) {
            appendViolation(new InvalidIntegerViolation(this));
            return null;
        }
    }
}
