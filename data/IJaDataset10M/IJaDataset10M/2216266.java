package org.newsml.toolkit.dom;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.newsml.toolkit.DataContent;
import org.newsml.toolkit.NewsMLException;

/**
 * DOM implementation of DataContent.
 *
 * @author Reuters PLC
 * @version 2.0
 */
class DOMDataContent extends DOMBaseNode implements DataContent {

    /**
     * Constructor.
     *
     * @param element The DOM node representing the data content.
     */
    protected DOMDataContent(Node element, DOMSessionCore session) {
        super(element, session);
    }

    /**
     * @see DataContent#getDOMNodes
     */
    public NodeList getDOMNodes() {
        return getDOMNode().getChildNodes();
    }

    /**
     * @see DataContent#setDOMNodes(NodeList)
     */
    public void setDOMNodes(NodeList nodes) {
        Node node = getDOMNode();
        DOMUtils.clearChildren(node);
        DOMUtils.addChildren(node, nodes);
    }

    /**
     * @see DataContent#getText
     */
    public String getText() {
        return DOMUtils.getText(getDOMNode());
    }

    /**
     * @see DataContent#setText(String)
     */
    public void setText(String text) {
        setDOMContent(text);
    }

    /**
     * @see DataContent#getXMLString
     */
    public String getXMLString() {
        StringWriter writer = new StringWriter();
        NodeList children = getDOMNode().getChildNodes();
        int length = children.getLength();
        for (int i = 0; i < length; i++) {
            try {
                DOMUtils.writeNode(children.item(i), writer, false, null, null);
            } catch (IOException e) {
                throw new RuntimeException("Internal IO exception");
            }
        }
        return writer.toString();
    }

    /**
     * @see DataContent#setXMLString(String)
     */
    public void setXMLString(String xml) throws NewsMLException {
        DOMFactory factory = ((DOMNewsMLFactory) getSession().getFactory()).getDOMFactory();
        NodeList nodes = null;
        try {
            nodes = factory.createDOM(new StringReader(xml), null).getChildNodes();
        } catch (IOException e) {
            throw new NewsMLException(e);
        }
        setDOMNodes(nodes);
    }
}
