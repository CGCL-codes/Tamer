package org.vikamine.kernel.formula.exception;

import java.util.Stack;

/**
 * Exception when a closing brace is missing. 
 * 
 * @author Tobias Vogele
 */
public class MissingClosingBraceException extends ASTBuildingException {

    private static final long serialVersionUID = -4732635770543972634L;

    public MissingClosingBraceException() {
        super();
    }

    public MissingClosingBraceException(String message) {
        super(message);
    }

    public MissingClosingBraceException(String message, Stack operandStack, Stack operatorStack) {
        super(message, operandStack, operatorStack);
    }
}
