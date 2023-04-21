package org.herasaf.xacml.pdp.locator.impl.index.impl.comparableIndex;

import java.util.List;
import org.herasaf.xacml.core.policy.Evaluatable;
import org.herasaf.xacml.pdp.locator.impl.index.impl.IndexedValue;
import org.herasaf.xacml.pdp.locator.impl.index.impl.PolicyContainer;

/**
 * Node in the AVL-tree of the comparable index.
 *
 * @author Stefan Oberholzer
 * @author Sacha Dolski
 * @version 1.0
 */
@SuppressWarnings("unchecked")
class AvlNode {

    private AvlNode left;

    private AvlNode right;

    private Comparable value;

    private int height;

    private IndexedValue indexedValue;

    /**
	 * Creates a new node of an AVL-tree.
	 *
	 * @param element -
	 *            The value of the Node. The node is indexed by the given value.
	 */
    AvlNode(Comparable element) {
        this(element, null, null);
    }

    /**
	 * Creates a new node of an AVL-tree.
	 *
	 * @param value
	 *            The value of the node. The node is indexed by the given value.
	 * @param lt
	 *            the left child node of this node
	 * @param rt
	 *            the right child node of this node
	 */
    AvlNode(Comparable value, AvlNode lt, AvlNode rt) {
        this.value = value;
        this.left = lt;
        this.right = rt;
        this.height = 0;
        this.indexedValue = new PolicyContainer();
    }

    /**
	 * Returns the left child node of this node.
	 *
	 * @return the left child node.
	 */
    protected AvlNode getLeft() {
        return left;
    }

    /**
	 * Returns the right child node of this node.
	 *
	 * @return the right child node.
	 */
    protected AvlNode getRight() {
        return right;
    }

    /**
	 * The actual height in the tree.
	 *
	 * @return The height of the node in the tree.
	 */
    protected int getHeight() {
        return height;
    }

    /**
	 * Sets the left child node of this node.
	 *
	 * @param left
	 *            child node to set.
	 */
    protected void setLeft(AvlNode left) {
        this.left = left;
    }

    /**
	 * Sets the right child node of this node.
	 *
	 * @param right
	 *            child node to set.
	 */
    protected void setRight(AvlNode right) {
        this.right = right;
    }

    /**
	 * Sets the height in the tree.
	 *
	 * @param height
	 *            The height in the tree.
	 */
    protected void setHeight(int height) {
        this.height = height;
    }

    /**
	 * Returns the value of this node.
	 *
	 * @return The value of this node.
	 */
    protected Comparable getValue() {
        return value;
    }

    /**
	 * Adds the {@link Evaluatable}s to the {@link PolicyContainer}. This method can only be
	 * used during the initialization of the tree.
	 *
	 * @param evals {@link List} of the {@link Evaluatable}s to add.
	 */
    protected void addEvaluatables(List<Evaluatable> evals) {
        ((PolicyContainer) indexedValue).add(evals);
    }

    /**
	 * Returns the indexed values.
	 *
	 * @return The indexed values.
	 */
    protected IndexedValue getIndexedValue() {
        return indexedValue;
    }

    /**
	 * Sets the indexed values.
	 *
	 * @param indexedValue
	 *            The indexed values.
	 */
    protected void setIndexedValue(IndexedValue indexedValue) {
        this.indexedValue = indexedValue;
    }
}
