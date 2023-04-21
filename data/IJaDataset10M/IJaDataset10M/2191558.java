package uk.ac.ncl.cs.instantsoap.esciencetool.cline.xmlParser;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import java.util.List;
import java.util.LinkedList;
import java.util.logging.*;
import java.io.StringWriter;

/**
 * Created by IntelliJ IDEA.
 * User: louis
 * Date: 13-Oct-2006
 * Time: 23:15:56
 * To change this backupTemplate use FileData | Settings | FileData Templates.
 */
final class DomExtractor {

    private DomExtractor() {
    }

    /**
     *
     * @param arguments
     * @return a list of direct child elements, or zero-length list is there is none
     */
    static List<Element> extractDirectChildElements(Element arguments) {
        if (arguments == null) throw new IllegalArgumentException("arguments must not be null");
        Node node = arguments.getFirstChild();
        List<Element> argumentList = new LinkedList<Element>();
        if (node == null) {
            return argumentList;
        }
        Element element;
        if (!(node instanceof Element)) {
            element = getNextSiblingElement(node);
            if (element == null) {
                return argumentList;
            }
        } else {
            element = (Element) node;
        }
        while (element != null) {
            argumentList.add(element);
            element = getNextSiblingElement(element);
        }
        return argumentList;
    }

    /**
     *
     * @param node
     * @return next element or null
     */
    static Element getNextSiblingElement(Node node) {
        if (node == null) {
            throw new IllegalArgumentException("argument must not be null");
        }
        Node next = node.getNextSibling();
        if (next == null) {
            return null;
        } else if (!(next instanceof Element)) {
            return getNextSiblingElement(next);
        } else {
            return (Element) next;
        }
    }

    /**
     *
     * @param parent
     * @param namespace
     * @param childTagName
     * @return element or null
     */
    static Element extractUniqueChildElement(Element parent, String namespace, String childTagName) {
        boolean inputIsNull = (parent == null) || (namespace == null) || (childTagName == null);
        if (inputIsNull) {
            throw new IllegalArgumentException("Input arguments must " + "not be null");
        }
        NodeList children = parent.getElementsByTagNameNS(namespace, childTagName);
        int length = children.getLength();
        Element targetElement = null;
        if (length == 0) {
        } else if (length == 1) {
            Node node = children.item(0);
            if (node instanceof Element) {
                targetElement = (Element) node;
            } else {
            }
        } else {
            throw new IllegalArgumentException("parent element " + parent + " contains " + length + " " + childTagName + " while " + childTagName + " element should be less or equal to one");
        }
        return targetElement;
    }

    static void validateElement(Element element, String tagName) {
        boolean isArgNull = (element == null) || (tagName == null);
        if (isArgNull) {
            throw new IllegalArgumentException("Input element must not be null");
        }
        String tName = element.getTagName();
        if (!(tName.equals(tagName))) {
            throw new IllegalArgumentException("Input element must be " + tagName + " but found: " + tName);
        }
    }

    static String elementContentsToString(Element element) {
        NodeList childern = element.getChildNodes();
        if (childern.getLength() == 0) {
            return element.getTagName();
        }
        DOMSource source = new DOMSource(element);
        StringWriter result = new StringWriter();
        StreamResult streamResult = new StreamResult(result);
        Transformer transformer = null;
        try {
            transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(source, streamResult);
        } catch (TransformerConfigurationException e) {
            throw new IllegalStateException("error occured while creating transformer to turn " + "DOM into string", e);
        } catch (TransformerException e) {
            throw new IllegalStateException("error occured while turning xml to string", e);
        }
        return rootElementRemover(xmlHeaderRemover(decode(result.toString())));
    }

    private static String decode(String xmlString) {
        xmlString = xmlString.replace("&gt;", ">");
        xmlString = xmlString.replace("&lt;", "<");
        xmlString = xmlString.replace("&amp;", "&");
        return xmlString;
    }

    private static String xmlHeaderRemover(String xmlString) {
        int flag = xmlString.indexOf("?>");
        if (flag < 0) return xmlString;
        return xmlString.substring(flag + 2);
    }

    private static String rootElementRemover(String xmlString) {
        int beginning = xmlString.indexOf(">");
        int end = xmlString.lastIndexOf("<");
        if ((beginning < 0) || (end < 0)) {
            throw new IllegalArgumentException("Input string is not in XML format");
        }
        return xmlString.substring(beginning + 1, end);
    }
}
