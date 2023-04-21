package odm.diagram.edit.parts;

import odm.diagram.edit.policies.DisjointUnionItemSemanticEditPolicy;
import org.eclipse.draw2d.Connection;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.notation.View;

/**
 * @generated
 */
public class DisjointUnionEditPart extends ConnectionNodeEditPart {

    /**
	 * @generated
	 */
    public static final int VISUAL_ID = 4032;

    /**
	 * @generated
	 */
    public DisjointUnionEditPart(View view) {
        super(view);
    }

    /**
	 * @generated
	 */
    protected void createDefaultEditPolicies() {
        super.createDefaultEditPolicies();
        installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE, new DisjointUnionItemSemanticEditPolicy());
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
        return new DashWithOpenArrowTargetFigure();
    }

    /**
	 * @generated
	 */
    public class DashWithOpenArrowTargetFigure extends org.eclipse.gmf.runtime.draw2d.ui.figures.PolylineConnectionEx {

        /**
		 * @generated
		 */
        public DashWithOpenArrowTargetFigure() {
            this.setLineStyle(org.eclipse.draw2d.Graphics.LINE_DASH);
            this.setForegroundColor(org.eclipse.draw2d.ColorConstants.black);
            setTargetDecoration(createTargetDecoration());
        }

        /**
		 * @generated
		 */
        private org.eclipse.draw2d.PolylineDecoration createTargetDecoration() {
            org.eclipse.draw2d.PolylineDecoration df = new org.eclipse.draw2d.PolylineDecoration();
            df.setFill(false);
            org.eclipse.draw2d.geometry.PointList pl = new org.eclipse.draw2d.geometry.PointList();
            pl.addPoint(-3, 2);
            pl.addPoint(0, 0);
            pl.addPoint(-3, -2);
            df.setTemplate(pl);
            df.setScale(getMapMode().DPtoLP(7), getMapMode().DPtoLP(3));
            return df;
        }
    }
}
