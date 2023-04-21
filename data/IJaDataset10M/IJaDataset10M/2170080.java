package org.itsnat.core.domutil;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Utility class used to easily iterate a DOM tree, specially DOM elements.
 *
 * <p>Most of methods have been inspired by <code>org.w3c.dom.traversal.TreeWalker</code>.</p>
 *
 * @author Jose Maria Arranz Santamaria
 */
public class ItsNatTreeWalker {

    /**
     * Informs whether the specified node has child elements.
     *
     * @param node the node to inspect.
     * @return true if the specified node has child elements.
     */
    public static boolean hasChildElements(Node node) {
        return (getFirstChildElement(node) != null);
    }

    /**
     * Returns the number of child elements of the specified node.
     *
     * @param node the node to inspect.
     * @return the number of child elements.
     */
    public static int getChildElementCount(Node node) {
        int count = 0;
        Element child = getFirstChildElement(node);
        while (child != null) {
            count++;
            child = getNextSiblingElement(child);
        }
        return count;
    }

    /**
     * Returns the first direct child <code>org.w3c.dom.Element</code> below the specified node.
     * Any child non-element is ignored.
     *
     * @param node the node parent.
     * @return the first child element. Null if the parent node has no child element.
     */
    public static Element getFirstChildElement(Node node) {
        Node child = node.getFirstChild();
        while (child != null) {
            if (child.getNodeType() == Node.ELEMENT_NODE) return (Element) child;
            child = child.getNextSibling();
        }
        return null;
    }

    /**
     * Returns the last direct child <code>org.w3c.dom.Element</code> below the specified node.
     * Any child non-element is ignored.
     *
     * @param node the node parent.
     * @return the last child element. Null if the parent node has no child element.
     */
    public static Element getLastChildElement(Node node) {
        Node child = node.getLastChild();
        while (child != null) {
            if (child.getNodeType() == Node.ELEMENT_NODE) return (Element) child;
            child = child.getPreviousSibling();
        }
        return null;
    }

    /**
     * Returns the next sibling <code>org.w3c.dom.Element</code> following the specified node.
     * Any non-element is ignored.
     *
     * @param node the original node.
     * @return the next sibling element. Null if the node has no next sibling element (last child element).
     */
    public static Element getNextSiblingElement(Node node) {
        Node sibling = node.getNextSibling();
        while (sibling != null) {
            if (sibling.getNodeType() == Node.ELEMENT_NODE) return (Element) sibling;
            sibling = sibling.getNextSibling();
        }
        return null;
    }

    /**
     * Returns the previous sibling <code>org.w3c.dom.Element</code> following the specified node.
     * Any non-element is ignored.
     *
     * @param node the original node.
     * @return the previous sibling element. Null if the node has no previous sibling element (first child element).
     */
    public static Element getPreviousSiblingElement(Node node) {
        Node sibling = node.getPreviousSibling();
        while (sibling != null) {
            if (sibling.getNodeType() == Node.ELEMENT_NODE) return (Element) sibling;
            sibling = sibling.getPreviousSibling();
        }
        return null;
    }

    /**
     * Returns the first parent <code>org.w3c.dom.Element</code> of specified node.
     * Any non-element parent is ignored.
     *
     * @param node the original node.
     * @return the first parent element. Null if the node has no parent element (for instance, the document root element).
     */
    public static Element getParentElement(Node node) {
        if (node == null) return null;
        Node parent = node.getParentNode();
        if (parent == null) return null;
        if (parent.getNodeType() == Node.ELEMENT_NODE) return (Element) parent;
        return null;
    }

    /**
     * Returns the previous <code>org.w3c.dom.Element</code> following the specified node in document order.
     * Any non-element is ignored.
     *
     * @param node the original node.
     * @return the previous element. Null if the node has no previous element.
     */
    public static Element getPreviousElement(Node node) {
        if (node == null) return null;
        Element prevSibling = getPreviousSiblingElement(node);
        if (prevSibling == null) {
            return getParentElement(node);
        }
        Element lastChild = getLastChildElement(prevSibling);
        if (lastChild == null) return prevSibling;
        Element prevChild;
        do {
            prevChild = lastChild;
            lastChild = getLastChildElement(prevChild);
        } while (lastChild != null);
        lastChild = prevChild;
        return lastChild;
    }

    /**
     * Returns the next <code>org.w3c.dom.Element</code> following the specified node in document order.
     * Any non-element is ignored.
     *
     * @param node the original node.
     * @return the next element. Null if the node has no next element.
     */
    public static Element getNextElement(Node node) {
        if (node == null) return null;
        Element result = getFirstChildElement(node);
        if (result != null) return result;
        result = getNextSiblingElement(node);
        if (result != null) return result;
        Element parent = getParentElement(node);
        while (parent != null) {
            result = getNextSiblingElement(parent);
            if (result != null) return result; else parent = getParentElement(parent);
        }
        return null;
    }

    /**
     * Returns the previous node following the specified node in document order.
     *
     * @param node the original node.
     * @return the previous node. Null if the node has no previous node.
     */
    public static Node getPreviousNode(Node node) {
        if (node == null) return null;
        Node prevSibling = node.getPreviousSibling();
        if (prevSibling == null) {
            return node.getParentNode();
        }
        Node lastChild = prevSibling.getLastChild();
        if (lastChild == null) return prevSibling;
        Node prevChild;
        do {
            prevChild = lastChild;
            lastChild = prevChild.getLastChild();
        } while (lastChild != null);
        lastChild = prevChild;
        return lastChild;
    }

    /**
     * Returns the next node following the specified node in document order.
     *
     * @param node the original node.
     * @return the next node. Null if the node has no next node.
     */
    public static Node getNextNode(Node node) {
        if (node == null) return null;
        Node result = node.getFirstChild();
        if (result != null) return result;
        result = node.getNextSibling();
        if (result != null) return result;
        Node parent = node.getParentNode();
        while (parent != null) {
            result = parent.getNextSibling();
            if (result != null) return result; else parent = parent.getParentNode();
        }
        return null;
    }

    /**
     * Returns the first direct child element with the specified tag name.
     *
     * @param parent the parent node.
     * @param tagName the tag name to search for, if an HTML tag use uppercase.
     * @return the first direct child element with this tag or null if not found.
     */
    public static Element getFirstChildElementWithTag(Node parent, String tagName) {
        Element child = getFirstChildElement(parent);
        while (child != null) {
            String currTagName = child.getTagName();
            if (currTagName.equals(tagName)) return child;
            child = getNextSiblingElement(child);
        }
        return null;
    }
}
