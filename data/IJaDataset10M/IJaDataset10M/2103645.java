package it.unisannio.rcost.callgraphanalyzer.diagram.edit.parts;

import it.unisannio.rcost.callgraphanalyzer.diagram.edit.policies.AspectBodyCompartmentCanonicalEditPolicy;
import it.unisannio.rcost.callgraphanalyzer.diagram.edit.policies.AspectBodyCompartmentItemSemanticEditPolicy;
import it.unisannio.rcost.callgraphanalyzer.diagram.part.Messages;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeCompartmentEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.CreationEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.DragDropEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.ResizableCompartmentEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.figures.ResizableCompartmentFigure;
import org.eclipse.gmf.runtime.notation.View;

/**
 * @generated
 */
public class AspectBodyCompartmentEditPart extends ShapeCompartmentEditPart {

    @Override
    public void deactivate() {
        try {
            super.deactivate();
        } catch (Exception e) {
        }
    }

    /**
	 * @generated
	 */
    public static final int VISUAL_ID = 5002;

    /**
	 * @generated
	 */
    public AspectBodyCompartmentEditPart(View view) {
        super(view);
    }

    /**
	 * @generated
	 */
    public String getCompartmentName() {
        return Messages.AspectBodyCompartmentEditPart_title;
    }

    /**
	 * @generated
	 */
    public IFigure createFigure() {
        ResizableCompartmentFigure result = (ResizableCompartmentFigure) super.createFigure();
        result.setTitleVisibility(false);
        return result;
    }

    /**
	 * @generated
	 */
    protected void createDefaultEditPolicies() {
        super.createDefaultEditPolicies();
        installEditPolicy(EditPolicy.PRIMARY_DRAG_ROLE, new ResizableCompartmentEditPolicy());
        installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE, new AspectBodyCompartmentItemSemanticEditPolicy());
        installEditPolicy(EditPolicyRoles.CREATION_ROLE, new CreationEditPolicy());
        installEditPolicy(EditPolicyRoles.DRAG_DROP_ROLE, new DragDropEditPolicy());
        installEditPolicy(EditPolicyRoles.CANONICAL_ROLE, new AspectBodyCompartmentCanonicalEditPolicy());
    }

    /**
	 * @generated
	 */
    protected void setRatio(Double ratio) {
    }
}
