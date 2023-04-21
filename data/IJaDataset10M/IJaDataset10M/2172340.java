package org.buglet.xml;

import org.buglet.util.StringParser;
import org.buglet.util.StringUtil;
import org.apache.xerces.dom.DocumentImpl;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import java.util.Vector;

/**
 * A sample DOM writer. This sample program illustrates how to
 * traverse a DOM tree in order to print a document that is parsed.
 *
 * @version $Id: DOMUtil.java,v 1.4 2001/08/28 19:50:20 stevedbrown Exp $
 */
public class DOMUtil {

    Document document;

    public DOMUtil(String uri) {
        DOMParserWrapper parser = new DOMParserWrapper();
        try {
            document = parser.parse(uri);
        } catch (Exception ex) {
            System.err.println("Error during parsing");
            ex.printStackTrace();
        }
    }

    public DOMUtil(Document document) {
        this.document = document;
    }

    public Element createElement(String name) {
        Element ne = document.createElement(name);
        return ne;
    }

    public Element createElement(String name, String text) {
        Element ne = document.createElement(name);
        ne.appendChild(document.createTextNode(text));
        return ne;
    }

    public Node addNode(String path, Node nn) {
        Node parent = getNode(path);
        if (parent == null) {
            System.out.println("parent is null");
            return null;
        }
        parent.appendChild(nn);
        return nn;
    }

    public Node addNode(String path, String name) {
        Node parent = getNode(path);
        if (parent == null) {
            System.out.println("parent is null");
            return null;
        }
        Element ne = document.createElement(name);
        parent.appendChild(ne);
        return ne;
    }

    public Node addNode(String path, String name, String text) {
        Node ne = addNode(path, name);
        if (ne == null) {
            System.out.println("parent is null");
            return null;
        }
        ne.appendChild(document.createTextNode(text));
        return ne;
    }

    public Node addAttribute(String path, String name, String text) {
        Element e = (Element) getNode(path);
        if (e == null) return null;
        e.setAttribute(name, text);
        return e;
    }

    public Node addText(String path, String text) {
        Element e = (Element) getNode(path);
        if (e == null) return null;
        e.appendChild(document.createTextNode(text));
        return e;
    }

    public Node setAttribute(String path, String name, String text) {
        Element e = (Element) getNode(path);
        if (e == null) return null;
        e.setAttribute(name, text);
        return e;
    }

    public Node setText(String path, String text) {
        Element e = (Element) getNode(path);
        if (e == null) return null;
        clearText(e);
        e.appendChild(document.createTextNode(text));
        return e;
    }

    public void clearText(Node node) {
        NodeList li = node.getChildNodes();
        for (int i = 0; i < li.getLength(); i++) {
            clearTextHelper(node, li.item(i));
        }
    }

    public Node getNode(String path) {
        StringParser parser = new StringParser(path, "/");
        String[] pathArray = parser.getArray();
        if (pathArray == null || pathArray.length == 0) return null;
        Element root = document.getDocumentElement();
        if (!root.getNodeName().equals(pathArray[0])) return null;
        return getNode(root, pathArray, 1);
    }

