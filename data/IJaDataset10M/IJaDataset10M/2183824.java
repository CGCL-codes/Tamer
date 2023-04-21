package uk.ac.bolton.archimate.editor.diagram.editparts.business;

import org.eclipse.draw2d.IFigure;
import uk.ac.bolton.archimate.editor.diagram.editparts.AbstractArchimateEditableTextFlowEditPart;
import uk.ac.bolton.archimate.editor.diagram.figures.business.BusinessProductFigure;

/**
 * Business Product Edit Part
 * 
 * @author Phillip Beauvoir
 */
public class BusinessProductEditPart extends AbstractArchimateEditableTextFlowEditPart {

    @Override
    protected IFigure createFigure() {
        return new BusinessProductFigure(getModel());
    }
}
