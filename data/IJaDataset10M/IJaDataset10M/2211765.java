package org.atricore.idbus.capabilities.josso.main;

/**
 * @author <a href="mailto:sgonzalez@atricore.org">Sebastian Gonzalez Oyuela</a>
 * @version $Id$
 */
public class JossoException extends Exception {

    public JossoException() {
        super();
    }

    public JossoException(String message) {
        super(message);
    }

    public JossoException(String message, Throwable cause) {
        super(message, cause);
    }

    public JossoException(Throwable cause) {
        super(cause);
    }
}
