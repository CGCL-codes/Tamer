package com.vmladenov.utils;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import com.vmladenov.Exceptions.XMLException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

/**
 * User: invincible
 * Date: 2006-12-17
 * Time: 20:00:53
 */
public class XMLUtils {

    /**
	 * Check if Attribute exists
	 *
	 * @param AttributeName Attribute name
	 * @param map           Attribute - Map
	 * @return is Exists
	 */
    public static Boolean hasAttribute(String AttributeName, NamedNodeMap map) {
        Boolean res = false;
        for (int i = 0; i < map.getLength(); i++) {
            if (map.item(i).getNodeName().equalsIgnoreCase(AttributeName)) {
                res = true;
            }
        }
        return res;
    }

    public static String getAttribute(String AttributeName, NamedNodeMap map) {
        String res = "";
        for (int i = 0; i < map.getLength(); i++) {
            if (map.item(i).getNodeName().equalsIgnoreCase(AttributeName)) {
                res = map.item(i).getNodeValue();
            }
        }
        return res;
    }

    /**
	 * Creates a new XML document and returns it.
	 *
	 * @return a new XML document without any data
	 * @throws XMLException if an error occurred creating the new XML document
	 */
    public static Document newDocument() throws XMLException {
        try {
            DocumentBuilderFactory factory;
            DocumentBuilder builder;
            factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(false);
            factory.setValidating(false);
            builder = factory.newDocumentBuilder();
            return builder.newDocument();
        } catch (ParserConfigurationException e) {
            throw new XMLException(e);
        }
    }

    /**
	 * Gets the first child element with the given name or returns null if one can't be found.
	 *
	 * @param parent     the parent element of the child element to search for
	 * @param childName  the name of the child element
	 * @param deepSearch - if True then the search will be performed on all levels
	 *                   If False then only Direct childs will be searched
	 * @return the first child element with the given name or null if one can't be found.
	 */
    public static Element getFirstChildElementNamed(Element parent, String childName, boolean deepSearch) {
        if (parent == null) {
            throw new NullPointerException("Parent element cannot be null");
        }
        NodeList children = parent.getChildNodes();
        Element child = null;
        for (int i = 0; i < children.getLength() && child == null; i++) {
            if (children.item(i).getNodeName().equalsIgnoreCase(childName)) {
                child = (Element) children.item(i);
            } else if ((deepSearch) && (children.item(i).getNodeType() == Element.ELEMENT_NODE)) {
                child = getFirstChildElementNamed((Element) children.item(i), childName, deepSearch);
            }
        }
        return child;
    }

    public static void toFile(Document doc, String filename) throws XMLException {
        TransformerFactory tFactory = TransformerFactory.newInstance();
        try {
            Transformer transformer = tFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            File f = new File(filename);
            FileWriter fw = new FileWriter(f);
            StreamResult result = new StreamResult(fw);
            transformer.transform(source, result);
        } catch (TransformerConfigurationException e) {
            throw new XMLException(e);
        } catch (IOException e) {
            throw new XMLException(e);
        } catch (TransformerException e) {
            throw new XMLException(e);
        }
    }

    public static Document StrToDocument(String str) throws XMLException {
        Document doc = null;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.parse(new InputSource(new StringReader(str)));
        } catch (ParserConfigurationException e) {
            throw new XMLException(e);
        } catch (SAXException e) {
            throw new XMLException(e);
        } catch (IOException e) {
            throw new XMLException(e);
        }
        return doc;
    }

    public static String DocToStr(Document doc) throws XMLException {
        StringWriter sw = new StringWriter();
        XMLSerializer ser = new XMLSerializer(sw, new OutputFormat(doc));
        try {
            ser.serialize(doc.getDocumentElement());
        } catch (IOException e) {
            throw new XMLException(e);
        }
        return sw.toString();
    }
}
