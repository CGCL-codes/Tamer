package org.mcisb.ui.util.tree;

import java.util.*;
import javax.swing.tree.*;

/**
 * @author Neil Swainston
 *
 */
public class SelectableTreeModel extends DefaultTreeModel {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    private final Map<Object, TreeNode> objectToTreeNode = new HashMap<Object, TreeNode>();

    /**
	 * @param root
	 */
    public SelectableTreeModel(final TreeNode root) {
        super(root);
        if (root instanceof DefaultMutableTreeNode) {
            objectToTreeNode.put(((DefaultMutableTreeNode) root).getUserObject(), root);
        }
    }

    /**
	 * 
	 * @param userObject
	 * @return TreeNode
	 */
    public TreeNode getNode(final Object userObject) {
        return objectToTreeNode.get(userObject);
    }

    @Override
    public void insertNodeInto(final MutableTreeNode newChild, final MutableTreeNode parent, final int index) {
        super.insertNodeInto(newChild, parent, index);
        if (newChild instanceof DefaultMutableTreeNode) {
            objectToTreeNode.put(((DefaultMutableTreeNode) newChild).getUserObject(), newChild);
        }
    }

    /**
	 * 
	 * @param newChild
	 * @param parent
	 */
    public void insertNodeInto(MutableTreeNode newChild, MutableTreeNode parent) {
        insertNodeInto(newChild, parent, parent.getChildCount());
    }

    @Override
    public void removeNodeFromParent(MutableTreeNode node) {
        super.removeNodeFromParent(node);
        if (node instanceof DefaultMutableTreeNode) {
            objectToTreeNode.remove(((DefaultMutableTreeNode) node).getUserObject());
        }
    }
}
