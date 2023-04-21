package edu.thu.keg.iw.app.description.ui.flow.editparts;

import java.beans.PropertyChangeEvent;
import org.eclipse.draw2d.IFigure;
import edu.thu.keg.iw.app.description.ui.editor.flow.figure.SequenceNodeFigure;

public class SequenceNodeEditPart extends SubFlowNodeEditPart {

    @Override
    protected IFigure createFigure() {
        return new SequenceNodeFigure();
    }

    @Override
    protected void createEditPolicies() {
        super.createEditPolicies();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        super.propertyChange(evt);
    }
}
