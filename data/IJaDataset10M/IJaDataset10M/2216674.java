package net.sf.xpontus.plugins.evaluator;

import net.sf.xpontus.utils.NullEntityResolver;
import org.apache.xerces.parsers.DOMParser;
import org.apache.xerces.xni.*;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * A sample of Adding lines to the DOM Node. This sample program illustrates:
 * - How to override methods from  DocumentHandler ( XMLDocumentHandler)
 * - How to turn off ignorable white spaces by overriding ignorableWhiteSpace
 * - How to use the SAX Locator to return row position ( line number of DOM element).
 * - How to attach user defined Objects to Nodes using method setUserData
 * This example relies on the following:
 * - Turning off the "fast" DOM so we can use set expansion to FULL
 * @version $Id: DOMAddLines.java,v 1.11 2005/05/09 01:01:40 mrglavas Exp $
 */
public class DOMAddLines extends DOMParser {

    private static boolean NotIncludeIgnorableWhiteSpaces = false;

    /** Print writer. */
    private PrintWriter out;

    private XMLLocator locator;

    public DOMAddLines(InputSource src) {
        try {
            this.setFeature("http://apache.org/xml/features/dom/defer-node-expansion", false);
            this.setEntityResolver(NullEntityResolver.getInstance());
            this.parse(src);
            out = new PrintWriter(new OutputStreamWriter(System.out, "UTF8"));
        } catch (IOException e) {
            System.err.println("except" + e);
        } catch (org.xml.sax.SAXException e) {
            System.err.println("except" + e);
        }
    }

    public int getLineNumber(Node node) {
        String lineRowColumn = (String) ((Node) node).getUserData("startLine");
        if (lineRowColumn == null) {
            return -1;
        } else {
            return Integer.parseInt(lineRowColumn);
        }
    }

    public int getColumnNumber(Node node) {
        String lineRowColumn = (String) ((Node) node).getUserData("startColumn");
        if (lineRowColumn == null) {
            return -1;
        } else {
            return Integer.parseInt(lineRowColumn);
        }
    }

    public String getLineInfo(Node node) {
        String lineRowColumn = (String) ((Node) node).getUserData("startLine");
        String lineRowColumn1 = (String) ((Node) node).getUserData("startColumn");
        return lineRowColumn + ":" + lineRowColumn1;
    }

    /** Prints the specified node, recursively. */
    public void print(Node node) {
        if (node == null) {
            return;
        }
        String lineRowColumn = (String) ((Node) node).getUserData("startLine");
        int type = node.getNodeType();
        switch(type) {
            case Node.DOCUMENT_NODE:
                {
                    out.println(lineRowColumn + ":" + "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                    print(((Document) node).getDocumentElement());
                    out.flush();
                    break;
                }
            case Node.ELEMENT_NODE:
                {
                    out.print(lineRowColumn + ":" + '<');
                    out.print(node.getNodeName());
                    Attr[] attrs = sortAttributes(node.getAttributes());
                    for (int i = 0; i < attrs.length; i++) {
                        Attr attr = attrs[i];
                        out.print(' ');
                        out.print(attr.getNodeName());
                        out.print("=\"");
                        out.print(attr.getNodeValue());
                        out.print('"');
                    }
                    out.print('>');
                    NodeList children = node.getChildNodes();
                    if (children != null) {
                        int len = children.getLength();
                        for (int i = 0; i < len; i++) {
                            print(children.item(i));
                        }
                    }
                    break;
                }
            case Node.ENTITY_REFERENCE_NODE:
                {
                    out.print('&');
                    out.print(node.getNodeName());
                    out.print(';');
                    break;
                }
            case Node.CDATA_SECTION_NODE:
                {
                    out.print("<![CDATA[");
                    out.print(node.getNodeValue());
                    out.print("]]>");
                    break;
                }
            case Node.TEXT_NODE:
                {
                    out.print(node.getNodeValue());
                    break;
                }
            case Node.PROCESSING_INSTRUCTION_NODE:
                {
                    out.print("<?");
                    out.print(node.getNodeName());
                    String data = node.getNodeValue();
                    if ((data != null) && (data.length() > 0)) {
                        out.print(' ');
                        out.print(data);
                    }
                    out.print("?>");
                    break;
                }
        }
        if (type == Node.ELEMENT_NODE) {
            out.print("</");
            out.print(node.getNodeName());
            out.print('>');
        }
        out.flush();
    }

    /** Returns a sorted list of attributes. */
    private Attr[] sortAttributes(NamedNodeMap attrs) {
        int len = (attrs != null) ? attrs.getLength() : 0;
        Attr[] array = new Attr[len];
        for (int i = 0; i < len; i++) {
            array[i] = (Attr) attrs.item(i);
        }
        for (int i = 0; i < (len - 1); i++) {
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

    public void startElement(QName elementQName, XMLAttributes attrList, Augmentations augs) throws XNIException {
        super.startElement(elementQName, attrList, augs);
        Node node = null;
        try {
            node = (Node) this.getProperty("http://apache.org/xml/properties/dom/current-element-node");
        } catch (org.xml.sax.SAXException ex) {
            System.err.println("except" + ex);
            ;
        }
        if (node != null) {
            node.setUserData("startLine", String.valueOf(locator.getLineNumber()), null);
            node.setUserData("startColumn", String.valueOf(locator.getColumnNumber()), null);
        }
    }

    public void startDocument(XMLLocator locator, String encoding, NamespaceContext namespaceContext, Augmentations augs) throws XNIException {
        super.startDocument(locator, encoding, namespaceContext, augs);
        this.locator = locator;
        Node node = null;
        try {
            node = (Node) this.getProperty("http://apache.org/xml/properties/dom/current-element-node");
        } catch (org.xml.sax.SAXException ex) {
            System.err.println("except" + ex);
            ;
        }
        if (node != null) {
            node.setUserData("startLine", String.valueOf(locator.getLineNumber()), null);
        }
    }

    public void ignorableWhitespace(XMLString text, Augmentations augs) throws XNIException {
        if (!NotIncludeIgnorableWhiteSpaces) {
            super.ignorableWhitespace(text, augs);
        } else {
            ;
        }
    }
}
