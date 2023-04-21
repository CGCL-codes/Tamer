package se.mdh.mrtc.saveccm.diagram.edit.parts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.LayoutEditPolicy;
import org.eclipse.gmf.runtime.diagram.core.edithelpers.CreateElementRequestAdapter;
import org.eclipse.gmf.runtime.diagram.ui.editparts.AbstractBorderedShapeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IBorderItemEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ITextAwareEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.BorderItemSelectionEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.ConstrainedToolbarLayoutEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.CreationEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.DragDropEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.figures.BorderItemLocator;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewAndElementRequest;
import org.eclipse.gmf.runtime.draw2d.ui.figures.ConstrainedToolbarLayout;
import org.eclipse.gmf.runtime.draw2d.ui.figures.WrapLabel;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.gef.ui.figures.DefaultSizeNodeFigure;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;
import org.eclipse.gmf.runtime.notation.View;
import se.mdh.mrtc.saveccm.diagram.edit.policies.ClockCanonicalEditPolicy;
import se.mdh.mrtc.saveccm.diagram.edit.policies.ClockItemSemanticEditPolicy;
import se.mdh.mrtc.saveccm.diagram.edit.policies.SaveccmTextSelectionEditPolicy;
import se.mdh.mrtc.saveccm.diagram.part.SaveccmVisualIDRegistry;
import se.mdh.mrtc.saveccm.diagram.providers.SaveccmElementTypes;

/**
 * @generated
 */
public class ClockEditPart extends AbstractBorderedShapeEditPart {

    /**
	 * @generated
	 */
    public static final int VISUAL_ID = 1003;

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
    public ClockEditPart(View view) {
        super(view);
    }

