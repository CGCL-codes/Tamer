package net.sf.vorg.editparts;

import java.beans.PropertyChangeEvent;
import java.util.List;
import java.util.Vector;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import net.sf.gef.core.editparts.AbstractDirectedNodeEditPart;
import net.sf.gef.core.model.IGEFNode;
import net.sf.vorg.figures.ActiveWaypointFigure;
import net.sf.vorg.figures.PilotBoatFigure;
import net.sf.vorg.policies.GNodePolicy;
import net.sf.vorg.vorgautopilot.models.ActiveWaypoint;
import net.sf.vorg.vorgautopilot.models.Boat;
import net.sf.vorg.vorgautopilot.models.PilotBoat;
import net.sf.vorg.vorgautopilot.models.PilotCommand;

public class PilotBoatEditPart extends AbstractDirectedNodeEditPart {

    public PilotBoat getCastedModel() {
        return (PilotBoat) getModel();
    }

    public void propertyChange(final PropertyChangeEvent evt) {
        final String prop = evt.getPropertyName();
        if (Boat.NAME_CHANGED.equals(prop)) {
            final PilotBoatFigure fig = (PilotBoatFigure) getFigure();
            fig.refreshContents();
            return;
        }
        if (Boat.BOAT_UPDATE.equals(prop)) {
            final PilotBoatFigure fig = (PilotBoatFigure) getFigure();
            fig.refreshContents();
        }
        if (Boat.IDENTIFICATION_CHANGED.equals(prop)) {
            final PilotBoatFigure fig = (PilotBoatFigure) getFigure();
            fig.refreshContents();
        }
        if (PilotBoat.STATE_CHANGED.equals(prop)) {
            final PilotBoatFigure fig = (PilotBoatFigure) getFigure();
            fig.refreshContents();
        }
        if (PilotBoat.STRUCTURE_CHANGED.equals(prop)) {
            refreshChildren();
        }
        if (PilotCommand.WAYPOINT_ACTIVATED.equals(prop)) {
            refreshChildren();
        }
    }

    @Override
    public String toString() {
        final StringBuffer buffer = new StringBuffer('\n');
        buffer.append("[PilotBoatEditPart:");
        buffer.append(getCastedModel().getBoatName()).append("-");
        buffer.append(((Figure) getFigure()).getLocation()).append("-");
        buffer.append(getModel().toString()).append("-");
        buffer.append(getFigure().toString()).append("-");
        buffer.append(super.toString()).append("]");
        buffer.append("]");
        return buffer.toString();
    }

    /**
	 * Processes the special action of adding an active waypoints. This is not inserted last but inserted on the
	 * second position after the Boat figure.
	 */
    @Override
    protected void addChildVisual(final EditPart childEditPart, final int index) {
        final IFigure child = ((GraphicalEditPart) childEditPart).getFigure();
        if (child instanceof ActiveWaypointFigure) {
            getContentPane().add(child, 2);
        } else {
            getContentPane().add(child);
        }
    }

    @Override
    protected void createEditPolicies() {
        installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new GNodePolicy());
        super.createEditPolicies();
    }

    /** Return the Boat and the Commands as its children. */
    @Override
    protected List<IGEFNode> getModelChildren() {
        final Vector<IGEFNode> childList = new Vector<IGEFNode>(2);
        childList.add(getCastedModel().getBoat());
        final PilotCommand activeCommand = getCastedModel().getActiveCommand();
        if (null != activeCommand) {
            final ActiveWaypoint activeWaypoint = getCastedModel().getActiveWaypoint();
            if (null != activeWaypoint) {
                childList.add(activeWaypoint);
            }
            childList.add(activeCommand);
        }
        return childList;
    }
}
