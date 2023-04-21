package Implementing;

import java.util.HashMap;
import java.util.Map;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SAXHandler extends DefaultHandler {

    private String currentElement = null;

    public Map mapElements = new HashMap();

    @Override
    public void startElement(String uri, String local_name, String raw_name, Attributes amap) throws SAXException {
        currentElement = raw_name;
        System.out.println("start " + raw_name + " found ");
    }

    @Override
    public void endElement(String uri, String local_name, String raw_name) throws SAXException {
        System.out.println("end " + raw_name + " found");
    }

    @Override
    public void startDocument() throws SAXException {
        System.out.println("start document");
    }

    @Override
    public void endDocument() throws SAXException {
        System.out.println("end document");
    }

    /**
     *
     * @param ch
     * @param start
     * @param length
     * @throws SAXException
     */
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String value = new String(ch, start, length);
        if (!Character.isISOControl(value.charAt(0))) {
            mapElements.put(currentElement, value);
            System.out.println("characters " + currentElement + " found " + mapElements.get(currentElement));
        }
    }
}
