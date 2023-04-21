package genomicMap.gui.components.tree;

import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 *
 * @author stewari1
 */
public class ViewTreeCellRenderer extends DefaultTreeCellRenderer {

    public ViewTreeCellRenderer() {
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        if (value instanceof ViewTreeNode) {
            ViewTreeNode node = (ViewTreeNode) value;
            ImageIcon icon = null;
            switch(node.getType()) {
                case ViewTreeNode.TYPE_ROOT:
                    break;
                case ViewTreeNode.TYPE_CHROMOSOME:
                    icon = new ImageIcon("resources/icons/folder_16.png");
                    break;
                case ViewTreeNode.TYPE_MODEL:
                    icon = new ImageIcon("resources/icons/model_16.png");
                    break;
                case ViewTreeNode.TYPE_MODEL_STAT:
                    icon = new ImageIcon("resources/icons/model_16.png");
                    break;
                case ViewTreeNode.TYPE_FIGURES:
                    icon = new ImageIcon("resources/icons/fig_16.png");
                    break;
                case ViewTreeNode.TYPE_LOG:
                    icon = new ImageIcon("resources/icons/model_16.png");
                    break;
            }
            if (icon != null) setIcon(icon);
            setToolTipText(node.getDescription());
        }
        return this;
    }
}
