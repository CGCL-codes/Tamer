package net.sf.saxon.dom;

import net.sf.saxon.om.*;
import org.w3c.dom.DOMException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * Implementation of DOM NamedNodeMap used to represent the attributes of an element, for use when
 * Saxon element and attribute nodes are accessed using the DOM API.
 * 
 * <p>Note that namespaces are treated as attributes.</p>
 *
 */
class DOMAttributeMap implements NamedNodeMap {

    private NodeInfo parent;

    private int numberOfNamespaces = -1;

    /**
     * Construct an AttributeMap for a given element node
     */
    public DOMAttributeMap(NodeInfo parent) {
        this.parent = parent;
    }

    /**
    * Get named attribute (DOM NamedNodeMap method)
    */
    public Node getNamedItem(String name) {
        if (name.equals("xmlns")) {
            int[] nsarray = parent.getDeclaredNamespaces(null);
            for (int i = 0; i < nsarray.length; i++) {
                if (nsarray[i] == -1) {
                    return null;
                } else if (((nsarray[i] >> 16) & 0xffff) == 0) {
                    NamespaceIterator.NamespaceNodeImpl nn = new NamespaceIterator.NamespaceNodeImpl(parent, nsarray[i], i + 1);
                    return NodeOverNodeInfo.wrap(nn);
                }
            }
            return null;
        } else if (name.startsWith("xmlns:")) {
            String prefix = name.substring(6);
            if (prefix.equals("xml")) {
                NamespaceIterator.NamespaceNodeImpl nn = new NamespaceIterator.NamespaceNodeImpl(parent, NamespaceConstant.XML_CODE, 0);
                return NodeOverNodeInfo.wrap(nn);
            }
            int[] buffer = new int[8];
            int[] nsarray = parent.getDeclaredNamespaces(buffer);
            for (int i = 0; i < nsarray.length; i++) {
                if (nsarray[i] == -1) {
                    return null;
                } else if (prefix.equals(parent.getNamePool().getPrefixFromNamespaceCode(nsarray[i]))) {
                    NamespaceIterator.NamespaceNodeImpl nn = new NamespaceIterator.NamespaceNodeImpl(parent, nsarray[i], i + 1);
                    return NodeOverNodeInfo.wrap(nn);
                }
            }
            return null;
        } else {
            AxisIterator atts = parent.iterateAxis(Axis.ATTRIBUTE);
            while (true) {
                NodeInfo att = (NodeInfo) atts.next();
                if (att == null) {
                    return null;
                }
                if (name.equals(att.getDisplayName())) {
                    return NodeOverNodeInfo.wrap(att);
                }
            }
        }
    }

    /**
    * Get n'th attribute (DOM NamedNodeMap method).
    * In this implementation we number the attributes as follows:
     * 0 - the xmlns:xml namespace declaration
     * 1-n further namespace declarations
     * n+1... "real" attribute declarations
    */
    public Node item(int index) {
        if (index < 0) {
            return null;
        }
        if (index == 0) {
            NamespaceIterator.NamespaceNodeImpl nn = new NamespaceIterator.NamespaceNodeImpl(parent, NamespaceConstant.XML_NAMESPACE_CODE, 0);
            return NodeOverNodeInfo.wrap(nn);
        }
        int nscount = getNumberOfNamespaces();
        if (index < nscount) {
            int[] buffer = new int[8];
            int[] nsList = parent.getDeclaredNamespaces(buffer);
            int nscode = nsList[index - 1];
            NamespaceIterator.NamespaceNodeImpl nn = new NamespaceIterator.NamespaceNodeImpl(parent, nscode, index);
            return NodeOverNodeInfo.wrap(nn);
        }
        int pos = 0;
        int attNr = (index - nscount);
        AxisIterator atts = parent.iterateAxis(Axis.ATTRIBUTE);
        while (true) {
            NodeInfo att = (NodeInfo) atts.next();
            if (att == null) {
                return null;
            }
            if (pos == attNr) {
                return NodeOverNodeInfo.wrap(att);
            }
            pos++;
        }
    }

    /**
     * Get the number of declared namespaces
     */
    private int getNumberOfNamespaces() {
        if (numberOfNamespaces == -1) {
            int[] buffer = new int[8];
            int[] nsList = parent.getDeclaredNamespaces(buffer);
            int count = nsList.length;
            for (int i = 0; i < count; i++) {
                if (nsList[i] == -1) {
                    count = i;
                    break;
                }
            }
            numberOfNamespaces = count + 1;
        }
        return numberOfNamespaces;
    }

    /**
    * Get number of attributes and namespaces (DOM NamedNodeMap method).
    */
    public int getLength() {
        int length = 0;
        AxisIterator atts = parent.iterateAxis(Axis.ATTRIBUTE);
        while (atts.next() != null) {
            length++;
        }
        return getNumberOfNamespaces() + length;
    }

    /**
    * Get named attribute (DOM NamedNodeMap method)
    */
    public Node getNamedItemNS(String uri, String localName) {
        if (uri == null) {
            uri = "";
        }
        if (NamespaceConstant.XMLNS.equals(uri)) {
            return getNamedItem("xmlns:" + localName);
        }
        if (uri.equals("") && localName.equals("xmlns")) {
            return getNamedItem("xmlns");
        }
        AxisIterator atts = parent.iterateAxis(Axis.ATTRIBUTE);
        while (true) {
            NodeInfo att = (NodeInfo) atts.next();
            if (att == null) {
                return null;
            }
            if (uri.equals(att.getURI()) && localName.equals(att.getLocalPart())) {
                return NodeOverNodeInfo.wrap(att);
            }
        }
    }

    /**
    * Set named attribute (DOM NamedNodeMap method: always fails)
    */
    public Node setNamedItem(Node arg) throws DOMException {
        NodeOverNodeInfo.disallowUpdate();
        return null;
    }

    /**
    * Remove named attribute (DOM NamedNodeMap method: always fails)
    */
    public Node removeNamedItem(String name) throws DOMException {
        NodeOverNodeInfo.disallowUpdate();
        return null;
    }

    /**
    * Set named attribute (DOM NamedNodeMap method: always fails)
    */
    public Node setNamedItemNS(Node arg) throws DOMException {
        NodeOverNodeInfo.disallowUpdate();
        return null;
    }

    /**
    * Remove named attribute (DOM NamedNodeMap method: always fails)
    */
    public Node removeNamedItemNS(String uri, String localName) throws DOMException {
        NodeOverNodeInfo.disallowUpdate();
        return null;
    }
}
