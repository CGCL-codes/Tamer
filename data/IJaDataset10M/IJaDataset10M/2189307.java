package net.sf.joafip.service;

import net.sf.joafip.NotStorableClass;

/**
 * throws when something is wrong with a class used by serialization
 * 
 * @author luc peuvrier
 * 
 */
@NotStorableClass
public class FilePersistenceInvalidClassException extends Exception {

    /**
	 * 
	 */
    private static final long serialVersionUID = 6851742097588803898L;

    public FilePersistenceInvalidClassException() {
        super();
    }

    public FilePersistenceInvalidClassException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public FilePersistenceInvalidClassException(final String message) {
        super(message);
    }

    public FilePersistenceInvalidClassException(final Throwable cause) {
        super(cause);
    }
}
