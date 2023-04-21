package org.mobicents.protocols.smpp;

/**
 * NotBoundException
 * 
 * @version $Id: NotBoundException.java 452 2009-01-15 16:56:36Z orank $
 */
public class NotBoundException extends org.mobicents.protocols.smpp.SMPPRuntimeException {

    static final long serialVersionUID = 2L;

    public NotBoundException() {
    }

    /**
     * Construct a new NotBoundException with specified message.
     */
    public NotBoundException(String s) {
        super(s);
    }
}
