package nu.staldal.xmlutil;

import java.io.PrintWriter;
import org.xml.sax.*;

/**
 * A filter to print messages to a PrintWriter for the events of a 
 * SAX2 ContentHandler. Useful for debugging.
 */
public class ContentHandlerSnooper implements ContentHandler {

    private static final boolean DEBUG = true;

    private ContentHandler ch;

    private PrintWriter out;

    /**
	 * Constructs a filter.
	 *
	 * @param ch  the SAX2 ContentHandler to fire events on.
	 * @param out  where to print the messages
	 */
    public ContentHandlerSnooper(ContentHandler ch, PrintWriter out) {
        this.ch = ch;
        this.out = out;
        if (DEBUG) out.println("New ContentHandlerSnooper");
    }

    public void setDocumentLocator(Locator locator) {
    }

    public void startDocument() throws SAXException {
        if (DEBUG) out.println("startDocument");
        ch.startDocument();
    }

    public void endDocument() throws SAXException {
        if (DEBUG) out.println("endDocument");
        ch.endDocument();
    }

    public void startElement(String namespaceURI, String localName, String qname, Attributes atts) throws SAXException {
        if (DEBUG) out.println("startElement(" + namespaceURI + ',' + localName + ',' + qname + ')');
        ch.startElement(namespaceURI, localName, qname, atts);
    }

    public void endElement(String namespaceURI, String localName, String qname) throws SAXException {
        if (DEBUG) out.println("endElement(" + namespaceURI + ',' + localName + ',' + qname + ')');
        ch.endElement(namespaceURI, localName, qname);
    }

    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        if (DEBUG) out.println("startPrefixMapping(" + ((prefix.length() == 0) ? "<default>" : prefix) + ',' + uri + ')');
        ch.startPrefixMapping(prefix, uri);
    }

    public void endPrefixMapping(String prefix) throws SAXException {
        if (DEBUG) out.println("endPrefixMapping(" + ((prefix.length() == 0) ? "<default>" : prefix) + ')');
        ch.endPrefixMapping(prefix);
    }

    public void characters(char[] chars, int start, int length) throws SAXException {
        if (DEBUG) out.println("characters");
        ch.characters(chars, start, length);
    }

    public void ignorableWhitespace(char[] chars, int start, int length) throws SAXException {
        if (DEBUG) out.println("ignorableWhitespace");
        ch.ignorableWhitespace(chars, start, length);
    }

    public void processingInstruction(String target, String data) throws SAXException {
        if (DEBUG) out.println("processingInstruction(" + target + ',' + data + ')');
        ch.processingInstruction(target, data);
    }

    public void skippedEntity(String name) throws SAXException {
        if (DEBUG) out.println("skippedEntity(" + name + ')');
        ch.skippedEntity(name);
    }
}
