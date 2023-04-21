package cz.vse.gebz.diagram.edit.parts;

import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeCompartmentEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.CreationEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.DragDropEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.draw2d.ui.figures.ConstrainedToolbarLayout;
import org.eclipse.gmf.runtime.notation.View;
import cz.vse.gebz.diagram.edit.policies.BazeZnalostiDotazyCanonicalEditPolicy;
import cz.vse.gebz.diagram.edit.policies.BazeZnalostiDotazyItemSemanticEditPolicy;
import cz.vse.gebz.diagram.part.Messages;

/**
 * @generated
 */
public class BazeZnalostiDotazyEditPart extends ShapeCompartmentEditPart {

    /**
	 * @generated
	 */
    public static final int VISUAL_ID = 7032;

    /**
	 * @generated
	 */
    public BazeZnalostiDotazyEditPart(View view) {
        super(view);
    }

    /**
	 * @generated
	 */
    public String getCompartmentName() {
        return Messages.BazeZnalostiDotazyEditPart_title;
    }

    /**
	 * @generated
	 */
    protected void createDefaultEditPolicies() {
        super.createDefaultEditPolicies();
        installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE, new BazeZnalostiDotazyItemSemanticEditPolicy());
        installEditPolicy(EditPolicyRoles.CREATION_ROLE, new CreationEditPolicy());
        installEditPolicy(EditPolicyRoles.DRAG_DROP_ROLE, new DragDropEditPolicy());
        installEditPolicy(EditPolicyRoles.CANONICAL_ROLE, new BazeZnalostiDotazyCanonicalEditPolicy());
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
