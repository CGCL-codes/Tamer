package com.googlecode.brui.beans;

/**
 * Indicates some text could not be parsed as a valid value of some type.
 */
public class ValidationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String invalidText;

    private final Class<?> validatedType;

    /**
	 * Create a new validation exception.
	 * 
	 * @param text the text being validated.
	 * @param type the type the text is being validated as.
	 */
    public ValidationException(final String text, final Class<?> type) {
        super(String.format("Input text '%s' is not a valid value for type '%s'.", text, type));
        this.invalidText = text;
        this.validatedType = type;
    }

    /**
	 * @return the text that was being validated.
	 */
    public String getInvalidText() {
        return invalidText;
    }

    /**
	 * @return the type that the text was being validated as.
	 */
    public Class<?> getValidatedType() {
        return validatedType;
    }
}
