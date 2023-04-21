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
import se.mdh.mrtc.saveccm.diagram.edit.policies.ComponentCanonicalEditPolicy;
import se.mdh.mrtc.saveccm.diagram.edit.policies.ComponentItemSemanticEditPolicy;
import se.mdh.mrtc.saveccm.diagram.edit.policies.SaveccmTextSelectionEditPolicy;
import se.mdh.mrtc.saveccm.diagram.part.SaveccmVisualIDRegistry;
import se.mdh.mrtc.saveccm.diagram.providers.SaveccmElementTypes;

/**
 * @generated
 */
public class ComponentEditPart extends AbstractBorderedShapeEditPart {

    /**
	 * @generated
	 */
    public static final int VISUAL_ID = 1006;

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
    public ComponentEditPart(View view) {
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
                        if (type == SaveccmElementTypes.Attribut_2044) {
                            EditPart compartmentEditPart = getChildBySemanticHint(SaveccmVisualIDRegistry.getType(ComponentComponentAttributsCompartmentEditPart.VISUAL_ID));
                            return compartmentEditPart == null ? null : compartmentEditPart.getCommand(request);
                        }
                        if (type == SaveccmElementTypes.Model_2043) {
                            EditPart compartmentEditPart = getChildBySemanticHint(SaveccmVisualIDRegistry.getType(ComponentComponentModelsCompartmentEditPart.VISUAL_ID));
                            return compartmentEditPart == null ? null : compartmentEditPart.getCommand(request);
                        }
                        if (type == SaveccmElementTypes.BindPort_2045) {
                            EditPart compartmentEditPart = getChildBySemanticHint(SaveccmVisualIDRegistry.getType(ComponentComponentBindPortsCompartmentEditPart.VISUAL_ID));
                            return compartmentEditPart == null ? null : compartmentEditPart.getCommand(request);
                        }
                    }
                    return super.getCommand(request);
                }
                return null;
            }
        });
        super.createDefaultEditPolicies();
        installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE, new ComponentItemSemanticEditPolicy());
        installEditPolicy(EditPolicyRoles.DRAG_DROP_ROLE, new DragDropEditPolicy());
        installEditPolicy(EditPolicyRoles.CANONICAL_ROLE, new ComponentCanonicalEditPolicy());
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
        ComponentFigure figure = new ComponentFigure();
        return primaryShape = figure;
    }

    /**
	 * @generated
	 */
    public ComponentFigure getPrimaryShape() {
        return (ComponentFigure) primaryShape;
    }

    /**
	 * @generated
	 */
    protected boolean addFixedChild(EditPart childEditPart) {
        if (childEditPart instanceof ComponentNameEditPart) {
            ((ComponentNameEditPart) childEditPart).setLabel(getPrimaryShape().getFigureComponentNameLabel());
            return true;
        }
        if (childEditPart instanceof ComponentFilenameEditPart) {
            ((ComponentFilenameEditPart) childEditPart).setLabel(getPrimaryShape().getFigureComponentFilenameLabel());
            return true;
        }
        if (childEditPart instanceof ComponentEntryEditPart) {
            ((ComponentEntryEditPart) childEditPart).setLabel(getPrimaryShape().getFigureComponentEntryLabel());
            return true;
        }
        if (childEditPart instanceof ComponentComponentAttributsCompartmentEditPart) {
            IFigure pane = getPrimaryShape().getFigureComponentAttributsFigure();
            setupContentPane(pane);
            pane.add(((ComponentComponentAttributsCompartmentEditPart) childEditPart).getFigure());
            return true;
        }
        if (childEditPart instanceof ComponentComponentModelsCompartmentEditPart) {
            IFigure pane = getPrimaryShape().getFigureComponentModelsFigure();
            setupContentPane(pane);
            pane.add(((ComponentComponentModelsCompartmentEditPart) childEditPart).getFigure());
            return true;
        }
        if (childEditPart instanceof ComponentComponentBindPortsCompartmentEditPart) {
            IFigure pane = getPrimaryShape().getFigureComponentBindPortsFigure();
            setupContentPane(pane);
            pane.add(((ComponentComponentBindPortsCompartmentEditPart) childEditPart).getFigure());
            return true;
        }
        if (childEditPart instanceof TriggerIn7EditPart) {
            BorderItemLocator locator = new BorderItemLocator(getMainFigure(), PositionConstants.WEST);
            getBorderedFigure().getBorderItemContainer().add(((TriggerIn7EditPart) childEditPart).getFigure(), locator);
            return true;
        }
        if (childEditPart instanceof TriggerOut7EditPart) {
            BorderItemLocator locator = new BorderItemLocator(getMainFigure(), PositionConstants.EAST);
            getBorderedFigure().getBorderItemContainer().add(((TriggerOut7EditPart) childEditPart).getFigure(), locator);
            return true;
        }
        if (childEditPart instanceof DataIn7EditPart) {
            BorderItemLocator locator = new BorderItemLocator(getMainFigure(), PositionConstants.WEST);
            getBorderedFigure().getBorderItemContainer().add(((DataIn7EditPart) childEditPart).getFigure(), locator);
            return true;
        }
        if (childEditPart instanceof DataOut7EditPart) {
            BorderItemLocator locator = new BorderItemLocator(getMainFigure(), PositionConstants.EAST);
            getBorderedFigure().getBorderItemContainer().add(((DataOut7EditPart) childEditPart).getFigure(), locator);
            return true;
        }
        if (childEditPart instanceof CombinedIn7EditPart) {
            BorderItemLocator locator = new BorderItemLocator(getMainFigure(), PositionConstants.WEST);
            getBorderedFigure().getBorderItemContainer().add(((CombinedIn7EditPart) childEditPart).getFigure(), locator);
            return true;
        }
        if (childEditPart instanceof CombinedOut7EditPart) {
            BorderItemLocator locator = new BorderItemLocator(getMainFigure(), PositionConstants.EAST);
            getBorderedFigure().getBorderItemContainer().add(((CombinedOut7EditPart) childEditPart).getFigure(), locator);
            return true;
        }
        return false;
    }

    /**
	 * @generated
	 */
    protected boolean removeFixedChild(EditPart childEditPart) {
        if (childEditPart instanceof ComponentComponentAttributsCompartmentEditPart) {
            IFigure pane = getPrimaryShape().getFigureComponentAttributsFigure();
            setupContentPane(pane);
            pane.remove(((ComponentComponentAttributsCompartmentEditPart) childEditPart).getFigure());
            return true;
        }
        if (childEditPart instanceof ComponentComponentModelsCompartmentEditPart) {
            IFigure pane = getPrimaryShape().getFigureComponentModelsFigure();
            setupContentPane(pane);
            pane.remove(((ComponentComponentModelsCompartmentEditPart) childEditPart).getFigure());
            return true;
        }
        if (childEditPart instanceof ComponentComponentBindPortsCompartmentEditPart) {
            IFigure pane = getPrimaryShape().getFigureComponentBindPortsFigure();
            setupContentPane(pane);
            pane.remove(((ComponentComponentBindPortsCompartmentEditPart) childEditPart).getFigure());
            return true;
        }
        if (childEditPart instanceof TriggerIn7EditPart) {
            getBorderedFigure().getBorderItemContainer().remove(((TriggerIn7EditPart) childEditPart).getFigure());
            return true;
        }
        if (childEditPart instanceof TriggerOut7EditPart) {
            getBorderedFigure().getBorderItemContainer().remove(((TriggerOut7EditPart) childEditPart).getFigure());
            return true;
        }
        if (childEditPart instanceof DataIn7EditPart) {
            getBorderedFigure().getBorderItemContainer().remove(((DataIn7EditPart) childEditPart).getFigure());
            return true;
        }
        if (childEditPart instanceof DataOut7EditPart) {
            getBorderedFigure().getBorderItemContainer().remove(((DataOut7EditPart) childEditPart).getFigure());
            return true;
        }
        if (childEditPart instanceof CombinedIn7EditPart) {
            getBorderedFigure().getBorderItemContainer().remove(((CombinedIn7EditPart) childEditPart).getFigure());
            return true;
        }
        if (childEditPart instanceof CombinedOut7EditPart) {
            getBorderedFigure().getBorderItemContainer().remove(((CombinedOut7EditPart) childEditPart).getFigure());
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
        if (editPart instanceof ComponentComponentAttributsCompartmentEditPart) {
            return getPrimaryShape().getFigureComponentAttributsFigure();
        }
        if (editPart instanceof ComponentComponentModelsCompartmentEditPart) {
            return getPrimaryShape().getFigureComponentModelsFigure();
        }
        if (editPart instanceof ComponentComponentBindPortsCompartmentEditPart) {
            return getPrimaryShape().getFigureComponentBindPortsFigure();
        }
        if (editPart instanceof TriggerIn7EditPart) {
            return getBorderedFigure().getBorderItemContainer();
        }
        if (editPart instanceof TriggerOut7EditPart) {
            return getBorderedFigure().getBorderItemContainer();
        }
        if (editPart instanceof DataIn7EditPart) {
            return getBorderedFigure().getBorderItemContainer();
        }
        if (editPart instanceof DataOut7EditPart) {
            return getBorderedFigure().getBorderItemContainer();
        }
        if (editPart instanceof CombinedIn7EditPart) {
            return getBorderedFigure().getBorderItemContainer();
        }
        if (editPart instanceof CombinedOut7EditPart) {
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
        return getChildBySemanticHint(SaveccmVisualIDRegistry.getType(ComponentNameEditPart.VISUAL_ID));
    }

    /**
	 * @generated
	 */
    public class ComponentFigure extends RectangleFigure {

        /**
		 * @generated
		 */
        private WrapLabel fFigureComponentNameLabel;

        /**
		 * @generated
		 */
        private WrapLabel fFigureComponentFilenameLabel;

        /**
		 * @generated
		 */
        private WrapLabel fFigureComponentEntryLabel;

        /**
		 * @generated
		 */
        private RectangleFigure fFigureComponentModelsFigure;

        /**
		 * @generated
		 */
        private RectangleFigure fFigureComponentAttributsFigure;

        /**
		 * @generated
		 */
        private RectangleFigure fFigureComponentBindPortsFigure;

        /**
		 * @generated
		 */
        public ComponentFigure() {
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
            WrapLabel componentLabel0 = new WrapLabel();
            componentLabel0.setText("<<Component>>");
            this.add(componentLabel0);
            fFigureComponentNameLabel = new WrapLabel();
            fFigureComponentNameLabel.setText("");
            this.add(fFigureComponentNameLabel);
            RectangleFigure componentHeadersFigure0 = new RectangleFigure();
            this.add(componentHeadersFigure0);
            ToolbarLayout layoutComponentHeadersFigure0 = new ToolbarLayout();
            layoutComponentHeadersFigure0.setStretchMinorAxis(true);
            layoutComponentHeadersFigure0.setMinorAlignment(ToolbarLayout.ALIGN_CENTER);
            layoutComponentHeadersFigure0.setSpacing(0);
            layoutComponentHeadersFigure0.setVertical(true);
            componentHeadersFigure0.setLayoutManager(layoutComponentHeadersFigure0);
            WrapLabel componentHeadersLabel1 = new WrapLabel();
            componentHeadersLabel1.setText("Headers:");
            componentHeadersFigure0.add(componentHeadersLabel1);
            fFigureComponentFilenameLabel = new WrapLabel();
            fFigureComponentFilenameLabel.setText("");
            componentHeadersFigure0.add(fFigureComponentFilenameLabel);
            fFigureComponentEntryLabel = new WrapLabel();
            fFigureComponentEntryLabel.setText("");
            componentHeadersFigure0.add(fFigureComponentEntryLabel);
            fFigureComponentModelsFigure = new RectangleFigure();
            this.add(fFigureComponentModelsFigure);
            fFigureComponentModelsFigure.setLayoutManager(new StackLayout());
            WrapLabel componentModelsLabel1 = new WrapLabel();
            componentModelsLabel1.setText("Models:");
            fFigureComponentModelsFigure.add(componentModelsLabel1);
            fFigureComponentAttributsFigure = new RectangleFigure();
            this.add(fFigureComponentAttributsFigure);
            fFigureComponentAttributsFigure.setLayoutManager(new StackLayout());
            WrapLabel componentAttributsLabel1 = new WrapLabel();
            componentAttributsLabel1.setText("Attributes:");
            fFigureComponentAttributsFigure.add(componentAttributsLabel1);
            fFigureComponentBindPortsFigure = new RectangleFigure();
            this.add(fFigureComponentBindPortsFigure);
            fFigureComponentBindPortsFigure.setLayoutManager(new StackLayout());
            WrapLabel componentBindPortssLabel1 = new WrapLabel();
            componentBindPortssLabel1.setText("BindPorts:");
            fFigureComponentBindPortsFigure.add(componentBindPortssLabel1);
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
        public WrapLabel getFigureComponentNameLabel() {
            return fFigureComponentNameLabel;
        }

        /**
		 * @generated
		 */
        public WrapLabel getFigureComponentFilenameLabel() {
            return fFigureComponentFilenameLabel;
        }

        /**
		 * @generated
		 */
        public WrapLabel getFigureComponentEntryLabel() {
            return fFigureComponentEntryLabel;
        }

        /**
		 * @generated
		 */
        public RectangleFigure getFigureComponentModelsFigure() {
            return fFigureComponentModelsFigure;
        }

        /**
		 * @generated
		 */
        public RectangleFigure getFigureComponentAttributsFigure() {
            return fFigureComponentAttributsFigure;
        }

        /**
		 * @generated
		 */
        public RectangleFigure getFigureComponentBindPortsFigure() {
            return fFigureComponentBindPortsFigure;
        }
    }

    public String getElementGuid() {
        return this.elementGuid;
    }
}
