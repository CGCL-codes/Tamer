package com.sf.plctest.testmodel.diagram.edit.parts;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.FlowLayoutEditPolicy;
import org.eclipse.gef.editpolicies.LayoutEditPolicy;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramColorRegistry;
import org.eclipse.gmf.runtime.draw2d.ui.figures.ConstrainedToolbarLayout;
import org.eclipse.gmf.runtime.draw2d.ui.figures.WrappingLabel;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.gef.ui.figures.DefaultSizeNodeFigure;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import com.sf.plctest.testmodel.coveragepackage.Jump;
import com.sf.plctest.testmodel.diagram.edit.policies.JumpItemSemanticEditPolicy;
import com.sf.plctest.testmodel.diagram.part.TestVisualIDRegistry;
import com.sf.plctest.testmodel.diagram.providers.TestElementTypes;

/**
 * @generated
 */
public class JumpEditPart extends ShapeNodeEditPart {

    /**
	 * @generated
	 */
    public static final int VISUAL_ID = 2001;

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
    public JumpEditPart(View view) {
        super(view);
    }

    /**
	 * @generated
	 */
    protected void createDefaultEditPolicies() {
        super.createDefaultEditPolicies();
        installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE, new JumpItemSemanticEditPolicy());
        installEditPolicy(EditPolicy.LAYOUT_ROLE, createLayoutEditPolicy());
    }

    /**
	 * @generated
	 */
    protected LayoutEditPolicy createLayoutEditPolicy() {
        FlowLayoutEditPolicy lep = new FlowLayoutEditPolicy() {

            protected Command createAddCommand(EditPart child, EditPart after) {
                return null;
            }

            protected Command createMoveChildCommand(EditPart child, EditPart after) {
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
        JumpFigure figure = new JumpFigure();
        return primaryShape = figure;
    }

    /**
	 * @generated
	 */
    public JumpFigure getPrimaryShape() {
        return (JumpFigure) primaryShape;
    }

    /**
	 * @generated
	 */
    protected boolean addFixedChild(EditPart childEditPart) {
        if (childEditPart instanceof JumpBlockNameEditPart) {
            ((JumpBlockNameEditPart) childEditPart).setLabel(getPrimaryShape().getFigureJumpSourceLineFigure());
            return true;
        }
        return false;
    }

    /**
	 * @generated
	 */
    protected boolean removeFixedChild(EditPart childEditPart) {
        if (childEditPart instanceof JumpBlockNameEditPart) {
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
	 * @generated NOT
	 */
    protected void setBackgroundColor(Color color) {
        if (primaryShape != null) {
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
        return getChildBySemanticHint(TestVisualIDRegistry.getType(JumpBlockNameEditPart.VISUAL_ID));
    }

    /**
	 * @generated
	 */
    public List getMARelTypesOnSource() {
        List types = new ArrayList();
        types.add(TestElementTypes.DiagramElementNext_4002);
        types.add(TestElementTypes.JumpJumps_to_4003);
        return types;
    }

    /**
	 * @generated
	 */
    public List getMARelTypesOnSourceAndTarget(IGraphicalEditPart targetEditPart) {
        List types = new ArrayList();
        if (targetEditPart instanceof com.sf.plctest.testmodel.diagram.edit.parts.JumpEditPart) {
            types.add(TestElementTypes.DiagramElementNext_4002);
        }
        if (targetEditPart instanceof ConditionEditPart) {
            types.add(TestElementTypes.DiagramElementNext_4002);
        }
        if (targetEditPart instanceof OtherEditPart) {
            types.add(TestElementTypes.DiagramElementNext_4002);
        }
        if (targetEditPart instanceof Condition2EditPart) {
            types.add(TestElementTypes.DiagramElementNext_4002);
        }
        if (targetEditPart instanceof com.sf.plctest.testmodel.diagram.edit.parts.JumpEditPart) {
            types.add(TestElementTypes.JumpJumps_to_4003);
        }
        if (targetEditPart instanceof ConditionEditPart) {
            types.add(TestElementTypes.JumpJumps_to_4003);
        }
        if (targetEditPart instanceof OtherEditPart) {
            types.add(TestElementTypes.JumpJumps_to_4003);
        }
        if (targetEditPart instanceof Condition2EditPart) {
            types.add(TestElementTypes.JumpJumps_to_4003);
        }
        return types;
    }

    /**
	 * @generated
	 */
    public List getMATypesForTarget(IElementType relationshipType) {
        List types = new ArrayList();
        if (relationshipType == TestElementTypes.DiagramElementNext_4002) {
            types.add(TestElementTypes.Jump_2001);
        }
        if (relationshipType == TestElementTypes.DiagramElementNext_4002) {
            types.add(TestElementTypes.Condition_2002);
        }
        if (relationshipType == TestElementTypes.DiagramElementNext_4002) {
            types.add(TestElementTypes.Other_2003);
        }
        if (relationshipType == TestElementTypes.DiagramElementNext_4002) {
            types.add(TestElementTypes.Condition_3001);
        }
        if (relationshipType == TestElementTypes.JumpJumps_to_4003) {
            types.add(TestElementTypes.Jump_2001);
        }
        if (relationshipType == TestElementTypes.JumpJumps_to_4003) {
            types.add(TestElementTypes.Condition_2002);
        }
        if (relationshipType == TestElementTypes.JumpJumps_to_4003) {
            types.add(TestElementTypes.Other_2003);
        }
        if (relationshipType == TestElementTypes.JumpJumps_to_4003) {
            types.add(TestElementTypes.Condition_3001);
        }
        return types;
    }

    /**
	 * @generated
	 */
    public List getMARelTypesOnTarget() {
        List types = new ArrayList();
        types.add(TestElementTypes.Condition_4001);
        types.add(TestElementTypes.DiagramElementNext_4002);
        types.add(TestElementTypes.JumpJumps_to_4003);
        return types;
    }

    /**
	 * @generated
	 */
    public List getMATypesForSource(IElementType relationshipType) {
        List types = new ArrayList();
        if (relationshipType == TestElementTypes.Condition_4001) {
            types.add(TestElementTypes.Condition_2002);
        }
        if (relationshipType == TestElementTypes.Condition_4001) {
            types.add(TestElementTypes.Condition_3001);
        }
        if (relationshipType == TestElementTypes.DiagramElementNext_4002) {
            types.add(TestElementTypes.Jump_2001);
        }
        if (relationshipType == TestElementTypes.DiagramElementNext_4002) {
            types.add(TestElementTypes.Condition_2002);
        }
        if (relationshipType == TestElementTypes.DiagramElementNext_4002) {
            types.add(TestElementTypes.Other_2003);
        }
        if (relationshipType == TestElementTypes.DiagramElementNext_4002) {
            types.add(TestElementTypes.Condition_3001);
        }
        if (relationshipType == TestElementTypes.JumpJumps_to_4003) {
            types.add(TestElementTypes.Jump_2001);
        }
        return types;
    }

    @Override
    protected void handleNotificationEvent(Notification notification) {
        super.handleNotificationEvent(notification);
        if (notification.getNotifier() instanceof Jump) {
            getPrimaryShape().updateFace();
        }
    }

    /**
	 * @generated
	 */
    public class JumpFigure extends RectangleFigure {

        /**
		 * @generated NOT
		 */
        private WrappingLabel fFigureJumpSourceLineFigure;

        /**
		 * @generated NOT
		 */
        public JumpFigure() {
            FlowLayout layoutThis = new FlowLayout();
            layoutThis.setStretchMinorAxis(false);
            layoutThis.setMinorAlignment(FlowLayout.ALIGN_LEFTTOP);
            layoutThis.setMajorAlignment(FlowLayout.ALIGN_LEFTTOP);
            layoutThis.setMajorSpacing(5);
            layoutThis.setMinorSpacing(5);
            layoutThis.setHorizontal(true);
            this.setLayoutManager(layoutThis);
            this.setLineWidth(1);
            updateFace();
            createContents();
        }

        public void updateFace() {
            Jump jump = (Jump) ((Node) JumpEditPart.this.getModel()).getElement();
            Color c = null;
            if (jump.getCoverageInPercent() == 100) c = DiagramColorRegistry.getInstance().getColor(new RGB(192, 255, 192)); else if (jump.getCoverageInPercent() == 50) c = DiagramColorRegistry.getInstance().getColor(new RGB(255, 255, 192)); else c = DiagramColorRegistry.getInstance().getColor(new RGB(255, 192, 192));
            setBackgroundColor(c);
        }

        /**
		 * @generated
		 */
        private void createContents() {
            fFigureJumpSourceLineFigure = new WrappingLabel();
            fFigureJumpSourceLineFigure.setText("<...>");
            this.add(fFigureJumpSourceLineFigure);
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
        public WrappingLabel getFigureJumpSourceLineFigure() {
            return fFigureJumpSourceLineFigure;
        }
    }
}
