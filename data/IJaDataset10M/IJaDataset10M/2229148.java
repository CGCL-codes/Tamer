package com.jiexplorer.ui.category;

import java.awt.Component;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import com.jiexplorer.model.ITreeNode;

public class CategoryCellRenderer extends DefaultTreeCellRenderer {

    /**
	 *
	 */
    private static final long serialVersionUID = -3850022427865221850L;

    @Override
    public Component getTreeCellRendererComponent(final JTree tree, final Object value, final boolean isSelected, final boolean isExpanded, final boolean leaf, final int row, final boolean hasFocus) {
        final Component component = super.getTreeCellRendererComponent(tree, value, isSelected, isExpanded, leaf, row, hasFocus);
        if ((value != null) && (value instanceof ITreeNode)) {
            final ITreeNode treeNode = (ITreeNode) value;
            setText(treeNode.toString());
        }
        return component;
    }
}
