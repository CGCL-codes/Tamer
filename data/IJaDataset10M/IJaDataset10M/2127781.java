package basis;

import java.io.OutputStreamWriter;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Super klasse van de XML Parsers
 */
public class XMLParser extends DefaultHandler {

    protected OutputStreamWriter out;

    protected SAXParserFactory factory;

    protected DefaultHandler handler;

    protected SAXParser saxParser;

    public XMLParser() {
        handler = this;
        factory = SAXParserFactory.newInstance();
        try {
            out = new OutputStreamWriter(System.out, "UTF8");
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public void characters(char buf[], int offset, int len) throws SAXException {
    }

    public void startElement(String nameSpaceURI, String lName, String qName, Attributes attrs) throws SAXException {
    }

    public void endElement(String namespaceURI, String lName, String qName) throws SAXException {
    }

    public boolean laadBestand(String bestandsNaam) {
        return true;
    }

    public boolean laadDirectory(String dir) {
        return true;
    }
}
