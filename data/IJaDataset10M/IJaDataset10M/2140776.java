package ingenias.dom;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import org.apache.xerces.parsers.DOMParser;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XMLLocator;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.XNIException;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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

    /** Print writer. */
    private PrintWriter out;

    private static boolean NotIncludeIgnorableWhiteSpaces = false;

    private XMLLocator locator;

    public DOMAddLines(String inputName) {
        try {
            this.setFeature("http://apache.org/xml/features/dom/defer-node-expansion", false);
            this.parse(inputName);
            out = new PrintWriter(new OutputStreamWriter(System.out, "UTF8"));
        } catch (IOException e) {
            System.err.println("except" + e);
        } catch (org.xml.sax.SAXException e) {
            System.err.println("except" + e);
        }
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
                    Attr attrs[] = sortAttributes(node.getAttributes());
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
                    if (data != null && data.length() > 0) {
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

    public void startElement(QName elementQName, XMLAttributes attrList, Augmentations augs) throws XNIException {
        super.startElement(elementQName, attrList, augs);
        Node node = null;
        try {
            node = (Node) this.getProperty("http://apache.org/xml/properties/dom/current-element-node");
        } catch (org.xml.sax.SAXException ex) {
            System.err.println("except" + ex);
            ;
        }
        if (node != null) node.setUserData("startLine", String.valueOf(locator.getLineNumber()), null);
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
        if (node != null) node.setUserData("startLine", String.valueOf(locator.getLineNumber()), null);
    }

    public void ignorableWhitespace(XMLString text, Augmentations augs) throws XNIException {
        if (!NotIncludeIgnorableWhiteSpaces) super.ignorableWhitespace(text, augs); else ;
    }

    /** Main program entry point. */
    public static void main(String argv[]) {
        if (argv.length == 0) {
            printUsage();
            System.exit(1);
        }
        for (int i = 0; i < argv.length; i++) {
            String arg = argv[i];
            if (arg.startsWith("-")) {
                if (arg.equals("-h")) {
                    printUsage();
                    System.exit(1);
                }
                if (arg.equals("-i")) {
                    NotIncludeIgnorableWhiteSpaces = true;
                    continue;
                }
            }
            DOMAddLines domAddExample = new DOMAddLines(arg);
            Document doc = domAddExample.getDocument();
            domAddExample.print(doc);
        }
    }

    /** Prints the usage. */
    private static void printUsage() {
        System.err.println("usage: jre dom.DOMAddLines (options) uri ...");
        System.err.println();
        System.err.println("  -h       Display help screen.");
        System.err.println("  -i       Don't print ignorable white spaces.");
    }
}
