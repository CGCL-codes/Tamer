package com.darwinit.xmlfiles;

import com.darwinit.xmlfiles.files.TextFile;
import com.darwinit.xmlfiles.xml.AttributeList;
import com.darwinit.xmlfiles.xml.DOMStringSerializer;
import com.darwinit.xmlfiles.xml.NodeSelected;
import com.darwinit.xmlfiles.xml.NodeSelection;
import com.darwinit.xmlfiles.xml.XMLNSResolver;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Vector;
import javax.swing.JFrame;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * XMLFile: Helper class to handle XML based Files. 
 *
 * See also http://www.ibm.com/developerworks/library/x-javaxpathapi.html for xpath processing.
 *
 * @author Martien van den Akker
 * @author Darwin IT Professionals
 */
public class XMLFile extends TextFile {

    private Document doc;

    private boolean parsed = false;

    private DOMStringSerializer stringSerializer = null;

    private XMLNSResolver nsRes;

    /**
     * Global initiator for constructors
     */
    private void init() {
        logger = Logger.getLogger(XMLFile.class);
    }

    /**
     * Constructor 
     * @param text Content
     * @param filePath path to the file to save the content
     */
    public XMLFile(String text, String filePath) {
        super.init(text, filePath);
        init();
    }

    /**
     * Constructor
     * @param filePath to the file 
     */
    public XMLFile(String filePath) {
        super.init(filePath);
        init();
    }

    /**
     * Constructor
     * @param file handle to the file
     */
    public XMLFile(File file) {
        super.init(file);
        init();
    }

    /**
     * Parameterless Constructor
     */
    public XMLFile() {
        init();
    }

    /**
     * Load the file
     */
    public void load() {
        super.load();
        parsed = false;
    }

    /**
     * Create a new DocumentBuilder.
     * @return
     * @throws ParserConfigurationException
     */
    private DocumentBuilder newDocBuilder() throws ParserConfigurationException {
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        domFactory.setNamespaceAware(true);
        DocumentBuilder docBuilder;
        docBuilder = domFactory.newDocumentBuilder();
        return docBuilder;
    }

    /**
     * Create an InputSource from the xml-text
     * @return
     */
    public InputSource getInputSource() {
        String text = super.getText();
        InputSource inputStream = null;
        if (text != null) {
            inputStream = new InputSource();
            inputStream.setCharacterStream(new StringReader(text));
        }
        return inputStream;
    }

    /**
     * Parse the XML in the file
     */
    public void parse() {
        try {
            InputSource inputSource = getInputSource();
            if (inputSource != null) {
                resetError();
                DocumentBuilder docBuilder = newDocBuilder();
                Document doc = docBuilder.parse(inputSource);
                setDoc(doc);
                parsed = true;
                log("File: " + super.getFilePath() + " is succesfully parsed");
            } else {
                log("File: " + super.getFilePath() + " empty or not loaded!");
            }
        } catch (ParserConfigurationException e) {
            setErrorCode(EC_ERROR);
            setError("Error creating parser: " + e.toString());
            error(e);
        } catch (SAXException e) {
            setErrorCode(EC_ERROR);
            setError("Error parsing XML: " + e.toString());
            error(e);
        } catch (IOException e) {
            setErrorCode(EC_ERROR);
            setError("Error reading XML: " + e.toString());
            error(e);
        }
    }

    /**
     * @param childNodes
     * @return vector with distinct names of all childnodes
     */
    public Vector<String> distinctChildNodes(NodeList childNodes) {
        Vector<String> nodeNames = new Vector<String>();
        for (int i = 0; i < childNodes.getLength(); i++) {
            String name = childNodes.item(i).getNodeName();
            int type = childNodes.item(i).getNodeType();
            if (type != Node.TEXT_NODE && (nodeNames.indexOf(name) < 0)) {
                nodeNames.addElement(name);
            }
        }
        return nodeNames;
    }

    /**
     * @param nodes
     * @return vector with distinct names of all childnodes of nodes
     */
    public Vector<String> distinctNlChildNodes(NodeList nodes) {
        Vector<String> nodeNames = new Vector<String>();
        int found = nodes.getLength();
        if (found > 0) {
            for (int z = 0; z < found; z++) {
                Node curNode = nodes.item(z);
                Vector<String> childNodeNames = distinctChildNodes(curNode.getChildNodes());
                for (int i = 0; i < childNodeNames.size(); i++) {
                    String nodeName = childNodeNames.elementAt(i);
                    if (nodeNames.indexOf(nodeName) < 0) {
                        nodeNames.addElement(nodeName);
                    }
                }
            }
        }
        return nodeNames;
    }

