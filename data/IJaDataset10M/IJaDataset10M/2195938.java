package org.dvb.application;

/**
 * The <code>IllegalProfileParameter</code> exception is thrown if the
 * application attempts to ask for a version number for a profile not specified
 * for the application.
 * @since MHP1.0
 */
public class IllegalProfileParameterException extends Exception {

    /**
	 * Construct a IllegalProfileParameterException with no detail message
	 */
    public IllegalProfileParameterException() {
    }

    /**
	 * Construct a IllegalProfileParameterException with a detail message
	 * @param s detail message
	 */
    public IllegalProfileParameterException(String s) {
        super(s);
    }
}
