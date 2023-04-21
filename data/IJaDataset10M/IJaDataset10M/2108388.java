package net.sf.f8api.xml;

import net.sf.f8api.FacebookException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Parse this:
 * 
 * <?xml version="1.0" encoding="UTF-8"?> <auth_createToken_response
 * xmlns="http://api.facebook.com/1.0/"
 * xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
 * xsi:schemaLocation="http://api.facebook.com/1.0/
 * http://api.facebook.com/1.0/facebook.xsd">3e4a22bb2f5ed75114b0fc9995ea85f1</auth_createToken_response>
 * 
 * 
 * @author <a href="mailto:jasonthrasher@gmail.com">Jason Thrasher</a>
 * 
 */
public class AuthCreateTokenHandler extends DefaultHandler {

    protected static Log log = LogFactory.getLog(AuthCreateTokenHandler.class);

    public static final String auth_createToken_response = "auth_createToken_response";

    private StringBuffer charContent = null;

    private String authToken;

    private ErrorResponseHandler errorHandler = null;

    public AuthCreateTokenHandler() {
        super();
    }

    public void startElement(String uri, String name, String qName, Attributes atts) {
        if (errorHandler != null) {
            errorHandler.startElement(uri, name, qName, atts);
            return;
        } else if (qName.equals(ErrorResponseHandler.error_response)) {
            log.warn("facebook error response");
            errorHandler = new ErrorResponseHandler();
            errorHandler.startElement(uri, name, qName, atts);
        }
        if (qName.equals(auth_createToken_response)) {
            charContent = new StringBuffer();
        }
    }

    public void endElement(String uri, String name, String qName) {
        if (errorHandler != null) {
            errorHandler.endElement(uri, name, qName);
            return;
        }
        if (qName.equals(auth_createToken_response)) {
            authToken = charContent.toString();
        }
    }

    public void characters(char ch[], int start, int length) {
        if (errorHandler != null) {
            errorHandler.characters(ch, start, length);
            return;
        }
        charContent.append(ch, start, length);
    }

    public String getAuthToken() throws FacebookException {
        if (errorHandler != null) {
            throw new FacebookException(errorHandler.getErrorResponse());
        }
        return authToken;
    }
}
