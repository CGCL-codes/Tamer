package org.dbe.composer.wfengine.xml;

import java.text.MessageFormat;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Class used to process errors encountered during parse operation. This class extends
 * DefaultHandler so that it may be used by the SAX parser.
 */
public class XMLParserErrorHandler extends DefaultHandler {

    /** Message template to log parse warnings/errors */
    protected static final String sMsgTemplate = "{0} at line number {1} : {2}";

    /** Flag to indicate that parse warnings have occurred */
    private boolean mParseWarnings;

    /** Flag to indicate if parse errors and warnings should be logged. */
    private boolean mLoggingEnabled;

    /**
     * Default Constructor.
     */
    public XMLParserErrorHandler() {
        this(true);
    }

    /**
     * Constructor which takes as input a flag indicating if logging of errors is to be performed.
     */
    public XMLParserErrorHandler(boolean aLoggingEnabled) {
        mLoggingEnabled = aLoggingEnabled;
    }

    /**
     * Utility method to log any problems which occur while parsing a file.
     * @param aException exception which was thrown
     * @param aType severity of problem
     */
    protected void logError(SAXParseException aException, String aType) {
        if (isLoggingEnabled()) {
            MessageFormat mf = new MessageFormat(sMsgTemplate);
            String lineNum = new Integer(aException.getLineNumber()).toString();
            String msg = mf.format(new Object[] { aType, lineNum, aException.getMessage() });
            System.out.println(msg);
        }
    }

    /**
     * Required implementation of warning handler.
     * @see org.xml.sax.ErrorHandler#warning(org.xml.sax.SAXParseException)
     */
    public void warning(SAXParseException aException) {
        setParseWarnings(true);
        logError(aException, "[Warning]");
    }

    /**
     * Required implementation of error handler.
     * @see org.xml.sax.ErrorHandler#error(org.xml.sax.SAXParseException)
     */
    public void error(SAXParseException aException) {
        setParseWarnings(true);
        logError(aException, "[Error]");
    }

    /**
     * Required implementation of fatal error handler.
     * @see org.xml.sax.ErrorHandler#fatalError(org.xml.sax.SAXParseException)
     */
    public void fatalError(SAXParseException aException) throws SAXException {
        logError(aException, "[Fatal Error]");
        throw aException;
    }

    /**
     * Returns flag indicating if any warnings occurred during parse.
     */
    public boolean hasParseWarnings() {
        return mParseWarnings;
    }

    /**
     * Allows ability to reset warnings indicator used to monitor parse errors.
     */
    public void resetParseWarnings() {
        setParseWarnings(false);
    }

    /**
     * Allow ability to set or reset the error handler to indicate if it has warnings.
     * @param aWarnings True if warnings exist False if not
     */
    protected void setParseWarnings(boolean aWarnings) {
        mParseWarnings = aWarnings;
    }

    /**
     * Returns flag indicating if logging is enabled for parse error handler.
     */
    protected boolean isLoggingEnabled() {
        return mLoggingEnabled;
    }
}
