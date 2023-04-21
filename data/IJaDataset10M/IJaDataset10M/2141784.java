package tuner3d.ds;

import java.util.Iterator;

public class AvlIterator implements Iterator {

    private AvlTree tree;

    private AvlNode[] nodes = new AvlNode[10];

    private AvlNode current;

    private int depth = 0;

    AvlIterator(AvlTree tree) {
        this.tree = tree;
        AvlNode n = tree.getRoot();
        while (n != null) {
            nodes[depth] = n;
            if (++depth >= nodes.length) {
                AvlNode[] old = nodes;
                nodes = new AvlNode[old.length + (old.length >> 1)];
                System.arraycopy(old, 0, nodes, 0, old.length);
            }
            n = n.left;
        }
    }

    public boolean hasLeft() {
        return current.left != null;
    }

    public boolean hasRight() {
        return current.right != null;
    }

    public AvlNode left() {
        current = current.left;
        return current;
    }

    public AvlNode right() {
        current = current.right;
        return current;
    }

    public boolean hasNext() {
        return (depth > 0);
    }

    public Object next() {
        if (depth <= 0) {
            current = null;
            return null;
        }
        current = nodes[depth - 1];
        nodes[depth - 1] = null;
        if (current.right != null) {
            AvlNode n = current.right;
            while (n != null) {
                nodes[depth] = n;
                if (++depth >= nodes.length) {
                    AvlNode[] old = nodes;
                    nodes = new AvlNode[old.length + (old.length >> 1)];
                    System.arraycopy(old, 0, nodes, 0, old.length);
                }
                n = n.left;
            }
        } else {
            while (depth > 0 && nodes[depth - 1] == null) {
                depth--;
            }
        }
        return current.element;
    }

    public void remove() {
        if (current != null) {
            tree.remove(current.element);
        }
        if (depth > 0) {
            Comparable next = nodes[depth - 1].element;
            AvlNode n = tree.getRoot();
            depth = 0;
            while (n != null) {
                int val = next.compareTo(n.element);
                nodes[depth] = (val <= 0) ? n : null;
                if (++depth >= nodes.length) {
                    AvlNode[] old = nodes;
                    nodes = new AvlNode[old.length + (old.length >> 1)];
                    System.arraycopy(old, 0, nodes, 0, old.length);
                }
                if (val == 0) {
                    break;
                }
                n = (val < 0) ? n.left : n.right;
            }
        }
        current = null;
    }
}
