package se.mdh.mrtc.saveccm.diagram.edit.parts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editpolicies.LayoutEditPolicy;
import org.eclipse.gef.editpolicies.ResizableEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ITextAwareEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.ConstrainedToolbarLayoutEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.draw2d.ui.figures.ConstrainedToolbarLayout;
import org.eclipse.gmf.runtime.draw2d.ui.figures.WrapLabel;
import org.eclipse.gmf.runtime.gef.ui.figures.DefaultSizeNodeFigure;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;
import org.eclipse.gmf.runtime.notation.View;
import se.mdh.mrtc.saveccm.diagram.edit.policies.AttributItemSemanticEditPolicy;
import se.mdh.mrtc.saveccm.diagram.edit.policies.SaveccmTextSelectionEditPolicy;
import se.mdh.mrtc.saveccm.diagram.part.SaveccmVisualIDRegistry;

/**
 * @generated
 */
public class AttributEditPart extends ShapeNodeEditPart {

    /**
	 * @generated
	 */
    public static final int VISUAL_ID = 1015;

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
    public AttributEditPart(View view) {
        super(view);
    }

    /**
	 * @generated
	 */
    protected void createDefaultEditPolicies() {
        super.createDefaultEditPolicies();
        installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE, new AttributItemSemanticEditPolicy());
        installEditPolicy(EditPolicy.LAYOUT_ROLE, createLayoutEditPolicy());
    }

    /**
	 * @generated
	 */
    protected LayoutEditPolicy createLayoutEditPolicy() {
        ConstrainedToolbarLayoutEditPolicy lep = new ConstrainedToolbarLayoutEditPolicy() {

            protected EditPolicy createChildEditPolicy(EditPart child) {
                if (child.getEditPolicy(EditPolicy.PRIMARY_DRAG_ROLE) == null) {
                    if (child instanceof ITextAwareEditPart) {
                        return new SaveccmTextSelectionEditPolicy();
                    }
                }
                return super.createChildEditPolicy(child);
            }
        };
        return lep;
    }

    /**
	 * @generated
	 */
    protected IFigure createNodeShape() {
        AttributFigure figure = new AttributFigure();
        return primaryShape = figure;
    }

    /**
	 * @generated
	 */
    public AttributFigure getPrimaryShape() {
        return (AttributFigure) primaryShape;
    }

    /**
	 * @generated
	 */
    protected boolean addFixedChild(EditPart childEditPart) {
        if (childEditPart instanceof AttributTypeEditPart) {
            ((AttributTypeEditPart) childEditPart).setLabel(getPrimaryShape().getFigureAttributTypeLabel());
            return true;
        }
        if (childEditPart instanceof AttributValueEditPart) {
            ((AttributValueEditPart) childEditPart).setLabel(getPrimaryShape().getFigureAttributValueLabel());
            return true;
        }
        if (childEditPart instanceof AttributCredibilityEditPart) {
            ((AttributCredibilityEditPart) childEditPart).setLabel(getPrimaryShape().getFigureAttributCredibilityLabel());
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
    protected IFigure getContentPaneFor(IGraphicalEditPart editPart) {
        return super.getContentPaneFor(editPart);
    }

    /**
	 * @generated
	 */
    protected NodeFigure createNodePlate() {
        DefaultSizeNodeFigure result = new DefaultSizeNodeFigure(getMapMode().DPtoLP(40), getMapMode().DPtoLP(40));
        return result;
    }

    /**
	 * @generated
	 */
    public EditPolicy getPrimaryDragEditPolicy() {
        EditPolicy result = super.getPrimaryDragEditPolicy();
        if (result instanceof ResizableEditPolicy) {
            ResizableEditPolicy ep = (ResizableEditPolicy) result;
            ep.setResizeDirections(PositionConstants.NONE);
        }
        return result;
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
        return getChildBySemanticHint(SaveccmVisualIDRegistry.getType(AttributTypeEditPart.VISUAL_ID));
    }

    /**
	 * @generated
	 */
    public class AttributFigure extends RectangleFigure {

        /**
		 * @generated
		 */
        private WrapLabel fFigureAttributTypeLabel;

        /**
		 * @generated
		 */
        private WrapLabel fFigureAttributValueLabel;

        /**
		 * @generated
		 */
        private WrapLabel fFigureAttributCredibilityLabel;

        /**
		 * @generated
		 */
        public AttributFigure() {
            ToolbarLayout layoutThis = new ToolbarLayout();
            layoutThis.setStretchMinorAxis(true);
            layoutThis.setMinorAlignment(ToolbarLayout.ALIGN_CENTER);
            layoutThis.setSpacing(0);
            layoutThis.setVertical(true);
            this.setLayoutManager(layoutThis);
            createContents();
        }

        /**
		 * @generated
		 */
        private void createContents() {
            WrapLabel attributLabel0 = new WrapLabel();
            attributLabel0.setText("Attribut");
            this.add(attributLabel0);
            fFigureAttributTypeLabel = new WrapLabel();
            fFigureAttributTypeLabel.setText("");
            this.add(fFigureAttributTypeLabel);
            fFigureAttributValueLabel = new WrapLabel();
            fFigureAttributValueLabel.setText("");
            this.add(fFigureAttributValueLabel);
            fFigureAttributCredibilityLabel = new WrapLabel();
            fFigureAttributCredibilityLabel.setText("");
            this.add(fFigureAttributCredibilityLabel);
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

        /**
		 * @generated
		 */
        public WrapLabel getFigureAttributTypeLabel() {
            return fFigureAttributTypeLabel;
        }

        /**
		 * @generated
		 */
        public WrapLabel getFigureAttributValueLabel() {
            return fFigureAttributValueLabel;
        }

        /**
		 * @generated
		 */
        public WrapLabel getFigureAttributCredibilityLabel() {
            return fFigureAttributCredibilityLabel;
        }
    }
}
