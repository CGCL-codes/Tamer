package uebung10.as.aufgabe03;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import uebung09.ml.aufgabe04.BinarySearchTree;

class AVLTreeImpl<K extends Comparable<? super K>, V> extends BinarySearchTree<K, V> {

    /**
   * After a BST-operation, actionNode shall points to where the balance has to
   * be checked. -> rebalance() will than be called with actionNode.
   */
    protected AVLNode actionNode;

    protected class AVLNode extends BinarySearchTree<K, V>.Node<K, V> {

        private int height;

        private Node parent;

        AVLNode(Entry<K, V> entry) {
            super(entry);
        }

        protected AVLNode setParent(AVLNode parent) {
            AVLNode old = parent;
            this.parent = parent;
            return old;
        }

        protected AVLNode getParent() {
            return (AVLNode) parent;
        }

        protected int setHeight(int height) {
            int old = this.height;
            this.height = height;
            return old;
        }

        protected int getHeight() {
            return height;
        }

        @Override
        public AVLNode getLeftChild() {
            return (AVLNode) super.getLeftChild();
        }

        @Override
        public AVLNode getRightChild() {
            return (AVLNode) super.getRightChild();
        }

        @Override
        public String toString() {
            String result = String.format("%2d", getEntry().getKey()) + " / " + getEntry().getValue() + " | h=" + height;
            if (parent == null) {
                result += " ROOT";
            } else {
                boolean left = (parent.getLeftChild() == this) ? true : false;
                result += (left ? " / " : " \\ ") + "parent(key)=" + parent.getEntry().getKey();
            }
            return result;
        }
    }

    protected AVLNode getRoot() {
        return (AVLNode) root;
    }

    public V put(K key, V value) {
        return null;
    }

    public V get(K key) {
        return null;
    }

    /**
   * Assures the balance of the tree from 'node' up to the root.
   * 
   * @param node
   *          The node from where to start.
   */
    protected void rebalance(AVLNode node) {
    }

    /**
   * Returns the height of this node.
   * 
   * @param node
   * @return The height or -1 if null.
   */
    protected int height(AVLNode node) {
        return (node != null) ? node.getHeight() : -1;
    }

    /**
   * Restructures the tree with rotations.
   * 
   * @param xPos
   *          The X-node.
   * @return The new root-node of this subtree.
   */
    protected AVLNode restructure(AVLNode xPos) {
        return null;
    }

    protected AVLNode tallerChild(AVLNode node) {
        return null;
    }

    protected AVLNode rotateWithLeftChild(AVLNode k2) {
        return null;
    }

    protected AVLNode doubleRotateWithLeftChild(AVLNode k3) {
        return null;
    }

    protected AVLNode rotateWithRightChild(AVLNode k1) {
        return null;
    }

    protected AVLNode doubleRotateWithRightChild(AVLNode k3) {
        return null;
    }

    /**
   * Assures that the childrens have theirs correct parents. Used after
   * rotations.
   * 
   * @param oldSubtreeRoot
   *          The old root-node of this subtree.
   * @param newSubtreeRoot
   *          The new root-node of this subtree.
   */
    protected void adjustParents(final AVLNode oldSubtreeRoot, final AVLNode newSubtreeRoot) {
    }

    protected boolean isBalanced(AVLNode node) {
        return false;
    }

    /**
   * Assures the correct height for node.
   * 
   * @param node
   */
    protected void setHeight(AVLNode node) {
    }

    /**
   * Factory-Method. Creates a new node.
   * 
   * @param entry
   *          The entry to be inserted in the new node.
   * @return The new created node.
   */
    @Override
    protected Node newNode(Entry<K, V> entry) {
        AVLNode avlNode = new AVLNode(entry);
        return avlNode;
    }

    public V remove(K key) {
        return null;
    }

    /**
   * Generates an inorder-node-list.
   * 
   * @param nodeList
   *          The node-list to fill in inorder.
   * @param node
   *          The node to start from.
   */
    protected void inorder(Collection<AVLNode> nodeList, AVLNode node) {
        if (node == null) return;
        inorder(nodeList, node.getLeftChild());
        nodeList.add(node);
        inorder(nodeList, node.getRightChild());
    }

    public void print() {
        List<AVLNode> nodeList = new LinkedList<AVLNode>();
        inorder(nodeList, (AVLNode) root);
        for (AVLNode node : nodeList) {
            System.out.println(node + "  ");
        }
    }
}
