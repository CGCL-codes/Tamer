package datatypes.list;

import java.util.Vector;

public abstract class NodeImpl<E extends Node<E>> implements Node<E>, Stakeholder<E> {

    private static int nodeCounter = 0;

    private E next = null, previous = null;

    private Vector<Stakeholder<E>> stakeholders = new Vector<Stakeholder<E>>();

    private int nodeId;

    private boolean removed = false;

    private E edgeToRecheck = null;

    public boolean hasStartedTraversingList = false;

    public NodeImpl() {
        nodeId = nodeCounter++;
    }

    public NodeImpl(E previous, E next) {
        this.setPrevious(previous);
        this.setNext(next);
    }

    public void registerAsStakeholder(Stakeholder<E> stakeholder) {
        stakeholders.add(stakeholder);
    }

    public void cancelStakeholderRegistration(Stakeholder<E> stakeholder) {
        stakeholders.remove(stakeholder);
    }

    public void remove() {
        setRemoved(true);
        referStakeholdersToNode(getNext());
        if (getPrevious() != null) {
            getPrevious().setNext(next);
        }
        if (getNext() != null) {
            getNext().setPrevious(previous);
        }
    }

    public void referStakeholdersToNode(E newNode) {
        for (Stakeholder<E> stakeholder : stakeholders) {
            stakeholder.setFirstEdgeToCheckForIntersection(newNode);
        }
        stakeholders.clear();
    }

    @Override
    public String toString() {
        return "Node " + nodeId;
    }

    private void setRemoved(boolean removed) {
        this.removed = removed;
    }

    public boolean isRemoved() {
        return removed;
    }

    @Override
    public void setNext(E next) {
        this.next = (E) next;
    }

    @Override
    public E getNext() {
        return next;
    }

    @Override
    public void setPrevious(E previous) {
        this.previous = previous;
    }

    @Override
    public E getPrevious() {
        return previous;
    }

    public void setFirstEdgeToCheckForIntersection(E intersectingEdge) {
        this.edgeToRecheck = intersectingEdge;
        if (this.edgeToRecheck != null) {
            this.edgeToRecheck.registerAsStakeholder(this);
        }
    }

    public E getFirstEdgeToCheckForIntersection() {
        return edgeToRecheck;
    }
}
