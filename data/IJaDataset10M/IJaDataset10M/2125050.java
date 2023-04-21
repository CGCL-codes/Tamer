package mod.treeview.sqldiff.dialog.result;

import java.awt.Component;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import org.dlib.gui.treeview.TreeViewNode;
import druid.util.gui.ImageFactory;

public class TreeViewRenderer extends DefaultTreeCellRenderer {

    public TreeViewRenderer() {
    }

    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean exp, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, exp, leaf, row, hasFocus);
        TreeViewNode node = (TreeViewNode) value;
        NodeInfo info = (NodeInfo) node.getUserData();
        if (info != null) setIcon(info.icon); else {
            if (node.isExpanded()) setIcon(ImageFactory.OFOLDER); else setIcon(ImageFactory.CFOLDER);
        }
        return this;
    }
}
