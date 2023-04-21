package org.apache.xml.security.utils;

import org.apache.xml.dtm.DTMManager;
import org.apache.xml.security.transforms.implementations.FuncHere;
import org.apache.xml.security.transforms.implementations.FuncHereContext;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xml.utils.PrefixResolverDefault;
import org.apache.xpath.CachedXPathAPI;
import org.apache.xpath.Expression;
import org.apache.xpath.XPath;
import org.apache.xpath.XPathContext;
import org.apache.xpath.compiler.FunctionTable;
import org.apache.xpath.objects.XObject;
import org.w3c.dom.*;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 *
 * @author $Author: coheigea $
 */
public class CachedXPathFuncHereAPI {

    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(CachedXPathFuncHereAPI.class);

    /**
     * XPathContext, and thus DTMManager and DTMs, persists through multiple
     *   calls to this object.
     */
    FuncHereContext funcHereContext = null;

    /** Field dtmManager */
    DTMManager dtmManager = null;

    XPathContext context = null;

    String xpathStr = null;

    XPath xpath = null;

    static FunctionTable funcTable = null;

    static {
        fixupFunctionTable();
    }

    /**
     * Method getFuncHereContext
     * @return the context for this object
     *
     */
    public FuncHereContext getFuncHereContext() {
        return this.funcHereContext;
    }

    /**
     * Constructor CachedXPathFuncHereAPI
     *
     * @param existingXPathContext
     */
    public CachedXPathFuncHereAPI(XPathContext existingXPathContext) {
        this.dtmManager = existingXPathContext.getDTMManager();
        this.context = existingXPathContext;
    }

    /**
     * Constructor CachedXPathFuncHereAPI
     *
     * @param previouslyUsed
     */
    public CachedXPathFuncHereAPI(CachedXPathAPI previouslyUsed) {
        this.dtmManager = previouslyUsed.getXPathContext().getDTMManager();
        this.context = previouslyUsed.getXPathContext();
    }

    /**
     * Use an XPath string to select a single node. XPath namespace
     * prefixes are resolved from the context node, which may not
     * be what you want (see the next method).
     *
     * @param contextNode The node to start searching from.
     * @param xpathnode A Node containing a valid XPath string.
     * @return The first node found that matches the XPath, or null.
     *
     * @throws TransformerException
     */
    public Node selectSingleNode(Node contextNode, Node xpathnode) throws TransformerException {
        return selectSingleNode(contextNode, xpathnode, contextNode);
    }

    /**
     * Use an XPath string to select a single node.
     * XPath namespace prefixes are resolved from the namespaceNode.
     *
     * @param contextNode The node to start searching from.
     * @param xpathnode
     * @param namespaceNode The node from which prefixes in the XPath will be resolved to namespaces.
     * @return The first node found that matches the XPath, or null.
     *
     * @throws TransformerException
     */
    public Node selectSingleNode(Node contextNode, Node xpathnode, Node namespaceNode) throws TransformerException {
        NodeList nl = selectNodeList(contextNode, xpathnode, getStrFromNode(xpathnode), namespaceNode);
        if (nl == null) {
            return null;
        }
        return nl.item(0);
    }

    /**
     *  Use an XPath string to select a nodelist.
     *  XPath namespace prefixes are resolved from the namespaceNode.
     *
     *  @param contextNode The node to start searching from.
     *  @param xpathnode
     *  @param str
     *  @param namespaceNode The node from which prefixes in the XPath will be resolved to namespaces.
     *  @return A NodeIterator, should never be null.
     *
     * @throws TransformerException
     */
    public NodeList selectNodeList(Node contextNode, Node xpathnode, String str, Node namespaceNode) throws TransformerException {
        XObject list = eval(contextNode, xpathnode, str, namespaceNode);
        return list.nodelist();
    }

    /**
     *  Evaluate XPath string to an XObject.
     *  XPath namespace prefixes are resolved from the namespaceNode.
     *  The implementation of this is a little slow, since it creates
     *  a number of objects each time it is called.  This could be optimized
     *  to keep the same objects around, but then thread-safety issues would arise.
     *
     *  @param contextNode The node to start searching from.
     *  @param xpathnode
     *  @param str
     *  @param namespaceNode The node from which prefixes in the XPath will be resolved to namespaces.
     *  @return An XObject, which can be used to obtain a string, number, nodelist, etc, should never be null.
     *  @see org.apache.xpath.objects.XObject
     *  @see org.apache.xpath.objects.XNull
     *  @see org.apache.xpath.objects.XBoolean
     *  @see org.apache.xpath.objects.XNumber
     *  @see org.apache.xpath.objects.XString
     *  @see org.apache.xpath.objects.XRTreeFrag
     *
     * @throws TransformerException
     */
    public XObject eval(Node contextNode, Node xpathnode, String str, Node namespaceNode) throws TransformerException {
        if (this.funcHereContext == null) {
            this.funcHereContext = new FuncHereContext(xpathnode, this.dtmManager);
        }
        PrefixResolverDefault prefixResolver = new PrefixResolverDefault((namespaceNode.getNodeType() == Node.DOCUMENT_NODE) ? ((Document) namespaceNode).getDocumentElement() : namespaceNode);
        if (!str.equals(xpathStr)) {
            if (str.indexOf("here()") > 0) {
                context.reset();
                dtmManager = context.getDTMManager();
            }
            xpath = createXPath(str, prefixResolver);
            xpathStr = str;
        }
        int ctxtNode = this.funcHereContext.getDTMHandleFromNode(contextNode);
        return xpath.execute(this.funcHereContext, ctxtNode, prefixResolver);
    }

