package org.dbgen.view;

import java.awt.*;
import javax.swing.*;

public class TreeViewRenderer extends javax.swing.JLabel implements javax.swing.tree.TreeCellRenderer {

    static ImageIcon tableImage = new ImageIcon((new Table_e16x16()).getImage());

    static ImageIcon projectOpenImage = new ImageIcon((new ProjectOpen_e16x16()).getImage());

    static ImageIcon projectCloseImage = new ImageIcon((new ProjectClose_e16x16()).getImage());

    static JLabel renderer = new JLabel(tableImage);

    static {
        renderer.setOpaque(true);
        renderer.setHorizontalAlignment(SwingConstants.LEFT);
    }

    /**
   * TreeViewRenderer constructor comment.
   */
    public TreeViewRenderer() {
        super();
    }

    /**
   * getTreeCellRendererComponent method comment.
   */
    public java.awt.Component getTreeCellRendererComponent(javax.swing.JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        if (value == null) return renderer;
        renderer.setFont(tree.getFont());
        Object userObj = ((javax.swing.tree.DefaultMutableTreeNode) value).getUserObject();
        if (userObj != null && userObj instanceof org.dbgen.Table) renderer.setIcon(tableImage); else if (expanded) renderer.setIcon(projectOpenImage); else renderer.setIcon(projectCloseImage);
        if (selected) {
            renderer.setForeground(tree.getBackground());
            renderer.setBackground(tree.getForeground());
        } else {
            renderer.setBackground(tree.getBackground());
            renderer.setForeground(tree.getForeground());
        }
        renderer.setText(value.toString());
        return renderer;
    }
}