    /**
     * @param node
     * @return Vector with the distinct attributes of the node
     */
    public Vector<String> distinctAttributes(Node node) {
        Vector<String> nodeNames = new Vector<String>();
        NamedNodeMap attributes = node.getAttributes();
        if (attributes != null) {
            for (int i = 0; i < attributes.getLength(); i++) {
                String attrName = attributes.item(i).getNodeName();
                if (nodeNames.indexOf(attrName) < 0) {
                    nodeNames.addElement(attrName);
                }
            }
        }
        return nodeNames;
    }

    /**
     * Get the Attributes of a Node
     * @param node
     * @return AttributeList of the attributes of the node
     */
    public AttributeList getAttributes(Node node) {
        AttributeList attrList = new AttributeList();
        NamedNodeMap attributes = node.getAttributes();
        if (attributes != null) {
            for (int i = 0; i < attributes.getLength(); i++) {
                String attrName = attributes.item(i).getNodeName();
                String attrValue = attributes.item(i).getNodeValue();
                attrList.addAttribute(attrName, attrValue);
            }
        }
        return attrList;
    }

    /**
     * Get the namespace declarations of a node
     * @param node
     * @return AttributeList of the attributes of the node
     */
    public AttributeList getNamespaces(Node node) {
        final String methodName = "AttributeList";
        debug("Start: " + methodName);
        AttributeList attrList = new AttributeList();
        NamedNodeMap attributes = node.getAttributes();
        if (attributes != null) {
            debug("Number of attributes: " + attributes.getLength());
            for (int i = 0; i < attributes.getLength(); i++) {
                Node attribute = attributes.item(i);
                String attrName = attribute.getNodeName();
                debug("Attribute: " + attrName);
                if (attrName.startsWith("xmlns:")) {
                    int pos = attrName.indexOf(":") + 1;
                    attrName = attrName.substring(pos);
                    String attrValue = attribute.getNodeValue();
                    debug("Namespace: " + attrName + ":" + attrValue);
                    attrList.addAttribute(attrName, attrValue);
                }
            }
        }
        debug("End: " + methodName);
        return attrList;
    }

    /**
     * @param nodes
     * @return Vector with the distinct attributes of all the nodes
     */
    public Vector<String> distinctNlAttributes(NodeList nodes) {
        Vector<String> attrNames = new Vector<String>();
        int found = nodes.getLength();
        if (found > 0) {
            for (int z = 0; z < found; z++) {
                Node curNode = nodes.item(z);
                Vector<String> nodeAttrNames = distinctAttributes(curNode);
                for (int i = 0; i < nodeAttrNames.size(); i++) {
                    String attrName = nodeAttrNames.elementAt(i);
                    if (attrNames.indexOf(attrName) < 0) {
                        attrNames.addElement(attrName);
                    }
                }
            }
        }
        return attrNames;
    }

    /**
     * @param node
     * @return Vector with the distinct attribute value pairs of the node
     */
    public Vector<String> distinctAttrValuePairs(Node node) {
        Vector<String> attrPairs = new Vector<String>();
        NamedNodeMap attributes = node.getAttributes();
        if (attributes != null) {
            for (int i = 0; i < attributes.getLength(); i++) {
                String attrName = attributes.item(i).getNodeName();
                String attrValue = attributes.item(i).getNodeValue();
                String attrPair = attrName + "=\"" + attrValue + "\"";
                if (attrPairs.indexOf(attrPair) < 0) {
                    attrPairs.addElement(attrPair);
                }
            }
        }
        return attrPairs;
    }

    /**
     * @param nodes
     * @return Vector with the distinct attribute value pairs of all the nodes
     */
    public Vector<String> distinctNlAttrValuePairs(NodeList nodes) {
        Vector<String> attrPairs = new Vector<String>();
        int found = nodes.getLength();
        if (found > 0) {
            for (int z = 0; z < found; z++) {
                Node curNode = nodes.item(z);
                Vector<String> nodeAttrPairs = distinctAttrValuePairs(curNode);
                for (int i = 0; i < nodeAttrPairs.size(); i++) {
                    String attrPair = nodeAttrPairs.elementAt(i);
                    if (attrPairs.indexOf(attrPair) < 0) {
                        attrPairs.addElement(attrPair);
                    }
                }
            }
        }
        return attrPairs;
    }

