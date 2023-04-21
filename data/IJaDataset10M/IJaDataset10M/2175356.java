package com.sevenirene.archetype.testingplatform.impl.exception;

/**
 * Thrown when the code under test makes an unexpected call - either when a different operation was expected or
 * when no operation at all was expected. Note that overloaded operations are considered to be different for the
 * purposes of testing. 
 */
public class UnexpectedOperationError extends Error {

    public UnexpectedOperationError() {
        super();
    }

    public UnexpectedOperationError(String s) {
        super(s);
    }
}
