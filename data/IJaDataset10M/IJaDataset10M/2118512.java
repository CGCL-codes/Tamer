package lif.webclient.validator;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class SchemaValidationErrorsHandler extends DefaultHandler {

    public boolean validationError = false;

    public SAXParseException saxParseException = null;

    public void error(SAXParseException exception) throws SAXException {
        validationError = true;
        saxParseException = exception;
    }

    public void fatalError(SAXParseException exception) throws SAXException {
        validationError = true;
        saxParseException = exception;
    }

    public void warning(SAXParseException exception) throws SAXException {
    }
}
