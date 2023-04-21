package stamina.kernel.environment.builder;

import stamina.kernel.environment.HttpBeanEnvironment;
import stamina.kernel.environment.event.*;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.LexicalHandler;

class HeaderHandler extends DefaultHandler {

    HttpBeanEnvironment env;

    XMLReader parser;

    ContentHandler oldHandler;

    public HeaderHandler(XMLReader parser) {
        super();
        this.parser = parser;
    }

    public void takeControl() {
        oldHandler = parser.getContentHandler();
        parser.setContentHandler(this);
    }

    public void setParser(XMLReader parser) {
        this.parser = parser;
    }

    ;

    public void element(String uri, String localpart, String rawname, Attributes attributes) throws SAXException {
        int length = attributes.getLength();
        for (int i = 0; i < length; i++) {
            String attrURI = attributes.getURI(i);
            String attrLocalpart = attributes.getLocalName(i);
            String attrRawname = attributes.getQName(i);
            System.out.print("URI " + attrURI + "  localpart" + attrLocalpart + "  rawname" + attrRawname + "}=");
            System.out.println(attributes.getValue(i));
        }
    }

    public void startElement(String uri, String localpart, String rawname, Attributes attributes) throws SAXException {
    }

    public void endElement(String uri, String localpart, String rawname) throws SAXException {
        if (rawname.equalsIgnoreCase("HEADER")) {
            parser.setContentHandler(oldHandler);
        }
    }
}
