package org.apache.ws.commons.schema.utils;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class XDOMUtil extends DOMUtil {

    /**
     * Creates a new instance of XDOMUtil
     */
    private XDOMUtil() {
    }

    public static Element getFirstChildElementNS(Node parent, String uri) {
        Node child = parent.getFirstChild();
        while (child != null) {
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                String childURI = child.getNamespaceURI();
                if (childURI != null && childURI.equals(uri)) {
                    return (Element) child;
                }
            }
            child = child.getNextSibling();
        }
        return null;
    }

    public static Element getNextSiblingElementNS(Node node, String uri) {
        Node sibling = node.getNextSibling();
        while (sibling != null) {
            if (sibling.getNodeType() == Node.ELEMENT_NODE) {
                String siblingURI = sibling.getNamespaceURI();
                if (siblingURI != null && siblingURI.equals(uri)) {
                    return (Element) sibling;
                }
            }
            sibling = sibling.getNextSibling();
        }
        return null;
    }

    public static boolean anyElementsWithNameNS(Element element, String uri, String name) {
        for (Element el = getFirstChildElementNS(element, uri); el != null; el = XDOMUtil.getNextSiblingElementNS(el, uri)) {
            if (el.getLocalName().equals(name) && el.getNamespaceURI().equals(uri)) {
                return true;
            }
        }
        return false;
    }
}
