package gui;

import javax.swing.event.ChangeEvent;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.TreeNode;

public class TreeView extends AbstractTreeView {

    private static final long serialVersionUID = 1L;

    public TreeView(TreeNode root) {
        super(root);
        init();
    }

    protected void init() {
        this.addTreeExpansionListener(new TreeExpansionListener() {

            public void treeExpanded(TreeExpansionEvent event) {
                NetSpotter.ns.tTable.editingStopped(new ChangeEvent(this));
            }

            public void treeCollapsed(TreeExpansionEvent event) {
                NetSpotter.ns.tTable.editingStopped(new ChangeEvent(this));
            }
        });
    }
}
