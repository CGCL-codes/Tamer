package org.josso.gateway;

/**
 * Standard SSO Exception
 *
 * @author <a href="mailto:sgonzalez@josso.org">Sebastian Gonzalez Oyuela</a>
 * @version $Id: SSOException.java 543 2008-03-18 21:34:58Z sgonzalez $
 */
public class SSOException extends Exception {

    public SSOException() {
        super();
    }

    public SSOException(String message) {
        super(message);
    }

    public SSOException(Throwable cause) {
        super(cause);
    }

    public SSOException(String message, Throwable cause) {
        super(message, cause);
    }
}
