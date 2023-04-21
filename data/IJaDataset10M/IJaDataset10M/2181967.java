package com.turnengine.client.global.error.validator;

import com.javabi.common.validator.type.LongValidator;

/**
 * The Stack Trace Id Validator.
 */
public class StackTraceIdValidator extends LongValidator {

    /** The min. */
    public static final long MIN = 0l;

    /** The max. */
    public static final long MAX = 9223372036854775807l;

    /** The null valid. */
    public static final boolean NULL_VALID = false;

    /** The instance. */
    public static final StackTraceIdValidator INSTANCE = new StackTraceIdValidator();

    /**
	 * Creates a new Stack Trace Id Validator.
	 */
    public StackTraceIdValidator() {
        super(MIN, MAX, NULL_VALID);
    }
}
