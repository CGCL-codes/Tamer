package org.lindenb.swing.tree;

import java.util.Vector;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;

/**
 * Support for {@link javax.swing.tree.TreeModel} can be then extended to fully support {@link javax.swing.tree.TreeModel}
 * @author lindenb
 *
 */
public class TreeModelSupport {

    private Vector<TreeModelListener> listeners = null;

    /**
	 * Constructor
	 */
    public TreeModelSupport() {
    }

    public void addTreeModelListener(TreeModelListener listener) {
        if (listener != null && (listeners == null || !listeners.contains(listener))) {
            if (listeners == null) listeners = new Vector<TreeModelListener>();
            listeners.addElement(listener);
        }
    }

    public void removeTreeModelListener(TreeModelListener listener) {
        if (listener != null && this.listeners != null) {
            listeners.removeElement(listener);
            if (this.listeners.isEmpty()) listeners = null;
        }
    }

    public void fireTreeNodesChanged(TreeModelEvent e) {
        if (this.listeners == null) return;
        for (TreeModelListener listener : this.listeners) {
            listener.treeNodesChanged(e);
        }
    }

    public void fireTreeNodesInserted(TreeModelEvent e) {
        if (this.listeners == null) return;
        for (TreeModelListener listener : this.listeners) {
            listener.treeNodesInserted(e);
        }
    }

    public void fireTreeNodesRemoved(TreeModelEvent e) {
        if (this.listeners == null) return;
        for (TreeModelListener listener : this.listeners) {
            listener.treeNodesRemoved(e);
        }
    }

    public void fireTreeStructureChanged(TreeModelEvent e) {
        if (this.listeners == null) return;
        for (TreeModelListener listener : this.listeners) {
            listener.treeStructureChanged(e);
        }
    }

    @Override
    public String toString() {
        return getClass().toString();
    }
}
