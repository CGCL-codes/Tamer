package org.skycastle.util.balancedtree.redblacktree;

import org.skycastle.util.balancedtree.BalancedTree;
import org.skycastle.util.balancedtree.BalancedTreeNode;
import java.util.Comparator;

/**
 * An implementation of a balanced tree, using the red-black tree algorithm
 * <p/>
 * Based on the Red-Black tree algorithm description on Wikipedia ( http://en.wikipedia.org/wiki/Red-black_tree ).
 *
 * @author Hans Haggstrom
 */
@SuppressWarnings({ "ObjectEquality", "CastToConcreteClass" })
public class BalancedTreeImpl<T> implements BalancedTree<T> {

    private final Comparator<T> myComparator;

    private BalancedTreeNodeImpl<T> myRootNode;

    public BalancedTreeImpl() {
        myComparator = null;
    }

    public BalancedTreeImpl(final Comparator<T> comparator) {
        myComparator = comparator;
    }

    public BalancedTreeNode<T> add(final T value) {
        final BalancedTreeNodeImpl<T> newNode = new BalancedTreeNodeImpl<T>(value, this);
        if (myRootNode == null) {
            myRootNode = newNode;
        } else {
            normalBinaryTreeInsertNode(myRootNode, newNode);
            balanceTreeAfterInsertCase1(newNode);
        }
        return newNode;
    }

    public void remove(final T value) {
        remove(findNode(value));
    }

    public BalancedTreeNode<T> findNode(final T element) {
        return searchBinaryTree(myRootNode, element);
    }

    public void remove(final BalancedTreeNode<T> nodeToRemove) {
        if (nodeToRemove != null) {
            BalancedTreeNodeImpl<T> node = (BalancedTreeNodeImpl<T>) nodeToRemove;
            if (node.getTree() != this) {
                throw new IllegalStateException("The node to be removed is not a part of this tree.\n" + "Expected 'this', but found '" + node.getTree() + "'.");
            }
            throw new UnsupportedOperationException("This method has not yet been implemented.");
        }
    }

    void setRootNode(final BalancedTreeNodeImpl<T> rootNode) {
        myRootNode = rootNode;
    }

    private BalancedTreeNode<T> searchBinaryTree(final BalancedTreeNodeImpl<T> node, final T element) {
        if (node == null) {
            return null;
        } else {
            final int comparsion = compareElements(element, node.getValue());
            if (comparsion < 0) {
                return searchBinaryTree((BalancedTreeNodeImpl<T>) node.getLeftChild(), element);
            } else if (comparsion > 0) {
                return searchBinaryTree((BalancedTreeNodeImpl<T>) node.getRightChild(), element);
            } else {
                return node;
            }
        }
    }

    private void normalBinaryTreeInsertNode(final BalancedTreeNodeImpl<T> node, final BalancedTreeNodeImpl<T> newNode) {
        final int comparsion = compare(newNode, node);
        if (comparsion <= 0) {
            if (node.getLeftChild() != null) {
                normalBinaryTreeInsertNode((BalancedTreeNodeImpl<T>) node.getLeftChild(), newNode);
            } else {
                node.setLeftChild(newNode);
            }
        } else {
            if (node.getRightChild() != null) {
                normalBinaryTreeInsertNode((BalancedTreeNodeImpl<T>) node.getRightChild(), newNode);
            } else {
                node.setRightChild(newNode);
            }
        }
    }

    private void balanceTreeAfterInsertCase1(final BalancedTreeNodeImpl<T> nodeToBalance) {
        if (nodeToBalance.getParent() == null) {
            nodeToBalance.setColor(NodeColor.BLACK);
        } else {
            balanceTreeAfterInsertCase2(nodeToBalance);
        }
    }

    private void balanceTreeAfterInsertCase2(final BalancedTreeNodeImpl<T> nodeToBalance) {
        final BalancedTreeNodeImpl<T> parent = (BalancedTreeNodeImpl<T>) nodeToBalance.getParent();
        if (parent.getColor() == NodeColor.RED) {
            balanceTreeAfterInsertCase3(nodeToBalance);
        }
    }

    private void balanceTreeAfterInsertCase3(final BalancedTreeNodeImpl<T> nodeToBalance) {
        final BalancedTreeNodeImpl<T> parent = (BalancedTreeNodeImpl<T>) nodeToBalance.getParent();
        final BalancedTreeNodeImpl<T> grandparent = (BalancedTreeNodeImpl<T>) nodeToBalance.getGrandparentNode();
        final BalancedTreeNodeImpl<T> uncle = (BalancedTreeNodeImpl<T>) nodeToBalance.getUncleNode();
        if (uncle != null && uncle.getColor() == NodeColor.RED) {
            parent.setColor(NodeColor.BLACK);
            uncle.setColor(NodeColor.BLACK);
            grandparent.setColor(NodeColor.RED);
            balanceTreeAfterInsertCase1(grandparent);
        } else {
            balanceTreeAfterInsertCase4(nodeToBalance);
        }
    }

    private void balanceTreeAfterInsertCase4(final BalancedTreeNodeImpl<T> nodeToBalance) {
        final BalancedTreeNodeImpl<T> parent = (BalancedTreeNodeImpl<T>) nodeToBalance.getParent();
        final BalancedTreeNodeImpl<T> grandparent = (BalancedTreeNodeImpl<T>) nodeToBalance.getGrandparentNode();
        BalancedTreeNode<T> nextNodeToBalance = nodeToBalance;
        if (nodeToBalance == parent.getRightChild() && parent == grandparent.getLeftChild()) {
            parent.rotateLeft();
            nextNodeToBalance = nodeToBalance.getLeftChild();
        } else if (nodeToBalance == parent.getLeftChild() && parent == grandparent.getRightChild()) {
            parent.rotateRight();
            nextNodeToBalance = nodeToBalance.getRightChild();
        }
        balanceTreeAfterInsertCase5((BalancedTreeNodeImpl<T>) nextNodeToBalance);
    }

    private void balanceTreeAfterInsertCase5(final BalancedTreeNodeImpl<T> nodeToBalance) {
        final BalancedTreeNodeImpl<T> parent = (BalancedTreeNodeImpl<T>) nodeToBalance.getParent();
        final BalancedTreeNodeImpl<T> grandparent = (BalancedTreeNodeImpl<T>) nodeToBalance.getGrandparentNode();
        parent.setColor(NodeColor.BLACK);
        grandparent.setColor(NodeColor.RED);
        if (nodeToBalance == parent.getLeftChild() && parent == grandparent.getLeftChild()) {
            grandparent.rotateRight();
        } else {
            assert nodeToBalance == parent.getRightChild() && parent == grandparent.getRightChild();
            grandparent.rotateLeft();
        }
    }

    private int compare(BalancedTreeNode<T> node1, BalancedTreeNode<T> node2) {
        final T element1 = node1.getValue();
        final T element2 = node2.getValue();
        return compareElements(element1, element2);
    }

    private int compareElements(final T element1, final T element2) {
        if (myComparator != null) {
            return myComparator.compare(element1, element2);
        } else if (element1 instanceof Comparable) {
            return ((Comparable) element1).compareTo(element2);
        } else {
            final int hash1 = element1.hashCode();
            final int hash2 = element2.hashCode();
            if (hash1 > hash2) {
                return 1;
            } else if (hash1 < hash2) {
                return -1;
            } else {
                return 0;
            }
        }
    }
}
