package org.fcrepo.utilities.xml;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.xpath.*;

/**
 *
 */
public class XPathSelectorImpl implements XPathSelector {

    private static final Log log = LogFactory.getLog(XPathSelector.class);

    /**
     * Importatnt: All access to the xpathCompiler should be synchronized on it
     * since it is not thread safe!
     */
    private static final XPath xpathCompiler = XPathFactory.newInstance().newXPath();

    private LRUCache<String, XPathExpression> cache;

    private NamespaceContext nsContext;

    public XPathSelectorImpl(NamespaceContext nsContext, int cacheSize) {
        this.nsContext = nsContext;
        this.cache = new LRUCache<String, XPathExpression>(cacheSize);
    }

    @Override
    public Integer selectInteger(Node node, String xpath, Integer defaultValue) {
        String strVal = selectString(node, xpath);
        if (strVal == null || "".equals(strVal)) {
            return defaultValue;
        }
        return Integer.valueOf(strVal);
    }

    @Override
    public Integer selectInteger(Node node, String xpath) {
        return selectInteger(node, xpath, null);
    }

    @Override
    public Double selectDouble(Node node, String xpath, Double defaultValue) {
        Double d = (Double) selectObject(node, xpath, XPathConstants.NUMBER);
        if (d == null || d.equals(Double.NaN)) {
            d = defaultValue;
        }
        return d;
    }

    @Override
    public Double selectDouble(Node node, String xpath) {
        return selectDouble(node, xpath, null);
    }

    @Override
    public Boolean selectBoolean(Node node, String xpath, Boolean defaultValue) {
        if (defaultValue == null || Boolean.TRUE.equals(defaultValue)) {
            String tmp = selectString(node, xpath, null);
            if (tmp == null) {
                return defaultValue;
            }
            return Boolean.parseBoolean(tmp);
        } else {
            return (Boolean) selectObject(node, xpath, XPathConstants.BOOLEAN);
        }
    }

    @Override
    public Boolean selectBoolean(Node node, String xpath) {
        return selectBoolean(node, xpath, false);
    }

    @Override
    public String selectString(Node node, String xpath, String defaultValue) {
        if ("".equals(defaultValue)) {
            return (String) selectObject(node, xpath, XPathConstants.STRING);
        }
        Node n = selectNode(node, xpath);
        if (n == null) {
            return defaultValue;
        }
        return (String) selectObject(node, xpath, XPathConstants.STRING);
    }

    @Override
    public String selectString(Node node, String xpath) {
        return selectString(node, xpath, "");
    }

    @Override
    public NodeList selectNodeList(Node dom, String xpath) {
        return (NodeList) selectObject(dom, xpath, XPathConstants.NODESET);
    }

    @Override
    public Node selectNode(Node dom, String xpath) {
        return (Node) selectObject(dom, xpath, XPathConstants.NODE);
    }

    private Object selectObject(Node dom, String xpath, QName returnType) {
        Object retval = null;
        try {
            XPathExpression exp = cache.get(xpath);
            if (exp == null) {
                synchronized (xpathCompiler) {
                    if (nsContext != null) {
                        xpathCompiler.setNamespaceContext(nsContext);
                    }
                    exp = xpathCompiler.compile(xpath);
                }
                cache.put(xpath, exp);
            }
            retval = exp.evaluate(dom, returnType);
        } catch (NullPointerException e) {
            log.debug(String.format("NullPointerException when extracting XPath '%s' on " + "element type %s. Returning null", xpath, returnType.getLocalPart()), e);
        } catch (XPathExpressionException e) {
            log.warn(String.format("Error in XPath expression '%s' when selecting %s: %s", xpath, returnType.getLocalPart(), e.getMessage()), e);
        }
        return retval;
    }

    void clearCache() {
        cache.clear();
    }
}
