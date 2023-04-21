package org.csiro.darjeeling.test.classes;

public class TreeNode {

    private TreeNode left, right;

    private int value;

    public TreeNode(int value) {
        right = left = null;
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void insert(TreeNode node) {
        if (node.getValue() < value) insertLeft(node); else insertRight(node);
    }

    private void insertLeft(TreeNode node) {
        if (left == null) left = node; else left.insert(node);
    }

    private void insertRight(TreeNode node) {
        if (right == null) right = node; else right.insert(node);
    }

    public TreeNode getLeft() {
        return left;
    }

    public TreeNode getRight() {
        return right;
    }
}
