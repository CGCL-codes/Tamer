package de.shandschuh.jaolt.gui.listener.core;

import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import de.shandschuh.jaolt.gui.core.CategoryJTree;
import de.shandschuh.jaolt.gui.dialogs.selectcategoryjdialog.CategoryJTreeNode;

public class CategoryJTreeExpansionListener implements TreeExpansionListener {

    private CategoryJTree categoryJTree;

    private boolean enabled;

    public CategoryJTreeExpansionListener(CategoryJTree categoryJTree) {
        this.categoryJTree = categoryJTree;
        enabled = true;
    }

    public void treeCollapsed(TreeExpansionEvent event) {
    }

    public void treeExpanded(TreeExpansionEvent treeExpansionEvent) {
        if (enabled) {
            categoryJTree.generateTreeNode((CategoryJTreeNode) treeExpansionEvent.getPath().getLastPathComponent());
        }
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
