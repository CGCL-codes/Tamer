package org.gamegineer.game.core.system;

/**
 * A checked exception that indicates an error occurred during the creation of a
 * game system.
 * 
 * <p>
 * This class is not intended to be extended by clients.
 * </p>
 */
public final class GameSystemException extends Exception {

    /** Serializable class version number. */
    private static final long serialVersionUID = 6805756396717226953L;

    /**
     * Initializes a new instance of the {@code GameSystemException} class with
     * no detail message and no cause.
     */
    public GameSystemException() {
        super();
    }

    /**
     * Initializes a new instance of the {@code GameSystemException} class with
     * the specified detail message and no cause.
     * 
     * @param message
     *        The detail message; may be {@code null}.
     */
    public GameSystemException(final String message) {
        super(message);
    }

    /**
     * Initializes a new instance of the {@code GameSystemException} class with
     * no detail message and the specified cause.
     * 
     * @param cause
     *        The cause; may be {@code null}.
     */
    public GameSystemException(final Throwable cause) {
        super(cause);
    }

    /**
     * Initializes a new instance of the {@code GameSystemException} class with
     * the specified detail message and cause.
     * 
     * @param message
     *        The detail message; may be {@code null}.
     * @param cause
     *        The cause; may be {@code null}.
     */
    public GameSystemException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
