package de.tu_clausthal.in.agrausch.weit.export.ootrans.xml;

import java.util.Iterator;
import org.w3c.dom.Node;

public class XMLIterator implements Iterator<Node>, Iterable<Node> {

    protected Node currentNode;

    public XMLIterator(Node node) {
        this.currentNode = node.getFirstChild();
    }

    public XMLIterator(Node node, Node startWith) {
        assert (startWith == null || startWith.getParentNode() == node);
        this.currentNode = startWith;
    }

    public boolean hasNext() {
        return currentNode != null;
    }

    public Node next() {
        Node node = currentNode;
        currentNode = currentNode.getNextSibling();
        return node;
    }

    public void remove() {
        throw new RuntimeException("Node removal not implemented");
    }

    public Iterator<Node> iterator() {
        return this;
    }
}
