package edu.thu.keg.iw.app.description.ui.flow.editparts;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import edu.thu.keg.iw.app.description.model.flow.FlowModel;
import edu.thu.keg.iw.app.description.model.flow.FlowNodeModel;
import edu.thu.keg.iw.app.description.ui.editor.flow.figure.FlowFigure;
import edu.thu.keg.iw.app.description.ui.flow.editpolicies.FlowLayoutEditPolicy;
import edu.thu.keg.iw.app.description.ui.flow.editpolicies.FlowEditPolicy;

public class FlowEditPart extends SubFlowNodeEditPart {

    public static int i = 1;

    @SuppressWarnings("unchecked")
    public List<FlowNodeModel> getModelChildren() {
        FlowModel flow = (FlowModel) getModel();
        if (flow.getChild() != null) {
            List<FlowNodeModel> children = new ArrayList<FlowNodeModel>();
            children.add(flow.getChild());
            return children;
        }
        return Collections.EMPTY_LIST;
    }

    @Override
    protected IFigure createFigure() {
        FlowFigure flowFigure = new FlowFigure();
        i++;
        return flowFigure;
    }

    protected void createEditPolicies() {
        installEditPolicy(EditPolicy.COMPONENT_ROLE, new FlowEditPolicy());
        installEditPolicy(EditPolicy.LAYOUT_ROLE, new FlowLayoutEditPolicy());
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String prop = evt.getPropertyName();
        if (FlowModel.PROP_SET_CHILD.equals(prop)) refreshChildren();
        refreshVisuals();
    }

    public void refreshVisuals() {
        FlowModel flow = (FlowModel) getModel();
        ((FlowFigure) getFigure()).setId(flow.getId());
        Point location = flow.getLocation();
        Dimension size = ((FlowFigure) getFigure()).getBestSize();
        Rectangle rect = new Rectangle(location, size);
        ((GraphicalEditPart) getParent()).setLayoutConstraint(this, getFigure(), rect);
    }
}
