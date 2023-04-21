package com.netime.commons.standard.gui.tree;

import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;

public abstract class LazyLoadingTreeNode extends DefaultMutableTreeNode {

    private DefaultTreeModel model;

    /**
	 * Default Constructor 
	 * @param userObject an Object provided by the user that constitutes
     *                   the node's data
	 * @param tree the JTree containing this Node
	 * @param cancelable 
	 */
    public LazyLoadingTreeNode(Object userObject, DefaultTreeModel model) {
        super(userObject);
        this.model = model;
        setAllowsChildren(true);
    }

    /**
	 * Define nodes children
	 * @param nodes new nodes
	 */
    protected void setChildren(MutableTreeNode... nodes) {
        int childCount = getChildCount();
        if (childCount > 0) {
            for (int i = 0; i < childCount; i++) {
                model.removeNodeFromParent((MutableTreeNode) getChildAt(0));
            }
        }
        for (int i = 0; nodes != null && i < nodes.length; i++) {
            model.insertNodeInto(nodes[i], this, i);
        }
    }

    /**
	 * Need some improvement ...
	 * This method should restore the Node initial state if the worker if canceled
	 */
    protected void reset() {
        int childCount = getChildCount();
        if (childCount > 0) {
            for (int i = 0; i < childCount; i++) {
                model.removeNodeFromParent((MutableTreeNode) getChildAt(0));
            }
        }
        setAllowsChildren(true);
    }

    /**
	 * 
	 * @return <code>true</code> if there are some childrens
	 */
    protected boolean areChildrenLoaded() {
        return getChildCount() > 0 && getAllowsChildren();
    }

    /**
	 * If the 
	 * @see #getAllowsChildren()
	 * @return false, this node can't be a leaf
	 */
    @Override
    public boolean isLeaf() {
        return !getAllowsChildren();
    }

    /**
	 * This method will be executed in a background thread. 
	 * If you have to do some GUI stuff use {@link SwingUtilities#invokeLater(Runnable)}  
	 * @param tree the tree
	 * @return the Created nodes
	 */
    public abstract MutableTreeNode[] loadChildren(DefaultTreeModel model);
}
