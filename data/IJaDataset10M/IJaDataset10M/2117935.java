package org.wsmostudio.bpmo.ui.editor.editpart.events;

import org.eclipse.draw2d.*;
import org.eclipse.gef.EditPart;
import org.wsmostudio.bpmo.figures.ErrorFigure;
import org.wsmostudio.bpmo.model.events.ErrorEventNode;

public class ErrorEventEditpart extends AbstractEventEditpart {

    public ErrorEventEditpart(EditPart context) {
        super(context);
    }

    protected IFigure createEventFigure() {
        ErrorFigure figure = new ErrorFigure();
        figure.setSize(((ErrorEventNode) getModel()).getPreferredSize());
        return figure;
    }
}
