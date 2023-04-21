package com.iv.flash.xml;

import org.w3c.dom.Node;
import javax.xml.transform.TransformerException;
import java.util.Iterator;

/**
 * XPath processor
 * <P>
 * Provides various methods for XPath processing
 *
 * @author Dmitry Skavish
 */
public interface XPathProcessor {

    /**
     * Evaluates XPath to list of nodes.
     *
     * @param expr   XPath expression
     * @param node   xml node to evaluated on
     * @return result of xpath execution - iterator on nodes
     * @exception Exception
     */
    public Iterator selectNodeList(String expr, Node node) throws TransformerException;

    /**
     * Evaluates XPath to one node.
     *
     * @param expr   XPath expression
     * @param node   xml node to be evaluated on
     * @return xml node
     * @exception Exception
     */
    public Node selectSingleNode(String expr, Node node) throws TransformerException;
}
