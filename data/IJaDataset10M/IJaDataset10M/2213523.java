package com.mibviewer.ui.model;

import javax.swing.tree.DefaultMutableTreeNode;

public class MibTreeNode extends DefaultMutableTreeNode {

    private Object nodeObject;

    public MibTreeNode(Object nodeObject) {
        super(nodeObject);
    }
}
