package com.turnengine.client.local.alliance.validator;

import com.javabi.common.validator.type.StringValidator;

/**
 * The Alliance Name Validator.
 */
public class AllianceNameValidator extends StringValidator {

    /** The min length. */
    public static final int MIN_LENGTH = 1;

    /** The max length. */
    public static final int MAX_LENGTH = 200;

    /** The null valid. */
    public static final boolean NULL_VALID = false;

    /** The instance. */
    public static final AllianceNameValidator INSTANCE = new AllianceNameValidator();

    /**
	 * Creates a new Alliance Name Validator.
	 */
    public AllianceNameValidator() {
        super(NULL_VALID, MIN_LENGTH, MAX_LENGTH);
    }
}
