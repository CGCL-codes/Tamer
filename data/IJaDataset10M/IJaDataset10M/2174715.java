package org.neurpheus.collections.tree;

/**
 * Generic tree interface.
 *
 * @author Jakub Strychowski
 */
public interface Tree {

    /**
     *  Returns the root node of the tree.
     *
     * @return The root node.
     */
    TreeNode getRoot();

    /**
     * Sets the root for this tree. 
     *
     * @param   root    New root node.
     */
    void setRoot(TreeNode root);

    /**
     * Removes all nodes from the tree. 
     */
    void clear();

    TreeFactory getFactory();
}
