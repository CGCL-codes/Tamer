package com.skruk.elvis.admin.manage.resources;

import javax.swing.tree.*;

/**
 * @author     skruk
 * @created    7 listopad 2003
 */
public class ResourceTreeModel extends com.skruk.elvis.admin.gui.ElvisTreeModel {

    /**  Description of the Field */
    private ResourceEntry rootEntry = null;

    /**
	 * Creates a new instance of StructureTreeModel
	 *
	 * @param  node  Description of the Parameter
	 */
    private ResourceTreeModel(TreeNode node) {
        super(node);
    }

    /**
	 *  Description of the Method
	 *
	 * @param  entry  Description of the Parameter
	 * @return        Description of the Return Value
	 */
    public static final ResourceTreeModel create(ResourceEntry entry) {
        ResourceTreeModel result = new ResourceTreeModel(entry.treeEntries());
        result.rootEntry = entry;
        return result;
    }

    /**  Description of the Method */
    public synchronized void doRefresh() {
        ((ResourceEntryTreeNode) this.getRoot()).killChildren();
        this.setRoot(this.rootEntry.treeEntries());
        this.reload();
    }

    /**
	 *  Description of the Method
	 *
	 * @param  fullExpand  Description of the Parameter
	 * @return             Description of the Return Value
	 */
    public TreeNode lastSelectedNode(boolean fullExpand) {
        return ((ResourceEntryTreeNode) this.getRoot()).lastSelected(fullExpand);
    }

    /**
	 *  Gets the node attribute of the StructureTreeModel object
	 *
	 * @param  obj  Description of the Parameter
	 * @return      The node value
	 */
    public TreeNode getNode(Object obj) {
        return ((ResourceEntryTreeNode) this.getRoot()).getNode(obj);
    }
}
