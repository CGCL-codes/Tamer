package edu.toronto.cs.openome_model.diagram.edit.parts;

import java.util.ArrayList;
import java.util.List;
import openome_model.figures.GoalAnchor;
import openome_model.figures.ResourceAnchor;
import openome_model.figures.SoftgoalAnchor;
import openome_model.figures.TaskAnchor;
import org.eclipse.draw2d.AbsoluteBendpoint;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.RotatableDecoration;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ITreeBranchEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.draw2d.ui.figures.PolylineConnectionEx;
import org.eclipse.gmf.runtime.draw2d.ui.figures.WrappingLabel;
import org.eclipse.gmf.runtime.notation.RelativeBendpoints;
import org.eclipse.gmf.runtime.notation.View;

/**
 * @generated
 */
public class HelpContributionEditPart extends ConnectionNodeEditPart implements ITreeBranchEditPart {

    /**
	 * @generated
	 */
    public static final int VISUAL_ID = 3004;

    /**
	 * @generated
	 */
    public HelpContributionEditPart(View view) {
        super(view);
    }

    /**
	 * @generated
	 */
    protected void createDefaultEditPolicies() {
        super.createDefaultEditPolicies();
        installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE, new edu.toronto.cs.openome_model.diagram.edit.policies.HelpContributionItemSemanticEditPolicy());
    }

    /**
	 * @generated
	 */
    protected boolean addFixedChild(EditPart childEditPart) {
        if (childEditPart instanceof edu.toronto.cs.openome_model.diagram.edit.parts.HelpContributionContributionTypeEditPart) {
            ((edu.toronto.cs.openome_model.diagram.edit.parts.HelpContributionContributionTypeEditPart) childEditPart).setLabel(getPrimaryShape().getFigureHelpContributionLabel());
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
        if (childEditPart instanceof edu.toronto.cs.openome_model.diagram.edit.parts.HelpContributionContributionTypeEditPart) {
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
        return new HelpContributionFigure();
    }

    /**
	 * @generated
	 */
    public HelpContributionFigure getPrimaryShape() {
        return (HelpContributionFigure) getFigure();
    }

    /**
	 * Make this line straight
	 */
    public void straightenLine() {
        getPrimaryShape().straightenLine();
        RelativeBendpoints bendpoints = (RelativeBendpoints) getEdge().getBendpoints();
        ArrayList<RelativeBendpoints> emptyList = new ArrayList<RelativeBendpoints>();
        bendpoints.eSetDeliver(false);
        bendpoints.setPoints(emptyList);
        bendpoints.eSetDeliver(true);
    }

    /**
	 * @generated
	 */
    public class HelpContributionFigure extends PolylineConnectionEx {

        /**
		 * @generated
		 */
        private WrappingLabel fFigureHelpContributionLabel;

        /**
		 * @generated
		 */
        public HelpContributionFigure() {
            this.setLineWidth(2);
            createContents();
            setTargetDecoration(createTargetDecoration());
        }

        /**
		 * Straighten the connector so that there are no bends or curves
		 */
        public void straightenLine() {
            Point sourcePoint = this.getPoints().getFirstPoint();
            Point targetPoint = this.getPoints().getLastPoint();
            ArrayList<AbsoluteBendpoint> list = new ArrayList<AbsoluteBendpoint>();
            list.add(new AbsoluteBendpoint(sourcePoint));
            list.add(new AbsoluteBendpoint(targetPoint));
            this.setRoutingConstraint(list);
        }

        /**
		 * @generated
		 */
        private void createContents() {
            fFigureHelpContributionLabel = new WrappingLabel();
            fFigureHelpContributionLabel.setText("+");
            this.add(fFigureHelpContributionLabel);
        }

        /**
		 * @generated
		 */
        private RotatableDecoration createTargetDecoration() {
            openome_model.figures.ContributionDecoration df = new openome_model.figures.ContributionDecoration();
            return df;
        }

        /**
		 * @generated
		 */
        public WrappingLabel getFigureHelpContributionLabel() {
            return fFigureHelpContributionLabel;
        }

        /**
		 * @generated NOT
		 */
        public void outlineShape(Graphics g) {
            ConnectionAnchor sourceAnchor = this.getSourceAnchor();
            ConnectionAnchor targetAnchor = this.getTargetAnchor();
            boolean goalAnchorInSameContainerAsTargetAnchor = ((sourceAnchor instanceof GoalAnchor) && ((GoalAnchor) sourceAnchor).collapsedInSameContainerAs(targetAnchor));
            boolean softGoalAnchorInSameContainerAsTargetAnchor = ((sourceAnchor instanceof SoftgoalAnchor) && ((SoftgoalAnchor) sourceAnchor).collapsedInSameContainerAs(targetAnchor));
            boolean TaskAnchorInSameContainerAsTargetAnchor = ((sourceAnchor instanceof TaskAnchor) && ((TaskAnchor) sourceAnchor).collapsedInSameContainerAs(targetAnchor));
            boolean ResourceAnchorInSameContainerAsTargetAnchor = ((sourceAnchor instanceof ResourceAnchor) && ((ResourceAnchor) sourceAnchor).collapsedInSameContainerAs(targetAnchor));
            List listOfChildren = this.getChildren();
            WrappingLabel label = new WrappingLabel();
            for (int i = 0; i < listOfChildren.size(); i++) {
                Object currentChild = listOfChildren.get(i);
                if (currentChild instanceof WrappingLabel) {
                    label = (WrappingLabel) currentChild;
                }
            }
            if (goalAnchorInSameContainerAsTargetAnchor || softGoalAnchorInSameContainerAsTargetAnchor || TaskAnchorInSameContainerAsTargetAnchor || ResourceAnchorInSameContainerAsTargetAnchor) {
                this.setVisible(false);
                this.getTargetDecoration().setVisible(false);
                label.setVisible(false);
            } else {
                super.outlineShape(g);
                this.setVisible(true);
                this.getTargetDecoration().setVisible(true);
                label.setVisible(true);
            }
        }
    }
}
