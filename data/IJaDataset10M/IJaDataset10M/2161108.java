package com.sdi.pws.gui.compo.db.tree;

import com.sdi.pws.gui.RecordSelector;
import com.sdi.pws.gui.compo.db.change.ChangeViewRecord;
import com.sdi.pws.db.PwsRecord;
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class TreeViewSelector implements RecordSelector, TreeSelectionListener {

    private PropertyChangeSupport support = new PropertyChangeSupport(this);

    private PwsRecord record;

    public TreeViewSelector(JTree aTree) {
        aTree.addTreeSelectionListener(this);
    }

    public boolean isInfoAvailable() {
        return !(record == null);
    }

    public PwsRecord getSelectedRecord() {
        return record;
    }

    public int getSelectedIndex() {
        return -1;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    public void valueChanged(TreeSelectionEvent aEvt) {
        final TreePath lPath = aEvt.getPath();
        final DefaultMutableTreeNode lNode = (DefaultMutableTreeNode) lPath.getLastPathComponent();
        if (lNode.getUserObject() instanceof TreeViewDatabase.LeafNode) {
            TreeViewDatabase.LeafNode lLeaf = (TreeViewDatabase.LeafNode) lNode.getUserObject();
            ChangeViewRecord lRec = lLeaf.getRecord();
            final Object lOldValue = record;
            record = lRec;
            final Object lNewValue = lRec;
            support.firePropertyChange("selectedRecord", lOldValue, lNewValue);
        } else {
            final Object lOldValue = record;
            record = null;
            support.firePropertyChange("selectedRecord", lOldValue, null);
        }
    }
}
