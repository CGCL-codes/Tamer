package edu.princeton.wordnet.browser.domw3c;

import javax.swing.JOptionPane;
import org.xml.sax.SAXParseException;

/**
 * Error diaog
 * 
 * @author <a href="mailto:bbou@ac-toulouse.fr">Bernard Bou</a>
 */
public class ErrorDialog extends ParseErrorLogger {

    /**
	 * Whether to skip message
	 */
    boolean skip = false;

    /**
	 * Constructor
	 */
    public ErrorDialog() {
        super();
    }

    /**
	 * Display message
	 * 
	 * @param thisLevel
	 *            level
	 * @param thisType
	 *            type
	 * @param thisSAXException
	 * @throws SAXParseException
	 */
    private void message(final String thisLevel, final int thisType, final SAXParseException thisSAXException) throws SAXParseException {
        final String thisLineArray[] = new String[6];
        thisLineArray[0] = thisLevel;
        thisLineArray[1] = "URI " + thisSAXException.getSystemId();
        thisLineArray[2] = "Line:" + thisSAXException.getLineNumber() + " Column:" + thisSAXException.getColumnNumber();
        thisLineArray[3] = "";
        thisLineArray[4] = thisSAXException.getMessage();
        if (thisType != JOptionPane.INFORMATION_MESSAGE) {
            if (!this.skip) {
                thisLineArray[5] = "Press 'Yes' to continue parsing.";
                final int thisAnswer = JOptionPane.showConfirmDialog(null, thisLineArray, "Continue parsing (Cancel to skip all)?", JOptionPane.YES_NO_CANCEL_OPTION, thisType);
                switch(thisAnswer) {
                    case JOptionPane.YES_OPTION:
                        return;
                    case JOptionPane.CANCEL_OPTION:
                        this.skip = true;
                        return;
                    case JOptionPane.NO_OPTION:
                        throw thisSAXException;
                }
            }
        } else {
            thisLineArray[5] = "This error is not recoverable.";
            JOptionPane.showMessageDialog(null, thisLineArray, "Fatal XML Error", thisType);
            throw thisSAXException;
        }
    }

    @Override
    public void terminate() {
        super.terminate();
    }

    @Override
    public void warning(final SAXParseException e) throws SAXParseException {
        super.warning(e);
        message("Warning reported by XML parser", JOptionPane.WARNING_MESSAGE, e);
    }

    @Override
    public void error(final SAXParseException e) throws SAXParseException {
        super.error(e);
        message("Non-fatal Error reported by XML parser", JOptionPane.ERROR_MESSAGE, e);
    }

    @Override
    public void fatalError(final SAXParseException e) throws SAXParseException {
        super.fatalError(e);
        message("Fatal Error reported by XML parser", JOptionPane.INFORMATION_MESSAGE, e);
    }
}
