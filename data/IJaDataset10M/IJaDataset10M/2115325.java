package net.sf.crispy.impl.rest;

import java.util.Hashtable;
import java.util.Vector;
import net.sf.crispy.util.Converter;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class RestContentHandler extends DefaultHandler {

    private Hashtable map = new Hashtable();

    private String name = null;

    private String type = null;

    private Integer parentRef = null;

    public Object getResult() {
        Object ret = map.get(Integer.valueOf("0"));
        return ret;
    }

    public void startElement(String pvUri, String pvLocalName, String pvName, Attributes pvAttributes) throws SAXException {
        name = pvName;
        type = null;
        parentRef = null;
        if (name.equals("CLASS")) {
            name = null;
            Integer id = Integer.valueOf(pvAttributes.getValue("id"));
            String className = pvAttributes.getValue("type");
            Integer parentRef = Integer.valueOf(pvAttributes.getValue("parentRef"));
            String lvName = pvAttributes.getValue("name");
            try {
                Object classObject = Class.forName(className).newInstance();
                map.put(id, classObject);
                Object parent = map.get(parentRef);
                if (parent != null) {
                    if (parent instanceof Hashtable) {
                        if (lvName != null) {
                            ((Hashtable) parent).put(lvName, classObject);
                        }
                    } else if (parent instanceof Vector) {
                        ((Vector) parent).add(classObject);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (name.equals("property")) {
            name = pvAttributes.getValue("name");
            type = pvAttributes.getValue("type");
            parentRef = Integer.valueOf(pvAttributes.getValue("parentRef"));
        }
    }

    public void endElement(String pvUri, String pvLocalName, String pvName) throws SAXException {
        name = null;
        type = null;
        parentRef = null;
    }

    public void characters(char[] pvCh, int pvStart, int pvLength) throws SAXException {
        StringBuffer sb = new StringBuffer();
        sb.append(pvCh, pvStart, pvLength);
        if (name != null) {
            Object lvClassObject = map.get(parentRef);
            if (name.equals("class")) {
                if (lvClassObject instanceof Hashtable) {
                    ((Hashtable) lvClassObject).put(name, sb.toString());
                } else if (lvClassObject instanceof Vector) {
                    System.out.println("--- Darf NICHT sein ---");
                }
            } else {
                try {
                    Object lvValue = Converter.convertString2Value(sb.toString(), type);
                    if (lvClassObject instanceof Hashtable) {
                        if (lvValue != null) {
                            ((Hashtable) lvClassObject).put(name, lvValue);
                        }
                    } else if (lvClassObject instanceof Vector) {
                        ((Vector) lvClassObject).add(sb.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if ((name == null) && (parentRef.equals(Integer.valueOf("-1")))) {
            try {
                Object lvValue = Converter.convertString2Value(sb.toString(), type);
                map.put(Integer.valueOf("0"), lvValue);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void startDocument() throws SAXException {
        map = new Hashtable();
        name = null;
    }

    public void endDocument() throws SAXException {
    }
}
