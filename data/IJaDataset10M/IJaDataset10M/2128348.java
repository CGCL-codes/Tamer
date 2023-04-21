package org.mlc.xml.utils;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import java.util.List;
import java.util.ArrayList;
import java.io.FileInputStream;
import java.util.Arrays;

/**
 * This class provides a rich set of convenience functions
 * for working with XML elements.  There is no support here
 * for parsing the documents, that should be done with 
 * by an established DOM or SAX parser.
 *
 * @author Michael Connor (mlconnor@yahoo.com)
 * @version 2.0
 * @date 2/1/2001
 */
public class XMLUtils {

    /**
     * Returns the text node of the path specified.
     * An example would be employee[2].address[1].city.
     *
     * @param node The root node to start from
     * @param path The node path to traverse before returning the text
     */
    public static String getChildText(Node node, String path) {
        Node child = getChild(node, path);
        return getText(child);
    }

    /**
     * Returns the text node of the path specified an an int.
     * An example would be company[0].employee[2].age
     *
     * @param node The root node to start from
     * @param path The node path to traverse before returning the value
     */
    public static int getChildTextAsInt(Node node, String path) throws NumberFormatException {
        Node child = getChild(node, path);
        String value = getText(child);
        return Integer.parseInt(value);
    }

    /**
     * Returns the short value of the path specified.
     * An example would be company[0].employee[2].age
     *
     * @param node The root node to start from
     * @param path The node path to traverse before returning the short
     */
    public static short getChildTextAsShort(Node node, String path) throws NumberFormatException {
        Node child = getChild(node, path);
        String value = getText(child);
        return Short.parseShort(value);
    }

    /**
     * Returns the long value of the path specified.
     * An example would be company[0].employee[2].age
     *
     * @param node The root node to start from
     * @param path The node path to traverse before returning the long
     */
    public static long getChildTextAsLong(Node node, String path) throws NumberFormatException {
        Node child = getChild(node, path);
        String value = getText(child);
        return Long.parseLong(value);
    }

    /**
     * Returns the float value of the path specified.
     * An example would be company[0].employee[2].hourlyRate
     *
     * @param node The root node to start from
     * @param path The node path to traverse before returning the long
     */
    public static float getChildTextAsFloat(Node node, String path) throws NumberFormatException {
        Node child = getChild(node, path);
        String value = getText(child);
        return Float.parseFloat(value);
    }

    /**
     * Returns the double value of the path specified.
     * An example would be company[0].employee[2].hourlyRate
     *
     * @param node The root node to start from
     * @param path The node path to traverse before returning the double
     */
    public static double getChildTextAsDouble(Node node, String path) throws NumberFormatException {
        Node child = getChild(node, path);
        String value = getText(child);
        return Double.parseDouble(value);
    }

    /**
     * Returns the boolean value of the path specified.
     * An example would be company[0].employee[2].married
     * The value should be 'true' or 'false'
     *
     * @param node The root node to start from
     * @param path The node path to traverse before returning the boolean
     */
    public static boolean getChildTextAsBoolean(Node node, String path) throws Exception {
        Node child = getChild(node, path);
        String value = getText(child);
        return (value != null && value.length() > 0 && value.substring(0, 1).equalsIgnoreCase("t"));
    }

