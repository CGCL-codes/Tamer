package org.objectwiz.javafx.checkboxtree;

/**
 *
 * @author Benoit Del Basso <benoit.delbasso at helmet.fr>
 */
public class NodeStatusUpdateEvent<E, F> {

    private boolean isRoot;

    private Object obj;

    private Boolean newStatus;

    public NodeStatusUpdateEvent(E root, Boolean newStatus) {
        this.isRoot = true;
        this.obj = root;
        this.newStatus = newStatus;
    }

    public NodeStatusUpdateEvent(F child, boolean newStatus) {
        this.isRoot = false;
        this.obj = child;
        this.newStatus = newStatus;
    }

    public boolean isRootUpdate(E root) {
        return isRoot && obj.equals(root);
    }

    public boolean isChildUpdate(F child) {
        return (!isRoot) && obj.equals(child);
    }

    public Boolean newStatus() {
        return newStatus;
    }

    @Override
    public String toString() {
        return "(" + (isRoot ? "root" : "child") + ") " + obj + ": " + (newStatus == null ? "undefined" : (newStatus ? "enabled" : "disabled"));
    }
}
