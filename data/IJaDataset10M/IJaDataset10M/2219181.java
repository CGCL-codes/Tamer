package javax.swing.tree;

/**
 * MutableTreeNode public interface
 
 * @author Andrew Selkirk
 */
public interface MutableTreeNode extends TreeNode {

    /**
   * Inserts a node as child at a given index.
   *
   * @param child the note to insert
   * @param index the index
   *
   * @see #remove(int)
   * @see #remove(MutableTreeNode)
   * @see #setParent(MutableTreeNode)
   */
    void insert(MutableTreeNode child, int index);

    /**
   * Removes the child node a given index.
   *
   * @param index the index
   *
   * @see #insert(MutableTreeNode,int)
   * @see #remove(MutableTreeNode)
   * @see #removeFromParent()
   */
    void remove(int index);

    /**
   * Removes a given child node.
   *
   * @param node the node to remove
   *
   * @see #insert(MutableTreeNode,int)
   * @see #remove(int)
   * @see #removeFromParent()
   */
    void remove(MutableTreeNode node);

    /**
   * Sets a user object, the data represented by the node. 
   *
   * @param object the data
   */
    void setUserObject(Object object);

    /**
   * Removes this node from its parent.
   *
   * @see #remove(int)
   * @see #remove(MutableTreeNode)
   */
    void removeFromParent();

    /**
   * Sets the parent of the node.
   *
   * @param parent the parent
   *
   * @see #insert(MutableTreeNode,int)
   */
    void setParent(MutableTreeNode parent);
}
