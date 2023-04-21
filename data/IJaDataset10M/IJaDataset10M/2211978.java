package org.ikasan.common.xml.parser;

import org.xml.sax.Locator;
import org.xml.sax.SAXParseException;

/**
 * This class extends <code>org.xml.sax.SAXParseException</code>
 * that encapsulates an XML parse fatal error.
 *
 * @author Ikasan Development Team
 *
 */
public class FatalErrorSAXParseException extends SAXParseException {

    /** Serial GUID */
    private static final long serialVersionUID = 6266340366950391902L;

    /**
     * Creates a new instance of <code>FatalErrorSAXParseException</code>
     * with the specified message and locator.
     * 
     * @param message 
     * @param locator 
     */
    public FatalErrorSAXParseException(String message, Locator locator) {
        super(message, locator);
    }

    /**
     * Creates a new instance of <code>FatalErrorSAXParseException</code>
     * with the specified message, locator and the existing exception.
     * 
     * @param message 
     * @param locator 
     * @param e 
     */
    public FatalErrorSAXParseException(String message, Locator locator, Exception e) {
        super(message, locator, e);
    }

    /**
     * Creates a new instance of <code>FatalErrorSAXParseException</code>
     * with the specified message, public identifier of the entity,
     * system identifier of the entity, exception line number and
     * exception column number.
     * 
     * @param message 
     * @param publicId 
     * @param systemId 
     * @param lineNumber 
     * @param columnNumber 
     */
    public FatalErrorSAXParseException(String message, String publicId, String systemId, int lineNumber, int columnNumber) {
        super(message, publicId, systemId, lineNumber, columnNumber);
    }

    /**
     * Creates a new instance of <code>FatalErrorSAXParseException</code>
     * with the specified message, public identifier of the entity,
     * system identifier of the entity, exception line number,
     * exception column number and the existing exception.
     * 
     * @param message 
     * @param publicId 
     * @param systemId 
     * @param lineNumber 
     * @param columnNumber 
     * @param e 
     */
    public FatalErrorSAXParseException(String message, String publicId, String systemId, int lineNumber, int columnNumber, Exception e) {
        super(message, publicId, systemId, lineNumber, columnNumber, e);
    }

    /**
     * Creates a new instance of <code>FatalErrorSAXParseException</code>
     * with the specified existing <code>SAXParseException</code> instance.
     * 
     * @param e 
     */
    public FatalErrorSAXParseException(SAXParseException e) {
        super(e.getMessage(), e.getPublicId(), e.getSystemId(), e.getLineNumber(), e.getColumnNumber(), e.getException());
    }
}
