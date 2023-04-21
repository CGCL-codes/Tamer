package org.ucdetector.example;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * detecting the error() method was very slow in one scenario!
 */
public class SlowExample extends DefaultHandler {

    @Override
    public void error(SAXParseException e) throws SAXException {
        System.err.println(e.getMessage());
    }

    public void error() {
    }
}
