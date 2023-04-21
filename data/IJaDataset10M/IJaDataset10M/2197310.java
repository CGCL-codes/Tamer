package com.fh.auge.model;

import javax.swing.tree.DefaultMutableTreeNode;

public class DefaultTreeNode extends DefaultMutableTreeNode {

    public DefaultTreeNode(Object userObject) {
        super(userObject);
    }

    @SuppressWarnings("unchecked")
    public DefaultTreeNode[] getChildren() {
        if (isLeaf()) return new DefaultTreeNode[] {};
        return (DefaultTreeNode[]) children.toArray(new DefaultTreeNode[0]);
    }
}
