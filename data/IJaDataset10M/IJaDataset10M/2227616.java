package se.mdh.mrtc.saveccm.diagram.edit.parts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ListCompartmentEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.CreationEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.DragDropEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.ResizableCompartmentEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.figures.ResizableCompartmentFigure;
import org.eclipse.gmf.runtime.notation.View;
import se.mdh.mrtc.saveccm.diagram.edit.policies.ComponentComponentBindPortsCompartmentCanonicalEditPolicy;
import se.mdh.mrtc.saveccm.diagram.edit.policies.ComponentComponentBindPortsCompartmentItemSemanticEditPolicy;
import se.mdh.mrtc.saveccm.diagram.part.Messages;

/**
 * @generated
 */
public class ComponentComponentBindPortsCompartmentEditPart extends ListCompartmentEditPart {

    /**
	 * @generated
	 */
    public static final int VISUAL_ID = 5009;

    /**
	 * @generated
	 */
    public ComponentComponentBindPortsCompartmentEditPart(View view) {
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
        return Messages.ComponentComponentBindPortsCompartmentEditPart_title;
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
        installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE, new ComponentComponentBindPortsCompartmentItemSemanticEditPolicy());
        installEditPolicy(EditPolicyRoles.CREATION_ROLE, new CreationEditPolicy());
        installEditPolicy(EditPolicyRoles.DRAG_DROP_ROLE, new DragDropEditPolicy());
        installEditPolicy(EditPolicyRoles.CANONICAL_ROLE, new ComponentComponentBindPortsCompartmentCanonicalEditPolicy());
    }

    /**
	 * @generated
	 */
    protected void setRatio(Double ratio) {
    }
}
