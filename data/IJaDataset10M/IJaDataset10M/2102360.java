package uk.ac.rothamsted.ovtk.Filter.combination.BackendXML;

import java.util.Enumeration;
import java.util.Vector;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreePath;
import org.w3c.dom.Document;

public class DomToTreeModelAdapter implements javax.swing.tree.TreeModel {

    public Document document;

    public DomToTreeModelAdapter(Document document) {
        this.document = document;
    }

    public Object getRoot() {
        return new AdapterNode(document);
    }

    public boolean isLeaf(Object aNode) {
        AdapterNode node = (AdapterNode) aNode;
        if (node.childCount() > 0) return false;
        return true;
    }

    public int getChildCount(Object parent) {
        AdapterNode node = (AdapterNode) parent;
        return node.childCount();
    }

    public Object getChild(Object parent, int index) {
        AdapterNode node = (AdapterNode) parent;
        return node.child(index);
    }

    public int getIndexOfChild(Object parent, Object child) {
        AdapterNode node = (AdapterNode) parent;
        return node.index((AdapterNode) child);
    }

    public void valueForPathChanged(TreePath path, Object newValue) {
    }

    private Vector<TreeModelListener> listenerList = new Vector<TreeModelListener>();

    public void addTreeModelListener(TreeModelListener listener) {
        if (listener != null && !listenerList.contains(listener)) {
            listenerList.addElement(listener);
        }
    }

    public void removeTreeModelListener(TreeModelListener listener) {
        if (listener != null) {
            listenerList.removeElement(listener);
        }
    }

    public void fireTreeNodesChanged(TreeModelEvent e) {
        Enumeration listeners = listenerList.elements();
        while (listeners.hasMoreElements()) {
            TreeModelListener listener = (TreeModelListener) listeners.nextElement();
            listener.treeNodesChanged(e);
        }
    }

    public void fireTreeNodesInserted(TreeModelEvent e) {
        Enumeration listeners = listenerList.elements();
        while (listeners.hasMoreElements()) {
            TreeModelListener listener = (TreeModelListener) listeners.nextElement();
            listener.treeNodesInserted(e);
        }
    }

    public void fireTreeNodesRemoved(TreeModelEvent e) {
        Enumeration listeners = listenerList.elements();
        while (listeners.hasMoreElements()) {
            TreeModelListener listener = (TreeModelListener) listeners.nextElement();
            listener.treeNodesRemoved(e);
        }
    }

    public void fireTreeStructureChanged(TreeModelEvent e) {
        Enumeration listeners = listenerList.elements();
        while (listeners.hasMoreElements()) {
            TreeModelListener listener = (TreeModelListener) listeners.nextElement();
            listener.treeStructureChanged(e);
        }
    }
}
