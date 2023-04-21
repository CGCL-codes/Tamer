package se.mdh.mrtc.saveccm.diagram.edit.parts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editpolicies.LayoutEditPolicy;
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
import org.eclipse.gmf.runtime.draw2d.ui.figures.ConstrainedToolbarLayout;
import org.eclipse.gmf.runtime.draw2d.ui.figures.WrapLabel;
import org.eclipse.gmf.runtime.gef.ui.figures.DefaultSizeNodeFigure;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;
import org.eclipse.gmf.runtime.notation.View;
import se.mdh.mrtc.saveccm.diagram.edit.policies.OpenDiagramEditPolicySwitch;
import se.mdh.mrtc.saveccm.diagram.edit.policies.SaveccmTextSelectionEditPolicy;
import se.mdh.mrtc.saveccm.diagram.edit.policies.SwitchCanonicalEditPolicy;
import se.mdh.mrtc.saveccm.diagram.edit.policies.SwitchItemSemanticEditPolicy;
import se.mdh.mrtc.saveccm.diagram.part.SaveccmVisualIDRegistry;

/**
 * @generated
 */
public class SwitchEditPart extends AbstractBorderedShapeEditPart {

    /**
	 * @generated
	 */
    public static final int VISUAL_ID = 1002;

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
    public SwitchEditPart(View view) {
        super(view);
    }

    /**
	 * @generated
	 */
    protected void createDefaultEditPolicies() {
        installEditPolicy(EditPolicyRoles.CREATION_ROLE, new CreationEditPolicy());
        super.createDefaultEditPolicies();
        installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE, new SwitchItemSemanticEditPolicy());
        installEditPolicy(EditPolicyRoles.DRAG_DROP_ROLE, new DragDropEditPolicy());
        installEditPolicy(EditPolicyRoles.CANONICAL_ROLE, new SwitchCanonicalEditPolicy());
        installEditPolicy(EditPolicy.LAYOUT_ROLE, createLayoutEditPolicy());
        installEditPolicy(EditPolicyRoles.OPEN_ROLE, new OpenDiagramEditPolicySwitch());
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
        SwitchFigure figure = new SwitchFigure();
        return primaryShape = figure;
    }

    /**
	 * @generated
	 */
    public SwitchFigure getPrimaryShape() {
        return (SwitchFigure) primaryShape;
    }

    /**
	 * @generated
	 */
    protected boolean addFixedChild(EditPart childEditPart) {
        if (childEditPart instanceof SwitchNameEditPart) {
            ((SwitchNameEditPart) childEditPart).setLabel(getPrimaryShape().getFigureSwitchNameLabel());
            return true;
        }
        if (childEditPart instanceof TriggerIn3EditPart) {
            BorderItemLocator locator = new BorderItemLocator(getMainFigure(), PositionConstants.WEST);
            getBorderedFigure().getBorderItemContainer().add(((TriggerIn3EditPart) childEditPart).getFigure(), locator);
            return true;
        }
        if (childEditPart instanceof TriggerOut3EditPart) {
            BorderItemLocator locator = new BorderItemLocator(getMainFigure(), PositionConstants.EAST);
            getBorderedFigure().getBorderItemContainer().add(((TriggerOut3EditPart) childEditPart).getFigure(), locator);
            return true;
        }
        if (childEditPart instanceof DataIn3EditPart) {
            BorderItemLocator locator = new BorderItemLocator(getMainFigure(), PositionConstants.WEST);
            getBorderedFigure().getBorderItemContainer().add(((DataIn3EditPart) childEditPart).getFigure(), locator);
            return true;
        }
        if (childEditPart instanceof DataOut3EditPart) {
            BorderItemLocator locator = new BorderItemLocator(getMainFigure(), PositionConstants.EAST);
            getBorderedFigure().getBorderItemContainer().add(((DataOut3EditPart) childEditPart).getFigure(), locator);
            return true;
        }
        if (childEditPart instanceof CombinedIn3EditPart) {
            BorderItemLocator locator = new BorderItemLocator(getMainFigure(), PositionConstants.WEST);
            getBorderedFigure().getBorderItemContainer().add(((CombinedIn3EditPart) childEditPart).getFigure(), locator);
            return true;
        }
        if (childEditPart instanceof CombinedOut3EditPart) {
            BorderItemLocator locator = new BorderItemLocator(getMainFigure(), PositionConstants.EAST);
            getBorderedFigure().getBorderItemContainer().add(((CombinedOut3EditPart) childEditPart).getFigure(), locator);
            return true;
        }
        return false;
    }

    /**
	 * @generated
	 */
    protected boolean removeFixedChild(EditPart childEditPart) {
        if (childEditPart instanceof TriggerIn3EditPart) {
            getBorderedFigure().getBorderItemContainer().remove(((TriggerIn3EditPart) childEditPart).getFigure());
            return true;
        }
        if (childEditPart instanceof TriggerOut3EditPart) {
            getBorderedFigure().getBorderItemContainer().remove(((TriggerOut3EditPart) childEditPart).getFigure());
            return true;
        }
        if (childEditPart instanceof DataIn3EditPart) {
            getBorderedFigure().getBorderItemContainer().remove(((DataIn3EditPart) childEditPart).getFigure());
            return true;
        }
        if (childEditPart instanceof DataOut3EditPart) {
            getBorderedFigure().getBorderItemContainer().remove(((DataOut3EditPart) childEditPart).getFigure());
            return true;
        }
        if (childEditPart instanceof CombinedIn3EditPart) {
            getBorderedFigure().getBorderItemContainer().remove(((CombinedIn3EditPart) childEditPart).getFigure());
            return true;
        }
        if (childEditPart instanceof CombinedOut3EditPart) {
            getBorderedFigure().getBorderItemContainer().remove(((CombinedOut3EditPart) childEditPart).getFigure());
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
        if (editPart instanceof TriggerIn3EditPart) {
            return getBorderedFigure().getBorderItemContainer();
        }
        if (editPart instanceof TriggerOut3EditPart) {
            return getBorderedFigure().getBorderItemContainer();
        }
        if (editPart instanceof DataIn3EditPart) {
            return getBorderedFigure().getBorderItemContainer();
        }
        if (editPart instanceof DataOut3EditPart) {
            return getBorderedFigure().getBorderItemContainer();
        }
        if (editPart instanceof CombinedIn3EditPart) {
            return getBorderedFigure().getBorderItemContainer();
        }
        if (editPart instanceof CombinedOut3EditPart) {
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
        return getChildBySemanticHint(SaveccmVisualIDRegistry.getType(SwitchNameEditPart.VISUAL_ID));
    }

    /**
	 * @generated
	 */
    public class SwitchFigure extends RectangleFigure {

        /**
		 * @generated
		 */
        private WrapLabel fFigureSwitchNameLabel;

        /**
		 * @generated
		 */
        public SwitchFigure() {
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
            WrapLabel switchLabel0 = new WrapLabel();
            switchLabel0.setText("<<Switch>>");
            this.add(switchLabel0);
            fFigureSwitchNameLabel = new WrapLabel();
            fFigureSwitchNameLabel.setText("");
            this.add(fFigureSwitchNameLabel);
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
        public WrapLabel getFigureSwitchNameLabel() {
            return fFigureSwitchNameLabel;
        }
    }

    public String getElementGuid() {
        return this.elementGuid;
    }
}
