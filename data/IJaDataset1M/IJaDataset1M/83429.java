package org.apache.xml.security.c14n;

import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.xml.security.utils.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * Base class which all Canonicalization algorithms extend.
 *
 * @author Christian Geuer-Pollmann
 */
public abstract class CanonicalizerSpi {

    /** Reset the writer after a c14n */
    protected boolean reset = false;

    /**
     * Method canonicalize
     *
     * @param inputBytes
     * @return the c14n bytes. 
     *
     * @throws CanonicalizationException
     * @throws java.io.IOException
     * @throws javax.xml.parsers.ParserConfigurationException
     * @throws org.xml.sax.SAXException
     */
    public byte[] engineCanonicalize(byte[] inputBytes) throws javax.xml.parsers.ParserConfigurationException, java.io.IOException, org.xml.sax.SAXException, CanonicalizationException {
        java.io.InputStream bais = new ByteArrayInputStream(inputBytes);
        InputSource in = new InputSource(bais);
        DocumentBuilderFactory dfactory = DocumentBuilderFactory.newInstance();
        dfactory.setNamespaceAware(true);
        DocumentBuilder db = dfactory.newDocumentBuilder();
        Document document = db.parse(in);
        return this.engineCanonicalizeSubTree(document);
    }

    /**
     * Method engineCanonicalizeXPathNodeSet
     *
     * @param xpathNodeSet
     * @return the c14n bytes
     * @throws CanonicalizationException
     */
    public byte[] engineCanonicalizeXPathNodeSet(NodeList xpathNodeSet) throws CanonicalizationException {
        return this.engineCanonicalizeXPathNodeSet(XMLUtils.convertNodelistToSet(xpathNodeSet));
    }

    /**
     * Method engineCanonicalizeXPathNodeSet
     *
     * @param xpathNodeSet
     * @param inclusiveNamespaces
     * @return the c14n bytes
     * @throws CanonicalizationException
     */
    public byte[] engineCanonicalizeXPathNodeSet(NodeList xpathNodeSet, String inclusiveNamespaces) throws CanonicalizationException {
        return this.engineCanonicalizeXPathNodeSet(XMLUtils.convertNodelistToSet(xpathNodeSet), inclusiveNamespaces);
    }

    /** 
     * Returns the URI of this engine.
     * @return the URI
     */
    public abstract String engineGetURI();

    /**
     * Returns true if comments are included
     * @return true if comments are included
     */
    public abstract boolean engineGetIncludeComments();

    /**
     * C14n a nodeset
     *
     * @param xpathNodeSet
     * @return the c14n bytes
     * @throws CanonicalizationException
     */
    public abstract byte[] engineCanonicalizeXPathNodeSet(Set<Node> xpathNodeSet) throws CanonicalizationException;

    /**
     * C14n a nodeset
     *
     * @param xpathNodeSet
     * @param inclusiveNamespaces
     * @return the c14n bytes
     * @throws CanonicalizationException
     */
    public abstract byte[] engineCanonicalizeXPathNodeSet(Set<Node> xpathNodeSet, String inclusiveNamespaces) throws CanonicalizationException;

    /**
     * C14n a node tree.
     *
     * @param rootNode
     * @return the c14n bytes
     * @throws CanonicalizationException
     */
    public abstract byte[] engineCanonicalizeSubTree(Node rootNode) throws CanonicalizationException;

    /**
     * C14n a node tree.
     *
     * @param rootNode
     * @param inclusiveNamespaces
     * @return the c14n bytes
     * @throws CanonicalizationException
     */
    public abstract byte[] engineCanonicalizeSubTree(Node rootNode, String inclusiveNamespaces) throws CanonicalizationException;

    /**
     * Sets the writer where the canonicalization ends. ByteArrayOutputStream if 
     * none is set.
     * @param os
     */
    public abstract void setWriter(OutputStream os);
}
