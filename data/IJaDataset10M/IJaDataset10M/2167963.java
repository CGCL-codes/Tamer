package org.sopera.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;
import org.sopera.metadata.impl.WsdlComponent;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * This utility class is used to serialize a DOM object into a writer, specially
 * into a string.
 * 
 * @author rme
 */
public class DomUtils {

    /** new line charachter. */
    public static final String LS = "\n";

    /** the namespace URI for XML namespaces. */
    public static final String NS_URI_XMLNS = "http://www.w3.org/2000/xmlns/";

    /** the namespace URI for XML namespaces. */
    public static final String NS_URI_XML = "http://www.w3.org/XML/1998/namespace";

    /**
	 * Return a string containing this node serialized as XML.
	 * 
	 * @param node
	 *            the node
	 * @param omitXMLDecl
	 *            the omit XML decl
	 * 
	 * @return the string
	 */
    public static String nodeToString(final Node node, final boolean omitXMLDecl) {
        StringWriter sw = new StringWriter();
        serializeAsXML(node, sw, omitXMLDecl);
        return sw.toString();
    }

    /**
	 * Return a string containing this node serialized as XML.
	 * 
	 * @param node
	 *            the node
	 * 
	 * @return the string
	 */
    public static String nodeToPrettyString(final Node node) {
        StringWriter sw = new StringWriter();
        serializeAsXML(node, sw, true, true);
        return sw.toString();
    }

    /**
	 * Serialize as XML.
	 * 
	 * @param node
	 *            the node to serialize.
	 * @param writer
	 *            the writer to serialize the DOM content to.
	 * @param omitXMLDecl
	 *            control if XML declarations are going to be ommitted or not.
	 */
    public static void serializeAsXML(final Node node, final Writer writer, final boolean omitXMLDecl) {
        serializeAsXML(node, writer, omitXMLDecl, false);
    }

    /**
	 * Serialize this node into the writer as XML.
	 * 
	 * @param node
	 *            the node
	 * @param writer
	 *            the writer
	 * @param omitXMLDecl
	 *            the omit XML decl
	 * @param pretty
	 *            the pretty
	 */
    public static void serializeAsXML(final Node node, final Writer writer, final boolean omitXMLDecl, final boolean pretty) {
        PrintWriter out = new PrintWriter(writer);
        String encoding = getEncoding(node);
        XMLEncoder encoder = XMLEncoderFactory.createInstance(encoding);
        if (!omitXMLDecl) {
            out.print("<?xml version=\"1.0\" encoding=\"");
            out.print(encoder.getEncoding());
            out.println("\"?>");
        }
        NSStack namespaceStack = new NSStack();
        print(node, namespaceStack, node, out, pretty, 0, encoder);
        out.flush();
    }

