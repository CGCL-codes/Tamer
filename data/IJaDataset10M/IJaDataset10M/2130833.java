package com.turnengine.client.local.player.validator;

import com.javabi.common.validator.type.IntegerValidator;

/**
 * The Player Limit Validator.
 */
public class PlayerLimitValidator extends IntegerValidator {

    /** The min. */
    public static final int MIN = 0;

    /** The max. */
    public static final int MAX = 2147483647;

    /** The null valid. */
    public static final boolean NULL_VALID = false;

    /** The instance. */
    public static final PlayerLimitValidator INSTANCE = new PlayerLimitValidator();

    /**
	 * Creates a new Player Limit Validator.
	 */
    public PlayerLimitValidator() {
        super(MIN, MAX, NULL_VALID);
    }
}
