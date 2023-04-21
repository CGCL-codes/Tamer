package greybird.gui.tree;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 *
 * @author dromoz
 */
public class TreeIconRender extends DefaultTreeCellRenderer {

    public TreeIconRender() {
        super();
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        super.setIcon(super.closedIcon);
        super.setText(value.toString());
        super.setBackgroundSelectionColor(Color.blue);
        super.setBorderSelectionColor(Color.blue);
        super.setTextSelectionColor(Color.black);
        super.validate();
        super.repaint();
        return this;
    }
}
