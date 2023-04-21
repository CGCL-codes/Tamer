package uk.ac.bolton.archimate.editor.diagram.editparts.extensions;

import org.eclipse.draw2d.IFigure;
import uk.ac.bolton.archimate.editor.diagram.editparts.AbstractArchimateEditableTextFlowEditPart;
import uk.ac.bolton.archimate.editor.diagram.figures.extensions.RequirementFigure;

/**
 * Requirement Edit Part
 * 
 * @author Phillip Beauvoir
 */
public class RequirementEditPart extends AbstractArchimateEditableTextFlowEditPart {

    @Override
    protected IFigure createFigure() {
        return new RequirementFigure(getModel());
    }
}
