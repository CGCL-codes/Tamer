package jfb.examples.gmf.school.diagram.edit.parts;

import jfb.examples.gmf.school.diagram.edit.policies.ClassroomClassroomStudentsCompartmentCanonicalEditPolicy;
import jfb.examples.gmf.school.diagram.edit.policies.ClassroomClassroomStudentsCompartmentItemSemanticEditPolicy;
import jfb.examples.gmf.school.diagram.part.Messages;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeCompartmentEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.CreationEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.DragDropEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.figures.ResizableCompartmentFigure;
import org.eclipse.gmf.runtime.draw2d.ui.figures.ConstrainedToolbarLayout;
import org.eclipse.gmf.runtime.notation.View;

/**
 * @generated
 */
public class ClassroomClassroomStudentsCompartmentEditPart extends ShapeCompartmentEditPart {

    /**
	 * @generated
	 */
    public static final int VISUAL_ID = 7002;

    /**
	 * @generated
	 */
    public ClassroomClassroomStudentsCompartmentEditPart(View view) {
        super(view);
    }

    /**
	 * @generated
	 */
    public String getCompartmentName() {
        return Messages.ClassroomClassroomStudentsCompartmentEditPart_title;
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
        installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE, new ClassroomClassroomStudentsCompartmentItemSemanticEditPolicy());
        installEditPolicy(EditPolicyRoles.CREATION_ROLE, new CreationEditPolicy());
        installEditPolicy(EditPolicyRoles.DRAG_DROP_ROLE, new DragDropEditPolicy());
        installEditPolicy(EditPolicyRoles.CANONICAL_ROLE, new ClassroomClassroomStudentsCompartmentCanonicalEditPolicy());
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
