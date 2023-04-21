package com.safi.workshop.edit.parts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.LayoutEditPolicy;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IBorderItemEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.BorderItemSelectionEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.CreationEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.DragDropEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.draw2d.ui.figures.ConstrainedToolbarLayout;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;
import org.eclipse.gmf.runtime.notation.View;
import com.safi.asterisk.figures.DefaultToolstepFigure;
import com.safi.workshop.edit.policies.ExecuteScriptCanonicalEditPolicy;
import com.safi.workshop.edit.policies.ExecuteScriptItemSemanticEditPolicy;
import com.safi.workshop.part.AsteriskVisualIDRegistry;

/**
 * @generated NOT
 */
public class ExecuteScriptEditPart extends ToolstepEditPart {

    /**
   * @generated
   */
    public static final int VISUAL_ID = 1037;

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
    public ExecuteScriptEditPart(View view) {
        super(view);
    }

    /**
   * @generated
   */
    @Override
    protected void createDefaultEditPolicies() {
        installEditPolicy(EditPolicyRoles.CREATION_ROLE, new CreationEditPolicy());
        super.createDefaultEditPolicies();
        installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE, new ExecuteScriptItemSemanticEditPolicy());
        installEditPolicy(EditPolicyRoles.DRAG_DROP_ROLE, new DragDropEditPolicy());
        installEditPolicy(EditPolicyRoles.CANONICAL_ROLE, new ExecuteScriptCanonicalEditPolicy());
        installEditPolicy(EditPolicy.LAYOUT_ROLE, createLayoutEditPolicy());
    }

    /**
   * @generated
   */
    protected LayoutEditPolicy createLayoutEditPolicy() {
        LayoutEditPolicy lep = new LayoutEditPolicy() {

            @Override
            protected EditPolicy createChildEditPolicy(EditPart child) {
                if (child instanceof IBorderItemEditPart) {
                    return new BorderItemSelectionEditPolicy();
                }
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
   * @generated
   */
    protected IFigure createNodeShape() {
        return primaryShape = new DefaultToolstepFigure();
    }

    /**
   * @generated
   */
    @Override
    public DefaultToolstepFigure getPrimaryShape() {
        return (DefaultToolstepFigure) primaryShape;
    }

    /**
   * @generated
   */
    @Override
    protected boolean addFixedChild(EditPart childEditPart) {
        if (super.addFixedChild(childEditPart)) return true;
        if (childEditPart instanceof ExecuteScriptNameEditPart) {
            ((ExecuteScriptNameEditPart) childEditPart).setLabel(getPrimaryShape().getToolstepNameLabel());
            return true;
        }
        return false;
    }

    /**
   * @generated
   */
    @Override
    protected boolean removeFixedChild(EditPart childEditPart) {
        if (childEditPart instanceof OutputEditPart) {
            getBorderedFigure().getBorderItemContainer().remove(((OutputEditPart) childEditPart).getFigure());
            return true;
        }
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
        if (editPart instanceof OutputEditPart) {
            return getBorderedFigure().getBorderItemContainer();
        }
        return super.getContentPaneFor(editPart);
    }

    /**
   * Creates figure for this edit part.
   * 
   * Body of this method does not depend on settings in generation model so you may safely
   * remove <i>generated</i> tag and modify it.
   * 
   * @generated
   */
    @Override
    protected NodeFigure createMainFigure() {
        NodeFigure figure = createNodePlate();
        figure.setLayoutManager(new StackLayout());
        IFigure shape = createNodeShape();
        figure.add(shape);
        contentPane = setupContentPane(shape);
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
    @Override
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
        return getChildBySemanticHint(AsteriskVisualIDRegistry.getType(ExecuteScriptNameEditPart.VISUAL_ID));
    }
}
