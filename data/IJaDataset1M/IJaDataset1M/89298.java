package org.statefive.xml;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Basic XML utilities.
 * 
 * @author rmeeking
 */
public class XmlUtilities {

    /**
   * Finds an element with name <code>elementName</code>, searching
   * depth-first from node <code>root</code>.
   * 
   * @param root
   *          the node to search from.
   * 
   * @param elementName
   *          the name of the element to find.
   * 
   * @return a non-<code>null</code> node if the specified element exists;
   *         <code>null</code> otherwise. Note that if the name of
   *         <code>root</code> matches <code>elementName</code>, then
   *         <code>root</code> is returned.
   */
    public static Node find(Node root, String elementName) {
        Node foundNode = null;
        if (root != null) {
            if (root.getNodeName().equals(elementName)) {
                foundNode = root;
            } else {
                NodeList list = root.getChildNodes();
                for (int i = 0; foundNode == null && i < list.getLength(); i++) {
                    Node child = list.item(i);
                    if (child.getNodeName().equals(elementName)) {
                        foundNode = child;
                        break;
                    } else {
                        foundNode = find(child, elementName);
                    }
                }
            }
        }
        return foundNode;
    }

    /**
   * Creates a new element without any attributes.
   * 
   * @param elementName
   *          the name of the element to create.
   * 
   * @return an XML fragment representing the specified element.
   */
    public static String beginElement(String elementName) {
        return new StringBuffer().append("<").append(elementName).append(">").toString();
    }

    /**
   * Creates the named element with the given attributes inside.
   * 
   * @param elementName
   *          the name of the element to create.
   * 
   * @param attrs
   *          an array of attribute names, if <code>null</code> will not be
   *          included.
   * 
   * @param values
   *          an array of values, each value at the <i>n</i>th position of the
   *          array being mapped to the attribute name of the same position if
   *          <code>null</code> will not be included.
   * 
   * @return an XML fragment representing the name of the specified element
   *         along with the attributes and values of the attributes.
   */
    public static String beginElement(String elementName, String[] attrs, String[] values) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<").append(elementName);
        for (int i = 0; attrs != null && values != null && i < attrs.length && i < values.length; i++) {
            buffer.append(" ");
            buffer.append(attrs[i]);
            buffer.append("=\"");
            buffer.append(values[i]);
            buffer.append("\"");
        }
        buffer.append(">");
        return buffer.toString();
    }

    /**
   * Utility method to create a full XML fragment representing an element, it's
   * attributes (if not <code>attrs</code> and <code>values</code> are not
   * <code>null</code>) and it's value.
   * 
   * @param elementName
   *          the name of the XML element.
   * 
   * @param elementValue
   *          the element value.
   * 
   * @param attrs
   *          an array of attribute names, if <code>null</code> will not be
   *          included.
   * 
   * @param values
   *          an array of values, each value at the <i>n</i>th position of the
   *          array being mapped to the attribute name of the same position if
   *          <code>null</code> will not be included.
   * 
   * @return the specified XML fragment.
   */
    public static String elementify(String elementName, String elementValue, String[] attrs, String[] values) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(XmlUtilities.beginElement(elementName, attrs, values));
        buffer.append(elementValue);
        buffer.append(XmlUtilities.endElement(elementName));
        return buffer.toString();
    }

    /**
   * Creates an XML fragment representing the end of the specified element name.
   * 
   * @param elementName
   *          the name of the element.
   * 
   * @return an XML fragment representing the specified element.
   */
    public static String endElement(String elementName) {
        return new StringBuffer().append("</").append(elementName).append(">").toString();
    }
}
