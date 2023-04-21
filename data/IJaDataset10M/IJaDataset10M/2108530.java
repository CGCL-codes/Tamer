package co.edu.uniquindio.chord.configurations;

import co.edu.uniquindio.chord.ChordException;

/**
 * The <code>ChordPropertiesException</code> class handles a configuration
 * exception, that could happen when trying to load the configuration files from
 * Chord project.
 * 
 * @author Daniel Pelaez
 * @author Hector Hurtado
 * @author Daniel Lopez
 * @version 1.0, 17/06/2010
 * @since 1.0
 */
public class ChordPropertiesException extends ChordException {

    /**
	 * Serial
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * Builds a exception by message
	 * 
	 * @param message
	 *            Message
	 */
    public ChordPropertiesException(String message) {
        super(message);
    }

    /**
	 * Builds a exception by message and throwable
	 * 
	 * @param message
	 *            Message
	 * @param throwable
	 *            Exception or error
	 */
    public ChordPropertiesException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
