package org.gems.designer.edit;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ManhattanConnectionRouter;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.RelativeBendpoint;
import org.eclipse.gef.AccessibleEditPart;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.gems.designer.GraphicsProvider;
import org.gems.designer.LogicMessages;
import org.gems.designer.ModelRepository;
import org.gems.designer.figures.FigureFactory;
import org.gems.designer.model.LogicElement;
import org.gems.designer.model.ModelObject;
import org.gems.designer.model.Wire;
import org.gems.designer.model.WireBendpoint;

/**
 * Implements a Connection Editpart to represnt a Wire like connection.
 * 
 */
public class WireEditPart extends AbstractConnectionEditPart implements PropertyChangeListener {

    AccessibleEditPart acc;

    public static final Color alive = new Color(Display.getDefault(), 0, 74, 168), dead = new Color(Display.getDefault(), 0, 0, 0);

    public void activate() {
        super.activate();
        getWire().addPropertyChangeListener(this);
        GraphicalPartLookup.putPart(getWire(), this);
    }

    public void activateFigure() {
        super.activateFigure();
        getFigure().addPropertyChangeListener(Connection.PROPERTY_CONNECTION_ROUTER, this);
        ModelRepository.getInstance().getModelProvider(getWire().getSource().getModelID()).getGraphicsProvider().decorateConnection((PolylineConnection) getWireFigure(), getWire());
    }

    /**
	 * Adds extra EditPolicies as required.
	 */
    protected void createEditPolicies() {
        installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE, new WireEndpointEditPolicy());
        refreshBendpointEditPolicy();
        installEditPolicy(EditPolicy.CONNECTION_ROLE, new WireEditPolicy());
    }

    /**
	 * Returns a newly created Figure to represent the connection.
	 * 
	 * @return The created Figure.
	 */
    protected IFigure createFigure() {
        if (getWire() == null) return null;
        Connection connx = FigureFactory.createNewBendableWire(getWire());
        GraphicalPartLookup.putFigure(this, connx);
        return connx;
    }

    public void deactivate() {
        getWire().removePropertyChangeListener(this);
        super.deactivate();
    }

    public void deactivateFigure() {
        getFigure().removePropertyChangeListener(Connection.PROPERTY_CONNECTION_ROUTER, this);
        super.deactivateFigure();
    }

    public AccessibleEditPart getAccessibleEditPart() {
        if (acc == null) acc = new AccessibleGraphicalEditPart() {

            public void getName(AccessibleEvent e) {
                e.result = LogicMessages.Wire_LabelText;
            }
        };
        return acc;
    }

    /**
	 * Returns the model of this represented as a Wire.
	 * 
	 * @return Model of this as <code>Wire</code>
	 */
    protected Wire getWire() {
        return (Wire) getModel();
    }

    /**
	 * Returns the Figure associated with this, which draws the Wire.
	 * 
	 * @return Figure of this.
	 */
    protected IFigure getWireFigure() {
        return (PolylineConnection) getFigure();
    }

    /**
	 * Listens to changes in properties of the Wire (like the contents being
	 * carried), and reflects is in the visuals.
	 * 
	 * @param event
	 *            Event notifying the change.
	 */
    public void propertyChange(PropertyChangeEvent event) {
        String property = event.getPropertyName();
        if (Connection.PROPERTY_CONNECTION_ROUTER.equals(property)) {
            refreshBendpoints();
            refreshBendpointEditPolicy();
        }
        if ("value".equals(property) || "Type".equals(property)) refreshVisuals();
        if (property != null && property.startsWith("Attribute:")) {
            refreshVisuals();
        }
        if ("bendpoint".equals(property)) refreshBendpoints(); else if (property.equals(ModelObject.ID_VISIBILITY)) {
            getFigure().setVisible(((Boolean) event.getNewValue()).booleanValue());
            Wire w = (Wire) getModel();
            if (w.getSource() != null) {
                GraphicsProvider gp = w.getSource().getModelProvider().getGraphicsProvider();
                gp.decorateConnection((PolylineConnection) getFigure(), (Wire) getModel());
            }
            refreshVisuals();
        } else if (property.equals("source") || property.equals("target")) {
            refreshVisuals();
        } else if (property.equals(LogicElement.TAGS)) {
            refreshVisuals();
        }
    }

    /**
	 * Updates the bendpoints, based on the model.
	 */
    protected void refreshBendpoints() {
        if (getConnectionFigure().getConnectionRouter() instanceof ManhattanConnectionRouter) return;
        List modelConstraint = getWire().getBendpoints();
        List figureConstraint = new ArrayList();
        for (int i = 0; i < modelConstraint.size(); i++) {
            WireBendpoint wbp = (WireBendpoint) modelConstraint.get(i);
            RelativeBendpoint rbp = new RelativeBendpoint(getConnectionFigure());
            rbp.setRelativeDimensions(wbp.getFirstRelativeDimension(), wbp.getSecondRelativeDimension());
            rbp.setWeight((i + 1) / ((float) modelConstraint.size() + 1));
            figureConstraint.add(rbp);
        }
        getConnectionFigure().setRoutingConstraint(figureConstraint);
    }

    private void refreshBendpointEditPolicy() {
        if (getConnectionFigure().getConnectionRouter() instanceof ManhattanConnectionRouter) installEditPolicy(EditPolicy.CONNECTION_BENDPOINTS_ROLE, null); else installEditPolicy(EditPolicy.CONNECTION_BENDPOINTS_ROLE, new WireBendpointEditPolicy());
    }

    /**
	 * Refreshes the visual aspects of this, based upon the model (Wire). It
	 * changes the wire color depending on the state of Wire.
	 * 
	 */
    protected void refreshVisuals() {
        GraphicsProvider gp = ModelRepository.getInstance().getModelProvider(getWire().getSource().getModelID()).getGraphicsProvider();
        refreshBendpoints();
        gp.decorateConnection((PolylineConnection) getWireFigure(), getWire());
    }
}
