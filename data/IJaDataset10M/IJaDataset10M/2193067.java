package net.sf.joafip.service;

import net.sf.joafip.NotStorableClass;

/**
 * data is inconsistent.
 * 
 * @author luc peuvrier
 * 
 */
@NotStorableClass
public class FilePersistenceDataCorruptedException extends Exception {

    /**
	 * 
	 */
    private static final long serialVersionUID = 3514149839625153039L;

    public FilePersistenceDataCorruptedException() {
        super();
    }

    public FilePersistenceDataCorruptedException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public FilePersistenceDataCorruptedException(final String message) {
        super(message);
    }

    public FilePersistenceDataCorruptedException(final Throwable cause) {
        super(cause);
    }
}
