package com.versusoft.packages.xml;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 *
 * @author vince
 */
public class MySAXErrorHandler implements ErrorHandler {

    private boolean error = false;

    private int typeError = 0;

    private int line = 0;

    private String msg = null;

    public void warning(SAXParseException exception) throws SAXException {
        line = exception.getLineNumber();
        msg = exception.getMessage();
        error = true;
    }

    public void error(SAXParseException exception) throws SAXException {
        line = exception.getLineNumber();
        msg = exception.getMessage();
        error = true;
    }

    public void fatalError(SAXParseException exception) throws SAXException {
        line = exception.getLineNumber();
        msg = exception.getMessage();
        error = true;
    }

    public String getMessage() {
        return msg;
    }

    public int getLineNumber() {
        return line;
    }

    public boolean hadError() {
        return error;
    }
}
