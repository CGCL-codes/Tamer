package misusecase.diagram.edit.parts;

import misusecase.diagram.edit.policies.SystemBoxItemSemanticEditPolicy;
import misusecase.diagram.part.SeaMonsterVisualIDRegistry;
import misusecase.diagram.providers.SeaMonsterElementTypes;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.LayoutEditPolicy;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gmf.runtime.diagram.core.edithelpers.CreateElementRequestAdapter;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.CreationEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewAndElementRequest;
import org.eclipse.gmf.runtime.draw2d.ui.figures.ConstrainedToolbarLayout;
import org.eclipse.gmf.runtime.draw2d.ui.figures.WrappingLabel;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.gef.ui.figures.DefaultSizeNodeFigure;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.swt.graphics.Color;

/**
 * @generated
 */
public class SystemBoxEditPart extends ShapeNodeEditPart {

    /**
	 * @generated
	 */
    public static final int VISUAL_ID = 2002;

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
    public SystemBoxEditPart(View view) {
        super(view);
    }

    /**
	 * @generated
	 */
    protected void createDefaultEditPolicies() {
        installEditPolicy(EditPolicyRoles.CREATION_ROLE, new CreationEditPolicy());
        super.createDefaultEditPolicies();
        installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE, new SystemBoxItemSemanticEditPolicy());
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
        SystemBoxFigure figure = new SystemBoxFigure();
        return primaryShape = figure;
    }

    /**
	 * @generated
	 */
    public SystemBoxFigure getPrimaryShape() {
        return (SystemBoxFigure) primaryShape;
    }

    /**
	 * @generated
	 */
    protected boolean addFixedChild(EditPart childEditPart) {
        if (childEditPart instanceof WrappingLabelEditPart) {
            ((WrappingLabelEditPart) childEditPart).setLabel(getPrimaryShape().getFigureSystemTitleLabel());
            return true;
        }
        if (childEditPart instanceof SystemBoxSystemCompartmentEditPart) {
            IFigure pane = getPrimaryShape().getFigureSystemBoxContainer();
            setupContentPane(pane);
            pane.add(((SystemBoxSystemCompartmentEditPart) childEditPart).getFigure());
            return true;
        }
        return false;
    }

    /**
	 * @generated
	 */
    protected boolean removeFixedChild(EditPart childEditPart) {
        if (childEditPart instanceof WrappingLabelEditPart) {
            return true;
        }
        if (childEditPart instanceof SystemBoxSystemCompartmentEditPart) {
            IFigure pane = getPrimaryShape().getFigureSystemBoxContainer();
            setupContentPane(pane);
            pane.remove(((SystemBoxSystemCompartmentEditPart) childEditPart).getFigure());
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
        if (editPart instanceof SystemBoxSystemCompartmentEditPart) {
            return getPrimaryShape().getFigureSystemBoxContainer();
        }
        return getContentPane();
    }

    /**
	 * @generated
	 */
    protected NodeFigure createNodePlate() {
        DefaultSizeNodeFigure result = new DefaultSizeNodeFigure(40, 40);
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
            layout.setSpacing(5);
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
    protected void setForegroundColor(Color color) {
        if (primaryShape != null) {
            primaryShape.setForegroundColor(color);
        }
    }

    /**
	 * @generated
	 */
    protected void setBackgroundColor(Color color) {
        if (primaryShape != null) {
            primaryShape.setBackgroundColor(color);
        }
    }

    /**
	 * @generated
	 */
    protected void setLineWidth(int width) {
        if (primaryShape instanceof Shape) {
            ((Shape) primaryShape).setLineWidth(width);
        }
    }

    /**
	 * @generated
	 */
    protected void setLineType(int style) {
        if (primaryShape instanceof Shape) {
            ((Shape) primaryShape).setLineStyle(style);
        }
    }

    /**
	 * @generated
	 */
    public EditPart getPrimaryChildEditPart() {
        return getChildBySemanticHint(SeaMonsterVisualIDRegistry.getType(WrappingLabelEditPart.VISUAL_ID));
    }

    /**
	 * @generated
	 */
    public EditPart getTargetEditPart(Request request) {
        if (request instanceof CreateViewAndElementRequest) {
            CreateElementRequestAdapter adapter = ((CreateViewAndElementRequest) request).getViewAndElementDescriptor().getCreateElementRequestAdapter();
            IElementType type = (IElementType) adapter.getAdapter(IElementType.class);
            if (type == SeaMonsterElementTypes.VulnerabilityUseCaseNode_3005) {
                return getChildBySemanticHint(SeaMonsterVisualIDRegistry.getType(SystemBoxSystemCompartmentEditPart.VISUAL_ID));
            }
            if (type == SeaMonsterElementTypes.SecurityUseCaseNode_3006) {
                return getChildBySemanticHint(SeaMonsterVisualIDRegistry.getType(SystemBoxSystemCompartmentEditPart.VISUAL_ID));
            }
            if (type == SeaMonsterElementTypes.NormalUseCaseNode_3007) {
                return getChildBySemanticHint(SeaMonsterVisualIDRegistry.getType(SystemBoxSystemCompartmentEditPart.VISUAL_ID));
            }
            if (type == SeaMonsterElementTypes.MisuseCaseNode_3008) {
                return getChildBySemanticHint(SeaMonsterVisualIDRegistry.getType(SystemBoxSystemCompartmentEditPart.VISUAL_ID));
            }
        }
        return super.getTargetEditPart(request);
    }

    /**
	 * @generated
	 */
    public class SystemBoxFigure extends RectangleFigure {

        /**
		 * @generated
		 */
        private WrappingLabel fFigureSystemTitleLabel;

        /**
		 * @generated
		 */
        private RectangleFigure fFigureSystemBoxContainer;

        /**
		 * @generated
		 */
        public SystemBoxFigure() {
            this.setFill(false);
            this.setLineWidth(2);
            this.setForegroundColor(ColorConstants.black);
            createContents();
        }

        /**
		 * @generated
		 */
        private void createContents() {
            fFigureSystemTitleLabel = new WrappingLabel();
            fFigureSystemTitleLabel.setText("System");
            this.add(fFigureSystemTitleLabel);
            fFigureSystemBoxContainer = new RectangleFigure();
            fFigureSystemBoxContainer.setFill(false);
            fFigureSystemBoxContainer.setOutline(false);
            fFigureSystemBoxContainer.setLineWidth(0);
            this.add(fFigureSystemBoxContainer);
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
        public WrappingLabel getFigureSystemTitleLabel() {
            return fFigureSystemTitleLabel;
        }

        /**
		 * @generated
		 */
        public RectangleFigure getFigureSystemBoxContainer() {
            return fFigureSystemBoxContainer;
        }
    }
}
