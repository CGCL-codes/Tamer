package se.mdh.mrtc.saveccm.diagram.edit.parts;

import org.eclipse.draw2d.Ellipse;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Polyline;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editpolicies.LayoutEditPolicy;
import org.eclipse.gef.editpolicies.ResizableEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editparts.BorderedBorderItemEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IBorderItemEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.BorderItemSelectionEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.ResizableShapeEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.figures.BorderItemLocator;
import org.eclipse.gmf.runtime.gef.ui.figures.DefaultSizeNodeFigure;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;
import org.eclipse.gmf.runtime.notation.View;
import se.mdh.mrtc.saveccm.diagram.edit.policies.DataOut4ItemSemanticEditPolicy;
import se.mdh.mrtc.saveccm.diagram.part.SaveccmVisualIDRegistry;

/**
 * @generated
 */
public class DataOut4EditPart extends BorderedBorderItemEditPart {

    /**
	 * @generated
	 */
    public static final int VISUAL_ID = 2018;

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
    public DataOut4EditPart(View view) {
        super(view);
    }

    /**
	 * @generated
	 */
    protected void createDefaultEditPolicies() {
        super.createDefaultEditPolicies();
        installEditPolicy(EditPolicy.PRIMARY_DRAG_ROLE, getPrimaryDragEditPolicy());
        installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE, new DataOut4ItemSemanticEditPolicy());
        installEditPolicy(EditPolicy.LAYOUT_ROLE, createLayoutEditPolicy());
    }

    /**
	 * @generated
	 */
    protected LayoutEditPolicy createLayoutEditPolicy() {
        XYLayoutEditPolicy lep = new XYLayoutEditPolicy() {

            protected EditPolicy createChildEditPolicy(EditPart child) {
                if (child instanceof IBorderItemEditPart) {
                    return new BorderItemSelectionEditPolicy();
                }
                EditPolicy result = super.createChildEditPolicy(child);
                if (result == null) {
                    return new ResizableShapeEditPolicy();
                }
                return result;
            }
        };
        return lep;
    }

    /**
	 * @generated
	 */
    protected IFigure createNodeShape() {
        DataOutFigure figure = new DataOutFigure();
        return primaryShape = figure;
    }

    /**
	 * @generated
	 */
    public DataOutFigure getPrimaryShape() {
        return (DataOutFigure) primaryShape;
    }

    /**
	 * @generated
	 */
    protected void addBorderItem(IFigure borderItemContainer, IBorderItemEditPart borderItemEditPart) {
        if (borderItemEditPart instanceof DataOutName4EditPart) {
            BorderItemLocator locator = new BorderItemLocator(getMainFigure(), PositionConstants.SOUTH);
            locator.setBorderItemOffset(new Dimension(-20, -20));
            borderItemContainer.add(borderItemEditPart.getFigure(), locator);
        } else {
            super.addBorderItem(borderItemContainer, borderItemEditPart);
        }
    }

    /**
	 * @generated
	 */
    protected NodeFigure createNodePlate() {
        DefaultSizeNodeFigure result = new DefaultSizeNodeFigure(getMapMode().DPtoLP(41), getMapMode().DPtoLP(17));
        result.getBounds().setSize(result.getPreferredSize());
        return result;
    }

    /**
	 * @generated
	 */
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
        return getChildBySemanticHint(SaveccmVisualIDRegistry.getType(DataOutName4EditPart.VISUAL_ID));
    }

    /**
	 * @generated
	 */
    public class DataOutFigure extends RectangleFigure {

        /**
		 * @generated
		 */
        public DataOutFigure() {
            this.setLayoutManager(new XYLayout());
            this.setFill(false);
            this.setOutline(false);
            this.setLineWidth(0);
            this.setPreferredSize(new Dimension(getMapMode().DPtoLP(41), getMapMode().DPtoLP(17)));
            this.setMaximumSize(new Dimension(getMapMode().DPtoLP(41), getMapMode().DPtoLP(17)));
            this.setMinimumSize(new Dimension(getMapMode().DPtoLP(41), getMapMode().DPtoLP(17)));
            createContents();
        }

        /**
		 * @generated
		 */
        private void createContents() {
            Ellipse dataOuportEllipse0 = new Ellipse();
            dataOuportEllipse0.setLocation(new Point(getMapMode().DPtoLP(32), getMapMode().DPtoLP(0)));
            dataOuportEllipse0.setSize(getMapMode().DPtoLP(16), getMapMode().DPtoLP(16));
            this.add(dataOuportEllipse0);
            Polyline dataOutportFigure0 = new Polyline();
            dataOutportFigure0.addPoint(new Point(getMapMode().DPtoLP(32), getMapMode().DPtoLP(8)));
            dataOutportFigure0.addPoint(new Point(getMapMode().DPtoLP(16), getMapMode().DPtoLP(8)));
            dataOutportFigure0.addPoint(new Point(getMapMode().DPtoLP(16), getMapMode().DPtoLP(0)));
            dataOutportFigure0.addPoint(new Point(getMapMode().DPtoLP(0), getMapMode().DPtoLP(0)));
            dataOutportFigure0.addPoint(new Point(getMapMode().DPtoLP(0), getMapMode().DPtoLP(16)));
            dataOutportFigure0.addPoint(new Point(getMapMode().DPtoLP(16), getMapMode().DPtoLP(16)));
            dataOutportFigure0.addPoint(new Point(getMapMode().DPtoLP(16), getMapMode().DPtoLP(8)));
            this.add(dataOutportFigure0);
        }

        /**
		 * @generated
		 */
        private boolean myUseLocalCoordinates = true;

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
    }
}
