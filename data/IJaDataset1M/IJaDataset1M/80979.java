package org.bungeni.editor.section.refactor.ui;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 *
 * @author ashok
 */
public class OdfJDomTreeCellRenderer extends DefaultTreeCellRenderer {

    private final Color elementColor = new Color(0, 0, 128);

    private final Color textColor = new Color(0, 128, 0);

    public OdfJDomTreeCellRenderer() {
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        OdfJDomTreeNode adapterNode = (OdfJDomTreeNode) value;
        value = adapterNode.toString();
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        if (!selected) {
            if (adapterNode.node.getTextTrim().length() == 0) {
                setForeground(elementColor);
            } else {
                setForeground(textColor);
            }
        }
        return this;
    }
}
