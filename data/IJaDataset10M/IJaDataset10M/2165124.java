package net.sf.hexagon.editparts;

import java.beans.PropertyChangeEvent;
import java.util.logging.Logger;
import org.eclipse.gef.EditPolicy;
import net.sf.gef.core.editparts.AbstractNodeEditPart;
import net.sf.gef.core.policies.GNodePolicy;
import net.sf.hexagon.figures.HexagonDockFigure;
import net.sf.hexagon.figures.HexagonPlanetFigure;
import net.sf.hexagon.models.HexagonDock;
import net.sf.hexagon.models.HexagonPlanet;

public class HexagonDockEditPart extends AbstractNodeEditPart {

    private static Logger logger = Logger.getLogger("net.sf.sandbox.editparts");

    public HexagonDockEditPart() {
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final String prop = evt.getPropertyName();
        if (HexagonPlanet.NAME_CHANGED.equals(prop)) {
            final HexagonPlanetFigure fig = (HexagonPlanetFigure) getFigure();
            HexagonDock planet = getCastedModel();
            fig.setName(planet.getName());
            return;
        }
    }

    @Override
    protected void createEditPolicies() {
        installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new GNodePolicy());
        super.createEditPolicies();
    }

    @Override
    protected void refreshVisuals() {
        HexagonDockFigure fig = (HexagonDockFigure) this.getFigure();
        HexagonDock theModel = this.getCastedModel();
        fig.setName(theModel.getName());
    }

    @Override
    public HexagonDock getCastedModel() {
        return (HexagonDock) getModel();
    }
}
