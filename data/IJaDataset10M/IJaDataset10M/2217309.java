package de.flingelli.scrum.gui.util;

import java.util.Enumeration;
import javax.swing.JTree;
import javax.swing.tree.TreePath;

/**
 * Storing and restoring the JTree expansion state.
 * 
 * @author Markus Flingelli
 * 
 */
public final class TreeUtil {

    private TreeUtil() {
    }

    public static Enumeration<TreePath> saveTreeState(JTree tree) {
        return tree.getExpandedDescendants(new TreePath(tree.getModel().getRoot()));
    }

    public static void restoreTreeState(JTree tree, Enumeration<TreePath> enumeration) {
        if (enumeration != null) {
            while (enumeration.hasMoreElements()) {
                tree.expandPath(enumeration.nextElement());
            }
        }
    }
}
