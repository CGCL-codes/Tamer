package com.gargoylesoftware.base.testing;

/**
 *  A exception that will be thrown when a test is unable to initialize
 *
 * @version  $Revision: 1.4 $
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class TestInitializationFailedException extends RuntimeException {

    private static final long serialVersionUID = -291866727591753894L;

    private final Throwable enclosedException_;

    /**
     *  Create an instance
     *
     * @param  message A message
     */
    public TestInitializationFailedException(final String message) {
        this(message, null);
    }

    /**
     *  Create an instance
     *
     * @param  message A text message
     * @param  t The throwable that caused the failure
     */
    public TestInitializationFailedException(final String message, final Throwable t) {
        super(message);
        enclosedException_ = t;
    }

    /**
     *  Gets the enclosedException attribute of the
     *  TestInitializationFailedException object
     *
     * @return  The enclosedException value or null if no exception was
     *      specified
     */
    public Throwable getEnclosedException() {
        return enclosedException_;
    }
}
