package com.ivis.xprocess.ui.diagram.model;

import org.eclipse.swt.widgets.Control;

public abstract class DiagLinkImpl implements DiagLink {

    private final DiagNode mySource;

    private final DiagNode myTarget;

    public DiagLinkImpl(DiagNode source, DiagNode target) {
        mySource = source;
        myTarget = target;
        mySource.addOutgoingLink(this);
        myTarget.addIncomingLink(this);
    }

    public DiagNode getSource() {
        return mySource;
    }

    public DiagNode getTarget() {
        return myTarget;
    }

    public boolean allowGlobalSelection() {
        return false;
    }

    public void openEditor(Control parent) {
    }

    public DiagNode getOpposite(DiagNode node) {
        return node.equals(getSource()) ? getTarget() : getSource();
    }

    @Override
    public String toString() {
        return getType() + ": " + mySource + "->" + myTarget;
    }
}
