package com.tll.common.model;

import com.tll.model.PropertyMetadata;
import com.tll.model.PropertyType;

/**
 * StringPropertyValue - Generic holder construct for entity properties.
 * @author jpk
 */
public class LongPropertyValue extends AbstractPropertyValue implements ISelfFormattingPropertyValue {

    protected Long value;

    /**
	 * Constructor
	 */
    public LongPropertyValue() {
    }

    /**
	 * Constructor
	 * @param propertyName
	 * @param value
	 */
    public LongPropertyValue(final String propertyName, final Long value) {
        this(propertyName, null, value);
    }

    /**
	 * Constructor
	 * @param propertyName
	 * @param metadata
	 * @param value
	 */
    public LongPropertyValue(final String propertyName, final PropertyMetadata metadata, final Long value) {
        super(propertyName, metadata);
        this.value = value;
    }

    @Override
    public PropertyType getType() {
        return PropertyType.LONG;
    }

    @Override
    public IPropertyValue copy() {
        return new LongPropertyValue(propertyName, metadata, value == null ? null : new Long(value.longValue()));
    }

    @Override
    public final Object getValue() {
        return value;
    }

    @Override
    public String asString() {
        return value == null ? null : value.toString();
    }

    @Override
    public void doSetValue(final Object obj) {
        if (obj == null) {
            this.value = null;
        } else if (obj instanceof Integer) {
            this.value = Long.valueOf(((Integer) obj).longValue());
        } else if (obj instanceof Number) {
            this.value = Long.valueOf(((Number) obj).longValue());
        } else {
            throw new IllegalArgumentException("The value must be a Long");
        }
    }

    public Long getLong() {
        return value;
    }
}
