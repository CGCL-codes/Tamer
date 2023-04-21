package com.oldportal.objectsbuilder.tree;

import javax.swing.tree.DefaultTreeCellRenderer;

/**
 * <p>Title: ObjectsBuilder</p>
 * <p>Description: Open Source CASE tool</p>
 * <p>Copyright: Copyright (c), Ognyannikov Dmitry, 2005</p>
 * <p>Company: OldPortal Project</p>
 * @author Ognyannikov Dmitry
 * @version 1.0
 */
public class TreeCellRenderer extends DefaultTreeCellRenderer {

    public TreeCellRenderer() {
    }

    public java.awt.Component getTreeCellRendererComponent(javax.swing.JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        return this;
    }
}
