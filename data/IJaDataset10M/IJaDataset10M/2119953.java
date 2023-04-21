package com.turnengine.client.global.translation.validator;

import com.javabi.common.validator.type.IntegerValidator;

/**
 * The Translation Id Validator.
 */
public class TranslationIdValidator extends IntegerValidator {

    /** The min. */
    public static final int MIN = 0;

    /** The max. */
    public static final int MAX = 2147483647;

    /** The null valid. */
    public static final boolean NULL_VALID = false;

    /** The instance. */
    public static final TranslationIdValidator INSTANCE = new TranslationIdValidator();

    /**
	 * Creates a new Translation Id Validator.
	 */
    public TranslationIdValidator() {
        super(MIN, MAX, NULL_VALID);
    }
}
