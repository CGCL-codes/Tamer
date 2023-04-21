package net.afternoonsun.imaso.common;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;

/**
 * Used in the UI as an adapter to avoid implementing unnecessary tree model
 * listener interface methods.
 *
 * @author Sergey Pisarenko aka drseergio (drseergio AT gmail DOT com)
 */
public abstract class AbstractTreeModelAdapter implements TreeModelListener {

    @Override
    public void treeNodesChanged(TreeModelEvent e) {
    }

    @Override
    public void treeNodesInserted(TreeModelEvent e) {
    }

    @Override
    public void treeNodesRemoved(TreeModelEvent e) {
    }

    @Override
    public void treeStructureChanged(TreeModelEvent e) {
    }
}
