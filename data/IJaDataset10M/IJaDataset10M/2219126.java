package hub.sam.mof.simulator.editor.diagram.edit.parts;

import hub.sam.mof.simulator.editor.diagram.edit.policies.MClassMOperationsCanonicalEditPolicy;
import hub.sam.mof.simulator.editor.diagram.edit.policies.MClassMOperationsItemSemanticEditPolicy;
import org.eclipse.draw2d.IFigure;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ListCompartmentEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.CreationEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.DragDropEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.ResizableCompartmentEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.figures.ResizableCompartmentFigure;
import org.eclipse.gmf.runtime.notation.View;

/**
 * @generated
 */
public class MClassMOperationsEditPart extends ListCompartmentEditPart {

    /**
	 * @generated
	 */
    public static final int VISUAL_ID = 5010;

    /**
	 * @generated
	 */
    public MClassMOperationsEditPart(View view) {
        super(view);
    }

    /**
	 * @generated
	 */
    protected boolean hasModelChildrenChanged(Notification evt) {
        return false;
    }

    /**
	 * @generated
	 */
    public String getCompartmentName() {
        return hub.sam.mof.simulator.editor.diagram.part.Messages.MClassMOperationsEditPart_title;
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
        installEditPolicy(org.eclipse.gef.EditPolicy.PRIMARY_DRAG_ROLE, new ResizableCompartmentEditPolicy());
        installEditPolicy(org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles.SEMANTIC_ROLE, new MClassMOperationsItemSemanticEditPolicy());
        installEditPolicy(org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles.CREATION_ROLE, new CreationEditPolicy());
        installEditPolicy(org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles.DRAG_DROP_ROLE, new DragDropEditPolicy());
        installEditPolicy(org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles.CANONICAL_ROLE, new MClassMOperationsCanonicalEditPolicy());
    }

    /**
	 * @generated
	 */
    protected void setRatio(Double ratio) {
    }
}
