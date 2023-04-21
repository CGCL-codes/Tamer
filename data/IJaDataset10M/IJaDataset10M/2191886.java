package com.metanology.mde.ui.pimEditor.model;

import org.eclipse.draw2d.*;

public class MoveBendpointCommand extends BendpointCommand {

    private Bendpoint oldBendpoint;

    public void execute() {
        LinkBendpoint bp = new LinkBendpoint();
        bp.setRelativeDimensions(getFirstRelativeDimension(), getSecondRelativeDimension());
        setOldBendpoint((Bendpoint) getLink().getBendpoints().get(getIndex()));
        getLink().setBendpoint(getIndex(), bp);
        super.execute();
    }

    protected Bendpoint getOldBendpoint() {
        return oldBendpoint;
    }

    public void setOldBendpoint(Bendpoint bp) {
        oldBendpoint = bp;
    }

    public void undo() {
        super.undo();
        getLink().setBendpoint(getIndex(), getOldBendpoint());
    }
}
