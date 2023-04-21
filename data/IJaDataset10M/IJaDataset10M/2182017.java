package javax.imageio.metadata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.TypeInfo;
import org.w3c.dom.UserDataHandler;
import javax.imageio.metadata.IIOMetadataFormatImpl.IIOMetadataNodeAttr;

public class IIOMetadataNode implements Element, NodeList {

    private String name;

    private HashMap attrs = new HashMap();

    private List children = new ArrayList();

    private IIOMetadataNode parent;

    private Object obj;

    /**
   * Simple NamedNodeMap class for IIOMetadataNode.
   *
   * @author jlquinn
   */
    private class IIONamedNodeMap implements NamedNodeMap {

        HashMap attrs;

        /**
     * @param attrs
     * @param node
     */
        public IIONamedNodeMap(HashMap attrs) {
            this.attrs = attrs;
        }

        public Node getNamedItem(String name) {
            return (Node) attrs.get(name);
        }

        public Node setNamedItem(Node arg) throws DOMException {
            if (arg instanceof IIOMetadataNodeAttr) {
                IIOMetadataNodeAttr attr = (IIOMetadataNodeAttr) arg;
                if (attr.owner != null) throw new DOMException(DOMException.INUSE_ATTRIBUTE_ERR, "");
                return (Node) attrs.put(attr.name, attr);
            }
            throw new DOMException(DOMException.HIERARCHY_REQUEST_ERR, "");
        }

        public Node removeNamedItem(String name) throws DOMException {
            return (Node) attrs.remove(name);
        }

        public Node item(int index) {
            return (Node) attrs.values().toArray()[index];
        }

        public int getLength() {
            return attrs.size();
        }

        public Node getNamedItemNS(String namespaceURI, String localName) {
            return getNamedItem(localName);
        }

        public Node setNamedItemNS(Node arg) throws DOMException {
            return setNamedItem(arg);
        }

        public Node removeNamedItemNS(String namespaceURI, String localName) throws DOMException {
            return removeNamedItem(localName);
        }
    }

    /**
   * Simple NodeList implementation for IIOMetadataNode.
   *
   * @author jlquinn
   *
   */
    private class IIONodeList implements NodeList {

        List children = new ArrayList();

        public Node item(int index) {
            return (index < children.size()) ? (Node) children.get(index) : null;
        }

        public int getLength() {
            return children.size();
        }
    }

    public IIOMetadataNode() {
    }

    public IIOMetadataNode(String nodename) {
        name = nodename;
    }

    public Object getUserObject() {
        return obj;
    }

    public void setUserObject(Object o) {
        obj = o;
    }

    public short compareDocumentPosition(Node other) throws DOMException {
        return Element.DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC;
    }

    public String getAttribute(String name) {
        Attr anode = (Attr) attrs.get(name);
        return anode != null ? anode.getValue() : null;
    }

    public Attr getAttributeNode(String name) {
        String val = getAttribute(name);
        if (val != null) return new IIOMetadataNodeAttr(this, name, val);
        return null;
    }

    public Attr getAttributeNodeNS(String namespaceURI, String localName) {
        return getAttributeNode(localName);
    }

    public String getAttributeNS(String namespaceURI, String localName) {
        return getAttribute(localName);
    }

    public String getBaseURI() {
        return null;
    }

    private void getElementsRecurse(IIONodeList list, String name) {
        for (int i = 0; i < children.size(); i++) {
            if (((Node) children.get(i)).getNodeName().equals(name)) list.children.add(children.get(i));
            getElementsRecurse(list, name);
        }
    }

    public NodeList getElementsByTagName(String name) {
        IIONodeList list = new IIONodeList();
        getElementsRecurse(list, name);
        return list;
    }

    public NodeList getElementsByTagNameNS(String namespaceURI, String localName) {
        IIONodeList list = new IIONodeList();
        getElementsRecurse(list, name);
        return list;
    }

    public String getTagName() {
        return name;
    }

    public boolean hasAttribute(String name) {
        return attrs.containsKey(name);
    }

    public boolean hasAttributeNS(String namespaceURI, String localName) {
        return attrs.containsKey(localName);
    }

    public void removeAttribute(String name) {
        attrs.remove(name);
    }

    public Attr removeAttributeNode(Attr oldAttr) {
        return (Attr) attrs.remove(oldAttr.getName());
    }

    public void removeAttributeNS(String namespaceURI, String localName) {
        removeAttribute(localName);
    }

    public void setAttribute(String name, String value) {
        Attr attr = (Attr) getAttributeNode(name);
        if (attr != null) attr.setValue(value); else attrs.put(name, new IIOMetadataNodeAttr(this, name, value));
    }

    public Attr setAttributeNode(Attr newAttr) {
        return (Attr) attrs.put(newAttr.getName(), newAttr);
    }

