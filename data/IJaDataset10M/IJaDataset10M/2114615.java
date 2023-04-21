package com.induslogic.uddi;

import java.io.*;
import java.util.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.*;
import com.induslogic.uddi.server.util.*;
import org.apache.soap.util.xml.*;

/**
 * UddiObject.java
 *
 *
 * Created: Wed Jul 11 09:58:55 2001
 *
 * @author <a href="mailto:tarung@induslogic.com"> "Tarun Garg</a>
 * @version
 */
public class UddiObject {

    protected Document doc;

    protected UddiObject() {
    }

    public UddiObject(String name) {
        doc = DocBuilder.getNewDocument();
        Element elem = doc.createElement(name);
        doc.appendChild(elem);
        setAttribute(UddiTags.XMLNS, "urn:uddi-org:api_v2");
    }

    public UddiObject(InputStream is) throws UDDIXmlException, IOException {
        doc = DocBuilder.parse(is);
    }

    protected UddiObject(Element e) {
        doc = DocBuilder.getNewDocument();
        Node importedNode = doc.importNode(e, true);
        doc.appendChild(importedNode);
        setAttribute(UddiTags.XMLNS, "urn:uddi-org:api_v2");
    }

    public String toString() {
        Element elem = doc.getDocumentElement();
        return DOM2Writer.nodeToString(elem);
    }

    public void addElement(UddiObject u) {
        Element e = u.getElement();
        Node importedNode = doc.importNode(e, true);
        Element elem = doc.getDocumentElement();
        elem.appendChild(importedNode);
    }

    public UddiObject getElement(String relativeName) throws UDDIXmlException {
        NodeList nodes = doc.getElementsByTagName(relativeName);
        int size = nodes.getLength();
        int numElements = 0;
        Node requiredNode = null;
        for (int i = 0; i < size; i++) {
            Node node = nodes.item(i);
            if (node.getNodeType() == node.ELEMENT_NODE) {
                numElements++;
                requiredNode = node;
            }
        }
        if (numElements == 0) {
            return null;
        }
        if (numElements > 1) {
            throw new UDDIXmlException("More than one element found. One expected");
        }
        return new UddiObject((Element) requiredNode);
    }

    public UddiObject getElementNS(String nameSpaceUri, String localName) throws UDDIXmlException {
        NodeList nodes = doc.getElementsByTagNameNS(nameSpaceUri, localName);
        int size = nodes.getLength();
        if (size == 0) {
            return null;
        }
        if (size > 1) {
            throw new UDDIXmlException("More than one element found. One expected");
        }
        Node node = nodes.item(0);
        if (node.getNodeType() != node.ELEMENT_NODE) {
            throw new UDDIXmlException("Element node expected. Found some other type.");
        }
        return new UddiObject((Element) node);
    }

    public Enumeration getChildElements() {
        Element elem = doc.getDocumentElement();
        Vector vec = new Vector();
        NodeList children = elem.getChildNodes();
        int size = children.getLength();
        for (int i = 0; i < size; i++) {
            Node child = children.item(i);
            if (child.getNodeType() == child.ELEMENT_NODE) {
                vec.addElement(new UddiObject((Element) child));
            }
        }
        return vec.elements();
    }

    public void setAttribute(String aName, String aValue) {
        if (aName == null) {
            return;
        }
        if (aValue == null) {
            aValue = "";
        }
        Element elem = doc.getDocumentElement();
        elem.setAttribute(aName, aValue);
    }

    public String getAttribute(String aName) {
        Element elem = doc.getDocumentElement();
        String attr = elem.getAttribute(aName).trim();
        if (attr.equals("")) return null;
        return attr;
    }

    /**
	Get all the children. Replace the very first text node with this value.
	If no text node is found, create one.
	*/
    public void setValue(String value) {
        if (value == null) {
            return;
        }
        Element elem = doc.getDocumentElement();
        NodeList nodes = elem.getChildNodes();
        int size = nodes.getLength();
        Node textNode = null;
        for (int i = 0; i < size; i++) {
            Node node = nodes.item(i);
            if (node.getNodeType() == node.TEXT_NODE) {
                textNode = node;
                break;
            }
        }
        if (textNode != null) {
            textNode.setNodeValue(value.trim());
            return;
        }
        textNode = doc.createTextNode(value.trim());
        elem.appendChild(textNode);
    }

    /**
	Returns the value of the first child that is a text node.
	*/
    public String getValue() {
        Element elem = doc.getDocumentElement();
        NodeList nodes = elem.getChildNodes();
        int size = nodes.getLength();
        for (int i = 0; i < size; i++) {
            Node node = nodes.item(i);
            if (node.getNodeType() == node.TEXT_NODE) {
                return node.getNodeValue().trim();
            }
        }
        return null;
    }

    public String getName() {
        Element elem = doc.getDocumentElement();
        return elem.getTagName().trim();
    }

    public Enumeration getElementsNamed(String name) {
        Element elem = doc.getDocumentElement();
        NodeList nodes = elem.getElementsByTagName(name);
        int size = nodes.getLength();
        Vector elemVec = new Vector(size);
        for (int i = 0; i < size; i++) {
            Node node = nodes.item(i);
            elemVec.addElement(new UddiObject((Element) node));
        }
        return elemVec.elements();
    }

    public Element getElement() {
        return doc.getDocumentElement();
    }

    public static void main(String arg[]) {
        try {
            FileInputStream fIn = new FileInputStream("bs.xml");
            UddiObject ud = new UddiObject(fIn);
            ud.setAttribute("myKey", "m");
            UddiObject ud2 = new UddiObject("newname");
            ud2.setValue("safe");
            ud.addElement(ud2);
            ud2.setValue("unsafe");
            ud.addElement(ud2);
            System.out.println(ud);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
