package net.sourceforge.xsdeclipse.transformer;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * collects a list of 'xs:element' elements and returns these as a bulk (List);
 *  
 * This class is not intended for client usage.
 * 
 * @author jmo
 *
 */
public class ElementHandler extends DefaultHandler {

    static final String ELEMENT = "element";

    static final String NAME = "name";

    private static Logger log = null;

    private int elementCount = 0;

    private int elementLevel = 0;

    private int maxLevel = 2;

    private List<String> elements = new ArrayList<String>();

    private List<String> elementTree = new ArrayList<String>();

    protected ElementHandler() {
        super();
        if (log == null) log = Logger.getLogger(ElementHandler.class);
    }

    public void startDocument() {
    }

    public void endDocument() {
    }

    public void startElement(String namespaceURI, String localName, String qualifiedName, Attributes atts) throws SAXException {
        if (localName.equals(ELEMENT)) {
            elementCount = elementCount + 1;
            elementLevel = elementLevel + 1;
            if (elementLevel > 1) {
                elements.add("/" + atts.getValue(NAME));
            } else {
                elements.add(atts.getValue(NAME));
            }
            if (elementLevel <= maxLevel) {
                String elementString = "";
                for (String element : elements) {
                    elementString = elementString + element;
                }
                elementTree.add(elementString);
            }
        }
    }

    public void endElement(String namespaceURI, String localName, String qualifiedName) throws SAXException {
        if (localName.equals(ELEMENT)) {
            elements.remove(elementLevel - 1);
            elementLevel = elementLevel - 1;
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
    }

    protected List<String> getElementTree() {
        return this.elementTree;
    }

    protected int getMaxLevel() {
        return maxLevel;
    }

    protected void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }
}
