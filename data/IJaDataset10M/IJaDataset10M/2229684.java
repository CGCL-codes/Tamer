package org.eclipse.gef.examples.logicdesigner.edit;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.AccessibleAnchorProvider;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editparts.DefaultAccessibleAnchorProvider;
import org.eclipse.gef.examples.logicdesigner.figures.AndGateFigure;
import org.eclipse.gef.examples.logicdesigner.figures.GateFigure;
import org.eclipse.gef.examples.logicdesigner.figures.OrGateFigure;
import org.eclipse.gef.examples.logicdesigner.figures.OutputFigure;
import org.eclipse.gef.examples.logicdesigner.figures.XOrGateFigure;
import org.eclipse.gef.examples.logicdesigner.model.AndGate;
import org.eclipse.gef.examples.logicdesigner.model.OrGate;
import org.eclipse.gef.examples.logicdesigner.model.XORGate;

/**
 * EditPart for holding gates in the Logic Example.
 */
public class GateEditPart extends OutputEditPart {

    private static final class GEP_Anon_DefaultAccessibleAnchorProvider extends DefaultAccessibleAnchorProvider {

        private final GateEditPart parentGep;

        private GEP_Anon_DefaultAccessibleAnchorProvider(AbstractGraphicalEditPart abstractGraphicalEditPart, GateEditPart parentGEP) {
            super(abstractGraphicalEditPart);
            parentGep = parentGEP;
        }

        public List getSourceAnchorLocations() {
            List list = new ArrayList();
            Vector sourceAnchors = parentGep.getNodeFigure().getSourceConnectionAnchors();
            for (int i = 0; i < sourceAnchors.size(); i++) {
                ConnectionAnchor anchor = (ConnectionAnchor) sourceAnchors.get(i);
                list.add(anchor.getReferencePoint().getTranslated(0, -3));
            }
            return list;
        }

        public List getTargetAnchorLocations() {
            List list = new ArrayList();
            Vector targetAnchors = parentGep.getNodeFigure().getTargetConnectionAnchors();
            for (int i = 0; i < targetAnchors.size(); i++) {
                ConnectionAnchor anchor = (ConnectionAnchor) targetAnchors.get(i);
                list.add(anchor.getReferencePoint());
            }
            return list;
        }
    }

    /**
 * Returns a newly created Figure of this.
 *
 * @return A new Figure of this.
 */
    protected IFigure createFigure() {
        OutputFigure figure;
        if (getModel() == null) return null;
        if (getModel() instanceof OrGate) figure = new OrGateFigure(); else if (getModel() instanceof AndGate) figure = new AndGateFigure(); else if (getModel() instanceof XORGate) figure = new XOrGateFigure(); else figure = new GateFigure();
        return figure;
    }

    public Object getAdapter(Class key) {
        if (key == AccessibleAnchorProvider.class) return new GEP_Anon_DefaultAccessibleAnchorProvider(this, this);
        return super.getAdapter(key);
    }
}
