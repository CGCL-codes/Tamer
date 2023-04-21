package org.xblackcat.rojac.gui.dialog.options;

import javax.swing.*;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;

/**
 * Proxy class to make tree node renderer more flexible.
 *
 * @author xBlackCat
 */
class OptionTreeCellRenderer implements TreeCellRenderer {

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        PropertyNode pn = (PropertyNode) value;
        TreeCellRenderer cr = ComponentFactory.createTreeCellRenderer(pn);
        return cr.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
    }
}
