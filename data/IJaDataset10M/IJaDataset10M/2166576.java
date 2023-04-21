package org.neurpheus.collections.tree.objecttree;

import java.io.Serializable;
import org.neurpheus.collections.tree.Tree;
import org.neurpheus.collections.tree.TreeFactory;
import org.neurpheus.collections.tree.TreeNode;

/**
 * Generic tree interface.
 *
 * @author Jakub Strychowski
 */
public class ObjectTree implements Tree, Serializable {

    static final long serialVersionUID = 770608070910114037l;

    private TreeNode root;

    public ObjectTree() {
        root = ObjectTreeFactory.getInstance().createTreeNode(null);
    }

    /**
     *  Returns the root node of the tree.
     *
     * @return The root node.
     */
    public TreeNode getRoot() {
        return root;
    }

    /**
     * Sets the root for this tree. 
     *
     * @param   root    New root node.
     */
    public void setRoot(TreeNode root) {
        this.root = root;
    }

    /**
     * Removes all nodes from the tree. 
     */
    public void clear() {
        if (root != null) {
            root.clear();
        }
        root = null;
    }

    public TreeFactory getFactory() {
        return ObjectTreeFactory.getInstance();
    }
}
