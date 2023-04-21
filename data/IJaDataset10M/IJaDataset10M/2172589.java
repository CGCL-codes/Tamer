package org.xito.blx;

import java.net.*;
import org.w3c.dom.*;

/**
 *
 * @author  Deane
 */
public class BLXDocument {

    private BLXAliasManager aliasManager;

    private BLXElement blxElement;

    /** Creates a new instance of BLXDocument */
    public BLXDocument(Document doc, URL contextURL, BLXAliasManager aliasManager) throws DOMException, InvalidBLXXMLException {
        this.aliasManager = aliasManager;
        processDoc(doc, contextURL);
    }

    /**
    * Gets the Root BLXElement of this Document
    */
    public BLXElement getBLXElement() {
        return blxElement;
    }

    /**
    * Gets this Documents AliasManager
    */
    public BLXAliasManager getAliasManager() {
        return aliasManager;
    }

    /**
    * Process this document pulling out first alias entries and looking for first BLXElement
    */
    private void processDoc(Document doc, URL contextURL) throws DOMException, InvalidBLXXMLException {
        Element docE = doc.getDocumentElement();
        String nodeName = BLXUtility.getLocalNodeName(docE);
        if (nodeName.equals(BLXElement.BLX_DOCUMENT_NAME) == false) {
            throw new InvalidBLXXMLException("Document does not define a BLX Document. No <" + BLXElement.BLX_NS + ":" + BLXElement.BLX_DOCUMENT_NAME + "> defined. Found element: <" + docE.getNodeName() + "> instead.");
        }
        NodeList nodes = docE.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node child = nodes.item(i);
            if (child.getNodeType() != Node.ELEMENT_NODE) continue;
            Element e = (Element) child;
            nodeName = BLXUtility.getLocalNodeName(e);
            if (nodeName.equals(BLXExtAliasElement.BLX_ALIAS_NODE_NAME)) {
                processAlias(e, contextURL);
            }
            if (nodeName.equals(BLXElement.BLX_OBJ_NODE_NAME) || e.getNodeName().equals(BLXElement.BLX_COMP_NODE_NAME)) {
                blxElement = new BLXElement(e, contextURL);
                return;
            }
        }
        throw new InvalidBLXXMLException("BLX Document does not contain a blx object description");
    }

    /**
    * Process an Extension Alias Element
    */
    private void processAlias(Element aliasElement, URL contextURL) throws DOMException, InvalidBLXXMLException {
        aliasManager.addExtAlias(new BLXExtAliasElement(aliasElement, contextURL));
    }
}