    /**
	 * @generated
	 */
    protected void createDefaultEditPolicies() {
        installEditPolicy(EditPolicyRoles.CREATION_ROLE, new CreationEditPolicy() {

            public Command getCommand(Request request) {
                if (understandsRequest(request)) {
                    if (request instanceof CreateViewAndElementRequest) {
                        CreateElementRequestAdapter adapter = ((CreateViewAndElementRequest) request).getViewAndElementDescriptor().getCreateElementRequestAdapter();
                        IElementType type = (IElementType) adapter.getAdapter(IElementType.class);
                        if (type == SaveccmElementTypes.Attribut_2022) {
                            EditPart compartmentEditPart = getChildBySemanticHint(SaveccmVisualIDRegistry.getType(ClockClockAttributsCompartmentEditPart.VISUAL_ID));
                            return compartmentEditPart == null ? null : compartmentEditPart.getCommand(request);
                        }
                        if (type == SaveccmElementTypes.Model_2021) {
                            EditPart compartmentEditPart = getChildBySemanticHint(SaveccmVisualIDRegistry.getType(ClockClockModelsCompartmentEditPart.VISUAL_ID));
                            return compartmentEditPart == null ? null : compartmentEditPart.getCommand(request);
                        }
                    }
                    return super.getCommand(request);
                }
                return null;
            }
        });
        super.createDefaultEditPolicies();
        installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE, new ClockItemSemanticEditPolicy());
        installEditPolicy(EditPolicyRoles.DRAG_DROP_ROLE, new DragDropEditPolicy());
        installEditPolicy(EditPolicyRoles.CANONICAL_ROLE, new ClockCanonicalEditPolicy());
        installEditPolicy(EditPolicy.LAYOUT_ROLE, createLayoutEditPolicy());
    }

    /**
	 * @generated
	 */
    protected LayoutEditPolicy createLayoutEditPolicy() {
        ConstrainedToolbarLayoutEditPolicy lep = new ConstrainedToolbarLayoutEditPolicy() {

            protected EditPolicy createChildEditPolicy(EditPart child) {
                if (child instanceof IBorderItemEditPart) {
                    return new BorderItemSelectionEditPolicy();
                }
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
        ClockFigure figure = new ClockFigure();
        return primaryShape = figure;
    }

    /**
	 * @generated
	 */
    public ClockFigure getPrimaryShape() {
        return (ClockFigure) primaryShape;
    }

    /**
	 * @generated
	 */
    protected boolean addFixedChild(EditPart childEditPart) {
        if (childEditPart instanceof ClockNameEditPart) {
            ((ClockNameEditPart) childEditPart).setLabel(getPrimaryShape().getFigureClockNameLabel());
            return true;
        }
        if (childEditPart instanceof ClockPeriodEditPart) {
            ((ClockPeriodEditPart) childEditPart).setLabel(getPrimaryShape().getFigureClockPeriodLabel());
            return true;
        }
        if (childEditPart instanceof ClockJitterEditPart) {
            ((ClockJitterEditPart) childEditPart).setLabel(getPrimaryShape().getFigureClockJitterLabel());
            return true;
        }
        if (childEditPart instanceof ClockClockAttributsCompartmentEditPart) {
            IFigure pane = getPrimaryShape().getFigureClockAttributsFigure();
            setupContentPane(pane);
            pane.add(((ClockClockAttributsCompartmentEditPart) childEditPart).getFigure());
            return true;
        }
        if (childEditPart instanceof ClockClockModelsCompartmentEditPart) {
            IFigure pane = getPrimaryShape().getFigureClockModelsFigure();
            setupContentPane(pane);
            pane.add(((ClockClockModelsCompartmentEditPart) childEditPart).getFigure());
            return true;
        }
        if (childEditPart instanceof TriggerIn4EditPart) {
            BorderItemLocator locator = new BorderItemLocator(getMainFigure(), PositionConstants.WEST);
            getBorderedFigure().getBorderItemContainer().add(((TriggerIn4EditPart) childEditPart).getFigure(), locator);
            return true;
        }
        if (childEditPart instanceof TriggerOut4EditPart) {
            BorderItemLocator locator = new BorderItemLocator(getMainFigure(), PositionConstants.EAST);
            getBorderedFigure().getBorderItemContainer().add(((TriggerOut4EditPart) childEditPart).getFigure(), locator);
            return true;
        }
        if (childEditPart instanceof DataIn4EditPart) {
            BorderItemLocator locator = new BorderItemLocator(getMainFigure(), PositionConstants.WEST);
            getBorderedFigure().getBorderItemContainer().add(((DataIn4EditPart) childEditPart).getFigure(), locator);
            return true;
        }
        if (childEditPart instanceof DataOut4EditPart) {
            BorderItemLocator locator = new BorderItemLocator(getMainFigure(), PositionConstants.EAST);
            getBorderedFigure().getBorderItemContainer().add(((DataOut4EditPart) childEditPart).getFigure(), locator);
            return true;
        }
        if (childEditPart instanceof CombinedIn4EditPart) {
            BorderItemLocator locator = new BorderItemLocator(getMainFigure(), PositionConstants.WEST);
            getBorderedFigure().getBorderItemContainer().add(((CombinedIn4EditPart) childEditPart).getFigure(), locator);
            return true;
        }
        if (childEditPart instanceof CombinedOut4EditPart) {
            BorderItemLocator locator = new BorderItemLocator(getMainFigure(), PositionConstants.EAST);
            getBorderedFigure().getBorderItemContainer().add(((CombinedOut4EditPart) childEditPart).getFigure(), locator);
            return true;
        }
        return false;
    }

    /**
	 * @generated
	 */
    protected boolean removeFixedChild(EditPart childEditPart) {
        if (childEditPart instanceof ClockClockAttributsCompartmentEditPart) {
            IFigure pane = getPrimaryShape().getFigureClockAttributsFigure();
            setupContentPane(pane);
            pane.remove(((ClockClockAttributsCompartmentEditPart) childEditPart).getFigure());
            return true;
        }
        if (childEditPart instanceof ClockClockModelsCompartmentEditPart) {
            IFigure pane = getPrimaryShape().getFigureClockModelsFigure();
            setupContentPane(pane);
            pane.remove(((ClockClockModelsCompartmentEditPart) childEditPart).getFigure());
            return true;
        }
        if (childEditPart instanceof TriggerIn4EditPart) {
            getBorderedFigure().getBorderItemContainer().remove(((TriggerIn4EditPart) childEditPart).getFigure());
            return true;
        }
        if (childEditPart instanceof TriggerOut4EditPart) {
            getBorderedFigure().getBorderItemContainer().remove(((TriggerOut4EditPart) childEditPart).getFigure());
            return true;
        }
        if (childEditPart instanceof DataIn4EditPart) {
            getBorderedFigure().getBorderItemContainer().remove(((DataIn4EditPart) childEditPart).getFigure());
            return true;
        }
        if (childEditPart instanceof DataOut4EditPart) {
            getBorderedFigure().getBorderItemContainer().remove(((DataOut4EditPart) childEditPart).getFigure());
            return true;
        }
        if (childEditPart instanceof CombinedIn4EditPart) {
            getBorderedFigure().getBorderItemContainer().remove(((CombinedIn4EditPart) childEditPart).getFigure());
            return true;
        }
        if (childEditPart instanceof CombinedOut4EditPart) {
            getBorderedFigure().getBorderItemContainer().remove(((CombinedOut4EditPart) childEditPart).getFigure());
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
        if (editPart instanceof ClockClockAttributsCompartmentEditPart) {
            return getPrimaryShape().getFigureClockAttributsFigure();
        }
        if (editPart instanceof ClockClockModelsCompartmentEditPart) {
            return getPrimaryShape().getFigureClockModelsFigure();
        }
        if (editPart instanceof TriggerIn4EditPart) {
            return getBorderedFigure().getBorderItemContainer();
        }
        if (editPart instanceof TriggerOut4EditPart) {
            return getBorderedFigure().getBorderItemContainer();
        }
        if (editPart instanceof DataIn4EditPart) {
            return getBorderedFigure().getBorderItemContainer();
        }
        if (editPart instanceof DataOut4EditPart) {
            return getBorderedFigure().getBorderItemContainer();
        }
        if (editPart instanceof CombinedIn4EditPart) {
            return getBorderedFigure().getBorderItemContainer();
        }
        if (editPart instanceof CombinedOut4EditPart) {
            return getBorderedFigure().getBorderItemContainer();
        }
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
	 * Creates figure for this edit part.
	 * 
	 * Body of this method does not depend on settings in generation model
	 * so you may safely remove <i>generated</i> tag and modify it.
	 * 
	 * @generated
	 */
    protected NodeFigure createMainFigure() {
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
        return getChildBySemanticHint(SaveccmVisualIDRegistry.getType(ClockNameEditPart.VISUAL_ID));
    }

    /**
	 * @generated
	 */
    public class ClockFigure extends RectangleFigure {

        /**
		 * @generated
		 */
        private WrapLabel fFigureClockNameLabel;

        /**
		 * @generated
		 */
        private WrapLabel fFigureClockPeriodLabel;

        /**
		 * @generated
		 */
        private WrapLabel fFigureClockJitterLabel;

        /**
		 * @generated
		 */
        private RectangleFigure fFigureClockModelsFigure;

        /**
		 * @generated
		 */
        private RectangleFigure fFigureClockAttributsFigure;

        /**
		 * @generated
		 */
        public ClockFigure() {
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
            WrapLabel clockLabel0 = new WrapLabel();
            clockLabel0.setText("<<Clock>>");
            this.add(clockLabel0);
            fFigureClockNameLabel = new WrapLabel();
            fFigureClockNameLabel.setText("");
            this.add(fFigureClockNameLabel);
            RectangleFigure clockHeadersFigure0 = new RectangleFigure();
            this.add(clockHeadersFigure0);
            ToolbarLayout layoutClockHeadersFigure0 = new ToolbarLayout();
            layoutClockHeadersFigure0.setStretchMinorAxis(true);
            layoutClockHeadersFigure0.setMinorAlignment(ToolbarLayout.ALIGN_CENTER);
            layoutClockHeadersFigure0.setSpacing(0);
            layoutClockHeadersFigure0.setVertical(true);
            clockHeadersFigure0.setLayoutManager(layoutClockHeadersFigure0);
            WrapLabel clockHeadersLabel1 = new WrapLabel();
            clockHeadersLabel1.setText("Headers:");
            clockHeadersFigure0.add(clockHeadersLabel1);
            fFigureClockPeriodLabel = new WrapLabel();
            fFigureClockPeriodLabel.setText("");
            clockHeadersFigure0.add(fFigureClockPeriodLabel);
            fFigureClockJitterLabel = new WrapLabel();
            fFigureClockJitterLabel.setText("");
            clockHeadersFigure0.add(fFigureClockJitterLabel);
            fFigureClockModelsFigure = new RectangleFigure();
            this.add(fFigureClockModelsFigure);
            fFigureClockModelsFigure.setLayoutManager(new StackLayout());
            WrapLabel clockModelsLabel1 = new WrapLabel();
            clockModelsLabel1.setText("Models:");
            fFigureClockModelsFigure.add(clockModelsLabel1);
            fFigureClockAttributsFigure = new RectangleFigure();
            this.add(fFigureClockAttributsFigure);
            fFigureClockAttributsFigure.setLayoutManager(new StackLayout());
            WrapLabel clockAttributsLabel1 = new WrapLabel();
            clockAttributsLabel1.setText("Attributes:");
            fFigureClockAttributsFigure.add(clockAttributsLabel1);
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
        public WrapLabel getFigureClockNameLabel() {
            return fFigureClockNameLabel;
        }

        /**
		 * @generated
		 */
        public WrapLabel getFigureClockPeriodLabel() {
            return fFigureClockPeriodLabel;
        }

        /**
		 * @generated
		 */
        public WrapLabel getFigureClockJitterLabel() {
            return fFigureClockJitterLabel;
        }

        /**
		 * @generated
		 */
        public RectangleFigure getFigureClockModelsFigure() {
            return fFigureClockModelsFigure;
        }

        /**
		 * @generated
		 */
        public RectangleFigure getFigureClockAttributsFigure() {
            return fFigureClockAttributsFigure;
        }
    }
}
