package org.form4j.form.util.xml;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerException;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXParseException;

public class CachedXPathAPI {

    public CachedXPathAPI() {
        {
            Object xobj = null;
            try {
                xpathAPI = Class.forName("org.apache.xpath.CachedXPathAPI").newInstance();
                xobj = Class.forName("org.apache.xpath.objects.XObject").newInstance();
            } catch (Exception e) {
            }
            if (xpathAPI == null) try {
                xpathAPI = Class.forName("com.sun.org.apache.xpath.internal.XPathAPI").newInstance();
                xobj = Class.forName("com.sun.org.apache.xpath.internal.objects.XObject").newInstance();
            } catch (Exception e) {
            }
            try {
                selectSingleNode = xpathAPI.getClass().getDeclaredMethod("selectSingleNode", new Class[] { Node.class, String.class });
                selectNodeList = xpathAPI.getClass().getDeclaredMethod("selectNodeList", new Class[] { Node.class, String.class });
                eval = xpathAPI.getClass().getDeclaredMethod("eval", new Class[] { Node.class, String.class });
                _evalStr = xobj.getClass().getDeclaredMethod("str", new Class[] {});
                _evalBool = xobj.getClass().getDeclaredMethod("bool", new Class[] {});
            } catch (Exception e) {
                LOG.error(e, e);
            }
        }
    }

    public NodeList selectNodeList(Node node, String path) throws TransformerException {
        try {
            return (NodeList) selectNodeList.invoke(xpathAPI, new Object[] { node, path });
        } catch (InvocationTargetException e) {
            LOG.debug("path " + path + ": " + e.getTargetException(), e);
            if (e.getTargetException() instanceof TransformerException) throw (TransformerException) e.getTargetException();
        } catch (Exception e) {
        }
        return null;
    }

    public Node selectSingleNode(Node node, String path) throws TransformerException {
        try {
            return (Node) selectSingleNode.invoke(xpathAPI, new Object[] { node, path });
        } catch (InvocationTargetException e) {
            LOG.debug("path " + path + ": " + e.getTargetException(), e);
            if (e.getTargetException() instanceof TransformerException) throw (TransformerException) e.getTargetException();
        } catch (Exception e) {
        }
        return null;
    }

    public Object eval(Node node, String path) {
        try {
            return eval.invoke(xpathAPI, new Object[] { node, path });
        } catch (Exception e) {
        }
        return null;
    }

    public String evalStr(Node node, String path) {
        try {
            Object xobj = eval.invoke(xpathAPI, new Object[] { node, path });
            return (String) _evalStr.invoke(xobj, new Object[] {});
        } catch (Exception e) {
        }
        return null;
    }

    public boolean evalBool(Node node, String path) {
        try {
            Object xobj = eval.invoke(xpathAPI, new Object[] { node, path });
            return ((Boolean) _evalBool.invoke(xobj, new Object[] {})).booleanValue();
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * Main for testing purposes: 
     * @param args
     */
    public static void main(String[] args) {
        try {
            Document doc = getDocument(args[0]);
            NodeList nodes = (NodeList) new CachedXPathAPI().selectNodeList(doc.getDocumentElement(), "//.");
            LOG.error(doc.getDocumentElement());
            LOG.error("Detected " + nodes.getLength() + " Nodes");
        } catch (Exception e) {
            if (LOG.isDebugEnabled()) LOG.debug(e, e);
            usage();
        }
    }

    private static void usage() {
        System.err.println("Usage:");
        System.err.println("  java -classpath build/classes org.form4j.form.util.xml.CachedXPathAPI URL");
        System.err.println("");
    }

    private static Document getDocument(final String url) throws Exception {
        return getDocument(url, true);
    }

    private static Document getDocument(final String url, final boolean ignoreWhitespace) throws Exception {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            if (ignoreWhitespace) {
                factory.setIgnoringElementContentWhitespace(true);
            }
            Document doc = factory.newDocumentBuilder().parse(url);
            if (ignoreWhitespace) doc.normalize();
            return doc;
        } catch (SAXParseException e) {
            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                factory.setValidating(true);
                factory.newDocumentBuilder().parse(url);
            } catch (SAXParseException e1) {
                LOG.error(url + ": line=" + e1.getLineNumber() + ", col=" + e1.getColumnNumber() + "  " + e1.getMessage());
            }
            throw (e);
        } catch (Exception e) {
            if (LOG.isDebugEnabled()) LOG.debug(e, e); else LOG.error(e);
            throw (e);
        }
    }

    private static final Logger LOG = Logger.getLogger(CachedXPathAPI.class.getName());

    private Object xpathAPI = null;

    private Method selectSingleNode;

    private Method selectNodeList;

    private Method eval;

    private Method _evalGetNodeValue;

    private Method _evalStr;

    private Method _evalBool;
}
