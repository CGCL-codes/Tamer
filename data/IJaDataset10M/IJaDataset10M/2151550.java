package edu.iicm.hvs.data;

import javax.swing.tree.*;
import javax.naming.*;
import javax.naming.directory.*;

/**
 * HVSTreeModel extends DefaultTreeModel to override valueForPathChanged().
 * This method is called as a result of the user editing the string name of
 * a node in the tree, which in turn results in the node being renamed in
 * the underlying namespace.
 * <BR><BR>
 * This class is based on the JNDI Browser example from Sun by Rosanna Lee.
 *
 * @author Peter Scheir
 */
public class HVSInputTreeModel extends DefaultTreeModel {

    /**
   * Creates a new instance of HVSTreeModel with newRoot set
   * to the root of this model.
   *
   * @param new_root The root node of the tree model.
   */
    public HVSInputTreeModel(TreeNode new_root) {
        super(new_root);
    }

    /**
   * Reloads the give node.
   *
   * @param new_root The node to be reloaded.
   */
    public void reload(TreeNode node) {
        if (node != null) {
            ((HVSTreeNode) node).loadChildren(true);
            super.reload(node);
        }
    }
}
