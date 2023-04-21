package net.taylor.uml2.pageflowdiagram.edit.parts;

import net.taylor.uml2.pageflowdiagram.edit.policies.PseudostateCanonicalEditPolicy;
import net.taylor.uml2.pageflowdiagram.edit.policies.PseudostateGraphicalNodeEditPolicy;
import net.taylor.uml2.pageflowdiagram.edit.policies.PseudostateItemSemanticEditPolicy;
import net.taylor.uml2.pageflowdiagram.part.UMLVisualIDRegistry;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.LayoutEditPolicy;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.draw2d.ui.figures.ConstrainedToolbarLayout;
import org.eclipse.gmf.runtime.gef.ui.figures.DefaultSizeNodeFigure;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;
import org.eclipse.gmf.runtime.notation.View;

/**
 * @generated
 */
public class PseudostateEditPart extends ShapeNodeEditPart {

    /**
	 * @generated
	 */
    public static final int VISUAL_ID = 1201;

    /**
	 * @generated
	 */
    protected IFigure contentPane;

    /**
	 * @generated
	 */
    protected IFigure primaryShape;

    /**
	 * @generated
	 */
    public PseudostateEditPart(View view) {
        super(view);
    }

    /**
	 * @generated
	 */
    protected void createDefaultEditPolicies() {
        super.createDefaultEditPolicies();
        installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE, new PseudostateItemSemanticEditPolicy());
        installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new PseudostateGraphicalNodeEditPolicy());
        installEditPolicy(EditPolicyRoles.CANONICAL_ROLE, new PseudostateCanonicalEditPolicy());
        installEditPolicy(EditPolicy.LAYOUT_ROLE, createLayoutEditPolicy());
    }

    /**
	 * @generated
	 */
    protected LayoutEditPolicy createLayoutEditPolicy() {
        LayoutEditPolicy lep = new LayoutEditPolicy() {

            protected EditPolicy createChildEditPolicy(EditPart child) {
                EditPolicy result = child.getEditPolicy(EditPolicy.PRIMARY_DRAG_ROLE);
                if (result == null) {
                    result = new NonResizableEditPolicy();
                }
                return result;
            }

            protected Command getMoveChildrenCommand(Request request) {
                return null;
            }

            protected Command getCreateCommand(CreateRequest request) {
                return null;
            }
        };
        return lep;
    }

    /**
	 * @generated
	 */
    protected IFigure createNodeShape() {
        PageFigure figure = new PageFigure();
        return primaryShape = figure;
    }

    /**
	 * @generated
	 */
    public PageFigure getPrimaryShape() {
        return (PageFigure) primaryShape;
    }

    /**
	 * @generated
	 */
    protected boolean addFixedChild(EditPart childEditPart) {
        if (childEditPart instanceof PseudostateNameEditPart) {
            ((PseudostateNameEditPart) childEditPart).setLabel(getPrimaryShape().getFigureNodeNameFigure());
            return true;
        }
        return false;
    }

    /**
	 * @generated
	 */
    protected boolean removeFixedChild(EditPart childEditPart) {
        return false;
    }

    /**
	 * @generated
	 */
    protected NodeFigure createNodePlate() {
        return new DefaultSizeNodeFigure(getMapMode().DPtoLP(40), getMapMode().DPtoLP(40));
    }

    /**
	 * Creates figure for this edit part.
	 * 
	 * Body of this method does not depend on settings in generation model
	 * so you may safely remove <i>generated</i> tag and modify it.
	 * 
	 * @generated
	 */
    protected NodeFigure createNodeFigure() {
        NodeFigure figure = createNodePlate();
        figure.setLayoutManager(new StackLayout());
        IFigure shape = createNodeShape();
        figure.add(shape);
        contentPane = setupContentPane(shape);
        return figure;
    }

    /**
	 * Default implementation treats passed figure as content pane.
	 * Respects layout one may have set for generated figure.
	 * @param nodeShape instance of generated figure class
	 * @generated
	 */
    protected IFigure setupContentPane(IFigure nodeShape) {
        if (nodeShape.getLayoutManager() == null) {
            ConstrainedToolbarLayout layout = new ConstrainedToolbarLayout();
            layout.setSpacing(getMapMode().DPtoLP(5));
            nodeShape.setLayoutManager(layout);
        }
        return nodeShape;
    }

    /**
	 * @generated
	 */
    public IFigure getContentPane() {
        if (contentPane != null) {
            return contentPane;
        }
        return super.getContentPane();
    }

    /**
	 * @generated
	 */
    public EditPart getPrimaryChildEditPart() {
        return getChildBySemanticHint(UMLVisualIDRegistry.getType(PseudostateNameEditPart.VISUAL_ID));
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
    protected void removeChildVisual(EditPart childEditPart) {
        if (removeFixedChild(childEditPart)) {
            return;
        }
        super.removeChildVisual(childEditPart);
    }

    /**
	 * @generated
	 */
    public class PageFigure extends org.eclipse.draw2d.RoundedRectangle {

        /**
		 * @generated
		 */
        public PageFigure() {
            this.setCornerDimensions(new org.eclipse.draw2d.geometry.Dimension(getMapMode().DPtoLP(20), getMapMode().DPtoLP(20)));
            createContents();
        }

        /**
		 * @generated
		 */
        private void createContents() {
            org.eclipse.gmf.runtime.draw2d.ui.figures.WrapLabel fig_0 = new org.eclipse.gmf.runtime.draw2d.ui.figures.WrapLabel();
            fig_0.setText("");
            setFigureNodeNameFigure(fig_0);
            Object layData0 = null;
            this.add(fig_0, layData0);
        }

        /**
		 * @generated
		 */
        private org.eclipse.gmf.runtime.draw2d.ui.figures.WrapLabel fNodeNameFigure;

        /**
		 * @generated
		 */
        public org.eclipse.gmf.runtime.draw2d.ui.figures.WrapLabel getFigureNodeNameFigure() {
            return fNodeNameFigure;
        }

        /**
		 * @generated
		 */
        private void setFigureNodeNameFigure(org.eclipse.gmf.runtime.draw2d.ui.figures.WrapLabel fig) {
            fNodeNameFigure = fig;
        }

        /**
		 * @generated
		 */
        private boolean myUseLocalCoordinates = false;

        /**
		 * @generated
		 */
        protected boolean useLocalCoordinates() {
            return myUseLocalCoordinates;
        }

        /**
		 * @generated
		 */
        protected void setUseLocalCoordinates(boolean useLocalCoordinates) {
            myUseLocalCoordinates = useLocalCoordinates;
        }
    }
}
