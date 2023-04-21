package net.lagerwey.xml;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Stack;

/**
 * This XmlReader class parses an XML file into smaller repeated parts.
 * @author lagerweij
 */
public class XmlReader {

    private XmlReaderListener listener;

    private String[] xpaths;

    /**
	 * Reads the XML string and parses it, where every child node is parsed and given to the listener.
	 * @param xml The XML to parse.
	 * @param listener The listener class that gets every child node.
	 */
    public void read(String xml, XmlReaderListener listener) {
        this.read(xml, (String) null, listener);
    }

    /**
	 * Reads the stream InputStream and parses it, where every child node is parsed and given to the listener.
	 * @param stream The InputStream to read the XML from.
	 * @param listener The listener class that gets every child node.
	 */
    public void read(InputStream stream, XmlReaderListener listener) {
        this.read(stream, (String) null, listener);
    }

    /**
	 * Reads the XML string and parses it, where every child node is parsed and given to the listener.
	 * @param xml The XML to parse.
	 * @param listener The listener class that gets every child node.
	 */
    public void read(String xml, String xpath, XmlReaderListener listener) {
        this.listener = listener;
        this.xpaths = new String[] { xpath };
        loadXml(xml);
    }

    /**
	 * Reads the XML string and parses it, where every child node is parsed and given to the listener.
	 * @param xml The XML to parse.
	 * @param listener The listener class that gets every child node.
	 */
    public void read(String xml, String[] xpaths, XmlReaderListener listener) {
        this.listener = listener;
        this.xpaths = (String[]) xpaths.clone();
        ;
        loadXml(xml);
    }

    /**
	 * Reads the stream InputStream and parses it, where every child node is parsed and given to the listener.
	 * @param stream The InputStream to read the XML from.
	 * @param listener The listener class that gets every child node.
	 */
    public void read(InputStream stream, String xpath, XmlReaderListener listener) {
        this.listener = listener;
        this.xpaths = new String[] { xpath };
        loadXml(stream);
    }

    /**
	 * Reads the stream InputStream and parses it, where every child node is parsed and given to the listener.
	 * @param stream The InputStream to read the XML from.
	 * @param listener The listener class that gets every child node.
	 */
    public void read(InputStream stream, String[] xpaths, XmlReaderListener listener) {
        this.listener = listener;
        this.xpaths = (String[]) xpaths.clone();
        loadXml(stream);
    }

    /**
	 * Every child node should be passed on to the listener.
	 * @param child The child node which is parsed.
	 */
    private void node(XmlNode child) {
        listener.node(child);
    }

    /**
	 * Loads the XML document from the specified string.  
	 * @param xml String containing the XML document to load.
	 * @throws XmlException There is a load or parse error in the XML.
	 */
    private void loadXml(String xml) throws XmlException {
        if (xml == null || xml.length() == 0) {
            return;
        }
        try {
            SAXParser.parse(new XmlDocHandler(), new ByteArrayInputStream(xml.getBytes()), false);
        } catch (Exception e) {
            throw new XmlException(e);
        }
    }

    /**
	 * Loads the XML document from the specified InputStream.  
	 * @param stream InputStream containing the XML document to load.
	 * @throws XmlException There is a load or parse error in the XML.
	 */
    private void loadXml(InputStream stream) throws XmlException {
        if (stream == null) {
            throw new XmlException("Stream is null.");
        }
        try {
            SAXParser.parse(new XmlDocHandler(), stream, false);
        } catch (Exception e) {
            throw new XmlException(e);
        }
    }

    /**
	 * Handles parser events of QDParser.
	 * @author lagerweij
	 */
    private class XmlDocHandler implements DocHandler {

        private Stack<XmlNode> stack = new Stack<XmlNode>();

        private XmlDocument document = new XmlDocument();

        /**
		 * Called when an element is started.
		 */
        public void startElement(String tag, Hashtable h) throws Exception {
            XmlNode node = null;
            if (!stack.isEmpty()) {
                XmlNode parent = stack.peek();
                node = parent.createChildNode(tag, h);
            } else {
                node = document.createChildNode(tag, h);
            }
            stack.push(node);
        }

        /**
		 * Called when an element has ended.
		 */
        public void endElement(String tag) throws Exception {
            XmlNode node = stack.pop();
            if (node.getName().trim().equals(tag.trim()) == false) {
                throw new XmlException("Found '</" + tag + ">' but expected '</" + node.getName() + ">' node");
            }
            if (stack.isEmpty()) {
                document.add(node);
            }
            if (xpaths != null && xpaths[0] == null && node.getParent() == document.getDocumentElement()) {
                node(node);
                document.getDocumentElement().getChildren().clear();
            } else if (xpaths != null && xpaths.length == 1) {
                if (node.getDocument().selectSingleNode(xpaths[0]) == node) {
                    node(node);
                    node.getParent().getChildren().clear();
                }
            } else if (xpaths != null && xpaths.length > 1) {
                for (int i = 0; i < xpaths.length; i++) {
                    String path = xpaths[i];
                    if (node.getDocument().selectSingleNode(path) == node) {
                        node(node);
                        node.getParent().getChildren().clear();
                        break;
                    }
                }
            }
        }

        /**
		 * Called when the document is started.
		 */
        public void startDocument() throws Exception {
        }

        /**
		 * Called when the document is ended.
		 */
        public void endDocument() throws Exception {
        }

        /**
		 * Called when text within a node is detected.
		 */
        public void text(String str) throws Exception {
            if (stack.isEmpty() == false) {
                if (str.trim().length() > 0) {
                    XmlNode node = stack.peek();
                    Text text = new Text(str);
                    node.appendChild(text);
                }
            }
        }

        /**
		 * Called when CDATA within a node is detected.
		 */
        public void cdata(String cdata) throws Exception {
            if (stack.isEmpty() == false) {
                CDATASection newSection = new CDATASection(cdata);
                XmlNode node = stack.peek();
                node.add(newSection);
            }
        }

        public void xmlDeclaration(String name, Hashtable h) {
        }

        public void comment(String comment) {
        }
    }
}