    /**
     * Serialize the XMLNode to the file-content
     */
    public void serialize() {
        if (stringSerializer == null) {
            stringSerializer = new DOMStringSerializer();
        }
        if (!parsed) {
            log("File " + getFilePath() + " cannot be serialized: not parsed!");
        } else {
            log("File " + getFilePath() + " is parsed so serialize!");
            super.setText(stringSerializer.serializeDocument(getDoc()));
            log("File " + getFilePath() + " is serialized!");
        }
    }

    /**
     * Reset to a new file, empty file, filepath etc. Create a new domDocument
     */
    public void newFile() {
        super.setFilePath(null);
        setError("");
        DocumentBuilder docBuilder;
        try {
            docBuilder = newDocBuilder();
            Document doc = docBuilder.newDocument();
            setDoc(doc);
            setParsed(true);
            serialize();
        } catch (ParserConfigurationException e) {
            setError("Error creating parser: " + e.toString());
            error(e);
        }
    }

    /**
     * Save the document after having serialized it.
     */
    public void save() {
        serialize();
        super.save();
    }

    public File chooseXMLFile(String dftDirectory, JFrame frame) {
        return super.chooseFile(dftDirectory, "xml", frame);
    }

    public File chooseXMLFile(JFrame frame) {
        return super.chooseFile("xml", frame);
    }

    public File chooseXMLFileSave(String dftDirectory, JFrame frame) {
        return super.chooseFileSave(dftDirectory, "xml", frame);
    }

    public File chooseXMLFileSave(JFrame frame) {
        return super.chooseFileSave("xml", frame);
    }

    public File chooseXSLFile(String dftDirectory, JFrame frame) {
        return super.chooseFile(dftDirectory, "xsl", frame);
    }

    public File chooseXSLFile(JFrame frame) {
        return super.chooseFile("xsl", frame);
    }

    public File chooseXSLFileSave(String dftDirectory, JFrame frame) {
        return super.chooseFileSave(dftDirectory, "xsl", frame);
    }

    public File chooseXSLFileSave(JFrame frame) {
        return super.chooseFileSave("xsl", frame);
    }

    public void chooseAndLoadXML(String dftDirectory, JFrame frame) {
        super.chooseAndLoad(dftDirectory, "xml", frame);
    }

    public void chooseAndLoadXML(JFrame frame) {
        super.chooseAndLoad("xml", frame);
    }

    public void chooseAndSaveXML(String dftDirectory, JFrame frame) {
        super.chooseAndSave(dftDirectory, "xml", frame);
    }

    public void chooseAndSaveXML(JFrame frame) {
        super.chooseAndSave("xml", frame);
    }

    public void chooseAndLoadXSL(String dftDirectory, JFrame frame) {
        super.chooseAndLoad(dftDirectory, "xsl", frame);
    }

    public void chooseAndLoadXSL(JFrame frame) {
        super.chooseAndLoad("xsl", frame);
    }

    public void chooseAndSaveXSL(String dftDirectory, JFrame frame) {
        super.chooseAndSave(dftDirectory, "xsl", frame);
    }

    public void chooseAndSaveXSL(JFrame frame) {
        super.chooseAndSave("xsl", frame);
    }

    /**
     * Evaluate xpath expression 
     * 
     * @param xpathExpr the xpath expression
     * @param returnType the return type that is expected.
     * http://www.ibm.com/developerworks/library/x-javaxpathapi.html:
     * XPathConstants.NODESET => node-set maps to an org.w3c.dom.NodeList
     * XPathConstants.BOOLEAN => boolean maps to a java.lang.Boolean
     * XPathConstants.NUMBER => number maps to a java.lang.Double
     * XPathConstants.STRING => string maps to a java.lang.String
     * XPathConstants.NODE
     * 
     * @throws XPathExpressionException
     */
    public Object evaluate(String xpathExpr, QName returnType) throws XPathExpressionException {
        XPathFactory factory = XPathFactory.newInstance();
        XPath xpath = factory.newXPath();
        XMLNSResolver nsRes = getNsRes();
        if (nsRes != null) {
            xpath.setNamespaceContext(nsRes);
        }
        XPathExpression expr = xpath.compile(xpathExpr);
        Document doc = getDoc();
        Object resultObj = expr.evaluate(doc, returnType);
        return resultObj;
    }

    /**
     * Evaluate xpath expression to a double (when a number is expected from the xpath expression)
     * http://www.ibm.com/developerworks/library/x-javaxpathapi.html
     * 
     * XPathConstants.NODESET => node-set maps to an org.w3c.dom.NodeList
     * XPathConstants.BOOLEAN => boolean maps to a java.lang.Boolean
     * XPathConstants.NUMBER => number maps to a java.lang.Double
     * XPathConstants.STRING => string maps to a java.lang.String
     * XPathConstants.NODE
     * 
     * @throws XPathExpressionException
     */
    public Double evaluateDouble(String xpathExpr) throws XPathExpressionException {
        Double result = null;
        Object resultObj = evaluate(xpathExpr, XPathConstants.NUMBER);
        if (resultObj instanceof Double) {
            result = (Double) resultObj;
        }
        return result;
    }

