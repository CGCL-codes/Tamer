package com.dcivision.workflow.applet;

import javax.swing.JTree;

public class WorkflowElementTree extends JTree {

    public static final String REVISION = "$Revision: 1.2 $";

    public WorkflowElementTree() {
        super();
        this.getSelectionModel().setSelectionMode(javax.swing.tree.TreeSelectionModel.SINGLE_TREE_SELECTION);
    }
}
