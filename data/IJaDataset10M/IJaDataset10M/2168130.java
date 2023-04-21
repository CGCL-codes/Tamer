package net.sf.istcontract.wsimport.util.xml;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.util.Stack;

/**
 * This is a simple utility class that adapts SAX events into StAX
 * {@link XMLStreamWriter} events, bridging between
 * the two parser technologies.
 *
 * This ContentHandler does not own the XMLStreamWriter.  Therefore, it will
 * not close or flush the writer at any point.
 *
 * @author Ryan.Shoemaker@Sun.COM
 * @version 1.0
 */
public class ContentHandlerToXMLStreamWriter extends DefaultHandler {

    private final XMLStreamWriter staxWriter;

    private final Stack prefixBindings;

    public ContentHandlerToXMLStreamWriter(XMLStreamWriter staxCore) {
        this.staxWriter = staxCore;
        prefixBindings = new Stack();
    }

    public void endDocument() throws SAXException {
        try {
            staxWriter.writeEndDocument();
            staxWriter.flush();
        } catch (XMLStreamException e) {
            throw new SAXException(e);
        }
    }

    public void startDocument() throws SAXException {
        try {
            staxWriter.writeStartDocument();
        } catch (XMLStreamException e) {
            throw new SAXException(e);
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        try {
            staxWriter.writeCharacters(ch, start, length);
        } catch (XMLStreamException e) {
            throw new SAXException(e);
        }
    }

    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
        characters(ch, start, length);
    }

    public void endPrefixMapping(String prefix) throws SAXException {
    }

    public void skippedEntity(String name) throws SAXException {
        try {
            staxWriter.writeEntityRef(name);
        } catch (XMLStreamException e) {
            throw new SAXException(e);
        }
    }

    public void setDocumentLocator(Locator locator) {
    }

    public void processingInstruction(String target, String data) throws SAXException {
        try {
            staxWriter.writeProcessingInstruction(target, data);
        } catch (XMLStreamException e) {
            throw new SAXException(e);
        }
    }

    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        if (prefix == null) {
            prefix = "";
        }
        if (prefix.equals("xml")) {
            return;
        }
        prefixBindings.add(prefix);
        prefixBindings.add(uri);
    }

    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
        try {
            staxWriter.writeEndElement();
        } catch (XMLStreamException e) {
            throw new SAXException(e);
        }
    }

    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
        try {
            staxWriter.writeStartElement(getPrefix(qName), localName, namespaceURI);
            String uri, prefix;
            while (prefixBindings.size() != 0) {
                uri = (String) prefixBindings.pop();
                prefix = (String) prefixBindings.pop();
                if (prefix.length() == 0) {
                    staxWriter.setDefaultNamespace(uri);
                } else {
                    staxWriter.setPrefix(prefix, uri);
                }
                staxWriter.writeNamespace(prefix, uri);
            }
            writeAttributes(atts);
        } catch (XMLStreamException e) {
            throw new SAXException(e);
        }
    }

    /**
     * Generate a StAX writeAttribute event for each attribute
     *
     * @param atts
     *                attributes from the SAX event
     */
    private void writeAttributes(Attributes atts) throws XMLStreamException {
        for (int i = 0; i < atts.getLength(); i++) {
            final String prefix = getPrefix(atts.getQName(i));
            if (!prefix.equals("xmlns")) {
                staxWriter.writeAttribute(prefix, atts.getURI(i), atts.getLocalName(i), atts.getValue(i));
            }
        }
    }

    /**
     * Pull the prefix off of the specified QName.
     *
     * @param qName
     *                the QName
     * @return the prefix or the empty string if it doesn't exist.
     */
    private String getPrefix(String qName) {
        int idx = qName.indexOf(':');
        if (idx == -1) {
            return "";
        } else {
            return qName.substring(0, idx);
        }
    }
}
