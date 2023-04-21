package gpsxml.util;

import gpsxml.gui.model.AccessTreeDataModel;
import javax.swing.JTree;
import javax.swing.tree.TreePath;

/**
 *
 * @author PLAYER, Keith Ralph
 */
public class TreeUtil {

    /** Creates a new instance of TreeUtil */
    public TreeUtil() {
    }

    public static Object getSelectedTreeNode(JTree tree) {
        TreePath treePath = tree.getSelectionPath();
        Object child = treePath.getLastPathComponent();
        return child;
    }

    /**
     * reloads tree's data model and expands and repaints the tree. 
     * Assumes the trees backing data model is AccessTreeDataModel.
     */
    public static void expandAccessTree(JTree tree) {
        ((AccessTreeDataModel) tree.getModel()).reload();
        int i = 0;
        while (i < tree.getRowCount()) {
            tree.expandRow(i);
            i++;
        }
        tree.repaint();
    }
}
