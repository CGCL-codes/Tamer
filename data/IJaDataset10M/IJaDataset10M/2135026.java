package com.volantis.xml.schema.validator;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Acts like a tee junction sending the error events to two separate handlers.
 */
public class TeeErrorHandler implements ErrorHandler {

    private ErrorHandler first;

    private ErrorHandler second;

    public ErrorHandler getFirst() {
        return first;
    }

    public void setFirst(ErrorHandler first) {
        this.first = first;
    }

    public ErrorHandler getSecond() {
        return second;
    }

    public void setSecond(ErrorHandler second) {
        this.second = second;
    }

    public void error(SAXParseException exception) throws SAXException {
        dispatchError(first, exception);
        dispatchError(second, exception);
    }

    private void dispatchError(ErrorHandler handler, SAXParseException exception) throws SAXException {
        if (handler != null) {
            handler.error(exception);
        }
    }

    public void fatalError(SAXParseException exception) throws SAXException {
        dispatchFatalError(first, exception);
        dispatchFatalError(second, exception);
        throw exception;
    }

    private void dispatchFatalError(ErrorHandler handler, SAXParseException exception) throws SAXException {
        if (handler != null) {
            handler.fatalError(exception);
        }
    }

    public void warning(SAXParseException exception) throws SAXException {
        dispatchWarning(first, exception);
        dispatchWarning(second, exception);
    }

    private void dispatchWarning(ErrorHandler handler, SAXParseException exception) throws SAXException {
        if (handler != null) {
            handler.warning(exception);
        }
    }
}
