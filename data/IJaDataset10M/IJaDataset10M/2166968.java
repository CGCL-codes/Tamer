package net.sf.joafip.redblacktree.service;

import net.sf.joafip.NotStorableClass;

@NotStorableClass
public class RBTException extends Exception {

    /**
	 * 
	 */
    private static final long serialVersionUID = -2603574104014809099L;

    public RBTException() {
        super();
    }

    public RBTException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public RBTException(final String message) {
        super(message);
    }

    public RBTException(final Throwable cause) {
        super(cause);
    }
}
