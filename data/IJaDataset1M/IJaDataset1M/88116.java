package info.repo.didl.impl.serialize;

import com.megginson.sax.DataWriter;
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
public class MegginsonDataCopier extends DefaultHandler2 {

    private DataWriter xmlwriter;

    /** Creates a new instance of MegginsontCopier */
    public MegginsonDataCopier(Writer writer) {
        xmlwriter = new DataWriter(writer);
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
    public void characters(char[] ch, int start, int length) throws SAXException {
        xmlwriter.characters(ch, start, length);
    }
}
