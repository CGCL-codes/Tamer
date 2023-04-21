package com.safi.workshop.edit.parts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.LayoutEditPolicy;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.gef.editpolicies.ResizableEditPolicy;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramColorRegistry;
import org.eclipse.gmf.runtime.draw2d.ui.figures.ConstrainedToolbarLayout;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.View;
import com.safi.asterisk.figures.OutputFigure;
import com.safi.core.actionstep.GetColMapping;
import com.safi.workshop.edit.policies.GetColMappingItemSemanticEditPolicy;
import com.safi.workshop.part.AsteriskVisualIDRegistry;

/**
 * @generated
 */
public class GetColMappingEditPart extends ShapeNodeEditPart {

    /**
   * @generated
   */
    public static final int VISUAL_ID = 2004;

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
    public GetColMappingEditPart(View view) {
        super(view);
    }

    @Override
    protected void handleNotificationEvent(org.eclipse.emf.common.notify.Notification notification) {
        Object feature = notification.getFeature();
        if (NotationPackage.eINSTANCE.getFillStyle_FillColor().equals(feature)) {
            Integer c = (Integer) notification.getNewValue();
            setBackgroundColor(DiagramColorRegistry.getInstance().getColor(c));
        } else if (NotationPackage.eINSTANCE.getLineStyle_LineColor().equals(feature)) {
            Integer c = (Integer) notification.getNewValue();
            setForegroundColor(DiagramColorRegistry.getInstance().getColor(c));
        }
        super.handleNotificationEvent(notification);
    }

    /**
   * @generated NOT
   */
    @Override
    protected void createDefaultEditPolicies() {
        super.createDefaultEditPolicies();
        installEditPolicy(EditPolicy.PRIMARY_DRAG_ROLE, getPrimaryDragEditPolicy());
        installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE, new GetColMappingItemSemanticEditPolicy());
        removeEditPolicy("NoteAttachmentReorient");
    }

    @Override
    public boolean canAttachNote() {
        return false;
    }

    public GetColMapping getGetColMapping() {
        return (GetColMapping) resolveSemanticElement();
    }

    /**
   * @generated
   */
    protected LayoutEditPolicy createLayoutEditPolicy() {
        LayoutEditPolicy lep = new LayoutEditPolicy() {

            @Override
            protected EditPolicy createChildEditPolicy(EditPart child) {
                EditPolicy result = child.getEditPolicy(EditPolicy.PRIMARY_DRAG_ROLE);
                if (result == null) {
                    result = new NonResizableEditPolicy();
                }
                return result;
            }

            @Override
            protected Command getMoveChildrenCommand(Request request) {
                return null;
            }

            @Override
            protected Command getCreateCommand(CreateRequest request) {
                return null;
            }
        };
        return lep;
    }

    /**
   * @generated NOT
   */
    protected IFigure createNodeShape() {
        primaryShape = new OutputFigure();
        ((OutputFigure) primaryShape).setCapped(false);
        return primaryShape;
    }

    /**
   * @generated
   */
    public OutputFigure getPrimaryShape() {
        return (OutputFigure) primaryShape;
    }

    /**
   * @generated
   */
    protected boolean addFixedChild(EditPart childEditPart) {
        if (childEditPart instanceof GetColMappingGetAsDatatypeEditPart) {
            ((GetColMappingGetAsDatatypeEditPart) childEditPart).setLabel(getPrimaryShape().getFigureOutputNameFigure());
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
    @Override
    protected void addChildVisual(EditPart childEditPart, int index) {
        if (addFixedChild(childEditPart)) {
            return;
        }
        super.addChildVisual(childEditPart, -1);
    }

    /**
   * @generated
   */
    @Override
    protected void removeChildVisual(EditPart childEditPart) {
        if (removeFixedChild(childEditPart)) {
            return;
        }
        super.removeChildVisual(childEditPart);
    }

    /**
   * @generated
   */
    @Override
    protected IFigure getContentPaneFor(IGraphicalEditPart editPart) {
        return super.getContentPaneFor(editPart);
    }

    /**
   * @generated NOT
   */
    protected NodeFigure createNodePlate() {
        return (NodeFigure) createNodeShape();
    }

    /**
   * @generated
   */
    @Override
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
   * Body of this method does not depend on settings in generation model so you may safely
   * remove <i>generated</i> tag and modify it.
   * 
   * @generated NOT
   */
    @Override
    protected NodeFigure createNodeFigure() {
        NodeFigure figure = createNodePlate();
        Dimension d = new Dimension(80, 17);
        figure.setPreferredSize(d);
        figure.setSize(d);
        contentPane = figure;
        return figure;
    }

    /**
   * Default implementation treats passed figure as content pane. Respects layout one may
   * have set for generated figure.
   * 
   * @param nodeShape
   *          instance of generated figure class
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
    @Override
    public IFigure getContentPane() {
        if (contentPane != null) {
            return contentPane;
        }
        return super.getContentPane();
    }

    /**
   * @generated
   */
    @Override
    public EditPart getPrimaryChildEditPart() {
        return getChildBySemanticHint(AsteriskVisualIDRegistry.getType(GetColMappingGetAsDatatypeEditPart.VISUAL_ID));
    }
}
