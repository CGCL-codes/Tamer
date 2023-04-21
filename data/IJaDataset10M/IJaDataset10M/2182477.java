package com.turnengine.client.global.game.validator;

import com.javabi.common.validator.type.IntegerValidator;

/**
 * The Game Instance Id Validator.
 */
public class GameInstanceIdValidator extends IntegerValidator {

    /** The min. */
    public static final int MIN = 0;

    /** The max. */
    public static final int MAX = 2147483647;

    /** The null valid. */
    public static final boolean NULL_VALID = false;

    /** The instance. */
    public static final GameInstanceIdValidator INSTANCE = new GameInstanceIdValidator();

    /**
	 * Creates a new Game Instance Id Validator.
	 */
    public GameInstanceIdValidator() {
        super(MIN, MAX, NULL_VALID);
    }
}
