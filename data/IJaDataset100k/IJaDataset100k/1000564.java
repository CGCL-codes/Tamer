package net.sf.saxon.tree.tiny;

import net.sf.saxon.tree.iter.AxisIteratorImpl;
import net.sf.saxon.om.Item;
import net.sf.saxon.om.SequenceIterator;
import net.sf.saxon.pattern.NodeTest;

/**
* This class supports both the descendant:: and descendant-or-self:: axes, which are
* identical except for the route to the first candidate node.
* It enumerates descendants of the specified node.
* The calling code must ensure that the start node is not an attribute or namespace node.
*/
final class DescendantEnumeration extends AxisIteratorImpl {

    private TinyTree tree;

    private TinyNodeImpl startNode;

    private boolean includeSelf;

    private int nextNodeNr;

    private int startDepth;

    private NodeTest test;

    /**
     * Create an iterator over the descendant axis
     * @param doc the containing TinyTree
     * @param node the node whose descendants are required
     * @param nodeTest test to be satisfied by each returned node
     * @param includeSelf true if the start node is to be included
     */
    DescendantEnumeration(TinyTree doc, TinyNodeImpl node, NodeTest nodeTest, boolean includeSelf) {
        tree = doc;
        startNode = node;
        this.includeSelf = includeSelf;
        test = nodeTest;
        nextNodeNr = node.nodeNr;
        startDepth = doc.depth[nextNodeNr];
    }

    public Item next() {
        if (position == 0 && includeSelf && test.matches(startNode)) {
            current = startNode;
            position++;
            return current;
        }
        do {
            nextNodeNr++;
            try {
                if (tree.depth[nextNodeNr] <= startDepth) {
                    nextNodeNr = -1;
                    current = null;
                    position = -1;
                    return null;
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                nextNodeNr = -1;
                current = null;
                position = -1;
                return null;
            }
        } while (!test.matches(tree, nextNodeNr));
        position++;
        current = tree.getNode(nextNodeNr);
        return current;
    }

    /**
    * Get another enumeration of the same nodes
    */
    public SequenceIterator getAnother() {
        return new DescendantEnumeration(tree, startNode, test, includeSelf);
    }
}