    /**
     *   Evaluate XPath string to an XObject.
     *   XPath namespace prefixes are resolved from the namespaceNode.
     *   The implementation of this is a little slow, since it creates
     *   a number of objects each time it is called.  This could be optimized
     *   to keep the same objects around, but then thread-safety issues would arise.
     *
     *   @param contextNode The node to start searching from.
     *   @param xpathnode
     *   @param str
     *   @param prefixResolver Will be called if the parser encounters namespace
     *                         prefixes, to resolve the prefixes to URLs.
     *   @return An XObject, which can be used to obtain a string, number, nodelist, etc, 
     *   should never be null.
     *   @see org.apache.xpath.objects.XObject
     *   @see org.apache.xpath.objects.XNull
     *   @see org.apache.xpath.objects.XBoolean
     *   @see org.apache.xpath.objects.XNumber
     *   @see org.apache.xpath.objects.XString
     *   @see org.apache.xpath.objects.XRTreeFrag
     *
     * @throws TransformerException
     */
    public XObject eval(Node contextNode, Node xpathnode, String str, PrefixResolver prefixResolver) throws TransformerException {
        if (!str.equals(xpathStr)) {
            if (str.indexOf("here()") > 0) {
                context.reset();
                dtmManager = context.getDTMManager();
            }
            try {
                xpath = createXPath(str, prefixResolver);
            } catch (TransformerException ex) {
                Throwable th = ex.getCause();
                if (th instanceof ClassNotFoundException && th.getMessage().indexOf("FuncHere") > 0) {
                    throw new RuntimeException(I18n.translate("endorsed.jdk1.4.0") + ex);
                }
                throw ex;
            }
            xpathStr = str;
        }
        if (this.funcHereContext == null) {
            this.funcHereContext = new FuncHereContext(xpathnode, this.dtmManager);
        }
        int ctxtNode = this.funcHereContext.getDTMHandleFromNode(contextNode);
        return xpath.execute(this.funcHereContext, ctxtNode, prefixResolver);
    }

    private XPath createXPath(String str, PrefixResolver prefixResolver) throws TransformerException {
        XPath xpath = null;
        Class[] classes = new Class[] { String.class, SourceLocator.class, PrefixResolver.class, int.class, ErrorListener.class, FunctionTable.class };
        Object[] objects = new Object[] { str, null, prefixResolver, Integer.valueOf(XPath.SELECT), null, funcTable };
        try {
            Constructor constructor = XPath.class.getConstructor(classes);
            xpath = (XPath) constructor.newInstance(objects);
        } catch (Throwable t) {
            if (log.isDebugEnabled()) {
                log.debug(t);
            }
        }
        if (xpath == null) {
            xpath = new XPath(str, null, prefixResolver, XPath.SELECT, null);
        }
        return xpath;
    }

    /**
     * Method getStrFromNode
     *
     * @param xpathnode
     * @return the string for the node.
     */
    public static String getStrFromNode(Node xpathnode) {
        if (xpathnode.getNodeType() == Node.TEXT_NODE) {
            StringBuffer sb = new StringBuffer();
            for (Node currentSibling = xpathnode.getParentNode().getFirstChild(); currentSibling != null; currentSibling = currentSibling.getNextSibling()) {
                if (currentSibling.getNodeType() == Node.TEXT_NODE) {
                    sb.append(((Text) currentSibling).getData());
                }
            }
            return sb.toString();
        } else if (xpathnode.getNodeType() == Node.ATTRIBUTE_NODE) {
            return ((Attr) xpathnode).getNodeValue();
        } else if (xpathnode.getNodeType() == Node.PROCESSING_INSTRUCTION_NODE) {
            return ((ProcessingInstruction) xpathnode).getNodeValue();
        }
        return null;
    }

    private static void fixupFunctionTable() {
        boolean installed = false;
        if (log.isDebugEnabled()) {
            log.debug("Registering Here function");
        }
        try {
            Class[] args = { String.class, Expression.class };
            Method installFunction = FunctionTable.class.getMethod("installFunction", args);
            if ((installFunction.getModifiers() & Modifier.STATIC) != 0) {
                Object[] params = { "here", new FuncHere() };
                installFunction.invoke(null, params);
                installed = true;
            }
        } catch (Throwable t) {
            log.debug("Error installing function using the static installFunction method", t);
        }
        if (!installed) {
            try {
                funcTable = new FunctionTable();
                Class[] args = { String.class, Class.class };
                Method installFunction = FunctionTable.class.getMethod("installFunction", args);
                Object[] params = { "here", FuncHere.class };
                installFunction.invoke(funcTable, params);
                installed = true;
            } catch (Throwable t) {
                log.debug("Error installing function using the static installFunction method", t);
            }
        }
        if (log.isDebugEnabled()) {
            if (installed) {
                log.debug("Registered class " + FuncHere.class.getName() + " for XPath function 'here()' function in internal table");
            } else {
                log.debug("Unable to register class " + FuncHere.class.getName() + " for XPath function 'here()' function in internal table");
            }
        }
    }
}
