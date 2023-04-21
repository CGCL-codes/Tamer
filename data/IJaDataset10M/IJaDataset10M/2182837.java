package org.jdesktop.xpath;

import java.io.StringWriter;
import java.util.Properties;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.jdesktop.dom.SimpleNodeList;
import org.jdesktop.xpath.function.XPathFunctionResolverImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * <p>Various utilities for working with XPath. This class uses a static XPath
 * object for most of it's work. This XPath object is preconfigured to use the
 * extended XPath functions of the org.jdesktop.xpath.function package.</p>
 * 
 * @author rbair
 */
public class XPathUtils {

    private static final XPath xpath;

    private static final XPathFunctionResolverImpl functionResolver;

    static {
        xpath = XPathFactory.newInstance().newXPath();
        functionResolver = new XPathFunctionResolverImpl();
        xpath.setNamespaceContext(functionResolver);
        xpath.setXPathFunctionResolver(functionResolver);
    }

    private XPathUtils() {
    }

    /**
     * Returns a {@link SimpleNodeList} containing all the nodes that match the given expression
     * when executed on the given node (as opposed to the dom as a whole).
     * 
     * @param expression an XPath expression
     * @param node the contextual node
     * @return SimpleNodeList containing the results from the expression. This will
     *         never be null, but may contain no results.
     * @throws IllegalArgumentException if the expression does not parse
     */
    public static synchronized SimpleNodeList getElements(String expression, Node node) throws XPathExpressionException {
        SimpleNodeList nodes = new SimpleNodeList((NodeList) xpath.evaluate(expression, node, XPathConstants.NODESET));
        return nodes;
    }

    /**
     * Returns a Node matching the given expression. If more than one node matches,
     * the return value is undefined.
     * 
     * @param expression an XPath expression
     * @param node the contextual node
     * @return Node. May be null.
     * @throws IllegalArgumentException if the expression does not parse
     */
    public static synchronized Node getElement(String expression, Node node) throws XPathExpressionException {
        Node n = (Node) xpath.evaluate(expression, node, XPathConstants.NODE);
        return n;
    }

    /**
     * Returns the text content of the Node matching the given expression. 
     * If more than one node matches, the return value is undefined.
     * 
     * @param expression an XPath expression
     * @param node the contextual node
     * @return text content of the selected Node. May be null.
     * @throws IllegalArgumentException if the expression does not parse
     */
    public static synchronized String getString(String expression, Node node) throws XPathExpressionException {
        Node n = (Node) xpath.evaluate(expression, node, XPathConstants.NODE);
        return n == null ? null : n.getTextContent();
    }

    public static synchronized String getString(String expression, Node node, String namespace, String namespacePrefix) throws XPathExpressionException {
        functionResolver.addNamespaceMapping(namespacePrefix, namespace);
        Node n = (Node) xpath.evaluate(expression, node, XPathConstants.NODE);
        functionResolver.removeNamespaceMapping(namespacePrefix);
        return n == null ? null : n.getTextContent();
    }

    /**
     * Returns a {@link SimpleNodeList} containing all the nodes that match the given expression
     * when executed on the given node (as opposed to the dom as a whole).
     * 
     * @param expression an XPath expression
     * @param node the contextual node
     * @return SimpleNodeList containing the results from the expression. This will
     *         never be null, but may contain no results.
     * @throws IllegalArgumentException if the expression does not parse
     */
    public static synchronized SimpleNodeList getElements(XPathExpression expression, Node node) throws XPathExpressionException {
        SimpleNodeList nodes = new SimpleNodeList((NodeList) expression.evaluate(node, XPathConstants.NODESET));
        return nodes;
    }

    /**
     * Returns a Node matching the given expression. If more than one node matches,
     * the return value is undefined.
     * 
     * @param expression an XPath expression
     * @param node the contextual node
     * @return Node. May be null.
     * @throws IllegalArgumentException if the expression does not parse
     */
    public static synchronized Node getElement(XPathExpression expression, Node node) throws XPathExpressionException {
        Node n = (Node) expression.evaluate(node, XPathConstants.NODE);
        return n;
    }

    /**
     * Returns the text content of the Node matching the given expression. 
     * If more than one node matches, the return value is undefined.
     * 
     * @param expression an XPath expression
     * @param node the contextual node
     * @return text content of the selected Node. May be null.
     * @throws IllegalArgumentException if the expression does not parse
     */
    public static synchronized String getString(XPathExpression expression, Node node) throws XPathExpressionException {
        Node n = (Node) expression.evaluate(node, XPathConstants.NODE);
        return n == null ? null : n.getTextContent();
    }

    /**
     * Compiles the given expression, using the internal XPath instance of this
     * class and returns the XPathExpression.
     * 
     * @param expression The XPath expression to compile
     * @return the compiled XPathExpression
     * @throws XPathExpressionException for a malformed XPath expression
     */
    public static synchronized XPathExpression compile(String expression) throws XPathExpressionException {
        return xpath.compile(expression);
    }

    /**
     * Exports this DOM as a String
     */
    public static String toXML(Document dom) {
        return toXML(dom, null);
    }

    public static String toXML(Document dom, Properties outputProperties) {
        try {
            DOMSource source = new DOMSource(dom);
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            Transformer tx = TransformerFactory.newInstance().newTransformer();
            if (outputProperties != null) {
                tx.setOutputProperties(outputProperties);
            }
            tx.transform(source, result);
            String s = writer.toString();
            writer.close();
            return s;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
