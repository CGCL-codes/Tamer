package org.academ.jabber.services;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import javax.xml.stream.*;
import javax.xml.stream.XMLInputFactory;
import static org.academ.jabber.constants.Constant.*;

/**
 *
 * @author artemy
 */
public class ParserXml {

    private XMLInputFactory inputFactory = null;

    private XMLStreamReader xmlReader = null;

    private String strXml;

    public ParserXml(String strXml) throws UnsupportedEncodingException {
        inputFactory = XMLInputFactory.newInstance();
        this.strXml = strXml;
    }

    public Map read() throws UnsupportedEncodingException, XMLStreamException {
        InputStream input = new ByteArrayInputStream(strXml.getBytes(ENCODING));
        xmlReader = inputFactory.createXMLStreamReader(input);
        Map mapXml = new HashMap();
        String key = null;
        String values = null;
        String oldkey = null;
        while (xmlReader.hasNext()) {
            int event = xmlReader.next();
            if (event == XMLStreamConstants.START_ELEMENT) {
                key = xmlReader.getLocalName();
            }
            if (event == XMLStreamConstants.CHARACTERS) {
                if (xmlReader.getText().length() > 1) {
                    if (key.equals(oldkey)) {
                        values = (String) mapXml.get(key);
                        values = values + xmlReader.getText();
                        mapXml.put(key, values);
                    } else {
                        oldkey = key;
                        mapXml.put(key, xmlReader.getText());
                    }
                }
            }
        }
        return mapXml;
    }
}
