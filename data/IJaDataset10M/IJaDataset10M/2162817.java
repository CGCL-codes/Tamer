package com.turnengine.client.local.turn.validator;

import com.javabi.common.validator.type.LongValidator;

/**
 * The Turn Timestamp Validator.
 */
public class TurnTimestampValidator extends LongValidator {

    /** The min. */
    public static final long MIN = 0l;

    /** The max. */
    public static final long MAX = 9223372036854775807l;

    /** The null valid. */
    public static final boolean NULL_VALID = false;

    /** The instance. */
    public static final TurnTimestampValidator INSTANCE = new TurnTimestampValidator();

    /**
	 * Creates a new Turn Timestamp Validator.
	 */
    public TurnTimestampValidator() {
        super(MIN, MAX, NULL_VALID);
    }
}
