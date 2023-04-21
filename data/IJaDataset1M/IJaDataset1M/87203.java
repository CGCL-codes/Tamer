package com.foursoft.fourever.xmlfileio.exception;

/**
 * Thrown during XML processing whenever an unrecoverable situation occurs and
 * an XML file could ot be fully processed, e.g. when a Node is defined in the
 * XML File that is not defined in the Objectmodel. IOExceptions should not be
 * wrapped in this class but rather passed on directly.
 * 
 * 
 */
public class XMLProcessingException extends Exception {

    /**
	 * Creates an XMLProcessingException with a message describing the problem
	 * encountered in processing the XML file.
	 * 
	 * @param s
	 *            description of the problem which caused failure in the XML
	 *            file loading procedure
	 */
    public XMLProcessingException(String s) {
        super(s);
    }

    /**
	 * Creates an XMLProcessingException with a message and wraps the exception
	 * that has caused the XML file loading procedure to fail.
	 * 
	 * @param s
	 *            description of the problem which caused failure in the XML
	 *            file loading procedure
	 * @param t
	 *            the throwable that has caused the load procedure to fail.
	 */
    public XMLProcessingException(String s, Throwable t) {
        super(s, t);
    }
}
