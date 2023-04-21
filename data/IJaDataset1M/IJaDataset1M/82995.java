package misusecase.diagram.edit.parts;

import java.util.ArrayList;
import java.util.List;
import misusecase.diagram.edit.policies.SecurityUseCaseNodeItemSemanticEditPolicy;
import misusecase.diagram.part.SeaMonsterVisualIDRegistry;
import misusecase.diagram.providers.SeaMonsterElementTypes;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Ellipse;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.LayoutEditPolicy;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
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
public class SecurityUseCaseNodeEditPart extends ShapeNodeEditPart {

    /**
	 * @generated
	 */
    public static final int VISUAL_ID = 2007;

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
    public SecurityUseCaseNodeEditPart(View view) {
        super(view);
    }

    /**
	 * @generated
	 */
    protected void createDefaultEditPolicies() {
        super.createDefaultEditPolicies();
        installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE, new SecurityUseCaseNodeItemSemanticEditPolicy());
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
        SecurityUseCaseNodeFigure figure = new SecurityUseCaseNodeFigure();
        return primaryShape = figure;
    }

    /**
	 * @generated
	 */
    public SecurityUseCaseNodeFigure getPrimaryShape() {
        return (SecurityUseCaseNodeFigure) primaryShape;
    }

    /**
	 * @generated
	 */
    protected boolean addFixedChild(EditPart childEditPart) {
        if (childEditPart instanceof SecurityUseCaseNodeNameEditPart) {
            ((SecurityUseCaseNodeNameEditPart) childEditPart).setLabel(getPrimaryShape().getFigureSecurityUseCaseNameFigure());
            return true;
        }
        return false;
    }

    /**
	 * @generated
	 */
    protected boolean removeFixedChild(EditPart childEditPart) {
        if (childEditPart instanceof SecurityUseCaseNodeNameEditPart) {
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
        return getChildBySemanticHint(SeaMonsterVisualIDRegistry.getType(SecurityUseCaseNodeNameEditPart.VISUAL_ID));
    }

    /**
	 * @generated
	 */
    public List getMARelTypesOnSource() {
        List types = new ArrayList();
        types.add(SeaMonsterElementTypes.UseCaseNodeExtend_4002);
        types.add(SeaMonsterElementTypes.UseCaseNodeInclude_4011);
        types.add(SeaMonsterElementTypes.UseCaseNodeResourceLink_4004);
        types.add(SeaMonsterElementTypes.SecurityUseCaseNodeMitigateMisuse_4009);
        types.add(SeaMonsterElementTypes.SecurityUseCaseNodeMitigateVulnerability_4014);
        return types;
    }

    /**
	 * @generated
	 */
    public List getMARelTypesOnSourceAndTarget(IGraphicalEditPart targetEditPart) {
        List types = new ArrayList();
        if (targetEditPart instanceof MisuseCaseNodeEditPart) {
            types.add(SeaMonsterElementTypes.UseCaseNodeExtend_4002);
        }
        if (targetEditPart instanceof VulnerabilityUseCaseNodeEditPart) {
            types.add(SeaMonsterElementTypes.UseCaseNodeExtend_4002);
        }
        if (targetEditPart instanceof NormalUseCaseNodeEditPart) {
            types.add(SeaMonsterElementTypes.UseCaseNodeExtend_4002);
        }
        if (targetEditPart instanceof misusecase.diagram.edit.parts.SecurityUseCaseNodeEditPart) {
            types.add(SeaMonsterElementTypes.UseCaseNodeExtend_4002);
        }
        if (targetEditPart instanceof VulnerabilityUseCaseNode2EditPart) {
            types.add(SeaMonsterElementTypes.UseCaseNodeExtend_4002);
        }
        if (targetEditPart instanceof SecurityUseCaseNode2EditPart) {
            types.add(SeaMonsterElementTypes.UseCaseNodeExtend_4002);
        }
        if (targetEditPart instanceof NormalUseCaseNode2EditPart) {
            types.add(SeaMonsterElementTypes.UseCaseNodeExtend_4002);
        }
        if (targetEditPart instanceof MisuseCaseNode2EditPart) {
            types.add(SeaMonsterElementTypes.UseCaseNodeExtend_4002);
        }
        if (targetEditPart instanceof MisuseCaseNodeEditPart) {
            types.add(SeaMonsterElementTypes.UseCaseNodeInclude_4011);
        }
        if (targetEditPart instanceof VulnerabilityUseCaseNodeEditPart) {
            types.add(SeaMonsterElementTypes.UseCaseNodeInclude_4011);
        }
        if (targetEditPart instanceof NormalUseCaseNodeEditPart) {
            types.add(SeaMonsterElementTypes.UseCaseNodeInclude_4011);
        }
        if (targetEditPart instanceof misusecase.diagram.edit.parts.SecurityUseCaseNodeEditPart) {
            types.add(SeaMonsterElementTypes.UseCaseNodeInclude_4011);
        }
        if (targetEditPart instanceof VulnerabilityUseCaseNode2EditPart) {
            types.add(SeaMonsterElementTypes.UseCaseNodeInclude_4011);
        }
        if (targetEditPart instanceof SecurityUseCaseNode2EditPart) {
            types.add(SeaMonsterElementTypes.UseCaseNodeInclude_4011);
        }
        if (targetEditPart instanceof NormalUseCaseNode2EditPart) {
            types.add(SeaMonsterElementTypes.UseCaseNodeInclude_4011);
        }
        if (targetEditPart instanceof MisuseCaseNode2EditPart) {
            types.add(SeaMonsterElementTypes.UseCaseNodeInclude_4011);
        }
        if (targetEditPart instanceof SecurityResourceNodeEditPart) {
            types.add(SeaMonsterElementTypes.UseCaseNodeResourceLink_4004);
        }
        if (targetEditPart instanceof MisuseCaseNodeEditPart) {
            types.add(SeaMonsterElementTypes.SecurityUseCaseNodeMitigateMisuse_4009);
        }
        if (targetEditPart instanceof MisuseCaseNode2EditPart) {
            types.add(SeaMonsterElementTypes.SecurityUseCaseNodeMitigateMisuse_4009);
        }
        if (targetEditPart instanceof VulnerabilityUseCaseNodeEditPart) {
            types.add(SeaMonsterElementTypes.SecurityUseCaseNodeMitigateVulnerability_4014);
        }
        if (targetEditPart instanceof VulnerabilityUseCaseNode2EditPart) {
            types.add(SeaMonsterElementTypes.SecurityUseCaseNodeMitigateVulnerability_4014);
        }
        return types;
    }

    /**
	 * @generated
	 */
    public List getMATypesForTarget(IElementType relationshipType) {
        List types = new ArrayList();
        if (relationshipType == SeaMonsterElementTypes.UseCaseNodeExtend_4002) {
            types.add(SeaMonsterElementTypes.MisuseCaseNode_2001);
        }
        if (relationshipType == SeaMonsterElementTypes.UseCaseNodeExtend_4002) {
            types.add(SeaMonsterElementTypes.VulnerabilityUseCaseNode_2003);
        }
        if (relationshipType == SeaMonsterElementTypes.UseCaseNodeExtend_4002) {
            types.add(SeaMonsterElementTypes.NormalUseCaseNode_2005);
        }
        if (relationshipType == SeaMonsterElementTypes.UseCaseNodeExtend_4002) {
            types.add(SeaMonsterElementTypes.SecurityUseCaseNode_2007);
        }
        if (relationshipType == SeaMonsterElementTypes.UseCaseNodeExtend_4002) {
            types.add(SeaMonsterElementTypes.VulnerabilityUseCaseNode_3005);
        }
        if (relationshipType == SeaMonsterElementTypes.UseCaseNodeExtend_4002) {
            types.add(SeaMonsterElementTypes.SecurityUseCaseNode_3006);
        }
        if (relationshipType == SeaMonsterElementTypes.UseCaseNodeExtend_4002) {
            types.add(SeaMonsterElementTypes.NormalUseCaseNode_3007);
        }
        if (relationshipType == SeaMonsterElementTypes.UseCaseNodeExtend_4002) {
            types.add(SeaMonsterElementTypes.MisuseCaseNode_3008);
        }
        if (relationshipType == SeaMonsterElementTypes.UseCaseNodeInclude_4011) {
            types.add(SeaMonsterElementTypes.MisuseCaseNode_2001);
        }
        if (relationshipType == SeaMonsterElementTypes.UseCaseNodeInclude_4011) {
            types.add(SeaMonsterElementTypes.VulnerabilityUseCaseNode_2003);
        }
        if (relationshipType == SeaMonsterElementTypes.UseCaseNodeInclude_4011) {
            types.add(SeaMonsterElementTypes.NormalUseCaseNode_2005);
        }
        if (relationshipType == SeaMonsterElementTypes.UseCaseNodeInclude_4011) {
            types.add(SeaMonsterElementTypes.SecurityUseCaseNode_2007);
        }
        if (relationshipType == SeaMonsterElementTypes.UseCaseNodeInclude_4011) {
            types.add(SeaMonsterElementTypes.VulnerabilityUseCaseNode_3005);
        }
        if (relationshipType == SeaMonsterElementTypes.UseCaseNodeInclude_4011) {
            types.add(SeaMonsterElementTypes.SecurityUseCaseNode_3006);
        }
        if (relationshipType == SeaMonsterElementTypes.UseCaseNodeInclude_4011) {
            types.add(SeaMonsterElementTypes.NormalUseCaseNode_3007);
        }
        if (relationshipType == SeaMonsterElementTypes.UseCaseNodeInclude_4011) {
            types.add(SeaMonsterElementTypes.MisuseCaseNode_3008);
        }
        if (relationshipType == SeaMonsterElementTypes.UseCaseNodeResourceLink_4004) {
            types.add(SeaMonsterElementTypes.SecurityResourceNode_2010);
        }
        if (relationshipType == SeaMonsterElementTypes.SecurityUseCaseNodeMitigateMisuse_4009) {
            types.add(SeaMonsterElementTypes.MisuseCaseNode_2001);
        }
        if (relationshipType == SeaMonsterElementTypes.SecurityUseCaseNodeMitigateMisuse_4009) {
            types.add(SeaMonsterElementTypes.MisuseCaseNode_3008);
        }
        if (relationshipType == SeaMonsterElementTypes.SecurityUseCaseNodeMitigateVulnerability_4014) {
            types.add(SeaMonsterElementTypes.VulnerabilityUseCaseNode_2003);
        }
        if (relationshipType == SeaMonsterElementTypes.SecurityUseCaseNodeMitigateVulnerability_4014) {
            types.add(SeaMonsterElementTypes.VulnerabilityUseCaseNode_3005);
        }
        return types;
    }

    /**
	 * @generated
	 */
    public List getMARelTypesOnTarget() {
        List types = new ArrayList();
        types.add(SeaMonsterElementTypes.UseCaseNodeExtend_4002);
        types.add(SeaMonsterElementTypes.UseCaseNodeInclude_4011);
        types.add(SeaMonsterElementTypes.GoodActorUseSecurity_4007);
        return types;
    }

    /**
	 * @generated
	 */
    public List getMATypesForSource(IElementType relationshipType) {
        List types = new ArrayList();
        if (relationshipType == SeaMonsterElementTypes.UseCaseNodeExtend_4002) {
            types.add(SeaMonsterElementTypes.MisuseCaseNode_2001);
        }
        if (relationshipType == SeaMonsterElementTypes.UseCaseNodeExtend_4002) {
            types.add(SeaMonsterElementTypes.VulnerabilityUseCaseNode_2003);
        }
        if (relationshipType == SeaMonsterElementTypes.UseCaseNodeExtend_4002) {
            types.add(SeaMonsterElementTypes.NormalUseCaseNode_2005);
        }
        if (relationshipType == SeaMonsterElementTypes.UseCaseNodeExtend_4002) {
            types.add(SeaMonsterElementTypes.SecurityUseCaseNode_2007);
        }
        if (relationshipType == SeaMonsterElementTypes.UseCaseNodeExtend_4002) {
            types.add(SeaMonsterElementTypes.VulnerabilityUseCaseNode_3005);
        }
        if (relationshipType == SeaMonsterElementTypes.UseCaseNodeExtend_4002) {
            types.add(SeaMonsterElementTypes.SecurityUseCaseNode_3006);
        }
        if (relationshipType == SeaMonsterElementTypes.UseCaseNodeExtend_4002) {
            types.add(SeaMonsterElementTypes.NormalUseCaseNode_3007);
        }
        if (relationshipType == SeaMonsterElementTypes.UseCaseNodeExtend_4002) {
            types.add(SeaMonsterElementTypes.MisuseCaseNode_3008);
        }
        if (relationshipType == SeaMonsterElementTypes.UseCaseNodeInclude_4011) {
            types.add(SeaMonsterElementTypes.MisuseCaseNode_2001);
        }
        if (relationshipType == SeaMonsterElementTypes.UseCaseNodeInclude_4011) {
            types.add(SeaMonsterElementTypes.VulnerabilityUseCaseNode_2003);
        }
        if (relationshipType == SeaMonsterElementTypes.UseCaseNodeInclude_4011) {
            types.add(SeaMonsterElementTypes.NormalUseCaseNode_2005);
        }
        if (relationshipType == SeaMonsterElementTypes.UseCaseNodeInclude_4011) {
            types.add(SeaMonsterElementTypes.SecurityUseCaseNode_2007);
        }
        if (relationshipType == SeaMonsterElementTypes.UseCaseNodeInclude_4011) {
            types.add(SeaMonsterElementTypes.VulnerabilityUseCaseNode_3005);
        }
        if (relationshipType == SeaMonsterElementTypes.UseCaseNodeInclude_4011) {
            types.add(SeaMonsterElementTypes.SecurityUseCaseNode_3006);
        }
        if (relationshipType == SeaMonsterElementTypes.UseCaseNodeInclude_4011) {
            types.add(SeaMonsterElementTypes.NormalUseCaseNode_3007);
        }
        if (relationshipType == SeaMonsterElementTypes.UseCaseNodeInclude_4011) {
            types.add(SeaMonsterElementTypes.MisuseCaseNode_3008);
        }
        if (relationshipType == SeaMonsterElementTypes.GoodActorUseSecurity_4007) {
            types.add(SeaMonsterElementTypes.GoodActor_2008);
        }
        return types;
    }

    /**
	 * @generated
	 */
    public class SecurityUseCaseNodeFigure extends Ellipse {

        /**
		 * @generated
		 */
        private WrappingLabel fFigureSecurityUseCaseNameFigure;

        /**
		 * @generated
		 */
        public SecurityUseCaseNodeFigure() {
            this.setLineWidth(2);
            this.setLineStyle(Graphics.LINE_DOT);
            this.setForegroundColor(ColorConstants.black);
            this.setBorder(new MarginBorder(getMapMode().DPtoLP(10), getMapMode().DPtoLP(10), getMapMode().DPtoLP(10), getMapMode().DPtoLP(10)));
            createContents();
        }

        /**
		 * @generated NOT
		 */
        private void createContents() {
            fFigureSecurityUseCaseNameFigure = new WrappingLabel();
            fFigureSecurityUseCaseNameFigure.setText("<security usecase>");
            fFigureSecurityUseCaseNameFigure.setTextWrap(true);
            fFigureSecurityUseCaseNameFigure.setAlignment(PositionConstants.CENTER);
            this.add(fFigureSecurityUseCaseNameFigure);
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
        public WrappingLabel getFigureSecurityUseCaseNameFigure() {
            return fFigureSecurityUseCaseNameFigure;
        }
    }
}
