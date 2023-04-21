package bg.tu_sofia.refg.imsqti.commons.xml;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 *
 * @author ekim
 */
public class ErrorHandler implements org.xml.sax.ErrorHandler {

    private String systemId;

    /** Creates a new instance of ErrorHandler */
    public ErrorHandler(String filePath) {
        this.systemId = filePath;
    }

    public void warning(SAXParseException exception) throws SAXException {
        System.out.println("Warning: " + exception.getMessage());
    }

    public void error(SAXParseException exception) throws SAXException {
        String exceptionId = exception.getSystemId();
        int line = exception.getLineNumber();
        int column = exception.getColumnNumber();
        String message = exception.getMessage();
        message = "Error at " + exceptionId + " at line " + line + " column " + column + "\n" + "Error: " + message;
        System.err.println(message);
        if (message.contains("DOCTYPE")) {
            return;
        }
        if (message.contains("manifest")) {
            return;
        }
        throw new SAXException(message);
    }

    public void fatalError(SAXParseException exception) throws SAXException {
        System.out.println("Fatal Error: " + exception.getMessage());
    }
}
