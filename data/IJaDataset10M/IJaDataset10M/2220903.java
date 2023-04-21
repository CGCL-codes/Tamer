package com.alexmcchesney.delicious;

/**
 * Exception thrown when the server returns invalid xml
 * @author AMCCHESNEY
 *
 */
public class InvalidXMLException extends DeliciousException {

    /**
	 * Constructor taking a source exception
	 * @param source
	 */
    public InvalidXMLException(Throwable source) {
        super(m_resources.getString("INVALID_XML_ERROR"), source);
    }
}
