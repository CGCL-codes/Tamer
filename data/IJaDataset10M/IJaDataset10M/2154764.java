package com.wizzer.m3g.toolkit.util;

import java.lang.IllegalArgumentException;
import java.util.LinkedList;
import com.wizzer.m3g.Node;
import com.wizzer.m3g.Group;

/**
 * A utility to determine paths in the scene graph.
 * 
 * @author Mark Millard
 */
public class Path {

    /** The current node in the scene graph. */
    protected Node m_node;

    private Path() {
    }

    /**
	 * Construct a path for the specified node.
	 * 
	 * @param node The node to root the path to.
	 */
    public Path(Node node) {
        if (node == null) throw new IllegalArgumentException();
        m_node = node;
    }

    /**
	 * Get the list of nodes from this Scene Graph element to the specified
	 * target.
	 * 
	 * @param target The Scene Graph element to find the path to.
	 * 
	 * @return An array of Nodes is returned where the first element is closest
	 * to the current node and the last element is closest to the target. There
	 * should be no duplicate Nodes in the array.
	 */
    public Node[] getPathTo(Node target) {
        LinkedList stack = new LinkedList();
        boolean found = buildPathTo(m_node, target, stack, null);
        if ((!found) || stack.isEmpty()) return null; else return (Node[]) stack.toArray();
    }

    private boolean buildPathTo(Node curNode, Node target, LinkedList stack, Node ignoreChild) {
        if (curNode.equals(target)) {
            stack.addFirst(curNode);
            return true;
        }
        if (curNode instanceof Group) {
            Group group = (Group) curNode;
            int numChildren = group.getChildCount();
            for (int i = 0; i < numChildren; i++) {
                Node child = group.getChild(i);
                if ((ignoreChild != null) && (child.equals(ignoreChild))) continue;
                boolean found = buildPathTo(child, target, stack, null);
                if (found) {
                    stack.addFirst(curNode);
                    return true;
                }
            }
        }
        Node parent = curNode.getParent();
        if (parent != null) {
            boolean found = buildPathTo(parent, target, stack, curNode);
            if (found) {
                stack.addFirst(curNode);
                return true;
            }
        }
        return false;
    }
}
