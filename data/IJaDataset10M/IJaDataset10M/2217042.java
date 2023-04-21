package gov.nasa.jpf.test;

/**
 * indicates we tried to create an object, which failed for some reason
 * (see details).
 * Used as a wrapper so that we don't have to deal with the gazillion of
 * different exception types during object creation
 */
public class TestException extends RuntimeException {

    public TestException(String details, Throwable cause) {
        super(details, cause);
    }

    public TestException(String details) {
        super(details);
    }
}
