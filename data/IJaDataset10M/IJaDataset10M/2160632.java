package net.sourceforge.dynamicgmf.diagram.edit.parts;

import net.sourceforge.dynamicgmf.diagram.edit.policies.EContainmentItemSemanticEditPolicy;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.RotatableDecoration;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.draw2d.ui.figures.PolylineConnectionEx;
import org.eclipse.gmf.runtime.notation.View;

/**
 * @generated
 */
public class EContainmentEditPart extends ConnectionNodeEditPart {

    /**
	 * @generated
	 */
    public static final int VISUAL_ID = 3002;

    /**
	 * @generated
	 */
    public EContainmentEditPart(View view) {
        super(view);
    }

    /**
	 * @generated
	 */
    protected void createDefaultEditPolicies() {
        super.createDefaultEditPolicies();
        installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE, new EContainmentItemSemanticEditPolicy());
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
        return new ContainmentFigure();
    }

    /**
	 * @generated
	 */
    public ContainmentFigure getPrimaryShape() {
        return (ContainmentFigure) getFigure();
    }

    /**
	 * @generated
	 */
    public class ContainmentFigure extends PolylineConnectionEx {

        /**
		 * @generated
		 */
        public ContainmentFigure() {
            setSourceDecoration(createSourceDecoration());
        }

        /**
		 * @generated
		 */
        private RotatableDecoration createSourceDecoration() {
            PolygonDecoration df = new PolygonDecoration();
            PointList pl = new PointList();
            pl.addPoint(getMapMode().DPtoLP(0), getMapMode().DPtoLP(0));
            pl.addPoint(getMapMode().DPtoLP(-1), getMapMode().DPtoLP(1));
            pl.addPoint(getMapMode().DPtoLP(-2), getMapMode().DPtoLP(0));
            pl.addPoint(getMapMode().DPtoLP(-1), getMapMode().DPtoLP(-1));
            df.setTemplate(pl);
            df.setScale(getMapMode().DPtoLP(7), getMapMode().DPtoLP(3));
            return df;
        }
    }
}
