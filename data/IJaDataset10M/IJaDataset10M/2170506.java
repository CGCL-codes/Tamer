package com.nitbcn.lib.xml;

import java.io.StringReader;
import java.util.Hashtable;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.XMLReaderFactory;
import org.apache.xerces.parsers.SAXParser;

/**
 *
 * @author raimon
 */
public class XMLReader {

    public XMLReader() {
    }

    public Hashtable<String, Object> read(String xmlString) {
        try {
            org.xml.sax.XMLReader r = XMLReaderFactory.createXMLReader();
            r.setFeature("http://xml.org/sax/features/validation", true);
            r.setFeature("http://apache.org/xml/features/validation/schema", true);
            r.setFeature("http://apache.org/xml/features/validation/schema-full-checking", true);
            XMLHandler def = new XMLHandler();
            r.setContentHandler(def);
            r.setErrorHandler(def);
            r.setProperty("http://xml.org/sax/properties/lexical-handler", def);
            r.parse(new InputSource(new StringReader(xmlString)));
            return def.getXMLDocument();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
