package cossak;

import javax.swing.tree.*;
import javax.swing.*;
import java.awt.Component;

/**
 *
 * @author hefferjr
 */
public class MyDefaultTreeCellRenderer extends DefaultTreeCellRenderer {

    /** Creates a new instance of MyDefaultTreeCellRenderer */
    public MyDefaultTreeCellRenderer() {
    }

    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        Component component = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        try {
            DefaultMutableTreeNode d = (DefaultMutableTreeNode) value;
            name_node n = (name_node) d.getUserObject();
            if (n._ping_status == 1) component.setForeground(java.awt.Color.black);
            if (n._ping_status == 2) component.setForeground(java.awt.Color.decode("0x00A000"));
            if (n._ping_status == 3) component.setForeground(java.awt.Color.decode("0xA00000"));
            if (n._ping_status < 1 || n._ping_status > 3) component.setForeground(java.awt.Color.gray);
        } catch (Exception ex) {
        }
        return component;
    }
}
