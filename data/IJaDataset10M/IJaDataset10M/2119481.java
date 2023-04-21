package com.alexmcchesney.twitter;

/**
 * Exception thrown when the returned message is valid xml
 * @author AMCCHESNEY
 *
 */
public class UnknownMessageException extends TwitterException {

    /**
	 * Constructor
	 */
    public UnknownMessageException() {
        super(m_resources.getString("UNKNOWN_MESSAGE_ERROR"));
    }

    /**
	 * Constructor taking source exception thrown when trying to parse a value
	 */
    public UnknownMessageException(Throwable source) {
        super(m_resources.getString("UNKNOWN_MESSAGE_ERROR"), source);
    }
}
