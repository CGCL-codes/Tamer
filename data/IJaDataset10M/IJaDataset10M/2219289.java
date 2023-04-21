package org.buglet.xml;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

/**
 *
 * @author  Registered User
 * @version
 */
public class DOMErrorHandler implements ErrorHandler {

    /** Creates new XBeta */
    public DOMErrorHandler() {
    }

    public void error(SAXParseException exception) {
        System.err.println("error:" + exception.getMessage());
    }

    public void fatalError(SAXParseException exception) {
        System.err.println("fatalError:" + exception.getMessage());
        System.exit(0);
    }

    public void warning(SAXParseException exception) {
        System.err.println("warning:" + exception.getMessage());
    }
}
