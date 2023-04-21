package org.adore.didl.content;

import info.repo.didl.DIDLException;
import java.io.InputStream;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * <code>DCTermsParser</code> is a SAX parser implementation for DCTerms
 * content types.  The de-serializers passes in an empty DCTerms object 
 * and parses the InputStream to populate a DCTerms object.
 * 
 * @author Kjell Lotigiers <kjell.lotigiers@ugent.be>
 * @author Patrick Hochstenbach <patrick.hochstenbach@ugent.be>
 * @author Xiaoming Liu <liu_x@lanl.gov>
 */
public class DCTermsParser extends DefaultHandler {

    private static final int BUFFER_SIZE = 1024;

    private InputStream stream;

    private StringBuffer value = new StringBuffer();

    private boolean inline = false;

    private DCTerms dcterm;

    /**
     * Parses an InputStream to populate the specified DCTerms object
     * @param dcterm empty DCTerms object to populate
     * @param stream InputStream containing DC XML fragment
     * @throws Exception error occurred during parse routine
     */
    public void parse(DCTerms dcterm, java.io.InputStream stream) throws Exception {
        this.dcterm = dcterm;
        XMLReader parser = XMLReaderFactory.createXMLReader(XMLConstants.DEFAULT_PARSER_NAME);
        parser.setFeature("http://xml.org/sax/features/namespaces", true);
        parser.setContentHandler(this);
        parser.parse(new InputSource(stream));
    }

    /**
     * Receive notification of the beginning of an element. 
     * If DCTerms namespace is found in the uri, the element is flagged for extraction.
     */
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        try {
            if (DCTerms.NAMESPACE.equals(uri)) {
                inline = true;
            } else throw new DIDLException(DIDLException.ALREADY_DEFINED, "not a DCTerms namespace");
        } catch (Exception ex) {
            throw new SAXException(ex);
        }
    }

    /**
     * Receive notification of the end of an element.
     * For DCTerms uri, checks localName agianst Key name, inserts dcterm value upon match.
     */
    public void endElement(String uri, String localName, String qName) throws SAXException {
        try {
            if (DCTerms.NAMESPACE.equals(uri)) {
                for (DCTerms.Key key : DCTerms.Key.values()) {
                    if (localName.equals(key.value())) {
                        dcterm.setKey(key);
                        dcterm.setValue(value.toString());
                        break;
                    }
                }
                if (dcterm.getKey() == null) {
                    throw new SAXException(localName + " is not expected");
                }
            } else {
                throw new SAXException(uri + " namespace is not expected");
            }
        } catch (Exception e) {
            throw new SAXException("SAXException in endElement", e);
        }
    }

    /**
     * Appends element to a string buffer if element is flagged for extraction
     */
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (inline) {
            value.append(new String(ch, start, length));
        }
    }
}
