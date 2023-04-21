package se.mdh.mrtc.saveccm.composite.diagram.edit.parts;

import org.eclipse.draw2d.Connection;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.draw2d.ui.figures.PolylineConnectionEx;
import org.eclipse.gmf.runtime.notation.View;
import se.mdh.mrtc.saveccm.composite.diagram.edit.policies.ConnectionItemSemanticEditPolicy;

/**
 * @generated
 */
public class ConnectionEditPart extends ConnectionNodeEditPart {

    /**
	 * @generated
	 */
    public static final int VISUAL_ID = 3001;

    /**
	 * @generated
	 */
    public ConnectionEditPart(View view) {
        super(view);
    }

    /**
	 * @generated
	 */
    protected void createDefaultEditPolicies() {
        super.createDefaultEditPolicies();
        installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE, new ConnectionItemSemanticEditPolicy());
    }

    /**
	 * Creates figure for this edit part.
	 * 
	 * Body of this method does not depend on settings in generation model
	 * so you may safely remove <i>generated</i> tag and modify it.
	 * 
	 * @generated
	 */
    protected Connection createConnectionFigure() {
        return new PolylineConnectionEx();
    }

    /**
	 * @generated
	 */
    public PolylineConnectionEx getPrimaryShape() {
        return (PolylineConnectionEx) getFigure();
    }
}
