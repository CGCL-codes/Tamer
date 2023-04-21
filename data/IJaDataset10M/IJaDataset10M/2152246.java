package it.cnr.stlab.xd.plugin.editor.parts;

import it.cnr.stlab.xd.plugin.editor.figures.IndividualFigure;
import it.cnr.stlab.xd.plugin.editor.model.NodeIndividual;
import org.eclipse.draw2d.IFigure;

public class IndividualEditPart extends EntityEditPart {

    protected IFigure createFigure() {
        IFigure figure = new IndividualFigure();
        return figure;
    }

    @Override
    protected void createEditPolicies() {
        super.createEditPolicies();
    }

    /**
	 * This method updates visual data
	 */
    protected void refreshVisuals() {
        super.refreshVisuals();
    }

    protected void refreshContent() {
        IndividualFigure figure = (IndividualFigure) getFigure();
        NodeIndividual model = (NodeIndividual) getModel();
        figure.setEntityName(model.getName());
        setTooltip();
    }
}
