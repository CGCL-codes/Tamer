package mindmap.diagram.edit.parts;

import java.util.ArrayList;
import java.util.List;
import mindmap.diagram.edit.policies.TopicItemSemanticEditPolicy;
import mindmap.diagram.part.MindmapVisualIDRegistry;
import mindmap.diagram.providers.MindmapElementTypes;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.FlowLayoutEditPolicy;
import org.eclipse.gef.editpolicies.LayoutEditPolicy;
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
public class TopicEditPart extends ShapeNodeEditPart {

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
    public TopicEditPart(View view) {
        super(view);
    }

    /**
	 * @generated
	 */
    protected void createDefaultEditPolicies() {
        super.createDefaultEditPolicies();
        installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE, new TopicItemSemanticEditPolicy());
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
        TopicFigure figure = new TopicFigure();
        return primaryShape = figure;
    }

    /**
	 * @generated
	 */
    public TopicFigure getPrimaryShape() {
        return (TopicFigure) primaryShape;
    }

    /**
	 * @generated
	 */
    protected boolean addFixedChild(EditPart childEditPart) {
        if (childEditPart instanceof TopicNameEditPart) {
            ((TopicNameEditPart) childEditPart).setLabel(getPrimaryShape().getFigureTopicNameFigure());
            return true;
        }
        return false;
    }

    /**
	 * @generated
	 */
    protected boolean removeFixedChild(EditPart childEditPart) {
        if (childEditPart instanceof TopicNameEditPart) {
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
        return getChildBySemanticHint(MindmapVisualIDRegistry.getType(TopicNameEditPart.VISUAL_ID));
    }

    /**
	 * @generated
	 */
    public List getMARelTypesOnSource() {
        List types = new ArrayList();
        types.add(MindmapElementTypes.TopicSubtopics_4001);
        return types;
    }

    /**
	 * @generated
	 */
    public List getMARelTypesOnSourceAndTarget(IGraphicalEditPart targetEditPart) {
        List types = new ArrayList();
        if (targetEditPart instanceof mindmap.diagram.edit.parts.TopicEditPart) {
            types.add(MindmapElementTypes.TopicSubtopics_4001);
        }
        return types;
    }

    /**
	 * @generated
	 */
    public List getMATypesForTarget(IElementType relationshipType) {
        List types = new ArrayList();
        if (relationshipType == MindmapElementTypes.TopicSubtopics_4001) {
            types.add(MindmapElementTypes.Topic_2001);
        }
        return types;
    }

    /**
	 * @generated
	 */
    public List getMARelTypesOnTarget() {
        List types = new ArrayList();
        types.add(MindmapElementTypes.TopicSubtopics_4001);
        return types;
    }

    /**
	 * @generated
	 */
    public List getMATypesForSource(IElementType relationshipType) {
        List types = new ArrayList();
        if (relationshipType == MindmapElementTypes.TopicSubtopics_4001) {
            types.add(MindmapElementTypes.Topic_2001);
        }
        return types;
    }

    /**
	 * @generated
	 */
    public class TopicFigure extends RectangleFigure {

        /**
		 * @generated
		 */
        private WrappingLabel fFigureTopicNameFigure;

        /**
		 * @generated
		 */
        public TopicFigure() {
            FlowLayout layoutThis = new FlowLayout();
            layoutThis.setStretchMinorAxis(false);
            layoutThis.setMinorAlignment(FlowLayout.ALIGN_LEFTTOP);
            layoutThis.setMajorAlignment(FlowLayout.ALIGN_LEFTTOP);
            layoutThis.setMajorSpacing(5);
            layoutThis.setMinorSpacing(5);
            layoutThis.setHorizontal(true);
            this.setLayoutManager(layoutThis);
            this.setLineWidth(1);
            createContents();
        }

        /**
		 * @generated
		 */
        private void createContents() {
            fFigureTopicNameFigure = new WrappingLabel();
            fFigureTopicNameFigure.setText("<...>");
            this.add(fFigureTopicNameFigure);
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
        public WrappingLabel getFigureTopicNameFigure() {
            return fFigureTopicNameFigure;
        }
    }
}