    /**
     * Returns the child node of the path specified.
     * An example would be company[0].employee[2].  If no index
     * is specified on a particular node, then the first child with
     * the matching name is returned.  So if there were no index on
     * the company example above, the first would be used and the
     * third employee of the first company would be returned.
     * If you wanted to get all employees then you would use
     * the getChildrenMatchingPath() method.
     *
     * @param node The root node to start from
     * @param path The node path to traverse
     */
    public static Node getChild(Node node, String path) {
        java.util.StringTokenizer tokenizer = new java.util.StringTokenizer(path, ".", false);
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            int leftBracket = token.indexOf('[');
            int childIndex = 0;
            if (leftBracket != -1) {
                int rightBracket = token.indexOf(']');
                if (rightBracket == -1 || (rightBracket - leftBracket) < 2) throw new RuntimeException("Syntax error in index specification " + token + " in path '" + path + "'");
                String indexString = token.substring(leftBracket + 1, rightBracket);
                try {
                    childIndex = Integer.parseInt(indexString);
                    token = token.substring(0, leftBracket);
                } catch (NumberFormatException nfe) {
                    throw new RuntimeException("Numeric parsing error in index specification " + token + " in path '" + path + "'");
                }
            }
            Node[] children = getMatching(node, token);
            if (children.length < (childIndex + 1)) return null;
            node = children[childIndex];
        }
        return node;
    }

    /**
     * Returns a node array of the children of the 
     * node parameter.  This method will ignore 
     * text, comment, nodes and processing instructions.
     *
     * @param node The node whose children you want to return
     */
    public static Node[] getChildren(Node node) {
        NodeList children = node.getChildNodes();
        List childList = new ArrayList();
        for (int index = 0; index < children.getLength(); index++) {
            if (children.item(index).getNodeType() == Node.ELEMENT_NODE) childList.add(children.item(index));
        }
        Node[] childArray = new Node[childList.size()];
        return (Node[]) childList.toArray(childArray);
    }

    /**
     * Returns an array of Nodes matching the
     * specified path. This method will accept
     * the dotted notation for the path.  The index
     * notation is also accepted.
     * so for example, you can use company.employee
     * to get all employees of all companies in the
     * document. or you can use company[0].employees
     * to get all employees of the first company.
     *
     * @param node The root node to start from
     * @param path The node path to traverse
     */
    public static Node[] getMatching(Node node, String path) {
        List matchingNodes = new ArrayList();
        matchingNodes.add(node);
        java.util.StringTokenizer tokenizer = new java.util.StringTokenizer(path, ".", false);
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            List newNodes = new ArrayList();
            for (int index = 0; index < matchingNodes.size(); index++) {
                Node matchingNode = (Node) matchingNodes.get(index);
                if (token.indexOf("[") > 0) {
                    Node child = null;
                    child = XMLUtils.getChild(matchingNode, token);
                    if (child != null) newNodes.add(child);
                } else {
                    NodeList children = matchingNode.getChildNodes();
                    for (int childIndex = 0; childIndex < children.getLength(); childIndex++) {
                        Node child = children.item(childIndex);
                        if (child.getNodeName().equalsIgnoreCase(token)) newNodes.add(child);
                    }
                }
            }
            matchingNodes = newNodes;
        }
        Node[] nodes = new Node[matchingNodes.size()];
        nodes = (Node[]) matchingNodes.toArray(nodes);
        return nodes;
    }

    /**
     * Returns a node array of the attributes of the 
     * node parameter with a name matching the name
     * provided.  The name matching is done in a case
     * insensitive way.
     *
     * @param node The node whose children you want to return
     * @return A node array containing the children
     */
    public static Node[] getAttributes(Node node) {
        NamedNodeMap attributes = node.getAttributes();
        Node[] attributeArray = new Node[attributes.getLength()];
        for (int index = 0; index < attributes.getLength(); index++) attributeArray[index] = attributes.item(index);
        return attributeArray;
    }

    /**
     * Returns an attribute node matching the name
     * specified.  The name matching is done in a case
     * insensitive way.
     *
     * @param node The node containing the attribute
     * @return The attribute node 
     */
    public static Node getAttributeNode(Node node, String name) {
        NamedNodeMap attributes = node.getAttributes();
        for (int index = 0; index < attributes.getLength(); index++) {
            Node attribute = attributes.item(index);
            if (attribute.getNodeName().equalsIgnoreCase(name)) return attribute;
        }
        return null;
    }

    private static String getAttribute(Node node, String name) {
        Node attribute = getAttributeNode(node, name);
        return attribute != null ? attribute.getNodeValue() : null;
    }

    /**
     * Returns a flag indicating whether the node
     * has an attribute with the name specified.  The name
     * matching is done in a case insensitive way.
     *
     * @param The node containing the attribute
     * @param The name of the attribute you want to test for
     * @return a boolean indicating whether or not the attribute exists
     */
    public static boolean hasAttribute(Node node, String name) {
        return XMLUtils.getAttribute(node, name) != null;
    }

    /**
     * Returns a flag indicating whether the node
     * has a child with the name specified.  The name
     * matching is done in a case insensitive way.  This
     * method accepts the index notation and dotted
     * child notation in the path.
     *
     * @param The node containing the child
     * @param The name of the child you want to test for
     * @return a boolean indicating whether or not the child exists
     */
    public static boolean hasChild(Node node, String path) {
        Node child = XMLUtils.getChild(node, path);
        return child != null;
    }

    /**
     * Returns the text of the Node provided.
     *
     * @param Node The node whose text you want to retrieve
     * @return The text of the node provided.
     */
    public static String getText(Node node) {
        NodeList children = node.getChildNodes();
        StringBuffer buffer = new StringBuffer();
        for (int index = 0; index < children.getLength(); index++) {
            Node child = children.item(index);
            if (child.getNodeType() == Node.TEXT_NODE || child.getNodeType() == Node.CDATA_SECTION_NODE) return child.getNodeValue();
        }
        return null;
    }

    /**
     * Sets the attribute on the node provided.
     *
     * @param node The node you wish to modify
     * @param name The name of the attribute you want to modify
     * @param value The value to assign to the attribute
     */
    public static void setAttribute(Node node, String name, String value) {
        NamedNodeMap attributes = node.getAttributes();
        Document document = node.getOwnerDocument();
        Node attribute = document.createAttribute(name);
        attribute.setNodeValue(value);
        attributes.setNamedItem(attribute);
    }

    /**
     * Sets the text on the node provided.
     *
     * @param node The node whose text you wish to set
     * @param text The text value to assign to the node
     */
    public static void setText(Node node, String text) {
        NodeList children = node.getChildNodes();
        Node textNode = null;
        for (int index = 0; index < children.getLength(); index++) {
            Node child = children.item(index);
            if (child.getNodeType() == Node.TEXT_NODE) textNode = child;
        }
        if (textNode != null) {
            textNode.setNodeValue(text);
        } else {
            Document doc = node.getOwnerDocument();
            textNode = doc.createTextNode(text);
            node.appendChild(textNode);
        }
    }

    /**
     * Returns the specified attribute of the given
     * node after converting it to an int.  The attribute
     * search on the node is done in a case insensitive 
     * manner.
     *
     * @param node The node containing the parameter
     * @param attributeName The attribute you want to retrieve
     * @return The value of the attribute
     */
    public static int getAttributeAsInt(Node node, String attributeName) throws NumberFormatException {
        String value = XMLUtils.getAttribute(node, attributeName);
        return Integer.parseInt(value);
    }

    /**
     * Returns the specified attribute of the given
     * node after converting it to a short.  The attribute
     * search on the node is done in a case insensitive 
     * manner.
     *
     * @param node The node containing the parameter
     * @param attributeName The attribute you want to retrieve
     * @return The value of the attribute
     */
    public static short getAttributeAsShort(Node node, String attributeName) throws NumberFormatException {
        String value = XMLUtils.getAttribute(node, attributeName);
        return Short.parseShort(value);
    }

    /**
     * Returns the specified attribute of the given
     * node after converting it to a long.  The attribute
     * search on the node is done in a case insensitive 
     * manner.
     *
     * @param node The node containing the parameter
     * @param attributeName The attribute you want to retrieve
     * @return The value of the attribute
     */
    public static long getAttributeAsLong(Node node, String attributeName) throws NumberFormatException {
        String value = XMLUtils.getAttribute(node, attributeName);
        return Long.parseLong(value);
    }

    /**
     * Returns the specified attribute of the given
     * node after converting it to a long.  The attribute
     * search on the node is done in a case insensitive 
     * manner.
     *
     * @param node The node containing the parameter
     * @param attributeName The attribute you want to retrieve
     * @return The value of the attribute
     */
    public static float getAttributeAsFloat(Node node, String attributeName) throws NumberFormatException {
        String value = XMLUtils.getAttribute(node, attributeName);
        return Float.parseFloat(value);
    }

    /**
     * Returns the specified attribute of the given
     * node after converting it to a double.  The attribute
     * search on the node is done in a case insensitive 
     * manner.
     *
     * @param node The node containing the parameter
     * @param attributeName The attribute you want to retrieve
     * @return The value of the attribute
     */
    public static double getAttributeAsDouble(Node node, String attributeName) throws NumberFormatException {
        String value = XMLUtils.getAttribute(node, attributeName);
        return Double.parseDouble(value);
    }

    /**
     * Returns the specified attribute of the given
     * node after converting it to a boolean.  The attribute
     * search on the node is done in a case insensitive 
     * manner.
     *
     * @param node The node containing the parameter
     * @param attributeName The attribute you want to retrieve
     * @return The value of the attribute
     */
    public static boolean getAttributeAsBoolean(Node node, String attributeName) throws Exception {
        String value = XMLUtils.getAttribute(node, attributeName);
        return (value != null && value.length() > 0 && value.substring(0, 1).equalsIgnoreCase("t"));
    }

    /**
     * Returns the specified attribute of the given.
     * The attribute search on the node is done in a 
     * case insensitive  manner.
     *
     * @param node The node containing the parameter
     * @param attributeName The attribute you want to retrieve
     * @return The value of the attribute
     */
    public static String getAttributeAsString(Node node, String attributeName) {
        return XMLUtils.getAttribute(node, attributeName);
    }

    /**
     * Returns a new XML Document that can be dynamically built.
     * Note that this method eats a ParserConfigurationException.
     * It will print it out to the screen but I thought it was better
     * to eat it since I didn't really see it being thrown due to
     * programmer error. Instead, a null pointer will be thrown.
     * I know, it's not elegant but it's convenient.
     *
     * @return The newly created XML Document
     */
    public static Document createDocument() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document document = null;
        try {
            builder = factory.newDocumentBuilder();
            document = builder.newDocument();
        } catch (ParserConfigurationException pe) {
            System.out.println(pe);
            pe.printStackTrace();
        }
        return document;
    }

    /**
     * Builds a path relative to the Node parameter.
     * If the path exists, this method will return 
     * normally.  If it does not exist, then the
     * heirarchy will be created as provided.
     *
     * @param node The root node to start from
     * @param path The path to build
     * @return The last child in the path specified
     */
    public static Node createPath(Node node, String path) throws DOMException {
        java.util.StringTokenizer tokenizer = new java.util.StringTokenizer(path, ".", false);
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            if (!XMLUtils.hasChild(node, token)) {
                Document document = node instanceof Document ? (Document) node : node.getOwnerDocument();
                Node child = document.createElement(token);
                node.appendChild(child);
                node = child;
            } else {
                node = XMLUtils.getChild(node, token);
            }
        }
        return node;
    }

    /**
     * This is a DOMWriter.  This should be replaced
     * by the DOMWriter in the new Xerces-J implementation.
     * This implementation does not take into account special
     * characters and other advanced XML features.
     *
     * @param Node the node to create a text representation of
     * @return Text representation of the Node
     */
    public static String getStringRepresentation(Node node) {
        StringBuffer buffer = new StringBuffer();
        int type = node.getNodeType();
        switch(type) {
            case Node.DOCUMENT_NODE:
                {
                    buffer.append("<?xml version=\"1.0\" encoding=\"" + "UTF-8" + "\"?>");
                    break;
                }
            case Node.ELEMENT_NODE:
                {
                    buffer.append('<' + node.getNodeName());
                    NamedNodeMap nnm = node.getAttributes();
                    if (nnm != null) {
                        int len = nnm.getLength();
                        Attr attr;
                        for (int i = 0; i < len; i++) {
                            attr = (Attr) nnm.item(i);
                            buffer.append(' ' + attr.getNodeName() + "=\"" + attr.getNodeValue() + '"');
                        }
                    }
                    buffer.append('>');
                    break;
                }
            case Node.ENTITY_REFERENCE_NODE:
                {
                    buffer.append('&' + node.getNodeName() + ';');
                    break;
                }
            case Node.CDATA_SECTION_NODE:
                {
                    buffer.append("<![CDATA[" + node.getNodeValue() + "]]>");
                    break;
                }
            case Node.TEXT_NODE:
                {
                    buffer.append(node.getNodeValue());
                    break;
                }
            case Node.PROCESSING_INSTRUCTION_NODE:
                {
                    buffer.append("<?" + node.getNodeName());
                    String data = node.getNodeValue();
                    if (data != null && data.length() > 0) {
                        buffer.append(' ');
                        buffer.append(data);
                    }
                    buffer.append("?>");
                    break;
                }
        }
        for (Node child = node.getFirstChild(); child != null; child = child.getNextSibling()) {
            buffer.append(getStringRepresentation(child));
        }
        if (type == Node.ELEMENT_NODE) {
            buffer.append("</" + node.getNodeName() + ">");
        }
        return buffer.toString();
    }

    /**
     * Returns a list of nodes containing both elements
     * and attributes.
     *
     * @param node The node to use
     * @return A list containing both elements and attributes
     */
    public static List getElementsAndAttributes(Node node) {
        List elementsAndAttributes = new ArrayList();
        elementsAndAttributes.addAll(Arrays.asList(XMLUtils.getAttributes(node)));
        elementsAndAttributes.addAll(Arrays.asList(XMLUtils.getChildren(node)));
        return elementsAndAttributes;
    }

    public static void main2(String args[]) {
        try {
            Document doc = XMLUtils.createDocument();
            Node node = XMLUtils.createPath(doc, "company.people.employees.michael");
            XMLUtils.setText(node, "is king!!!");
            System.out.println(XMLUtils.getStringRepresentation(doc));
        } catch (DOMException e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            DOMParser parser = new DOMParser();
            FileInputStream stream = new FileInputStream("c:\\source\\chep\\java\\com\\marconi\\chep\\common\\conveyor.xml");
            InputSource inputSource = new InputSource(stream);
            parser.parse(inputSource);
            Document document = parser.getDocument();
            Node node = XMLUtils.getChild(document, "data-appliance.IT500_IDENTIFY_TRIES");
            if (node == null) {
                System.out.println("COULDNT FIND NODE");
            } else System.out.println("Value : " + XMLUtils.getText(node));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
