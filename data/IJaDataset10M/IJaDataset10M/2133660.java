package net.sf.vorg.factories;

import es.ftgroup.gef.model.IGEFModel;
import es.ftgroup.gef.model.IWireModel;
import es.ftgroup.ui.figures.IFigureFactory;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.gef.EditPart;
import net.sf.vorg.figures.ActiveWaypointFigure;
import net.sf.vorg.figures.BoatFigure;
import net.sf.vorg.figures.PilotBoatFigure;
import net.sf.vorg.figures.PilotCommandFigure;
import net.sf.vorg.figures.WaypointFigure;
import net.sf.vorg.vorgautopilot.models.ActiveWaypoint;
import net.sf.vorg.vorgautopilot.models.Boat;
import net.sf.vorg.vorgautopilot.models.PilotBoat;
import net.sf.vorg.vorgautopilot.models.PilotCommand;
import net.sf.vorg.vorgautopilot.models.Waypoint;

public class PilotFigureFactory implements IFigureFactory {

    public Figure createFigure(EditPart part, IGEFModel unit) {
        if (unit instanceof ActiveWaypoint) return new ActiveWaypointFigure((ActiveWaypoint) unit);
        if (unit instanceof Waypoint) return new WaypointFigure((Waypoint) unit);
        if (unit instanceof PilotCommand) return new PilotCommandFigure((PilotCommand) unit);
        if (unit instanceof Boat) return new BoatFigure((Boat) unit);
        if (unit instanceof PilotBoat) return new PilotBoatFigure((PilotBoat) unit);
        throw new IllegalArgumentException();
    }

    public PolylineConnection createConnection(IWireModel newWire) {
        return null;
    }

    public Figure createFigure(EditPart part, IGEFModel unit, String subType) {
        return this.createFigure(part, unit);
    }
}
