package skycastle.util.balancedtree;

/**
 * A node in the balanced tree.
 *
 * @author Hans H�ggstr�m
 */
public interface BalancedTreeNode<T> {

    /**
     * @return the user data stored at this node, or null if no data stored.
     */
    T getValue();

    /**
     * @return the parent node, or null if none available.
     */
    BalancedTreeNode<T> getParent();

    /**
     * @return the left child node, or null if none available.
     */
    BalancedTreeNode<T> getLeftChild();

    /**
     * @return the right child node, or null if none available.
     */
    BalancedTreeNode<T> getRightChild();

    /**
     * @return the parent nodes parent node, or null if not available.
     */
    BalancedTreeNode<T> getGrandparentNode();

    /**
     * @return the parent nodes sibling node, or null if not available.
     */
    BalancedTreeNode<T> getUncleNode();

    /**
     * @return the balanced tree that this node belongs to.
     */
    BalancedTree<T> getTree();
}
