package org.atricore.idbus.capabilities.sso.main;

/**
 * @author <a href="mailto:sgonzalez@atricore.org">Sebastian Gonzalez Oyuela</a>
 * @version $Id: SSOException.java 1245 2009-06-05 19:32:53Z sgonzalez $
 */
public class SSOException extends Exception {

    public SSOException() {
        super();
    }

    public SSOException(String message) {
        super(message);
    }

    public SSOException(String message, Throwable cause) {
        super(message, cause);
    }

    public SSOException(Throwable cause) {
        super(cause);
    }
}
