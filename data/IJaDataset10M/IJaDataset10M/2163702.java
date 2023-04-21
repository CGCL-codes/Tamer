package com.nex.content.xtm;

import org.xml.sax.AttributeList;
import org.xml.sax.HandlerBase;
import org.xml.sax.Parser;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.ParserFactory;
import org.xml.sax.ErrorHandler;
import org.apache.xerces.parsers.SAXParser;

/**
  * Note: extending HandlerBase is deprecated. Fixing that requires extending
  * org.xml.sax.helpers.DefaultHandler
  * References to AttributeList has been deprecated. Fixint that requires
  * org.xml.sax.Attributes
  * Staying as is means we are not Sax2 compatible.
  */
public class XTMSaxParser extends HandlerBase implements ErrorHandler {

    /** Default parser name. */
    protected static final String DEFAULT_PARSER_NAME = "org.apache.xerces.parsers.SAXParser";

    protected iXTMParserHandler host = null;

    public XTMSaxParser(iXTMParserHandler h) {
        this.host = h;
    }

    public void parse(InputSource ins) {
        try {
            System.out.println("Got input source " + ins);
            Parser parser = ParserFactory.makeParser(DEFAULT_PARSER_NAME);
            parser.setDocumentHandler(this);
            parser.setErrorHandler(this);
            System.out.println("Starting Parse");
            parser.parse(ins);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    /** Processing instruction. */
    public void processingInstruction(String target, String data) {
        host.handlePI(target, data);
    }

    /** Start document. */
    public void startDocument() {
        host.handleStartDocument();
    }

    /** Start element. */
    public void startElement(String name, AttributeList attrs) {
        host.handleStartElement(name, attrs);
    }

    /** Characters. */
    public void characters(char ch[], int start, int length) {
        host.handleCharacters(ch, start, length);
    }

    /** Ignorable whitespace. */
    public void ignorableWhitespace(char ch[], int start, int length) {
    }

    /** End element. */
    public void endElement(String name) {
        host.handleEndElement(name);
    }

    /** End document. */
    public void endDocument() {
        host.handleEndDocument();
    }

    /** Warning. */
    public void warning(SAXParseException ex) {
        System.err.println("[Warning] " + getLocationString(ex) + ": " + ex.getMessage());
    }

    /** Error. */
    public void error(SAXParseException ex) {
        System.err.println("[Error] " + getLocationString(ex) + ": " + ex.getMessage());
    }

    /** Fatal error. */
    public void fatalError(SAXParseException ex) throws SAXException {
        System.err.println("[Fatal Error] " + getLocationString(ex) + ": " + ex.getMessage());
        throw ex;
    }

    /** Returns a string of the location. */
    private String getLocationString(SAXParseException ex) {
        StringBuffer str = new StringBuffer();
        String systemId = ex.getSystemId();
        if (systemId != null) {
            int index = systemId.lastIndexOf('/');
            if (index != -1) systemId = systemId.substring(index + 1);
            str.append(systemId);
        }
        str.append(':');
        str.append(ex.getLineNumber());
        str.append(':');
        str.append(ex.getColumnNumber());
        return str.toString();
    }
}
