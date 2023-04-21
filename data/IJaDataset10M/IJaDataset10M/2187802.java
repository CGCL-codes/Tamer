package org.torweg.phoneutria.util;

/**
 * Thrown, if an {@code DecomposedURL} cannot be correctly resolved.
 * 
 * @author Thomas Weber
 * @version $Revision$
 */
public class UnresolvableURLException extends PhoneutriaException {

    /**
	 * serialVersionUID.
	 */
    private static final long serialVersionUID = -7148250810901494175L;

    /**
	 * creates a new {@code UnresolvableURLException} with the given details
	 * message.
	 * 
	 * @param msg
	 *            the details message
	 */
    public UnresolvableURLException(final String msg) {
        super(msg);
    }
}
