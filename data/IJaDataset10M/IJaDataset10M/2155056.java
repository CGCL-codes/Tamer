package net.sf.yaxdiff.tree;

import java.util.ArrayDeque;
import java.util.ArrayList;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * @author Ramon Nogueira (ramon döt nogueira at g maíl döt cöm)
 * 
 */
public class IdTreeBuilder implements ContentHandler {

    private final INodeFactory factory;

    private ArrayDeque<ArrayList<Node>> stack;

    private ArrayList<Node> currentNodes;

    public IdTreeBuilder(INodeFactory factory) {
        this.factory = factory;
    }

    public Node getRootNode() {
        assert currentNodes.size() == 1 : "can only have a single root node";
        return currentNodes.get(0);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        Node node = factory.createCData(ch, start, length);
        currentNodes.add(node);
    }

    @Override
    public void endDocument() throws SAXException {
    }

    @Override
    public void endElement(String uri, String localName, String name) throws SAXException {
        Node node = factory.createElement(uri.toCharArray(), name.toCharArray(), currentNodes);
        currentNodes = stack.pop();
        currentNodes.add(node);
    }

    @Override
    public void endPrefixMapping(String prefix) throws SAXException {
    }

    @Override
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
    }

    @Override
    public void processingInstruction(String target, String data) throws SAXException {
    }

    @Override
    public void setDocumentLocator(Locator locator) {
    }

    @Override
    public void skippedEntity(String name) throws SAXException {
    }

    @Override
    public void startDocument() throws SAXException {
        stack = new ArrayDeque<ArrayList<Node>>(32);
        currentNodes = new ArrayList<Node>();
    }

    @Override
    public void startElement(String uri, String localName, String name, Attributes atts) throws SAXException {
        stack.push(currentNodes);
        currentNodes = new ArrayList<Node>();
        for (int i = 0; i < atts.getLength(); i++) {
            String atURI = atts.getURI(i);
            String atName = atts.getLocalName(i);
            String value = atts.getValue(i);
            Node attributeNode = factory.createAttribute(atURI.toCharArray(), atName.toCharArray(), value.toCharArray());
            currentNodes.add(attributeNode);
        }
    }

    @Override
    public void startPrefixMapping(String prefix, String uri) throws SAXException {
    }
}
