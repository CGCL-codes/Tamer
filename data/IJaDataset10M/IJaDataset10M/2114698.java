package misusecase.diagram.edit.parts;

import misusecase.diagram.edit.policies.SecurityUseCaseNodeMitigateMisuseItemSemanticEditPolicy;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.PolylineDecoration;
import org.eclipse.draw2d.RotatableDecoration;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ITreeBranchEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.draw2d.ui.figures.PolylineConnectionEx;
import org.eclipse.gmf.runtime.draw2d.ui.figures.WrappingLabel;
import org.eclipse.gmf.runtime.notation.View;

/**
 * @generated
 */
public class SecurityUseCaseNodeMitigateMisuseEditPart extends ConnectionNodeEditPart implements ITreeBranchEditPart {

    /**
	 * @generated
	 */
    public static final int VISUAL_ID = 4009;

    /**
	 * @generated
	 */
    public SecurityUseCaseNodeMitigateMisuseEditPart(View view) {
        super(view);
    }

    /**
	 * @generated
	 */
    protected void createDefaultEditPolicies() {
        super.createDefaultEditPolicies();
        installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE, new SecurityUseCaseNodeMitigateMisuseItemSemanticEditPolicy());
    }

    /**
	 * @generated
	 */
    protected boolean addFixedChild(EditPart childEditPart) {
        if (childEditPart instanceof WrappingLabel6EditPart) {
            ((WrappingLabel6EditPart) childEditPart).setLabel(getPrimaryShape().getFigureMitigateLabelFigure());
            return true;
        }
        return false;
    }

    /**
	 * @generated
	 */
    protected void addChildVisual(EditPart childEditPart, int index) {
        if (addFixedChild(childEditPart)) {
            return;
        }
        super.addChildVisual(childEditPart, -1);
    }

    /**
	 * @generated
	 */
    protected boolean removeFixedChild(EditPart childEditPart) {
        if (childEditPart instanceof WrappingLabel6EditPart) {
            return true;
        }
        return false;
    }

    /**
	 * @generated
	 */
    protected void removeChildVisual(EditPart childEditPart) {
        if (removeFixedChild(childEditPart)) {
            return;
        }
        super.removeChildVisual(childEditPart);
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
        return new MitigateFigure();
    }

    /**
	 * @generated
	 */
    public MitigateFigure getPrimaryShape() {
        return (MitigateFigure) getFigure();
    }

    /**
	 * @generated
	 */
    public class MitigateFigure extends PolylineConnectionEx {

        /**
		 * @generated
		 */
        private WrappingLabel fFigureMitigateLabelFigure;

        /**
		 * @generated
		 */
        public MitigateFigure() {
            this.setLineWidth(2);
            this.setLineStyle(Graphics.LINE_DASH);
            this.setForegroundColor(ColorConstants.black);
            createContents();
            setTargetDecoration(createTargetDecoration());
        }

        /**
		 * @generated
		 */
        private void createContents() {
            fFigureMitigateLabelFigure = new WrappingLabel();
            fFigureMitigateLabelFigure.setText("<<mitigate>>");
            this.add(fFigureMitigateLabelFigure);
        }

        /**
		 * @generated
		 */
        private RotatableDecoration createTargetDecoration() {
            PolylineDecoration df = new PolylineDecoration();
            df.setLineWidth(2);
            return df;
        }

        /**
		 * @generated
		 */
        public WrappingLabel getFigureMitigateLabelFigure() {
            return fFigureMitigateLabelFigure;
        }
    }
}
