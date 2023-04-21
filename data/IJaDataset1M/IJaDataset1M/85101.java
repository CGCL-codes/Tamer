package net.sourceforge.dynamicgmf.diagram.edit.parts;

import net.sourceforge.dynamicgmf.diagram.edit.policies.EReferenceItemSemanticEditPolicy;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.PolylineDecoration;
import org.eclipse.draw2d.RotatableDecoration;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.draw2d.ui.figures.PolylineConnectionEx;
import org.eclipse.gmf.runtime.notation.View;

/**
 * @generated
 */
public class EReferenceEditPart extends ConnectionNodeEditPart {

    /**
	 * @generated
	 */
    public static final int VISUAL_ID = 3003;

    /**
	 * @generated
	 */
    public EReferenceEditPart(View view) {
        super(view);
    }

    /**
	 * @generated
	 */
    protected void createDefaultEditPolicies() {
        super.createDefaultEditPolicies();
        installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE, new EReferenceItemSemanticEditPolicy());
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
        return new ReferenceFigure();
    }

    /**
	 * @generated
	 */
    public ReferenceFigure getPrimaryShape() {
        return (ReferenceFigure) getFigure();
    }

    /**
	 * @generated
	 */
    public class ReferenceFigure extends PolylineConnectionEx {

        /**
		 * @generated
		 */
        public ReferenceFigure() {
            setTargetDecoration(createTargetDecoration());
        }

        /**
		 * @generated
		 */
        private RotatableDecoration createTargetDecoration() {
            PolylineDecoration df = new PolylineDecoration();
            return df;
        }
    }
}
