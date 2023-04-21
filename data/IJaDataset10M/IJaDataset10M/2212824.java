package de.fhdarmstadt.fbi.dtree.model;

/**
 * An exception indicating that the used pattern does not fit the given
 * alphabet. The character that triggered the error is supplied. There is
 * no guarantee, that the pattern does not contain additional invalid characters.
 */
public final class IllegalPatternException extends IllegalArgumentException {

    /** The character that caused the trouble. */
    private Character invalidCharacter;

    /**
   * Createa a new exception with no additional message using the given
   * character as cause.
   *
   * @param invalidCharacter the invalid character.
   */
    public IllegalPatternException(final Character invalidCharacter) {
        this.invalidCharacter = invalidCharacter;
    }

    /**
   * Createa a new exception with the given message using the given
   * character as cause.
   *
   * @param invalidCharacter the invalid character.
   * @param message a text describing the error
   */
    public IllegalPatternException(final String message, final Character invalidCharacter) {
        super(message);
        this.invalidCharacter = invalidCharacter;
    }

    /**
   * Returns the invalid character that caused the trouble or
   * null, if the character is not known.
   *
   * @return the invalid character.
   */
    public final Character getInvalidCharacter() {
        return invalidCharacter;
    }
}
