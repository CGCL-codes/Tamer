package de.sepp.aigaebeditormodule.extension;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 *
 * @author "Boris Koprinarov ( bkoprinarov@googlemail.com )"
 */
public class JAXBNamespaceFilterHandler implements ContentHandler {

    ContentHandler _handler;

    public JAXBNamespaceFilterHandler(ContentHandler ch) {
        _handler = ch;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        if (!qName.equals(localName)) {
            _handler.startElement("", localName, localName, atts);
        } else {
            _handler.startElement(uri, localName, qName, atts);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        _handler.characters(ch, start, length);
    }

    @Override
    public void endDocument() throws SAXException {
        _handler.endDocument();
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        _handler.endElement(uri, localName, qName);
    }

    @Override
    public void endPrefixMapping(String prefix) throws SAXException {
        _handler.endPrefixMapping(prefix);
    }

    @Override
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
        _handler.ignorableWhitespace(ch, start, length);
    }

    @Override
    public void processingInstruction(String target, String data) throws SAXException {
        _handler.processingInstruction(target, data);
    }

    @Override
    public void setDocumentLocator(Locator locator) {
        _handler.setDocumentLocator(locator);
    }

    @Override
    public void skippedEntity(String name) throws SAXException {
        _handler.skippedEntity(name);
    }

    @Override
    public void startDocument() throws SAXException {
        _handler.startDocument();
    }

    @Override
    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        _handler.startPrefixMapping(prefix, uri);
    }
}
