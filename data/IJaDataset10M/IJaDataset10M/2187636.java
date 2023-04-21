package info.repo.didl.impl.serialize;

import com.megginson.sax.XMLWriter;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DefaultHandler2;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * a wrapper 
 *
 * @author liu_x
 */
public class MegginsonXMLCopier extends DefaultHandler2 {

    private XMLWriter xmlwriter;

    /** Creates a new instance of MegginsontCopier */
    public MegginsonXMLCopier(Writer writer) {
        xmlwriter = new XMLWriter(writer);
    }

    /**
     * Implements SAX Handler
     */
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        xmlwriter.startElement(uri, localName, qName, attributes);
    }

    /**
     * Implements SAX Handler
     */
    public void endElement(String uri, String localName, String qName) throws SAXException {
        xmlwriter.endElement(uri, localName, qName);
    }

    /**
     * Implements SAX Handler
     */
    public void processingInstruction(String target, String data) throws SAXException {
        xmlwriter.processingInstruction(target, data);
    }

    /**
     * Implements SAX Handler
     */
    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        xmlwriter.startPrefixMapping(prefix, uri);
    }

    /**
     * Implements SAX Handler
     */
    public void endPrefixMapping(String prefix) throws SAXException {
        xmlwriter.endPrefixMapping(prefix);
    }

    /**
     * Implements SAX Handler
     */
    public void characters(char[] ch, int start, int length) throws SAXException {
        xmlwriter.characters(ch, start, length);
    }
}
