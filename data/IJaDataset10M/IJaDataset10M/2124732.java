package net.sf.doolin.gui.field.tree.support;

import java.awt.Component;
import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import net.sf.doolin.gui.field.tree.model.GUITreeNode;
import net.sf.doolin.gui.swing.LabelInfo;

/**
 * Tree renderer that delegates rendering to the inner nodes.
 * 
 * @see GUITreeNode#getLabelInfo()
 * @author Damien Coraboeuf
 */
public class GUITreeRenderer extends DefaultTreeCellRenderer {

    /**
	 * Render the cell
	 */
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        GUITreeNode node = (GUITreeNode) value;
        LabelInfo ti = node.getLabelInfo();
        super.getTreeCellRendererComponent(tree, ti.getText(), sel, expanded, leaf, row, hasFocus);
        Icon icon = ti.getIcon();
        setIcon(icon);
        return this;
    }
}
