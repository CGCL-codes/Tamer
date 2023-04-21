package hub.sam.mof.simulator.behaviour.diagram.edit.parts;

import hub.sam.mof.simulator.behaviour.diagram.edit.policies.MAtomicGroupMAtomicGroupContentPaneCompartment2CanonicalEditPolicy;
import hub.sam.mof.simulator.behaviour.diagram.edit.policies.MAtomicGroupMAtomicGroupContentPaneCompartment2ItemSemanticEditPolicy;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeCompartmentEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.CreationEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.DragDropEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.figures.ResizableCompartmentFigure;
import org.eclipse.gmf.runtime.draw2d.ui.figures.ConstrainedToolbarLayout;
import org.eclipse.gmf.runtime.notation.View;

/**
 * @generated
 */
public class MAtomicGroupMAtomicGroupContentPaneCompartment2EditPart extends ShapeCompartmentEditPart {

    /**
	 * @generated
	 */
    public static final int VISUAL_ID = 7005;

    /**
	 * @generated
	 */
    public MAtomicGroupMAtomicGroupContentPaneCompartment2EditPart(View view) {
        super(view);
    }

    /**
	 * @generated
	 */
    public String getCompartmentName() {
        return hub.sam.mof.simulator.behaviour.diagram.part.Messages.MAtomicGroupMAtomicGroupContentPaneCompartment2EditPart_title;
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
        installEditPolicy(org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles.SEMANTIC_ROLE, new MAtomicGroupMAtomicGroupContentPaneCompartment2ItemSemanticEditPolicy());
        installEditPolicy(org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles.CREATION_ROLE, new CreationEditPolicy());
        installEditPolicy(org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles.DRAG_DROP_ROLE, new DragDropEditPolicy());
        installEditPolicy(org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles.CANONICAL_ROLE, new MAtomicGroupMAtomicGroupContentPaneCompartment2CanonicalEditPolicy());
    }

    /**
	 * @generated
	 */
    protected void setRatio(Double ratio) {
        if (getFigure().getParent().getLayoutManager() instanceof ConstrainedToolbarLayout) {
            super.setRatio(ratio);
        }
    }
}
