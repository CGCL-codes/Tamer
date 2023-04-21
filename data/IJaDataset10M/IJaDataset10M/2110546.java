package com.tensegrity.palorules.util;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import com.tensegrity.palorules.PaloRulesPlugin;

/**
 * Utility funtions to parse a XML DOM.
 * @author AndreasEbbert
 * @version $Id: XMLUtil.java,v 1.6 2008/04/17 14:15:11 AndreasEbbert Exp $
 */
public class XMLUtil {

    private static final XPath xpath = XPathFactory.newInstance().newXPath();

    public static final NodeList getXPathNodeList(Node node, String xpr) throws XPathExpressionException {
        if (node == null) throw new IllegalArgumentException("given node must not be null.");
        if (xpr == null) throw new IllegalArgumentException("given xpath expression must not be null.");
        return (NodeList) xpath.evaluate(xpr, node, XPathConstants.NODESET);
    }

    public static final String getChildNodeText(Node parentNode, String descNodeName, String lang) {
        if (parentNode == null) throw new IllegalArgumentException("given node must not be null.");
        if (lang == null) throw new IllegalArgumentException("given language attribute value must not be null.");
        try {
            NodeList nl = getXPathNodeList(parentNode, "./" + descNodeName + "[@language='" + lang + "']/text()");
            if (nl != null && nl.getLength() > 0) return nl.item(0).getNodeValue();
        } catch (XPathExpressionException e) {
            PaloRulesPlugin.logError(e);
        }
        return null;
    }

    public static final Node getChild(Node node, String name) {
        final NodeList nl = node.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            if (nl.item(i).getNodeType() == Node.ELEMENT_NODE && nl.item(i).getNodeName().equals(name)) {
                return nl.item(i);
            }
        }
        return null;
    }

    /**
   * Returns an array containing all element child nodes with the given node
   * name.
   * @param node the parent node.
   * @param name the name of the child nodes to return.
   * @return an array containing all element child nodes with the given node
   *         name.
   */
    public static final Node[] getChildren(Node node, String name) {
        if (node == null) throw new IllegalArgumentException("Given parent node must not be null!");
        if (name == null) throw new IllegalArgumentException("Node name must not be null!");
        final NodeList nl = node.getChildNodes();
        ArrayList<Node> ch = new ArrayList<Node>();
        for (int i = 0; i < nl.getLength(); i++) {
            if (nl.item(i).getNodeType() == Node.ELEMENT_NODE && name.equals(nl.item(i).getNodeName())) {
                ch.add(nl.item(i));
            }
        }
        return ch.toArray(new Node[0]);
    }

    public static final String getArgValue(Node n, String argName) {
        NamedNodeMap nnm = n.getAttributes();
        if (nnm == null) return null;
        Node nNode = nnm.getNamedItem(argName);
        if (nNode == null) return null;
        return nNode.getNodeValue();
    }

    public static final String dumpNode(Node doc) {
        try {
            Source source = new DOMSource(doc);
            StringWriter sw = new StringWriter();
            Result result = new StreamResult(sw);
            Transformer xformer = TransformerFactory.newInstance().newTransformer();
            xformer.transform(source, result);
            return sw.toString();
        } catch (TransformerConfigurationException e) {
            PaloRulesPlugin.logError(e);
        } catch (TransformerFactoryConfigurationError e) {
            PaloRulesPlugin.logError(e);
        } catch (TransformerException e) {
            PaloRulesPlugin.logError(e);
        }
        return "error";
    }

    public static final Node parse(String xml) {
        if (xml == null || xml.length() == 0) {
            return null;
        }
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            Document doc = factory.newDocumentBuilder().parse(new InputSource(new StringReader(xml)));
            final Node rNode = doc.getDocumentElement();
            return rNode;
        } catch (Exception e) {
            PaloRulesPlugin.logError(e);
        }
        return null;
    }
}
