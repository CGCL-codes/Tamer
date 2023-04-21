package org.exist.dom;

import org.exist.numbering.NodeId;

public class NamedNode extends StoredNode implements QNameable {

    protected QName nodeName = null;

    public NamedNode(short nodeType) {
        super(nodeType);
    }

    /**
     * @param nodeType
     */
    public NamedNode(short nodeType, QName qname) {
        super(nodeType);
        this.nodeName = qname;
    }

    /**
     * 
     * 
     * @param nodeId 
     * @param qname 
     * @param nodeType 
     */
    public NamedNode(short nodeType, NodeId nodeId, QName qname) {
        super(nodeType, nodeId);
        this.nodeName = qname;
    }

    public NamedNode(NamedNode other) {
        super(other);
        this.nodeName = other.nodeName;
    }

    public QName getQName() {
        return nodeName;
    }

    public void setNodeName(QName name) {
        nodeName = name;
    }

    public void clear() {
        super.clear();
        nodeName = null;
    }
}
