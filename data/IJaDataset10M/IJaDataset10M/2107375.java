package com.usoog.commons.gamecore.exception;

/**
 * This exception is thrown when a replay failed to load for some reason.
 *
 * @author Jimmy Axenhus
 * @author Hylke van der Schaaf
 */
public class FailedToLoadReplayException extends GameCoreException {

    /**
	 * Just for serialization compatibility.
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * Default constructor.
	 */
    public FailedToLoadReplayException() {
        super();
    }

    /**
	 * Constructor with a message.
	 *
	 * @param message The message to bring with this exception.
	 */
    public FailedToLoadReplayException(String message) {
        super(message);
    }

    /**
	 * Constructor with a message and a cause.
	 *
	 * @param message The message to bring with this exception.
	 * @param cause The cause of this exception being throwned.
	 */
    public FailedToLoadReplayException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
	 * Constructor with a cause.
	 *
	 * @param cause The cause of this exception being throwned.
	 */
    public FailedToLoadReplayException(Throwable cause) {
        super(cause);
    }
}
