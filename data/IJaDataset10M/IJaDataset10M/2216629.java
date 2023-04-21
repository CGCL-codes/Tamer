package jaxlib.swing.tree;

import java.awt.Component;
import java.awt.Rectangle;
import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeModel;

/**
 * @author  jw
 * @since   JaXLib 1.0
 * @version $Id: UITreeCellRenderer.java 3016 2011-11-28 06:17:26Z joerg_wassmer $
 */
public class UITreeCellRenderer extends DefaultTreeCellRenderer {

    /**
   * @since JaXLib 1.0
   */
    private static final long serialVersionUID = 1L;

    private transient Rectangle iconRect;

    private transient Rectangle itemRect;

    private transient Rectangle textRect;

    public UITreeCellRenderer() {
        super();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Component getTreeCellRendererComponent(final JTree tree, Object value, final boolean selected, final boolean expanded, final boolean leaf, final int row, final boolean hasFocus) {
        TreeModel model = tree.getModel();
        if ((model instanceof UITreeModel) || (value instanceof UITreeNode)) {
            final int state = (selected ? UITreeNode.SELECTED : 0) | (hasFocus ? UITreeNode.FOCUSED : 0) | (expanded ? UITreeNode.EXPANDED : 0) | (leaf ? UITreeNode.LEAF : 0);
            final Icon oldClosedIcon = super.closedIcon;
            final Icon oldLeafIcon = super.leafIcon;
            final Icon oldOpenIcon = super.openIcon;
            final Icon icon;
            if (model instanceof UITreeModel) {
                icon = ((UITreeModel) model).getIcon(tree, value, state, row);
                value = ((UITreeModel) model).getLabel(tree, value, state, row);
            } else {
                icon = ((UITreeNode) value).getIcon(tree, state, row);
                value = ((UITreeNode) value).getLabel(tree, state, row);
            }
            super.closedIcon = icon;
            super.leafIcon = icon;
            super.openIcon = icon;
            model = null;
            try {
                return super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
            } finally {
                super.closedIcon = oldClosedIcon;
                super.leafIcon = oldLeafIcon;
                super.openIcon = oldOpenIcon;
            }
        } else {
            model = null;
            return super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
        }
    }
}
