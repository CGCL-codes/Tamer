package net.sf.signs.optimization;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/**
 * <p>
 * Title: StNode
 * </p>
 * 
 * <p>
 * Description: Abstract class that define the general methods for the nodes in
 * a syntactic tree.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * 
 * @author Pablo Muñiz, Guenter Bartsch
 * @version 1.0
 */
public abstract class Node {

    protected Node rightChild, leftChild;

    public boolean isOrLeftChild = false;

    public boolean isOrRightChild = false;

    protected HashSet<Node> parents = new HashSet<Node>(1);

    abstract boolean isConstant();

    abstract int getType();

    public Node getLeftChild() {
        return leftChild;
    }

    public Node getRightChild() {
        return rightChild;
    }

    public abstract boolean toSumOfProducts();

    protected void notifyParents() {
        for (Iterator<Node> i = parents.iterator(); i.hasNext(); ) {
            Node parent = i.next();
            parent.notifyChildChanged();
        }
    }

    public void setLeftChild(Node leftChild) {
        this.leftChild = leftChild;
        if (leftChild != null) {
            leftChild.addParent(this);
            if (getType() != OperationNode.OP_AND) {
                isOrLeftChild = false;
            } else {
                isOrLeftChild = leftChild.getType() == OperationNode.OP_OR;
            }
        } else isOrLeftChild = false;
    }

    public void setRightChild(Node rightChild) {
        this.rightChild = rightChild;
        if (rightChild != null) {
            rightChild.addParent(this);
            if (getType() != OperationNode.OP_AND) {
                isOrRightChild = false;
            } else {
                isOrRightChild = rightChild.getType() == OperationNode.OP_OR;
            }
        } else isOrRightChild = false;
    }

    public void notifyChildChanged() {
        setLeftChild(leftChild);
        setRightChild(rightChild);
    }

    public void addParent(Node parent) {
        this.parents.add(parent);
    }

    public HashSet<Node> getParents() {
        return parents;
    }

    public void removeParent(Node parent_) {
        parents.remove(parent_);
    }

    public void addParents(HashSet<Node> parents_) {
        parents.addAll(parents_);
    }

    public abstract boolean calc(HashMap<String, Boolean> values);
}
