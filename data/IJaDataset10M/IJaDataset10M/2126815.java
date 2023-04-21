package pub.utils;

import org.xml.sax.*;
import org.xml.sax.helpers.XMLReaderFactory;
import pub.xml.LocalDtdEntityResolver;
import java.io.*;
import java.util.Iterator;
import pub.xml.SaxPullParser;

/**
 * A few utility functions for working with Xml documents.  Includes a
 * method for constructing Xml parsers.
 */
public class XmlUtils {

    /**
     * Returns a new XMLReader class.
     */
    public static XMLReader constructSaxParser() throws SAXException {
        pub.utils.Log.getLogger(pub.utils.XmlUtils.class).debug("Constructing XML parser from " + PubProperties.getXmlSaxParserClass());
        XMLReader parser = XMLReaderFactory.createXMLReader(PubProperties.getXmlSaxParserClass());
        turnOffValidation(parser);
        parser.setEntityResolver(new LocalDtdEntityResolver());
        return parser;
    }

    public static String getChildText(org.w3c.dom.Node node) {
        org.w3c.dom.NodeList children = node.getChildNodes();
        StringBuffer buffer = new StringBuffer();
        if (children != null) {
            for (int i = 0; i < children.getLength(); i++) {
                org.w3c.dom.Node childNode = children.item(i);
                if (childNode instanceof org.w3c.dom.Text) {
                    buffer.append(childNode.getNodeValue());
                }
            }
        }
        return buffer.toString();
    }

    /**
     * Attempt to turn off validation --- we might be off the network.
     * Furthermore, we can't trust DTDs much either --- if DTDs are
     * themselves not well formed, I don't want the XML parser to just
     * die on us.
     */
    private static void turnOffValidation(XMLReader parser) {
        Log.getLogger(pub.utils.XmlUtils.class).debug("XML sax parser validation is off");
        try {
            parser.setFeature("http://xml.org/sax/features/validation", false);
            parser.setFeature("http://xml.org/sax/features/external-general-entities", false);
            parser.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        } catch (Exception e) {
            Log.getLogger(XmlUtils.class).warn(e);
        }
    }

    public static boolean equivalent(String xmlText1, String xmlText2) throws SAXException {
        return prettyPrint(xmlText1).equals(prettyPrint(xmlText2));
    }

    public static String prettyPrint(String xmlText) throws SAXException {
        org.xml.sax.InputSource inputSource = new org.xml.sax.InputSource(new StringReader(xmlText));
        XMLReader reader = constructSaxParser();
        SaxPullParser parser = new SaxPullParser(reader);
        Iterator iter = new SaxPullParser.EventsWithoutWhitespaceIterator(parser.parse(inputSource));
        StringWriter buffer = new StringWriter();
        com.megginson.sax.DataWriter xmlWriter = new com.megginson.sax.DataWriter(buffer);
        xmlWriter.setIndentStep(2);
        feedSaxStreamIntoContentHandler(iter, xmlWriter);
        return buffer.toString();
    }

    private static void feedSaxStreamIntoContentHandler(Iterator iter, ContentHandler handler) throws SAXException {
        while (iter.hasNext()) {
            SaxPullParser.Event event = (SaxPullParser.Event) iter.next();
            if (event.getType().equals(SaxPullParser.Event.START_DOCUMENT)) {
                handler.startDocument();
            } else if (event.getType().equals(SaxPullParser.Event.END_DOCUMENT)) {
                handler.endDocument();
            } else if (event.getType().equals(SaxPullParser.Event.START_ELEMENT)) {
                handler.startElement(event.getNamespaceUri(), event.getLocalName(), event.getName(), event.getAttributes());
            } else if (event.getType().equals(SaxPullParser.Event.END_ELEMENT)) {
                handler.endElement(event.getNamespaceUri(), event.getLocalName(), event.getName());
            } else if (event.getType().equals(SaxPullParser.Event.CHARACTERS)) {
                handler.characters(event.getText().toCharArray(), 0, event.getText().length());
            }
        }
    }
}
