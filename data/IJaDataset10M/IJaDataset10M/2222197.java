package fsl;

public class TreeModelSupport {

    private java.util.Vector vector = new java.util.Vector();

    public void addTreeModelListener(javax.swing.event.TreeModelListener listener) {
        if (listener != null && !vector.contains(listener)) {
            vector.addElement(listener);
        }
    }

    public void removeTreeModelListener(javax.swing.event.TreeModelListener listener) {
        if (listener != null) {
            vector.removeElement(listener);
        }
    }

    public void fireTreeNodesChanged(javax.swing.event.TreeModelEvent e) {
        java.util.Enumeration listeners = vector.elements();
        while (listeners.hasMoreElements()) {
            javax.swing.event.TreeModelListener listener = (javax.swing.event.TreeModelListener) listeners.nextElement();
            listener.treeNodesChanged(e);
        }
    }

    public void fireTreeNodesInserted(javax.swing.event.TreeModelEvent e) {
        java.util.Enumeration listeners = vector.elements();
        while (listeners.hasMoreElements()) {
            javax.swing.event.TreeModelListener listener = (javax.swing.event.TreeModelListener) listeners.nextElement();
            listener.treeNodesInserted(e);
        }
    }

    public void fireTreeNodesRemoved(javax.swing.event.TreeModelEvent e) {
        java.util.Enumeration listeners = vector.elements();
        while (listeners.hasMoreElements()) {
            javax.swing.event.TreeModelListener listener = (javax.swing.event.TreeModelListener) listeners.nextElement();
            listener.treeNodesRemoved(e);
        }
    }

    public void fireTreeStructureChanged(javax.swing.event.TreeModelEvent e) {
        java.util.Enumeration listeners = vector.elements();
        while (listeners.hasMoreElements()) {
            javax.swing.event.TreeModelListener listener = (javax.swing.event.TreeModelListener) listeners.nextElement();
            listener.treeStructureChanged(e);
        }
    }
}
