package picounit.ruby.exception;

import picounit.exception.PicoUnitException;

public class InvalidExpression extends PicoUnitException {

    private static final long serialVersionUID = 1L;

    public InvalidExpression(String context, String expression, Exception cause) {
        super(String.format("%s(%s)", context, expression), cause);
    }
}
