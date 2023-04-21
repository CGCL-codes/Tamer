package org.vikamine.gui.util;

import javax.swing.event.EventListenerList;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;

/**
 * 
 * @author atzmueller
 */
public abstract class AbstractTreeModel implements TreeModel {

    protected final EventListenerList listenerList = new EventListenerList();

    /**
     * AbstractTreeModel constructor comment.
     */
    public AbstractTreeModel() {
        super();
    }

    /**
     * addTreeModelListener method comment.
     * 
     * @param l
     *                TreeModelListener
     * @see javax.swing.tree.TreeModel#addTreeModelListener(TreeModelListener)
     */
    public void addTreeModelListener(TreeModelListener l) {
        listenerList.add(TreeModelListener.class, l);
    }

    protected void fireTreeNodesChanged(Object source, Object[] path, int[] childIndices, Object[] children) {
        Object[] listeners = listenerList.getListenerList();
        TreeModelEvent e = null;
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TreeModelListener.class) {
                if (e == null) {
                    e = new TreeModelEvent(source, path, childIndices, children);
                }
                ((TreeModelListener) listeners[i + 1]).treeNodesChanged(e);
            }
        }
    }

    protected void fireTreeNodesInserted(Object source, Object[] path, int[] childIndices, Object[] children) {
        Object[] listeners = listenerList.getListenerList();
        TreeModelEvent e = null;
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TreeModelListener.class) {
                if (e == null) {
                    e = new TreeModelEvent(source, path, childIndices, children);
                }
                ((TreeModelListener) listeners[i + 1]).treeNodesInserted(e);
            }
        }
    }

    protected void fireTreeNodesRemoved(Object source, Object[] path, int[] childIndices, Object[] children) {
        Object[] listeners = listenerList.getListenerList();
        TreeModelEvent e = null;
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TreeModelListener.class) {
                if (e == null) {
                    e = new TreeModelEvent(source, path, childIndices, children);
                }
                ((TreeModelListener) listeners[i + 1]).treeNodesRemoved(e);
            }
        }
    }

    protected void fireTreeStructureChanged(Object source, Object[] path, int[] childIndices, Object[] children) {
        Object[] listeners = listenerList.getListenerList();
        TreeModelEvent e = null;
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TreeModelListener.class) {
                if (e == null) {
                    e = new TreeModelEvent(source, path, childIndices, children);
                }
                ((TreeModelListener) listeners[i + 1]).treeStructureChanged(e);
            }
        }
    }

    public Object getParentOf(Object node) {
        return getParentOf(getRoot(), node);
    }

    private Object getParentOf(Object currParent, Object node) {
        if (!isLeaf(currParent)) {
            for (int i = 0; i < getChildCount(currParent); i++) {
                Object child = getChild(currParent, i);
                if (child == node) {
                    return currParent;
                } else {
                    Object parentOfChild = getParentOf(child, node);
                    if (parentOfChild != null) return parentOfChild;
                }
            }
        }
        return null;
    }

    /**
     * Builds the parents of node up to and including the root node, where the
     * original node is the last element in the returned array. The length of
     * the returned array gives the node's depth in the tree.
     * 
     * @param aNode
     *                the TreeNode to get the path for
     * @return Object[]
     */
    public Object[] getPathToRoot(Object aNode) {
        return getPathToRoot(aNode, 0);
    }

    /**
     * Builds the parents of node up to and including the root node, where the
     * original node is the last element in the returned array. The length of
     * the returned array gives the node's depth in the tree.
     * 
     * @param aNode
     *                the TreeNode to get the path for
     * @param depth
     *                an int giving the number of steps already taken towards
     *                the root (on recursive calls), used to size the returned
     *                array
     * @return an array of TreeNodes giving the path from the root to the
     *         specified node
     */
    protected Object[] getPathToRoot(Object aNode, int depth) {
        Object[] retNodes;
        if (aNode == null) {
            if (depth == 0) {
                return null;
            } else {
                retNodes = new Object[depth];
            }
        } else {
            final int newDepth = depth + 1;
            if (aNode == getRoot()) {
                retNodes = new Object[newDepth];
            } else {
                retNodes = getPathToRoot(getParentOf(aNode), newDepth);
            }
            retNodes[retNodes.length - newDepth] = aNode;
        }
        return retNodes;
    }

    /**
     * Invoke this method after you've changed how the children identified by
     * childIndicies are to be represented in the tree.
     */
    protected void nodesChanged(Object node, int[] childIndices) {
        if (node != null && childIndices != null) {
            final int cCount = childIndices.length;
            if (cCount > 0) {
                Object[] cChildren = new Object[cCount];
                for (int counter = 0; counter < cCount; counter++) {
                    cChildren[counter] = getChild(node, childIndices[counter]);
                }
                fireTreeNodesChanged(this, getPathToRoot(node), childIndices, cChildren);
            }
        }
    }

    /**
     * Invoke this method if you've totally changed the children of node and its
     * childrens children... This will post a treeStructureChanged event.
     */
    protected void nodeStructureChanged(Object node) {
        if (node != null) {
            fireTreeStructureChanged(this, getPathToRoot(node), null, null);
        }
    }

    /**
     * Invoke this method after you've inserted some TreeNodes into node.
     * childIndices should be the index of the new elements and must be sorted
     * in ascending order.
     */
    protected void nodesWereInserted(Object node, int[] childIndices) {
        if (listenerList != null && node != null && childIndices != null && childIndices.length > 0) {
            final int cCount = childIndices.length;
            Object[] newChildren = new Object[cCount];
            for (int counter = 0; counter < cCount; counter++) {
                newChildren[counter] = getChild(node, childIndices[counter]);
            }
            fireTreeNodesInserted(this, getPathToRoot(node), childIndices, newChildren);
        }
    }

    /**
     * Invoke this method after you've removed some TreeNodes from node.
     * childIndices should be the index of the removed elements and must be
     * sorted in ascending order. And removedChildren should be the array of the
     * children objects that were removed.
     */
    protected void nodesWereRemoved(Object node, int[] childIndices, Object[] removedChildren) {
        if (node != null && childIndices != null) {
            fireTreeNodesRemoved(this, getPathToRoot(node), childIndices, removedChildren);
        }
    }

    /**
     * Invoke this method if you've modified the TreeNodes upon which this model
     * depends. The model will notify all of its listeners that the model has
     * changed.
     */
    public void reload() {
        reload(getRoot());
    }

    public void reload(Object node) {
        nodeStructureChanged(node);
    }

    /**
     * removeTreeModelListener method comment.
     * 
     * @param l
     *                TreeModelListener
     * @see javax.swing.tree.TreeModel#removeTreeModelListener(TreeModelListener)
     */
    public void removeTreeModelListener(TreeModelListener l) {
        listenerList.remove(TreeModelListener.class, l);
    }
}
