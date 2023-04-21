package org.apache.axiom.om.impl.builder;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMContainer;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMException;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNode;
import org.apache.axiom.om.impl.OMContainerEx;
import org.apache.axiom.om.impl.OMNodeEx;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.DefaultHandler;
import java.util.ArrayList;
import java.util.List;

public class SAXOMBuilder extends DefaultHandler implements LexicalHandler {

    OMElement root = null;

    OMNode lastNode = null;

    OMElement nextElem = null;

    OMFactory factory = OMAbstractFactory.getOMFactory();

    List prefixMappings = new ArrayList();

    int textNodeType = OMNode.TEXT_NODE;

    public void setDocumentLocator(Locator arg0) {
    }

    public void startDocument() throws SAXException {
    }

    public void endDocument() throws SAXException {
    }

    public void startDTD(String name, String publicId, String systemId) throws SAXException {
    }

    public void endDTD() throws SAXException {
    }

    protected OMElement createNextElement(String localName) throws OMException {
        OMElement e;
        if (lastNode == null) {
            root = e = factory.createOMElement(localName, null, null, null);
        } else if (lastNode.isComplete()) {
            e = factory.createOMElement(localName, null, lastNode.getParent(), null);
            ((OMNodeEx) lastNode).setNextOMSibling(e);
            ((OMNodeEx) e).setPreviousOMSibling(lastNode);
        } else {
            OMContainerEx parent = (OMContainerEx) lastNode;
            e = factory.createOMElement(localName, null, (OMElement) lastNode, null);
            parent.setFirstChild(e);
        }
        return e;
    }

    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        if (nextElem == null) nextElem = createNextElement(null);
        if (prefix.length() == 0) {
            nextElem.declareDefaultNamespace(uri);
        } else {
            nextElem.declareNamespace(uri, prefix);
        }
    }

    public void endPrefixMapping(String arg0) throws SAXException {
    }

    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
        if (localName == null || localName.trim().equals("")) localName = qName.substring(qName.indexOf(':') + 1);
        if (nextElem == null) nextElem = createNextElement(localName); else nextElem.setLocalName(localName);
        nextElem.setNamespace(nextElem.findNamespace(namespaceURI, null));
        int j = atts.getLength();
        for (int i = 0; i < j; i++) {
            if (!atts.getQName(i).startsWith("xmlns")) {
                OMAttribute attr = nextElem.addAttribute(atts.getLocalName(i), atts.getValue(i), nextElem.findNamespace(atts.getURI(i), null));
                attr.setAttributeType(atts.getType(i));
            }
        }
        lastNode = nextElem;
        nextElem = null;
    }

    public void endElement(String arg0, String arg1, String arg2) throws SAXException {
        if (lastNode.isComplete()) {
            OMContainer parent = lastNode.getParent();
            ((OMNodeEx) parent).setComplete(true);
            lastNode = (OMNode) parent;
        } else {
            OMElement e = (OMElement) lastNode;
            ((OMNodeEx) e).setComplete(true);
        }
    }

    public void startCDATA() throws SAXException {
        textNodeType = OMNode.CDATA_SECTION_NODE;
    }

    public void endCDATA() throws SAXException {
        textNodeType = OMNode.TEXT_NODE;
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        if (lastNode == null) {
            throw new SAXException("");
        }
        OMNode node;
        if (lastNode.isComplete()) {
            node = factory.createOMText(lastNode.getParent(), new String(ch, start, length), textNodeType);
            ((OMNodeEx) lastNode).setNextOMSibling(node);
            ((OMNodeEx) node).setPreviousOMSibling(lastNode);
        } else {
            OMContainerEx e = (OMContainerEx) lastNode;
            node = factory.createOMText(e, new String(ch, start, length), textNodeType);
            e.setFirstChild(node);
        }
        lastNode = node;
    }

    public void ignorableWhitespace(char[] arg0, int arg1, int arg2) throws SAXException {
    }

    public void processingInstruction(String arg0, String arg1) throws SAXException {
    }

    public void comment(char[] ch, int start, int length) throws SAXException {
        if (lastNode == null) {
            return;
        }
        OMNode node;
        if (lastNode.isComplete()) {
            node = factory.createOMComment(lastNode.getParent(), new String(ch, start, length));
            ((OMNodeEx) lastNode).setNextOMSibling(node);
            ((OMNodeEx) node).setPreviousOMSibling(lastNode);
        } else {
            OMContainerEx e = (OMContainerEx) lastNode;
            node = factory.createOMComment(e, new String(ch, start, length));
            e.setFirstChild(node);
        }
        lastNode = node;
    }

    public void skippedEntity(String arg0) throws SAXException {
    }

    public void startEntity(String name) throws SAXException {
    }

    public void endEntity(String name) throws SAXException {
    }

    /**
     * Get the root element of the Axiom tree built by this content handler.
     * 
     * @return the root element of the tree
     * @throws OMException if the tree is not complete
     */
    public OMElement getRootElement() {
        if (root != null && root.isComplete()) {
            return root;
        } else {
            throw new OMException("Tree not complete");
        }
    }
}
