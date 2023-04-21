package org.lokee.punchcard.persistence;

import org.lokee.punchcard.PunchCardRunTimeException;

/**
 * @author CLaguerre
 *
 */
public class PersistentException extends PunchCardRunTimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 4120853244051534387L;

    /**
	 * @param message
	 * @param severity
	 * @param exception
	 */
    public PersistentException(String message, String severity, Exception exception) {
        super(message, severity, exception);
    }

    /**
	 * @param message
	 * @param severity
	 */
    public PersistentException(String message, String severity) {
        super(message, severity);
    }
}
