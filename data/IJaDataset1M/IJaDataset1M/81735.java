package net.sf.buildbox.util;

import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.InputSource;
import java.util.LinkedList;
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.ParserConfigurationException;

public abstract class PathAwareSaxParser extends DefaultHandler {

    private LinkedList<String> parentStack = new LinkedList<String>();

    private StringBuilder currentChars = new StringBuilder();

    private XMLReader xmlReader;

    private String currentMiniPath;

    private SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();

    protected PathAwareSaxParser() throws ParserConfigurationException {
        saxParserFactory.setNamespaceAware(true);
        saxParserFactory.setValidating(false);
    }

    public void parse(File file) throws IOException, SAXException {
        try {
            createXmlReader();
            xmlReader.parse(file.getAbsolutePath());
        } catch (ParserConfigurationException e) {
            throw new SAXException(e.getMessage(), e);
        }
    }

    public void parse(InputSource input) throws IOException, SAXException {
        try {
            createXmlReader();
            xmlReader.parse(input);
        } catch (ParserConfigurationException e) {
            throw new SAXException(e.getMessage(), e);
        }
    }

    private void createXmlReader() throws ParserConfigurationException, SAXException {
        final SAXParser saxParser = saxParserFactory.newSAXParser();
        xmlReader = saxParser.getXMLReader();
        xmlReader.setContentHandler(this);
    }

    private void clearChars() {
        currentChars.setLength(0);
    }

    @Override
    public final void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        currentMiniPath = makeMiniPath(localName);
        paStartElement(uri, localName, qName, attributes);
        parentStack.add(localName);
        clearChars();
    }

    protected abstract void paStartElement(String uri, String localName, String qName, Attributes attributes) throws SAXException;

    @Override
    public final void endElement(String uri, String localName, String qName) throws SAXException {
        parentStack.removeLast();
        currentMiniPath = makeMiniPath(localName);
        paEndElement(uri, localName, qName);
        clearChars();
    }

    protected abstract void paEndElement(String uri, String localName, String qName) throws SAXException;

    @Override
    public final void characters(char ch[], int start, int length) throws SAXException {
        super.characters(ch, start, length);
        currentChars.append(ch, start, length);
    }

    protected final String getLastTextValue() {
        return currentChars.toString();
    }

    private String makeMiniPath(String localName) {
        final StringBuilder sb = new StringBuilder();
        final int sz = parentStack.size();
        if (sz > 0) {
            sb.append(parentStack.getLast());
        }
        sb.append('/');
        sb.append(localName);
        return sb.toString();
    }

    public String getPath(String delimiter, String localName) {
        final StringBuilder sb = new StringBuilder();
        for (String item : parentStack) {
            sb.append(item);
            sb.append(delimiter);
        }
        sb.append(localName);
        return sb.toString();
    }

    public String getMiniPath() {
        return currentMiniPath;
    }
}
