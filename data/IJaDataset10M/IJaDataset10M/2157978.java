package org.jikesrvm.opt;

import java.util.Enumeration;
import java.util.ArrayList;
import java.util.ListIterator;

/**
 * This class provides enumeration of a tree in bottom-up order
 * It guarantees that all children of a node will be visited before the parent.
 * This is not necessarily the same as a bottom-up level walk.
 *
 * @author Michael Hind
 */
final class OPT_TreeBottomUpEnumerator implements Enumeration<OPT_TreeNode> {

    /**
   * List of nodes in postorder
   */
    private final ArrayList<OPT_TreeNode> list;

    /**
   * an iterator of the above list
   */
    private final ListIterator<OPT_TreeNode> iterator;

    /**
   * constructor: it creates the list of nodes
   * @param root  Root of the tree whose elements are to be visited.
   */
    OPT_TreeBottomUpEnumerator(OPT_TreeNode root) {
        list = new ArrayList<OPT_TreeNode>();
        DFS(root);
        iterator = list.listIterator();
    }

    /**
   * any elements left?
   * @return whether there are any elements left
   */
    public boolean hasMoreElements() {
        return iterator.hasNext();
    }

    /**
   * returns the next element in the list iterator
   * @return the next element in the list iterator or null
   */
    public OPT_TreeNode nextElement() {
        return iterator.next();
    }

    /**
   * A postorder depth first traversal, adding nodes to the list
   * @param node
   */
    private void DFS(OPT_TreeNode node) {
        Enumeration<OPT_TreeNode> childEnum = node.getChildren();
        while (childEnum.hasMoreElements()) {
            OPT_TreeNode child = childEnum.nextElement();
            DFS(child);
        }
        list.add(node);
    }
}
