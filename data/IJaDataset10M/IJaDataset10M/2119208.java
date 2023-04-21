package com.turnengine.client.global.admin.validator;

import com.javabi.common.validator.type.StringValidator;

/**
 * The Email Subject Validator.
 */
public class EmailSubjectValidator extends StringValidator {

    /** The min length. */
    public static final int MIN_LENGTH = 4;

    /** The max length. */
    public static final int MAX_LENGTH = 120;

    /** The null valid. */
    public static final boolean NULL_VALID = false;

    /** The instance. */
    public static final EmailSubjectValidator INSTANCE = new EmailSubjectValidator();

    /**
	 * Creates a new Email Subject Validator.
	 */
    public EmailSubjectValidator() {
        super(NULL_VALID, MIN_LENGTH, MAX_LENGTH);
    }
}
