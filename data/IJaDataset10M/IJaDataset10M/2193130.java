package com.turnengine.client.global.admin.validator;

import com.javabi.common.validator.type.StringValidator;

/**
 * The Email Body Validator.
 */
public class EmailBodyValidator extends StringValidator {

    /** The min length. */
    public static final int MIN_LENGTH = 4;

    /** The max length. */
    public static final int MAX_LENGTH = 60000;

    /** The null valid. */
    public static final boolean NULL_VALID = false;

    /** The instance. */
    public static final EmailBodyValidator INSTANCE = new EmailBodyValidator();

    /**
	 * Creates a new Email Body Validator.
	 */
    public EmailBodyValidator() {
        super(NULL_VALID, MIN_LENGTH, MAX_LENGTH);
    }
}
