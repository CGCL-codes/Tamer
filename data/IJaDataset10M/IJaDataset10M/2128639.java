package org.cheetahworkflow.designer.part;

import java.beans.PropertyChangeEvent;
import java.util.List;
import org.cheetahworkflow.designer.figure.EndEllipseFigure;
import org.cheetahworkflow.designer.figure.ModuleRectangleFigure;
import org.cheetahworkflow.designer.figure.SequenceRectangleFigure;
import org.cheetahworkflow.designer.figure.StartEllipseFigure;
import org.cheetahworkflow.designer.figure.SwitchDiamondFigure;
import org.cheetahworkflow.designer.model.Arrow;
import org.cheetahworkflow.designer.model.Chart;
import org.cheetahworkflow.designer.model.EndNode;
import org.cheetahworkflow.designer.model.LogicNode;
import org.cheetahworkflow.designer.model.ModuleNode;
import org.cheetahworkflow.designer.model.Node;
import org.cheetahworkflow.designer.model.RapidView;
import org.cheetahworkflow.designer.model.SequenceNode;
import org.cheetahworkflow.designer.model.StartNode;
import org.cheetahworkflow.designer.model.SwitchNode;
import org.cheetahworkflow.designer.policy.NodeGraphicalEditPolicy;
import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;

public class LogicNodePart extends NodePart implements NodeEditPart {

    @Override
    protected IFigure createFigure() {
        LogicNode model = (LogicNode) getModel();
        if (model instanceof SequenceNode) {
            return new SequenceRectangleFigure();
        } else if (model instanceof SwitchNode) {
            return new SwitchDiamondFigure();
        } else if (model instanceof ModuleNode) {
            return new ModuleRectangleFigure();
        } else if (model instanceof StartNode) {
            return new StartEllipseFigure();
        } else if (model instanceof EndNode) {
            return new EndEllipseFigure();
        } else {
            throw new IllegalArgumentException("Cannot create figure for given type: " + model.getClass().getName());
        }
    }

    @Override
    protected void createEditPolicies() {
        super.createEditPolicies();
        installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new NodeGraphicalEditPolicy());
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        super.propertyChange(event);
        String propName = event.getPropertyName();
        if (Node.PROP_SIZE.equals(propName)) {
            refreshVisuals();
        } else if (LogicNode.PROP_INCOMING.equals(propName)) {
            refreshTargetConnections();
        } else if (LogicNode.PROP_OUTGOING.equals(propName)) {
            refreshSourceConnections();
        }
    }

    @Override
    public void eraseTargetFeedback(Request request) {
        super.eraseTargetFeedback(request);
        LogicNode model = (LogicNode) getModel();
        Chart chart = model.getChart();
        chart.removeRapidView();
    }

    @Override
    public void showTargetFeedback(Request request) {
        super.showTargetFeedback(request);
        if (request.getType() == RequestConstants.REQ_SELECTION_HOVER) {
            LogicNode model = (LogicNode) getModel();
            Chart chart = model.getChart();
            RapidView rapidView = model.getRapidView();
            chart.setRapidView(rapidView);
        }
    }

    @Override
    public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart arg0) {
        return new ChopboxAnchor(getFigure());
    }

    @Override
    public ConnectionAnchor getSourceConnectionAnchor(Request arg0) {
        return new ChopboxAnchor(getFigure());
    }

    @Override
    public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart arg0) {
        return new ChopboxAnchor(getFigure());
    }

    @Override
    public ConnectionAnchor getTargetConnectionAnchor(Request arg0) {
        return new ChopboxAnchor(getFigure());
    }

    @Override
    protected List<Arrow> getModelSourceConnections() {
        List<Arrow> obj = ((LogicNode) this.getModel()).getOutgoingArrow();
        return obj;
    }

    @Override
    protected List<Arrow> getModelTargetConnections() {
        return ((LogicNode) this.getModel()).getIncomingArrows();
    }
}
