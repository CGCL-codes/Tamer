package org.es.uma.XMLEditor.xerces;

import java.util.Vector;
import java.util.Enumeration;
import org.xml.sax.SAXParseException;
import org.es.uma.XMLEditor.gui.MessageDisplayer;

/**
 * This class implements an error handler that stores all errors during
 * parses and can give them back
 */
public class ErrorHandler implements org.xml.sax.ErrorHandler {

    /**
     * This Vector stores all the errors occured during parsing
     */
    private Vector errorNodes = new Vector();

    /**
     * called when a warning occurs during parsing
     * @param ex The SAX parse exception
     */
    public void warning(SAXParseException ex) {
        store(ex, "[Warning]");
    }

    /**
     * called when an error occurs during parsing
     * @param ex The SAX parse exception
     */
    public void error(SAXParseException ex) {
        store(ex, "[Error]");
    }

    /**
     * called when a fatasl error occurs during parsing
     * @param ex The SAX parse exception
     */
    public void fatalError(SAXParseException ex) {
        store(ex, "[Fatal Error]");
    }

    /**
     * displays all errors stored in this errorHandler
     * @param msgDis the MessageDisplayer used for error displaying
     */
    public void displayErrors(MessageDisplayer msgDis) {
        if (errorNodes.size() > 100) {
            msgDis.displayMessage("**********************************************************************\n" + "There were more than 100 errors while parsing. This should not be xml.\n" + "**********************************************************************\n");
        } else {
            Enumeration errors = errorNodes.elements();
            while (errors.hasMoreElements()) {
                msgDis.displayMessage((String) errors.nextElement());
            }
        }
    }

    /**
     * resets the error handler by clearing all errors
     */
    public void reset() {
        errorNodes.clear();
    }

    /**
     * stores an error
     * @param ex the exception received due to this error
     * @param type a string giving the level of error (warning, error or fatal error)
     */
    private void store(SAXParseException ex, String type) {
        String errorString = type + " at line number, " + ex.getLineNumber() + ": " + ex.getMessage();
        errorNodes.add(errorString);
    }

    /**
     * says whether the last parsing did generate errors or not
     * @return true if it did
     */
    public boolean errorsExisting() {
        return !errorNodes.isEmpty();
    }
}
