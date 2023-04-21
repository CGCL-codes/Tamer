package org.openje.http.server.config.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public abstract class HInitParamDocumentHandler extends HBaseDocumentHandler {

    String paramName, paramValue;

    StringBuffer buf;

    public HInitParamDocumentHandler(HBaseDocumentHandler parent, XMLReader parser) {
        super(parent, parser);
    }

    public void startElement(String prefix, String uri, String qName, Attributes atts) throws SAXException {
        qName.trim();
        if (!"param-name".equals(qName) && !"param-value".equals(qName)) super.startElement(prefix, uri, qName, atts);
        buf = new StringBuffer();
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        buf.append(ch, start, length);
    }

    public void endElement(String prefix, String uri, String qName) throws SAXException {
        if ("param-name".equals(qName)) paramName = buf.toString().trim();
        if ("param-value".equals(qName)) paramValue = buf.toString(); else if ("init-param".equals(qName)) {
            endHandler();
            super.endElement(prefix, uri, qName);
        }
    }

    public abstract void endHandler() throws SAXException;
}
