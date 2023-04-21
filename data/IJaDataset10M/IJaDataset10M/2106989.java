package uk.ac.ebi.intact.application.dataConversion.util;

import org.w3c.dom.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class DOMUtil {

    public static class TagNotFoundException extends RuntimeException {

        public TagNotFoundException() {
        }

        public TagNotFoundException(Throwable cause) {
            super(cause);
        }

        public TagNotFoundException(String message) {
            super(message);
        }

        public TagNotFoundException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class MultipleTagFoundException extends RuntimeException {

        public MultipleTagFoundException() {
        }

        public MultipleTagFoundException(Throwable cause) {
            super(cause);
        }

        public MultipleTagFoundException(String message) {
            super(message);
        }

        public MultipleTagFoundException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static Element getFirstElement(final Element element, final String name) {
        if (element == null) {
            throw new IllegalArgumentException("You must give a non null DOM Element to parse.");
        }
        if (name == null || "".equals(name.trim())) {
            throw new IllegalArgumentException("You must give a non null/empty DOM Element's name to look for.");
        }
        final NodeList list = element.getChildNodes();
        Node myNode = null;
        for (int i = 0; i < list.getLength(); i++) {
            final Node node = list.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            if (name.equals(((Element) node).getTagName())) {
                if (myNode == null) {
                    myNode = node;
                } else {
                    throw new MultipleTagFoundException("More than one element named " + name + " in the tag " + element.getTagName());
                }
            }
        }
        if (myNode != null) {
            return (Element) myNode;
        } else {
            return null;
        }
    }

    public static String getSimpleElementText(final Element node, final String name) {
        final Element namedElement = getFirstElement(node, name);
        if (namedElement == null) {
            return null;
        }
        return getSimpleElementText(namedElement);
    }

    public static String getSimpleElementText(final Element node) {
        final StringBuffer sb = new StringBuffer();
        final NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            final Node child = children.item(i);
            if (child instanceof Text) {
                sb.append(child.getNodeValue());
            }
        }
        return sb.toString();
    }

    /**
     * @param names
     *
     * @return
     */
    public static String getShortLabel(final Element names) {
        return getSimpleElementText(names, "shortLabel");
    }

    /**
     * @param names
     *
     * @return
     */
    public static String getFullName(final Element names) {
        return getSimpleElementText(names, "fullName");
    }

    /**
     * Display a string representation of the element given in parameter. It display all the parents from the root up to
     * the element given.
     *
     * @param element
     *
     * @return
     */
    public static String getContext(final Element element) {
        Node e = element;
        final StringBuffer sb = new StringBuffer();
        String name;
        do {
            if (e == null) {
                break;
            }
            name = e.getNodeName();
            final NamedNodeMap attrs = e.getAttributes();
            sb.insert(0, '}');
            if (attrs != null) {
                final int numAttrs = attrs.getLength();
                for (int i = 0; i < numAttrs; i++) {
                    final Attr attr = (Attr) attrs.item(i);
                    final String attrName = attr.getNodeName();
                    final String attrValue = attr.getNodeValue();
                    sb.insert(0, ')').insert(0, attrValue).insert(0, '=').insert(0, attrName).insert(0, '(');
                }
                if (numAttrs > 0) {
                    sb.insert(0, ':');
                }
            }
            sb.insert(0, name).insert(0, '{');
            e = e.getParentNode();
        } while (false == "entry".equals(name));
        return sb.toString();
    }

    /**
     * Look for an Element by name but only under the given node. <br> This is not a recursive search.
     *
     * @param element the root element.
     * @param name    the element name we are looking for.
     *
     * @return a Collection of element matching the given name being children of the given root.
     */
    public static Collection getDirectElementsByTagName(Element element, String name) {
        Collection children = null;
        NodeList list = element.getChildNodes();
        int count = list.getLength();
        for (int i = 0; i < count; i++) {
            Node node = list.item(i);
            if (node.getNodeName().equals(name)) {
                if (children == null) {
                    children = new ArrayList(4);
                }
                children.add(node);
            }
        }
        if (children == null) {
            children = Collections.EMPTY_LIST;
        }
        return children;
    }
}
