package org.dm.webkit.dom;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class NodeListWrapper implements NodeList {

    Node[] nodes;

    public NodeListWrapper(Node[] nodes) {
        this.nodes = nodes;
    }

    public int getLength() {
        return (nodes == null) ? 0 : nodes.length;
    }

    public Node item(int i) {
        if (i < 0 || i >= getLength()) return null;
        return nodes[i];
    }
}
