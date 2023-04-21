package org.softnetwork.xml.dom;

import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Node;

/**
 * @author $Author: smanciot $
 *
 * @version $Revision: 98 $
 */
class Nodes implements DOMNodeList {

    List nodes = null;

    Nodes() {
        this(10);
    }

    Nodes(int capacity) {
        nodes = new ArrayList(capacity);
    }

    public final int getLength() {
        return nodes.size();
    }

    public final Node item(int i) {
        return (Node) nodes.get(i);
    }

    public final int indexOf(Node node) {
        return nodes.indexOf(node);
    }

    public Node[] toArray() {
        return (Node[]) nodes.toArray(new Node[0]);
    }

    Node append(Node node) {
        if (node != null && nodes.add(node)) return node;
        return null;
    }
}
