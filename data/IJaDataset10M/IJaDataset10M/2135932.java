package kpc.xml;

import java.io.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

public class XMLParser extends DefaultHandler {

    public XMLParser() {
        super();
    }

    public void parse(String data) {
        try {
            XMLReader r = XMLReaderFactory.createXMLReader();
            r.setContentHandler(this);
            r.setErrorHandler(this);
            r.parse(new InputSource(new StringReader(data)));
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