    public Node getNode(Node curr, String[] path, int pos) {
        if (pos == path.length) return curr;
        NodeList nl = curr.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            if (nl.item(i).getNodeName().equals(path[pos])) {
                return getNode(nl.item(i), path, ++pos);
            }
        }
        return null;
    }

    public boolean nodeExists(String path) {
        return (getNode(path) != null);
    }

    public Vector getNodes(String path) {
        StringParser parser = new StringParser(path, "/");
        String[] pathArray = parser.getArray();
        if (pathArray == null || pathArray.length == 0) return null;
        Element root = document.getDocumentElement();
        if (!root.getNodeName().equals(pathArray[0])) return null;
        Vector vec = new Vector();
        System.out.println("path is " + path + " and pathArray.length is " + pathArray.length);
        getNodes(root, pathArray, 0, vec);
        return vec;
    }

    public void getNodes(Node curr, String[] path, int pos, Vector vec) {
        System.out.println("getNodes:pos=" + pos);
        if (!path[pos++].equals(curr.getNodeName())) return;
        System.out.println("pos=" + pos + "&path.length=" + path.length);
        if (pos == path.length) {
            vec.add(curr);
            return;
        }
        NodeList nl = curr.getChildNodes();
        System.out.println("pos=" + pos + "&path[pos]=" + path[pos] + "&curr.getNodeName()=" + curr.getNodeName());
        for (int i = 0; i < nl.getLength(); i++) {
            System.out.println("path[pos]=" + path[pos]);
            System.out.println(nl.item(i).getNodeName());
            if (nl.item(i).getNodeName().equals(path[pos])) {
                getNodes(nl.item(i), path, pos, vec);
            }
        }
    }

    public String getNodeText(String path) {
        Node node = getNode(path);
        if (node == null) return null;
        return unnormalize(nodeVal(node));
    }

    public String getNodeAttribute(String path, String attr) {
        Element node = (Element) getNode(path);
        if (node == null) return null;
        return unnormalize(node.getAttribute(attr));
    }

    public String print() {
        return print(document);
    }

    /** Prints the resulting document tree. */
    public static String print(Document document) {
        StringBuffer buf = new StringBuffer();
        print(document, buf);
        return buf.toString();
    }

    /** Prints the specified node, recursively. */
    public static void print(Node node, StringBuffer buf) {
        if (node == null) {
            return;
        }
        int type = node.getNodeType();
        switch(type) {
            case Node.DOCUMENT_NODE:
                {
                    buf.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                    NodeList children = node.getChildNodes();
                    for (int iChild = 0; iChild < children.getLength(); iChild++) {
                        print(children.item(iChild), buf);
                    }
                    break;
                }
            case Node.ELEMENT_NODE:
                {
                    buf.append('<');
                    buf.append(node.getNodeName());
                    Attr attrs[] = sortAttributes(node.getAttributes());
                    for (int i = 0; i < attrs.length; i++) {
                        Attr attr = attrs[i];
                        buf.append(' ');
                        buf.append(attr.getNodeName());
                        buf.append("=\"");
                        buf.append(normalize(attr.getNodeValue()));
                        buf.append('"');
                    }
                    buf.append('>');
                    NodeList children = node.getChildNodes();
                    if (children != null) {
                        int len = children.getLength();
                        for (int i = 0; i < len; i++) {
                            print(children.item(i), buf);
                        }
                    }
                    break;
                }
            case Node.ENTITY_REFERENCE_NODE:
                {
                    buf.append('&');
                    buf.append(node.getNodeName());
                    buf.append(';');
                    break;
                }
            case Node.CDATA_SECTION_NODE:
                {
                    buf.append("<![CDATA[");
                    buf.append(node.getNodeValue());
                    buf.append("]]>");
                    break;
                }
            case Node.TEXT_NODE:
                {
                    buf.append(normalize(node.getNodeValue()));
                    break;
                }
            case Node.PROCESSING_INSTRUCTION_NODE:
                {
                    buf.append("<?");
                    buf.append(node.getNodeName());
                    String data = node.getNodeValue();
                    if (data != null && data.length() > 0) {
                        buf.append(' ');
                        buf.append(data);
                    }
                    buf.append("?>\n");
                    break;
                }
        }
        if (type == Node.ELEMENT_NODE) {
            buf.append("</");
            buf.append(node.getNodeName());
            buf.append('>');
        }
    }

    public static String nodeVal(Node node) {
        StringBuffer buf = new StringBuffer();
        nodeVal(node, buf);
        return buf.toString();
    }

    /** Prints the text contents of the specified node */
    public static void nodeVal(Node node, StringBuffer buf) {
        if (node == null) {
            return;
        }
        int type = node.getNodeType();
        switch(type) {
            case Node.DOCUMENT_NODE:
                {
                    return;
                }
            case Node.ELEMENT_NODE:
                {
                    NodeList children = node.getChildNodes();
                    if (children != null) {
                        int len = children.getLength();
                        for (int i = 0; i < len; i++) {
                            if (children.item(i).getNodeType() == Node.ENTITY_REFERENCE_NODE || children.item(i).getNodeType() == Node.TEXT_NODE) print(children.item(i), buf);
                        }
                    }
                    break;
                }
            case Node.ENTITY_REFERENCE_NODE:
                {
                    buf.append('&');
                    buf.append(node.getNodeName());
                    buf.append(';');
                    break;
                }
            case Node.CDATA_SECTION_NODE:
                {
                    return;
                }
            case Node.TEXT_NODE:
                {
                    buf.append(normalize(node.getNodeValue()));
                    break;
                }
            case Node.PROCESSING_INSTRUCTION_NODE:
                {
                    return;
                }
        }
    }

    /** Prints the text contents of the specified node */
    public static void clearTextHelper(Node parent, Node node) {
        if (node == null) return;
        int type = node.getNodeType();
        switch(type) {
            case Node.TEXT_NODE:
                parent.removeChild(node);
            case Node.DOCUMENT_NODE:
            case Node.ELEMENT_NODE:
            case Node.ENTITY_REFERENCE_NODE:
            case Node.CDATA_SECTION_NODE:
            case Node.PROCESSING_INSTRUCTION_NODE:
                return;
        }
    }

    /** Returns a sorted list of attributes. */
    protected static Attr[] sortAttributes(NamedNodeMap attrs) {
        int len = (attrs != null) ? attrs.getLength() : 0;
        Attr array[] = new Attr[len];
        for (int i = 0; i < len; i++) {
            array[i] = (Attr) attrs.item(i);
        }
        for (int i = 0; i < len - 1; i++) {
            String name = array[i].getNodeName();
            int index = i;
            for (int j = i + 1; j < len; j++) {
                String curName = array[j].getNodeName();
                if (curName.compareTo(name) < 0) {
                    name = curName;
                    index = j;
                }
            }
            if (index != i) {
                Attr temp = array[i];
                array[i] = array[index];
                array[index] = temp;
            }
        }
        return (array);
    }

    /** Normalizes the given string. */
    public static String normalize(String s) {
        StringBuffer str = new StringBuffer();
        int len = (s != null) ? s.length() : 0;
        for (int i = 0; i < len; i++) {
            char ch = s.charAt(i);
            switch(ch) {
                case '<':
                    {
                        str.append("&lt;");
                        break;
                    }
                case '>':
                    {
                        str.append("&gt;");
                        break;
                    }
                case '&':
                    {
                        str.append("&amp;");
                        break;
                    }
                case '"':
                    {
                        str.append("&quot;");
                        break;
                    }
                case '\'':
                    {
                        str.append("&apos;");
                        break;
                    }
                default:
                    {
                        str.append(ch);
                    }
            }
        }
        return (str.toString());
    }

    /** Normalizes the given string. */
    public static String unnormalize(String s) {
        s = StringUtil.replace(s, "&lt;", "<");
        s = StringUtil.replace(s, "&gt;", ">");
        s = StringUtil.replace(s, "&amp;", "&");
        s = StringUtil.replace(s, "&quot;", "\"");
        s = StringUtil.replace(s, "&apos;", "'");
        return s;
    }

    public static void main(String[] args) {
        DocumentImpl di = new DocumentImpl(false);
        Element elem = di.createElement("One");
        di.appendChild(elem);
        DOMUtil dom = new DOMUtil(di);
        dom.addNode("One", "Two");
        Element thr = di.createElement("Three");
        dom.addNode("One/Two", thr);
        System.out.println("hi");
        System.out.println("dom.getNodes(\"One/Two/Three\").size()" + dom.getNodes("One/Two/Three").size());
        System.out.println(DOMUtil.print(di));
    }
}