    public Attr setAttributeNodeNS(Attr newAttr) {
        return (Attr) attrs.put(newAttr.getName(), newAttr);
    }

    public void setAttributeNS(String namespaceURI, String qualifiedName, String value) {
        setAttribute(qualifiedName, value);
    }

    public int getLength() {
        return children.size();
    }

    public Node item(int index) {
        if (index < children.size()) return (Node) children.get(index); else return null;
    }

    public Node appendChild(Node newChild) {
        if (newChild == null) throw new IllegalArgumentException("Child node is null");
        IIOMetadataNode child = (IIOMetadataNode) newChild;
        children.add(child);
        child.parent = this;
        return this;
    }

    public Node cloneNode(boolean deep) {
        IIOMetadataNode newnode = new IIOMetadataNode(name);
        newnode.parent = null;
        newnode.obj = obj;
        if (deep) {
            for (int i = 0; i < children.size(); i++) newnode.children.add(((Node) children.get(i)).cloneNode(deep));
        }
        for (Iterator it = attrs.values().iterator(); it.hasNext(); ) {
            IIOMetadataNodeAttr attr = (IIOMetadataNodeAttr) it.next();
            newnode.attrs.put(attr.name, attr.cloneNode(deep));
            attr.owner = newnode;
        }
        return newnode;
    }

    public NamedNodeMap getAttributes() {
        return new IIONamedNodeMap(attrs);
    }

    public NodeList getChildNodes() {
        return this;
    }

    public Object getFeature(String feature, String version) {
        return null;
    }

    public Node getFirstChild() {
        return (children.size() > 0) ? (Node) children.get(0) : null;
    }

    public Node getLastChild() {
        return (children.size() > 0) ? (Node) children.get(children.size() - 1) : null;
    }

    public String getLocalName() {
        return name;
    }

    public String getNamespaceURI() {
        return null;
    }

    public Node getNextSibling() {
        if (parent == null) return null;
        int idx = parent.children.indexOf(this);
        return (idx == parent.children.size() - 1) ? null : (Node) parent.children.get(idx + 1);
    }

    public String getNodeName() {
        return name;
    }

    public short getNodeType() {
        return ELEMENT_NODE;
    }

    public String getNodeValue() {
        return null;
    }

    public Document getOwnerDocument() {
        return null;
    }

    public Node getParentNode() {
        return parent;
    }

    public String getPrefix() {
        return null;
    }

    public Node getPreviousSibling() {
        if (parent == null) return null;
        int idx = parent.children.indexOf(this);
        return (idx == 0) ? null : (Node) parent.children.get(idx - 1);
    }

    public TypeInfo getSchemaTypeInfo() {
        return null;
    }

    public String getTextContent() throws DOMException {
        return null;
    }

    public Object getUserData(String key) {
        return null;
    }

    public boolean hasAttributes() {
        return !attrs.isEmpty();
    }

    public boolean hasChildNodes() {
        return !children.isEmpty();
    }

    public Node insertBefore(Node newChild, Node refChild) {
        if (newChild == null) throw new IllegalArgumentException();
        int idx = children.indexOf(refChild);
        if (idx == -1) children.add(newChild); else children.add(idx, newChild);
        ((IIOMetadataNode) newChild).parent = this;
        return newChild;
    }

    public boolean isDefaultNamespace(String namespaceURI) {
        return true;
    }

    public boolean isEqualNode(Node arg) {
        return true;
    }

    public boolean isSameNode(Node other) {
        return this == other;
    }

    public boolean isSupported(String feature, String version) {
        return false;
    }

    public String lookupNamespaceURI(String prefix) {
        return null;
    }

    public String lookupPrefix(String namespaceURI) {
        return null;
    }

    public void normalize() {
    }

    public Node removeChild(Node oldChild) {
        if (oldChild == null) throw new IllegalArgumentException();
        children.remove(oldChild);
        ((IIOMetadataNode) oldChild).parent = null;
        return oldChild;
    }

    public Node replaceChild(Node newChild, Node oldChild) {
        if (newChild == null) throw new IllegalArgumentException();
        children.set(children.indexOf(oldChild), newChild);
        ((IIOMetadataNode) oldChild).parent = null;
        return oldChild;
    }

    public void setIdAttribute(String name, boolean isId) throws DOMException {
    }

    public void setIdAttributeNode(Attr idAttr, boolean isId) throws DOMException {
    }

    public void setIdAttributeNS(String namespaceURI, String localName, boolean isId) throws DOMException {
    }

    public void setNodeValue(String nodeValue) throws DOMException {
    }

    public void setPrefix(String prefix) {
    }

    public void setTextContent(String textContent) throws DOMException {
    }

    public Object setUserData(String key, Object data, UserDataHandler handler) {
        return null;
    }
}