    /**
	 * This method converts string representation of the xml into the DOMSource.
	 * 
	 * @param src
	 *            the input xml string
	 * 
	 * @return the DOMSource
	 */
    public static Document docFromInputSource(final InputSource src) {
        Document res = null;
        ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(DomUtils.class.getClassLoader());
            DocumentBuilderFactory factory = getDocumentBuilderFactory();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            res = builder.parse(src);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } finally {
            Thread.currentThread().setContextClassLoader(oldClassLoader);
        }
        return res;
    }

    /**
	 * This method converts string representation of the xml into the DOMSource.
	 * 
	 * @param src
	 *            the input xml string
	 * 
	 * @return the DOMSource
	 */
    public static Document docFromStream(final InputStream src) {
        Document res = null;
        ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(DomUtils.class.getClassLoader());
            DocumentBuilderFactory factory = getDocumentBuilderFactory();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            res = builder.parse(src);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } finally {
            Thread.currentThread().setContextClassLoader(oldClassLoader);
        }
        return res;
    }

    public static Document docFromString(final String src) {
        Document res = null;
        ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(DomUtils.class.getClassLoader());
            DocumentBuilderFactory factory = getDocumentBuilderFactory();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            res = builder.parse(new InputSource(new StringReader(src)));
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } finally {
            Thread.currentThread().setContextClassLoader(oldClassLoader);
        }
        return res;
    }

    /**
	 * Gets the document builder factory.
	 * 
	 * @return -- an instance of a non-validating but namespace aware
	 *         DocumentBuilder that ignores ignoreable white spaces
	 */
    public static DocumentBuilderFactory getDocumentBuilderFactory() {
        ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(DomUtils.class.getClassLoader());
            DocumentBuilderFactory myBuilderFactory = DocumentBuilderFactory.newInstance();
            myBuilderFactory.setValidating(false);
            myBuilderFactory.setNamespaceAware(true);
            myBuilderFactory.setIgnoringElementContentWhitespace(true);
            return myBuilderFactory;
        } finally {
            Thread.currentThread().setContextClassLoader(oldClassLoader);
        }
    }

    /**
	 * this method traverses the DOM tree to print out each node charachteristic
	 * into a writer.
	 * 
	 * @param node
	 *            the current node to be printed. This nodes childs are
	 *            traversed.
	 * @param namespaceStack
	 *            namespace stack to be used in order to have correct namespace
	 *            handling.
	 * @param startnode
	 *            the root node.
	 * @param out
	 *            the printwriter to print the serialized information to.
	 * @param pretty
	 *            indicate if we do intending for the print out or not.
	 * @param indent
	 *            the number of intends while pretty printing.
	 * @param encoder
	 *            the char encoder to be used for the correct charachter
	 *            encoding.
	 */
    private static void print(final Node node, final NSStack namespaceStack, final Node startnode, final PrintWriter out, final boolean pretty, final int indent, final XMLEncoder encoder) {
        if (node == null) {
            return;
        }
        boolean hasChildren = false;
        int type = node.getNodeType();
        switch(type) {
            case Node.DOCUMENT_NODE:
                NodeList children1 = node.getChildNodes();
                if (children1 != null) {
                    int numChildren = children1.getLength();
                    for (int i = 0; i < numChildren; i++) {
                        print(children1.item(i), namespaceStack, startnode, out, pretty, indent, encoder);
                    }
                }
                break;
            case Node.DOCUMENT_FRAGMENT_NODE:
                NodeList children2 = node.getChildNodes();
                if (children2 != null) {
                    int numChildren = children2.getLength();
                    for (int i = 0; i < numChildren; i++) {
                        print(children2.item(i), namespaceStack, startnode, out, pretty, indent, encoder);
                    }
                }
                break;
            case Node.ELEMENT_NODE:
                namespaceStack.push();
                if (pretty) {
                    for (int i = 0; i < indent; i++) {
                        out.print(' ');
                    }
                }
                out.print('<' + node.getNodeName());
                String elPrefix = node.getPrefix();
                String elNamespaceURI = node.getNamespaceURI();
                if (elPrefix != null && elNamespaceURI != null && elPrefix.length() > 0) {
                    boolean prefixIsDeclared = false;
                    try {
                        String namespaceURI = namespaceStack.getNamespaceURI(elPrefix);
                        if (elNamespaceURI.equals(namespaceURI)) {
                            prefixIsDeclared = true;
                        }
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    }
                    if (!prefixIsDeclared) {
                        printNamespaceDecl(node, elPrefix, namespaceStack, startnode, out);
                    }
                }
                NamedNodeMap attrs = node.getAttributes();
                int len = (attrs != null) ? attrs.getLength() : 0;
                for (int i = 0; i < len; i++) {
                    Attr attr = (Attr) attrs.item(i);
                    out.print(' ' + attr.getNodeName() + "=\"" + normalize(attr.getValue(), encoder) + '\"');
                    String attrPrefix = attr.getPrefix();
                    String attrNamespaceURI = attr.getNamespaceURI();
                    if (attrPrefix != null && attrNamespaceURI != null && attrPrefix.length() > 0) {
                        boolean prefixIsDeclared = false;
                        try {
                            String namespaceURI = namespaceStack.getNamespaceURI(attrPrefix);
                            if (attrNamespaceURI.equals(namespaceURI)) {
                                prefixIsDeclared = true;
                            }
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        }
                        if (!prefixIsDeclared) {
                            printNamespaceDecl(attr, attrPrefix, namespaceStack, startnode, out);
                        }
                    }
                    String valuePrefix = attr.getValue().substring(0, attr.getValue().indexOf(":") < 0 ? 0 : attr.getValue().indexOf(":"));
                    valuePrefix = valuePrefix.trim();
                    valuePrefix = valuePrefix.length() == 0 ? null : valuePrefix;
                    if (valuePrefix != null) {
                        boolean prefixIsDeclared = false;
                        String probableNS = getNamespaceUri(valuePrefix, attr.getOwnerElement(), attr.getOwnerElement().getOwnerDocument());
                        if (probableNS != null) {
                            try {
                                String namespaceURI = namespaceStack.getNamespaceURI(valuePrefix);
                                if (probableNS.equals(namespaceURI)) {
                                    prefixIsDeclared = true;
                                }
                            } catch (IllegalArgumentException e) {
                                e.printStackTrace();
                            }
                            if (!prefixIsDeclared) {
                                printNamespaceDecl(attr, valuePrefix, namespaceStack, startnode, out);
                            }
                        }
                    }
                }
                NodeList children = node.getChildNodes();
                if (children != null) {
                    int numChildren = children.getLength();
                    hasChildren = (numChildren > 0);
                    if (hasChildren) {
                        out.print('>');
                        if (pretty) {
                            out.print(LS);
                        }
                    }
                    for (int i = 0; i < numChildren; i++) {
                        print(children.item(i), namespaceStack, startnode, out, pretty, indent + 1, encoder);
                    }
                } else {
                    hasChildren = false;
                }
                if (!hasChildren) {
                    out.print("/>");
                    if (pretty) {
                        out.print(LS);
                    }
                }
                namespaceStack.pop();
                break;
            case Node.ENTITY_REFERENCE_NODE:
                out.print('&');
                out.print(node.getNodeName());
                out.print(';');
                break;
            case Node.CDATA_SECTION_NODE:
                out.print("<![CDATA[");
                out.print(node.getNodeValue());
                out.print("]]>");
                break;
            case Node.TEXT_NODE:
                out.print(normalize(node.getNodeValue(), encoder));
                break;
            case Node.COMMENT_NODE:
                out.print("<!--");
                out.print(node.getNodeValue());
                out.print("-->");
                if (pretty) {
                    out.print(LS);
                }
                break;
            case Node.PROCESSING_INSTRUCTION_NODE:
                out.print("<?");
                out.print(node.getNodeName());
                String data = node.getNodeValue();
                if (data != null && data.length() > 0) {
                    out.print(' ');
                    out.print(data);
                }
                out.println("?>");
                if (pretty) {
                    out.print(LS);
                }
                break;
            default:
                System.out.print("");
        }
        if (type == Node.ELEMENT_NODE && hasChildren == true) {
            if (pretty) {
                for (int i = 0; i < indent; i++) {
                    out.print(' ');
                }
            }
            out.print("</");
            out.print(node.getNodeName());
            out.print('>');
            if (pretty) {
                out.print(LS);
            }
            hasChildren = false;
        }
    }

    /**
	 * pushes the namespace declaration of the given node into the given print
	 * writer.
	 * 
	 * @param node
	 *            the node from which to find the namespace.
	 * @param namespaceStack
	 *            the NS stack to search through.
	 * @param startnode
	 *            the root node from where we started.
	 * @param out
	 *            the printwriter to push the value into.
	 * @param thePrefix
	 *            the the prefix
	 */
    private static void printNamespaceDecl(final Node node, final String thePrefix, final NSStack namespaceStack, final Node startnode, final PrintWriter out) {
        switch(node.getNodeType()) {
            case Node.ATTRIBUTE_NODE:
                printNamespaceDecl(((Attr) node).getOwnerElement(), node, thePrefix, namespaceStack, startnode, out);
                break;
            case Node.ELEMENT_NODE:
                printNamespaceDecl((Element) node, node, thePrefix, namespaceStack, startnode, out);
                break;
            default:
                System.out.print("");
        }
    }

    /**
	 * print the namespace declaration of an element node.
	 * 
	 * @param owner
	 *            the owner
	 * @param node
	 *            the node
	 * @param thePrefix
	 *            the the prefix
	 * @param namespaceStack
	 *            the namespace stack
	 * @param startnode
	 *            the startnode
	 * @param out
	 *            the out
	 */
    private static void printNamespaceDecl(final Element owner, final Node node, final String thePrefix, final NSStack namespaceStack, final Node startnode, final PrintWriter out) {
        String namespaceURI = node.getNamespaceURI();
        String prefix = thePrefix;
        if (namespaceURI == null) {
            namespaceURI = getNamespaceUri(prefix, owner, owner.getOwnerDocument());
        }
        if (!(namespaceURI.equals(NS_URI_XMLNS) && prefix.equals("xmlns")) && !(namespaceURI.equals(NS_URI_XML) && prefix.equals("xml"))) {
            if (getNamespaceUri(prefix, owner, startnode) == null) {
                out.print(" xmlns:" + prefix + "=\"" + namespaceURI + '\"');
            }
        } else {
            prefix = node.getLocalName();
            namespaceURI = node.getNodeValue();
        }
        namespaceStack.add(namespaceURI, prefix);
    }

    /**
	 * Searches for the namespace URI of the given prefix in the given DOM
	 * range.
	 * 
	 * The namespace is not searched in parent of the "stopNode". This is
	 * usefull to get all the needed namespaces when you need to ouput only a
	 * subtree of a DOM document.
	 * 
	 * @param prefix
	 *            the prefix to find
	 * @param e
	 *            the starting node
	 * @param stopNode
	 *            null to search in all the document or a parent node where the
	 *            search must stop.
	 * 
	 * @return null if no namespace is found, or the namespace URI.
	 */
    public static String getNamespaceUri(final String prefix, Node e, final Node stopNode) {
        Node node = e;
        while (node != null && (node.getNodeType() == Node.ELEMENT_NODE)) {
            Attr attr = null;
            if (prefix == null) {
                attr = ((Element) node).getAttributeNode("xmlns");
            } else {
                attr = ((Element) node).getAttributeNodeNS(NS_URI_XMLNS, prefix);
            }
            if (attr != null) {
                return attr.getValue();
            }
            if (node == stopNode) {
                return null;
            }
            node = node.getParentNode();
        }
        return null;
    }

    /**
	 * Searches the closest namespace declaration of the given URI and returns
	 * the corresponding prefix
	 * 
	 * @param uri
	 *            the uri to find
	 * @param e
	 *            the starting node
	 * @param stopNode
	 *            null to search up to the document node or a parent node where
	 *            the search must stop
	 * @return
	 */
    public static String getNamespacePrefix(final String uri, Node e, final Node stopNode) {
        Node node = e;
        while (node != null && (node.getNodeType() == Node.ELEMENT_NODE)) {
            NamedNodeMap attributes = ((Element) node).getAttributes();
            for (int i = 0; i < attributes.getLength(); i++) {
                Attr attribute = (Attr) attributes.item(i);
                if (NS_URI_XMLNS.equals(attribute.getNamespaceURI())) {
                    String value = attribute.getValue();
                    String localName = attribute.getLocalName();
                    if (!localName.equals("xmlns") && (value.equals(uri))) {
                        return localName;
                    }
                }
            }
            if (node == stopNode) {
                return null;
            }
            node = node.getParentNode();
        }
        return null;
    }

    /**
	 * normalizes a String using the given encoder.
	 * 
	 * @param s
	 *            the String to be normalized.
	 * @param encoder
	 *            the encoder to use.
	 * 
	 * @return the normalized representation of the String.
	 */
    private static String normalize(final String s, final XMLEncoder encoder) {
        return encoder.encode(s);
    }

    /**
	 * This method tries to figure out the encoding used to build this node.
	 * 
	 * @param node
	 *            the node to examine
	 * 
	 * @return the encoding used in the XML declaration to build this document.
	 */
    private static String getEncoding(final Node node) {
        String encoding = null;
        visit(node.getOwnerDocument(), 0, encoding);
        if (encoding == null) {
            encoding = "UTF-8";
        }
        return encoding;
    }

    /**
	 * visits all values in the node to find out the encoding.
	 * 
	 * @param node
	 *            the node
	 * @param level
	 *            the level
	 * @param encoding
	 *            the encoding
	 */
    private static void visit(final Node node, final int level, final String encoding) {
        if (encoding != null) {
            return;
        }
        if (node == null) {
            return;
        }
        NodeList list = node.getChildNodes();
        String enc = encoding;
        for (int i = 0; i < list.getLength(); i++) {
            Node childNode = list.item(i);
            if (childNode.getNodeType() == Node.PROCESSING_INSTRUCTION_NODE) {
                ProcessingInstruction pi = (ProcessingInstruction) childNode;
                if ("xml".equals(pi.getTarget()) && pi.getData().startsWith("encoding")) {
                    enc = pi.getData().substring(pi.getData().indexOf("\"") + 1, pi.getData().lastIndexOf("\""));
                    return;
                }
            }
            visit(childNode, level + 1, enc);
        }
    }

    public static Element adoptChild(Element parent, Element child) {
        Element clone = (Element) child.cloneNode(true);
        Document doc = parent.getOwnerDocument();
        doc.adoptNode(clone);
        parent.appendChild(clone);
        return clone;
    }

    public static Element appendChild(Element parent, String childName) {
        Document document = parent.getOwnerDocument();
        String uri = parent.getNamespaceURI();
        Element newChild = document.createElementNS(uri, childName);
        parent.appendChild(newChild);
        return newChild;
    }

    public static Element appendChildNS(Element parent, String namespaceURI, String prefix, String childName) {
        Document document = parent.getOwnerDocument();
        Element newChild = document.createElementNS(namespaceURI, childName);
        newChild.setPrefix(prefix);
        parent.appendChild(newChild);
        return newChild;
    }

    public static Element appendSibling(Element existingNode, Element newChild) {
        Node nextSibling = existingNode.getNextSibling();
        Node parentNode = existingNode.getParentNode();
        parentNode.insertBefore(newChild, nextSibling);
        return newChild;
    }

    public static Element appendSibling(Element existingNode, String childName) {
        Document document = existingNode.getOwnerDocument();
        Element newChild = document.createElement(childName);
        return appendSibling(existingNode, newChild);
    }

    public static Element appendSiblingNS(Element existingNode, String namespaceURI, String prefix, String childName) {
        Document document = existingNode.getOwnerDocument();
        Element newChild = document.createElementNS(namespaceURI, childName);
        newChild.setPrefix(prefix);
        return appendSibling(existingNode, newChild);
    }

    /**
	 * Gets the first child named.
	 * 
	 * @param nsURI
	 *            the ns URI
	 * @param nsLocal
	 *            the ns local
	 * @param parent
	 *            the parent
	 * 
	 * @return the first child named
	 */
    public static Element getFirstChildNamed(final String nsURI, final String nsLocal, final Element parent) {
        for (Node n = parent.getFirstChild(); n != null; n = n.getNextSibling()) {
            if (n.getNodeType() == Node.ELEMENT_NODE && nsLocal.equals(n.getLocalName()) && nsURI.equals(n.getNamespaceURI())) {
                return (Element) n;
            }
        }
        return null;
    }

    /**
	 * Gets the most direct ancestor of <code>child</code> with the given name
	 * 
	 * @param nsURI
	 * @param nsLocal
	 * @param child
	 * @return
	 */
    public static Element getFirstAncestorNamed(final String nsURI, final String nsLocal, final Node child) {
        Node n = child.getParentNode();
        while (null != n) {
            if (n.getNodeType() == Node.ELEMENT_NODE && nsLocal.equals(n.getLocalName()) && nsURI.equals(n.getNamespaceURI())) {
                return (Element) n;
            }
            n = n.getParentNode();
        }
        return null;
    }

    /**
	 * Gets the next sibling named.
	 * 
	 * @param nsURI
	 *            the ns URI
	 * @param nsLocal
	 *            the ns local
	 * @param element
	 *            the element
	 * 
	 * @return the next sibling named
	 */
    public static Element getNextSiblingNamed(final String nsURI, final String nsLocal, final Element element) {
        for (Node n = element.getNextSibling(); n != null; n = n.getNextSibling()) {
            if (n.getNodeType() == Node.ELEMENT_NODE && nsLocal.equals(n.getLocalName()) && nsURI.equals(n.getNamespaceURI())) {
                return (Element) n;
            }
        }
        return null;
    }

    public static Element prependSibling(Element existingNode, Element newChild) {
        Node parentNode = existingNode.getParentNode();
        parentNode.insertBefore(newChild, existingNode);
        return newChild;
    }

    public static Element prependSibling(Element existingNode, String childName) {
        Document document = existingNode.getOwnerDocument();
        Element newChild = document.createElement(childName);
        return prependSibling(existingNode, newChild);
    }

    public static Element prependSiblingNS(Element existingNode, String namespaceURI, String prefix, String childName) {
        Document document = existingNode.getOwnerDocument();
        Element newChild = document.createElementNS(namespaceURI, childName);
        newChild.setPrefix(prefix);
        return prependSibling(existingNode, newChild);
    }

    /**
	 * gets all namespace definitions that are declared directly at <code>element</code>
	 * 
	 * @param element
	 * @return
	 */
    public static Map<String, String> getNamespaceDeclarations(Element element) {
        Map<String, String> ret = new HashMap<String, String>();
        NamedNodeMap attributes = element.getAttributes();
        int i = 0;
        Attr attrib = (Attr) attributes.item(i++);
        while (null != attrib) {
            if (WsdlComponent.XMLNS.equals(attrib.getNamespaceURI())) {
                String name = attrib.getName();
                String[] nameParts = name.split(":");
                String nsPrefix = null;
                if (nameParts.length == 2) {
                    nsPrefix = nameParts[1];
                }
                ret.put(nsPrefix, attrib.getValue());
            }
            attrib = (Attr) attributes.item(i++);
        }
        return ret;
    }

    /**
	 * gets all namespace declarations from <code>element</code> and all ancestors
	 * In case of conflicts the most specific declaration is used
	 * 
	 * @param element
	 * @return
	 */
    public static Map<String, String> getNamespaceContext(Element element) {
        Map<String, String> ret = new HashMap<String, String>();
        Node currentElement = element;
        while ((null != currentElement) && (currentElement.getNodeType() == Node.ELEMENT_NODE)) {
            Map<String, String> ancestorDeclarations = getNamespaceDeclarations((Element) currentElement);
            for (Entry<String, String> entry : ancestorDeclarations.entrySet()) {
                if (!ret.containsKey(entry.getKey())) {
                    ret.put(entry.getKey(), entry.getValue());
                }
            }
            currentElement = currentElement.getParentNode();
        }
        return ret;
    }
}
