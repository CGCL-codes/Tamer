package de.bea.domingo.map;

import de.bea.domingo.DNotesException;

/**
 * Signals errors during mapping from or to domingo.
 *
 * @author <a href="mailto:kriede@users.sourceforge.net">Kurt Riede</a>
 */
public class MapperRegistrationException extends DNotesException {

    /** serial version ID for serialization. */
    private static final long serialVersionUID = 3257572801899870265L;

    /**
     * Constructor.
     *
     * @param message The detail message for this exception.
     */
    public MapperRegistrationException(final String message) {
        super(message);
    }

    /**
     * Constructor.
     *
     * @param throwable the root cause of the exception
     */
    public MapperRegistrationException(final Throwable throwable) {
        super(throwable);
    }

    /**
     * Constructor.
     *
     * @param message The detail message for this exception.
     * @param throwable the root cause of the exception
     */
    public MapperRegistrationException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
