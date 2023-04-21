package de.hdm.cefx.concurrency.operations;

import java.io.Serializable;

@SuppressWarnings("serial")
public class UpdateOperations implements Serializable {

    public static final int DELETE = 0;

    public static final int INSERT = 1;

    public static final int SET = 2;

    public static final int TEXT = 0;

    public static final int ATTRIBUT = 1;

    protected int operation;

    protected int nodeType;

    protected String attributName;

    protected NodePosition nodePosition;

    protected UpdateOperationImpl parent;

    UpdateOperations() {
        operation = 0;
        nodeType = 0;
        attributName = null;
        nodePosition = null;
    }

    public boolean isReady() {
        if (nodePosition == null) return false;
        if (nodeType == UpdateOperations.ATTRIBUT) {
            if (attributName == null) return false;
            if ("".equals(attributName)) return false;
        }
        return true;
    }

    public void undoTransformation() {
    }

    public int getOperation() {
        return operation;
    }

    public void setOperation(int operation) {
        this.operation = operation;
    }

    public int getNodeType() {
        return nodeType;
    }

    public void setNodeType(int nodeType) {
        this.nodeType = nodeType;
    }

    public String getAttributName() {
        return attributName;
    }

    public void setAttributName(String attributName) {
        this.attributName = attributName;
    }

    public NodePosition getNodePosition() {
        return nodePosition;
    }

    public void setNodePosition(NodePosition nodePosition) {
        this.nodePosition = nodePosition;
    }

    public UpdateOperationImpl getParent() {
        return parent;
    }

    public void setParent(UpdateOperationImpl parent) {
        this.parent = parent;
    }
}
