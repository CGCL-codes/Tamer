package odm.diagram.edit.parts;

import odm.diagram.edit.policies.ObjectPropertyCanonicalEditPolicy;
import odm.diagram.edit.policies.ObjectPropertyGraphicalNodeEditPolicy;
import odm.diagram.edit.policies.ObjectPropertyItemSemanticEditPolicy;
import odm.diagram.part.OdmVisualIDRegistry;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.LayoutEditPolicy;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ShapeNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.draw2d.ui.figures.ConstrainedToolbarLayout;
import org.eclipse.gmf.runtime.gef.ui.figures.DefaultSizeNodeFigure;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;
import org.eclipse.gmf.runtime.notation.View;

/**
 * @generated
 */
public class ObjectPropertyEditPart extends ShapeNodeEditPart {

    /**
	 * @generated
	 */
    public static final int VISUAL_ID = 2003;

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
    public ObjectPropertyEditPart(View view) {
        super(view);
    }

    protected void createDefaultEditPolicies() {
        super.createDefaultEditPolicies();
        installEditPolicy(EditPolicyRoles.SEMANTIC_ROLE, new ObjectPropertyItemSemanticEditPolicy());
        installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new ObjectPropertyGraphicalNodeEditPolicy());
        installEditPolicy(EditPolicyRoles.CANONICAL_ROLE, new ObjectPropertyCanonicalEditPolicy());
        installEditPolicy(EditPolicy.LAYOUT_ROLE, createLayoutEditPolicy());
        removeEditPolicy(EditPolicyRoles.POPUPBAR_ROLE);
        removeEditPolicy(EditPolicyRoles.CONNECTION_HANDLES_ROLE);
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
        RhombusFigure figure = new RhombusFigure();
        return primaryShape = figure;
    }

    /**
	 * @generated
	 */
    public RhombusFigure getPrimaryShape() {
        return (RhombusFigure) primaryShape;
    }

    /**
	 * @generated
	 */
    protected NodeFigure createNodePlate() {
        return new DefaultSizeNodeFigure(getMapMode().DPtoLP(40), getMapMode().DPtoLP(40));
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
        return getChildBySemanticHint(OdmVisualIDRegistry.getType(ObjectPropertyTagEditPart.VISUAL_ID));
    }

    public class RhombusPolygon extends org.eclipse.draw2d.Polygon {

        private int labelIndent = 17;

        private org.eclipse.draw2d.geometry.Point p1 = new org.eclipse.draw2d.geometry.Point(0, 0);

        private org.eclipse.draw2d.geometry.Point p2 = new org.eclipse.draw2d.geometry.Point(0, 0);

        private org.eclipse.draw2d.geometry.Point p3 = new org.eclipse.draw2d.geometry.Point(0, 0);

        private org.eclipse.draw2d.geometry.Point p4 = new org.eclipse.draw2d.geometry.Point(0, 0);

        public RhombusPolygon() {
            addPoint(p1);
            addPoint(p2);
            addPoint(p3);
            addPoint(p4);
            addPoint(p1);
        }

        private Rectangle lastUsedDimension;

        @Override
        public void paintFigure(Graphics graphics) {
            Rectangle r = getParent().getParent().getBounds();
            if (lastUsedDimension == null || !r.equals(lastUsedDimension)) {
                int x = r.x;
                int y = r.y;
                int w = r.width - Math.max(1, lineWidth);
                int h = r.height - Math.max(1, lineWidth);
                p1.x = x + (w / 2);
                p1.y = y + 0 + labelIndent;
                p2.x = x + w;
                p2.y = y + (h / 2) + (labelIndent / 2);
                p3.x = x + (w / 2);
                p3.y = y + h;
                p4.x = x + 0;
                p4.y = y + (h / 2) + (labelIndent / 2);
                erase();
                getPoints().setPoint(p1, 0);
                getPoints().setPoint(p2, 1);
                getPoints().setPoint(p3, 2);
                getPoints().setPoint(p4, 3);
                getPoints().setPoint(p1, 4);
                bounds = null;
                lastUsedDimension = new Rectangle(r);
                repaint();
            }
            super.paintFigure(graphics);
        }
    }

    /**
	 * @generated
	 */
    public class RhombusFigure extends org.eclipse.draw2d.Layer {

        private org.eclipse.draw2d.Polygon fig_1;

        /**
		 * @generated
		 */
        public RhombusFigure() {
            this.setPreferredSize(getMapMode().DPtoLP(150), getMapMode().DPtoLP(100));
            this.setMaximumSize(new org.eclipse.draw2d.geometry.Dimension(getMapMode().DPtoLP(200), getMapMode().DPtoLP(175)));
            this.setMinimumSize(new org.eclipse.draw2d.geometry.Dimension(getMapMode().DPtoLP(50), getMapMode().DPtoLP(45)));
            createContents();
        }

        private void createContents() {
            org.eclipse.draw2d.RectangleFigure fig_0 = new org.eclipse.draw2d.RectangleFigure();
            fig_0.setFill(false);
            fig_0.setOutline(false);
            setFigureRohmbusContainer(fig_0);
            Object layData0 = null;
            this.add(fig_0, layData0);
            fig_1 = new RhombusPolygon();
            Object layData1 = null;
            fig_0.add(fig_1, layData1);
        }

        @Override
        public void paintFigure(Graphics graphics) {
            fig_1.paintFigure(graphics);
            super.paintFigure(graphics);
        }

        /**
		 * @generated
		 */
        private org.eclipse.draw2d.RectangleFigure fRohmbusContainer;

        /**
		 * @generated
		 */
        public org.eclipse.draw2d.RectangleFigure getFigureRohmbusContainer() {
            return fRohmbusContainer;
        }

        /**
		 * @generated
		 */
        private void setFigureRohmbusContainer(org.eclipse.draw2d.RectangleFigure fig) {
            fRohmbusContainer = fig;
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
    }
}
