package org.nicaragua.helpers;

import org.nicaragua.helpers.SimpleXMLHandler;
import org.nicaragua.helpers.SimpleXMLParser;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class NamesByElementNameHandler extends SimpleXMLHandler {

    String elementName;

    String returnString = new String();

    String lineSeparator = System.getProperty("line.separator");

    public NamesByElementNameHandler(String elementName) {
        this.elementName = elementName;
    }

    public void startElement(String uri, String localName, String qualifiedName, Attributes attributes) throws SAXException {
        if (qualifiedName == this.elementName) {
            if (returnString.length() > 0) {
                returnString = (returnString + attributes.getValue("name") + lineSeparator);
            } else {
                returnString = (attributes.getValue("name") + lineSeparator);
            }
        }
    }

    public String getReturnString() {
        return returnString;
    }
}
