package org.aoplib4j.failurehandling;

/**
 * Exception used for testing purposes.
 * 
 * @author Adrian Citu
 *
 */
public class TestingException extends Exception {

    /**
     * serial version id.
     */
    private static final long serialVersionUID = 1486860741799606755L;

    /**
     * @param message the error message.
     */
    public TestingException(final String message) {
        super(message);
    }
}
