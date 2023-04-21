package jfb.examples.gmf.math.diagram.edit.parts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import jfb.examples.gmf.math.Number;
import jfb.examples.gmf.math.Result;
import jfb.examples.gmf.math.diagram.edit.parts.custom.AutomaticComputationHelper;
import jfb.examples.gmf.math.diagram.edit.parts.custom.DefaultSizeNodeFigureWithFixedAnchors;
import jfb.examples.gmf.math.diagram.edit.policies.ResultItemSemanticEditPolicy;
import jfb.examples.gmf.math.diagram.part.MathVisualIDRegistry;
import jfb.examples.gmf.math.diagram.providers.MathElementTypes;
import org.eclipse.draw2d.Ellipse;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EReference;
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
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.swt.graphics.Color;

/**
 * @generated
 */
public class ResultEditPart extends ShapeNodeEditPart {

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
    public ResultEditPart(View view) {
        super(view);
    }

    protected void handleNotificationEvent(Notification notification) {
        super.handleNotificationEvent(notification);
        if (notification.getNotifier() instanceof Result) {
            if (notification.getFeature() instanceof EAttribute) {
                String attrName = ((EAttribute) notification.getFeature()).getName();
                if ("value".equals(attrName)) {
                    AutomaticComputationHelper.numberValueChanged((Number) notification.getNotifier());
                }
            } else if (notification.getFeature() instanceof EReference) {
                String refName = ((EReference) notification.getFeature()).getName();
                if ("operatorOutput".equals(refName)) {
                    AutomaticComputationHelper.operatorOutputToResultConnectionChanged((Result) notification.getNotifier());
                }
            }
        }
    }

    /**
	 * @generated
	 */
    protected void createDefaultEditPolicies() {
        super.createDefaultEditPolicies();
        installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE, new ResultItemSemanticEditPolicy());
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
        ResultFigure figure = new ResultFigure();
        return primaryShape = figure;
    }

    /**
	 * @generated
	 */
    public ResultFigure getPrimaryShape() {
        return (ResultFigure) primaryShape;
    }

    /**
	 * @generated
	 */
    protected boolean addFixedChild(EditPart childEditPart) {
        if (childEditPart instanceof ResultValueEditPart) {
            ((ResultValueEditPart) childEditPart).setLabel(getPrimaryShape().getFigureResultValueFigure());
            return true;
        }
        return false;
    }

    /**
	 * @generated
	 */
    protected boolean removeFixedChild(EditPart childEditPart) {
        if (childEditPart instanceof ResultValueEditPart) {
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
	 * @generated NOT
	 */
    protected NodeFigure createNodePlate() {
        HashMap<String, PrecisionPoint> anchorLocations = new HashMap<String, PrecisionPoint>();
        anchorLocations.put("WEST", new PrecisionPoint(0, 0.5d));
        anchorLocations.put("EAST", new PrecisionPoint(1d, 0.5d));
        anchorLocations.put("NORTH", new PrecisionPoint(0.5d, 0));
        anchorLocations.put("SOUTH", new PrecisionPoint(0.5d, 1d));
        DefaultSizeNodeFigureWithFixedAnchors result = new DefaultSizeNodeFigureWithFixedAnchors(40, 40, anchorLocations);
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
        return getChildBySemanticHint(MathVisualIDRegistry.getType(ResultValueEditPart.VISUAL_ID));
    }

    /**
	 * @generated
	 */
    public List getMARelTypesOnSource() {
        List types = new ArrayList();
        types.add(MathElementTypes.NumberOperatorInputs_4001);
        return types;
    }

    /**
	 * @generated
	 */
    public List getMARelTypesOnSourceAndTarget(IGraphicalEditPart targetEditPart) {
        List types = new ArrayList();
        if (targetEditPart instanceof OperatorInputEditPart) {
            types.add(MathElementTypes.NumberOperatorInputs_4001);
        }
        return types;
    }

    /**
	 * @generated
	 */
    public List getMATypesForTarget(IElementType relationshipType) {
        List types = new ArrayList();
        if (relationshipType == MathElementTypes.NumberOperatorInputs_4001) {
            types.add(MathElementTypes.OperatorInput_3001);
        }
        return types;
    }

    /**
	 * @generated
	 */
    public List getMARelTypesOnTarget() {
        List types = new ArrayList();
        types.add(MathElementTypes.OperatorOutputResult_4003);
        return types;
    }

    /**
	 * @generated
	 */
    public List getMATypesForSource(IElementType relationshipType) {
        List types = new ArrayList();
        if (relationshipType == MathElementTypes.OperatorOutputResult_4003) {
            types.add(MathElementTypes.OperatorOutput_3003);
        }
        return types;
    }

    /**
	 * @generated
	 */
    public class ResultFigure extends Ellipse {

        /**
		 * @generated
		 */
        private WrappingLabel fFigureResultValueFigure;

        /**
		 * @generated
		 */
        public ResultFigure() {
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
            fFigureResultValueFigure = new WrappingLabel();
            fFigureResultValueFigure.setText("<...>");
            this.add(fFigureResultValueFigure);
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
        public WrappingLabel getFigureResultValueFigure() {
            return fFigureResultValueFigure;
        }
    }
}
