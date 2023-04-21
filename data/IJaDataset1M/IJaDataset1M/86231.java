package com.google.code.p.keytooliui.ktl.swing.tree;

import java.awt.Component;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

public class TipOnLeafRenderer extends DefaultTreeCellRenderer {

    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        setToolTipText(null);
        if (leaf && value instanceof TipOnLeafNode) {
            TipOnLeafNode tln = (TipOnLeafNode) value;
            setToolTipText(tln.getTip());
        }
        return this;
    }
}