    /**
     * Select Nodes using Xpath
     * 
     * @param xpath
     * @return NodeList
     * @throws XPathExpressionException 
     */
    public NodeList selectNodes(String xpath) throws XPathExpressionException {
        NodeList nl = (NodeList) evaluate(xpath, XPathConstants.NODESET);
        return nl;
    }

    /**
     * Select Nodes using Xpath
     * 
     * @param xpath
     * @return NodeSelection
     * @throws XPathExpressionException 
     */
    public NodeSelection selectNodeSel(String xpath) throws XPathExpressionException {
        NodeList nl = selectNodes(xpath);
        NodeSelection ns = new NodeSelection(nl);
        return ns;
    }

    /**
     * Select Nodes using Xpath, filtered on Element Nodes.
     * @param xpath
     * @return NodeSelection
     * @throws XPathExpressionException 
     */
    public NodeSelection selectNodeSelElmt(String xpath) throws XPathExpressionException {
        NodeList nl = selectNodes(xpath);
        NodeSelection ns = NodeSelection.getElementNodes(nl);
        return ns;
    }

    /**
     * Select AttributeNodes using Xpath, filtered on Element Nodes.
     * @param xpath
     * @return NodeSelection 
     * @throws XPathExpressionException 
     */
    public NodeSelection selectNodeSelAttr(String xpath) throws XPathExpressionException {
        NodeList nl = selectNodes(xpath);
        NodeSelection ns = NodeSelection.getAttributeNodes(nl);
        return ns;
    }

    /**
     * Get the text content of a node.
     * @param xpath
     * @return
     * @throws XPathExpressionException
     */
    public String getXpathTextContent(String xpath) throws XPathExpressionException {
        NodeList nl = selectNodes(xpath);
        String result = null;
        if (nl.getLength() > 0) {
            result = nl.item(0).getNodeValue();
        }
        return result;
    }

    /**
     * Select nodes using Xpath with Namespace included
     * @param xpathStr
     * @return Nodelist
     */
    public NodeList selectNodesNS(String xpathStr) throws XPathExpressionException {
        NodeList nl = selectNodes(xpathStr);
        return nl;
    }

    /**
     * Select nodes using Xpath with Namespace included
     * @param xpathStr
     * @return Nodelist 
     */
    public NodeSelection selectNodeSelNS(String xpathStr) throws XPathExpressionException {
        NodeSelection ns = selectNodeSel(xpathStr);
        return ns;
    }

    /**
     * Set the XML Document
     * @param doc
     */
    public void setDoc(Document doc) {
        this.doc = doc;
    }

    /**
     * Get the XML Document as a Document
     * @return
     */
    public Document getDoc() {
        return this.doc;
    }

    /**
     * Get the root Element
     * 
     * @return
     */
    public Element getXmlRoot() {
        Document doc = getDoc();
        Element xmlRoot = doc.getDocumentElement();
        return xmlRoot;
    }

    /**
     * Get the root element as Node
     * 
     * @return
     */
    public Node getXmlRootNode() {
        return getXmlRoot();
    }

    /**
     * Get the root element as NodeSelected
     * 
     * @return
     */
    public NodeSelected getXmlRootNodeSel() {
        Node root = getXmlRootNode();
        NodeSelected nodeSel = new NodeSelected(root);
        return nodeSel;
    }

    /**
     * Set the Parsed Flag
     * @param parsed
     */
    public void setParsed(boolean parsed) {
        this.parsed = parsed;
    }

    /**
     * Is the XML Document Parsed?
     * @return
     */
    public boolean isParsed() {
        return parsed;
    }

    /**
     * Add Namespace to resolver
     * @param abbrev
     * @param namespace
     */
    public void addNS(String abbrev, String namespace) {
        if (nsRes == null) {
            setNsRes(new XMLNSResolver());
        }
        nsRes.addNS(abbrev, namespace);
    }

    /**
     * Set the Namespace Resolver
     * @param nsRes
     */
    public void setNsRes(XMLNSResolver nsRes) {
        this.nsRes = nsRes;
    }

    /**
     * Get the Namespace Resolver.
     * @return
     */
    public XMLNSResolver getNsRes() {
        return nsRes;
    }
}
