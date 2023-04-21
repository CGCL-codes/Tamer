package org.armedbear.j;

import java.awt.Rectangle;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public class SidebarTree extends JTree {

    public SidebarTree(TreeModel model) {
        super(model);
    }

    protected void scrollNodeToCenter(DefaultMutableTreeNode node) {
        TreePath treePath = new TreePath(node.getPath());
        TreePath parentPath = treePath.getParentPath();
        if (parentPath != null) expandPath(parentPath);
        int row = getRowForPath(treePath);
        scrollRowToCenter(row);
        setSelectionRow(row);
    }

    protected void scrollRowToCenter(int row) {
        Rectangle rect = getVisibleRect();
        int top = getClosestRowForLocation(rect.x, rect.y);
        int bottom = top + getVisibleRowCount() - 1;
        int margin = getVisibleRowCount() / 4;
        int first = row - margin;
        if (first < 0) first = 0;
        int last = row + margin;
        if (last > getRowCount() - 1) last = getRowCount() - 1;
        if (first < top || first > bottom) {
            scrollRowToVisible(first);
            rect = getVisibleRect();
            top = getClosestRowForLocation(rect.x, rect.y);
            bottom = top + getVisibleRowCount() - 1;
        }
        if (last < top || last > bottom) scrollRowToVisible(last);
    }
}
