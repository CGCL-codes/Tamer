package net.sf.xmlutils;

import java.io.*;
import javax.xml.parsers.*;
import org.apache.xerces.xni.*;
import org.apache.xerces.parsers.DOMParser;
import org.xml.sax.*;
import org.w3c.dom.*;

/** Parser of XML documents, adding the following user data on each node :
 *    - start line (line where the node starts)
 *    - file path (full path of the file containing the node)
 *  The document read can be retrieved by 'getDocument()'.
 */
public class XmlReader extends DOMParser {

    public XmlReader(String nFilePath) throws Exception {
        super();
        InitWithFilePath(nFilePath);
    }

    protected void InitWithFilePath(String nFilePath) throws Exception {
        _FilePath = nFilePath;
        setFeature("http://apache.org/xml/features/dom/defer-node-expansion", false);
        parse(new InputSource(new FileInputStream(nFilePath)));
    }

    protected XMLLocator locator;

    protected String _FilePath;

    public void startDocument(XMLLocator this_locator, String encoding, NamespaceContext namespaceContext, Augmentations augs) throws XNIException {
        Node CurrentNode;
        this.locator = this_locator;
        super.startDocument(locator, encoding, namespaceContext, augs);
        try {
            CurrentNode = (Node) this.getProperty("http://apache.org/xml/properties/dom/current-element-node");
            if (CurrentNode != null) {
                CurrentNode.setUserData(XmlUtils.AttributeName_StartLineNumber, String.valueOf(locator.getLineNumber()), null);
                CurrentNode.setUserData(XmlUtils.AttributeName_FilePath, _FilePath, null);
            }
        } catch (org.xml.sax.SAXException e) {
            e.printStackTrace(System.err);
        }
    }

    public void startElement(QName elementQName, XMLAttributes attrList, Augmentations augs) throws XNIException {
        Node CurrentNode;
        super.startElement(elementQName, attrList, augs);
        try {
            CurrentNode = (Node) this.getProperty("http://apache.org/xml/properties/dom/current-element-node");
            if (CurrentNode != null) {
                CurrentNode.setUserData(XmlUtils.AttributeName_StartLineNumber, String.valueOf(locator.getLineNumber()), null);
                CurrentNode.setUserData(XmlUtils.AttributeName_FilePath, _FilePath, null);
            }
        } catch (org.xml.sax.SAXException e) {
            e.printStackTrace(System.err);
        }
    }

    protected XmlReader() {
    }
}
