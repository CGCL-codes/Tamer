package com.google.buzz.parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;
import com.google.buzz.exception.BuzzIOException;
import com.google.buzz.exception.BuzzParsingException;
import com.google.buzz.model.BuzzUserProfile;
import com.google.buzz.parser.handler.UserProfileHandler;

/**
 * Parser for element: <b>user profile<b/>.
 * 
 * @author roberto.estivill
 */
public class BuzzUserProfileParser {

    /**
     * Parse an xml string into a BuzzUserProfile model object.<br/>
     * 
     * @param xmlResponse to be parsed.
     * @return the user profile.
     * @throws BuzzIOException if any IO error occurs.
     * @throws BuzzParsingException if a parsing error occurs.
     */
    public static BuzzUserProfile parseProfile(String xmlResponse) throws BuzzParsingException, BuzzIOException {
        UserProfileHandler handler;
        XMLReader xr;
        try {
            xr = XMLReaderFactory.createXMLReader();
            handler = new UserProfileHandler(xr);
            xr.setContentHandler(handler);
            xr.setErrorHandler(handler);
            xr.parse(new InputSource(new ByteArrayInputStream(xmlResponse.getBytes("UTF-8"))));
        } catch (SAXException e) {
            throw new BuzzParsingException(e);
        } catch (IOException e) {
            throw new BuzzIOException(e);
        }
        return handler.getProfile();
    }
}
