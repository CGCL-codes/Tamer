package com.icesoft.net.messaging.expression;

import com.icesoft.util.Validator;
import com.icesoft.net.messaging.IdentifierValidator;

public class Identifier implements Operand {

    private static final Validator IDENTIFIER_VALIDATOR = new IdentifierValidator();

    private String value;

    /**
     * <p>
     *   Creates a new Identifier object with the specified <code>value</code>.
     * </p>
     *
     * @param      value
     *                 the value of the Identifier.
     * @throws     IllegalArgumentException
     *                 if the specified <code>value</code> is not valid.
     */
    public Identifier(final String value) throws IllegalArgumentException {
        if (!IDENTIFIER_VALIDATOR.isValid(value)) {
            throw new IllegalArgumentException("Illegal value: " + value);
        }
        this.value = value;
    }

    /**
     * <p>
     *   Gets the value of this Identifier.
     * </p>
     *
     * @return     the value.
     */
    public String getValue() {
        return value;
    }

    public String toString() {
        return value;
    }
}
