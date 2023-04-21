package uebung06.as.aufgabe03;

import gvs.tree.GVSBinaryTreeNode;
import gvs.typ.node.GVSNodeTyp;

public class Node<V> implements GVSBinaryTreeNode {

    private V value;

    private Node<V> right;

    private Node<V> left;

    public Node(V val) {
        this(val, null, null);
    }

    public Node(V val, Node<V> a, Node<V> b) {
        value = val;
        left = a;
        right = b;
    }

    public V getValue() {
        return value;
    }

    public void setLeftChild(Node<V> n) {
        left = n;
    }

    public void setRightChild(Node<V> n) {
        right = n;
    }

    public Node<V> getLeftChild() {
        return left;
    }

    public Node<V> getRightChild() {
        return right;
    }

    public GVSBinaryTreeNode getGVSLeftChild() {
        return getLeftChild();
    }

    public GVSBinaryTreeNode getGVSRightChild() {
        return getRightChild();
    }

    public String getNodeLabel() {
        return getValue().toString();
    }

    public GVSNodeTyp getNodeTyp() {
        return null;
    }
}
