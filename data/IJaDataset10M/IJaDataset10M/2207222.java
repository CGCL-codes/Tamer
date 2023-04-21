package net.sf.doolin.util.xml;

import net.sf.doolin.util.StringCodes;
import net.sf.jstring.LocalizableException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.xpath.*;

/**
 * XPath utility class. It caches the XPath context so the XPath requests can be
 * more efficient.
 * 
 * @author Damien Coraboeuf
 */
public class XPathUtils {

    /**
	 * Context used for XPath evaluations.
	 */
    private XPath xpath;

    /**
	 * Constructor.
	 */
    public XPathUtils() {
        this.xpath = XPathFactory.newInstance().newXPath();
    }

    /**
	 * Evaluates a boolean from an XPath expression.
	 * 
	 * @param root
	 *            Root node where to evaluate the XPath from.
	 * @param path
	 *            XPath expression.
	 * @return Result of the expression.
	 */
    public boolean evaluateBoolean(Node root, String path) {
        try {
            XPathExpression expression = this.xpath.compile(path);
            Boolean result = (Boolean) expression.evaluate(root, XPathConstants.BOOLEAN);
            return result != null && result.booleanValue();
        } catch (XPathException e) {
            throw new LocalizableException(StringCodes.XPATHUTIL_XPATH_ERROR, e, new Object[] { path, e });
        }
    }

    /**
	 * Evaluates a string from an XPath expression.
	 * 
	 * @param root
	 *            Root node where to evaluate the XPath from.
	 * @param path
	 *            XPath expression.
	 * @return Result of the expression.
	 */
    public String evaluateString(Node root, String path) {
        try {
            XPathExpression expression = this.xpath.compile(path);
            String value = expression.evaluate(root);
            return value;
        } catch (XPathException e) {
            throw new LocalizableException(StringCodes.XPATHUTIL_XPATH_ERROR, e, new Object[] { path, e });
        }
    }

    /**
	 * Get a list of node using an XPath expression from a root node.
	 * 
	 * @param root
	 *            Root node where to evaluate the XPath from.
	 * @param path
	 *            XPath expression.
	 * @return List of nodes
	 */
    public NodeList selectNodeList(Node root, String path) {
        try {
            XPathExpression expression = this.xpath.compile(path);
            NodeList result = (NodeList) expression.evaluate(root, XPathConstants.NODESET);
            return result;
        } catch (XPathException e) {
            throw new LocalizableException(StringCodes.XPATHUTIL_XPATH_ERROR, e, new Object[] { path, e });
        }
    }

    /**
	 * Get a single node using an XPath expression from a root node.
	 * 
	 * @param root
	 *            Root node where to evaluate the XPath from.
	 * @param path
	 *            XPath expression.
	 * @return List of nodes
	 */
    public Node selectSingleNode(Node root, String path) {
        NodeList nodelist = selectNodeList(root, path);
        int length = nodelist.getLength();
        if (length > 0) {
            return nodelist.item(0);
        } else {
            return null;
        }
    }
}
