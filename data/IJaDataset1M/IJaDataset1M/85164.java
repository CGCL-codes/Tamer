package net.stevechaloner.intellijad.gui.tree;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import java.awt.Color;
import java.awt.Component;

/**
 * Tree node renderer for checkbox trees.
 */
class CheckBoxTreeNodeRenderer implements TreeCellRenderer {

    private static final Color SELECTION_FOREGROUND = UIManager.getColor("Tree.selectionForeground");

    private static final Color SELECTION_BACKGROUND = UIManager.getColor("Tree.selectionBackground");

    private static final Color TEXT_FOREGROUND = UIManager.getColor("Tree.textForeground");

    private static final Color TEXT_BACKGROUND = UIManager.getColor("Tree.textBackground");

    /** {@inheritDoc} */
    public Component getTreeCellRendererComponent(JTree jTree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        IconicCheckBox cb = new IconicCheckBox();
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
        CheckBoxTreeNode cbtn = (CheckBoxTreeNode) node.getUserObject();
        JLabel label = cb.getLabel();
        label.setText(cbtn.getText());
        JCheckBox checkBox = cb.getCheckBox();
        checkBox.setSelected(cbtn.isSelected());
        label.setIcon(NodeIconUtil.getIconFor(jTree, value, expanded));
        prepare(label, selected);
        prepare(checkBox, selected);
        JPanel contentPane = cb.getContentPane();
        prepare(contentPane, selected);
        return contentPane;
    }

    /**
     * Prepares the component for correct rendering.
     *
     * @param component the component to prepare
     * @param selected the tree-selection state of the component
     */
    private void prepare(JComponent component, boolean selected) {
        component.setOpaque(selected);
        component.setForeground(selected ? SELECTION_FOREGROUND : TEXT_FOREGROUND);
        component.setBackground(selected ? SELECTION_BACKGROUND : TEXT_BACKGROUND);
    }
}
