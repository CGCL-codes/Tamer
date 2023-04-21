package com.turnengine.client.local.turn.validator;

import com.javabi.common.validator.type.LongValidator;

/**
 * The Turn Interval Validator.
 */
public class TurnIntervalValidator extends LongValidator {

    /** The min. */
    public static final long MIN = 0l;

    /** The max. */
    public static final long MAX = 9223372036854775807l;

    /** The null valid. */
    public static final boolean NULL_VALID = false;

    /** The instance. */
    public static final TurnIntervalValidator INSTANCE = new TurnIntervalValidator();

    /**
	 * Creates a new Turn Interval Validator.
	 */
    public TurnIntervalValidator() {
        super(MIN, MAX, NULL_VALID);
    }
}
