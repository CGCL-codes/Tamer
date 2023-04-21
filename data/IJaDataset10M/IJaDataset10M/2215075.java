package org.jives.exceptions;

/**
 * Thrown when a network exception occur
 * @see JivesException
 * @author simonesegalini
 */
@SuppressWarnings("serial")
public class JivesNetworkException extends JivesException {

    /**
	 * The constructor of the JivesNetworkException
	 * 
	 * @param logLevel 
	 * 					the logLevel of the exception
	 * 
	 * @param message
	 * 					the message specifying the exception
	 */
    public JivesNetworkException(int logLevel, String message) {
        super(logLevel, message);
    }
}
