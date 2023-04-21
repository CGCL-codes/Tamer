package com.turnengine.client.local.property.validator;

import com.javabi.common.validator.type.StringValidator;

/**
 * The Local Property Value Validator.
 */
public class LocalPropertyValueValidator extends StringValidator {

    /** The min length. */
    public static final int MIN_LENGTH = 0;

    /** The max length. */
    public static final int MAX_LENGTH = 1000000;

    /** The null valid. */
    public static final boolean NULL_VALID = false;

    /** The instance. */
    public static final LocalPropertyValueValidator INSTANCE = new LocalPropertyValueValidator();

    /**
	 * Creates a new Local Property Value Validator.
	 */
    public LocalPropertyValueValidator() {
        super(NULL_VALID, MIN_LENGTH, MAX_LENGTH);
    }
}
