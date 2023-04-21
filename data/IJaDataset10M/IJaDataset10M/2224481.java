package com.safi.workshop.edit.parts;

import java.util.ArrayList;
import java.util.Collections;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.ConnectionRouter;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayeredPane;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayeredPane;
import org.eclipse.draw2d.LayoutManager;
import org.eclipse.draw2d.Panel;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gmf.runtime.diagram.ui.figures.BorderItemsAwareFreeFormLayer;
import org.eclipse.gmf.runtime.diagram.ui.figures.DiagramColorConstants;
import org.eclipse.gmf.runtime.diagram.ui.render.editparts.RenderedDiagramRootEditPart;
import org.eclipse.gmf.runtime.draw2d.ui.internal.figures.ConnectionLayerEx;
import org.eclipse.gmf.runtime.draw2d.ui.internal.routers.BorderItemRectilinearRouter;
import org.eclipse.gmf.runtime.notation.MeasurementUnit;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import com.safi.asterisk.figures.OutputFigure;

public class AsteriskRootEditPart extends RenderedDiagramRootEditPart {

    public static final String GRID_ROUTER_LAYER = "Grid Router Debug";

    private IFigure debugBackgroundFigure;

    private IFigure contentPane;

    private Image debugBackground;

    private ImageData debugBGImageData;

    public AsteriskRootEditPart() {
        super();
    }

    public AsteriskRootEditPart(MeasurementUnit mu) {
        super(mu);
    }

    @Override
    protected LayeredPane createPrintableLayers() {
        FreeformLayeredPane layeredPane = new FreeformLayeredPane();
        FreeformLayer debugLayer = new FreeformLayer() {

            @Override
            public Rectangle getFreeformExtent() {
                Insets insets = getInsets();
                return new Rectangle(0, 0, insets.getWidth(), insets.getHeight());
            }

            @Override
            protected void fireFigureMoved() {
                if (debugBackgroundFigure != null) {
                    Rectangle clientArea = getClientArea();
                    debugBackgroundFigure.setBounds(clientArea);
                }
                super.fireFigureMoved();
            }
        };
        debugLayer.setLayoutManager(new FreeformLayout());
        debugBackgroundFigure = new Panel();
        layeredPane.add(new FreeformLayer(), GRID_ROUTER_LAYER);
        debugBackgroundFigure.setBounds(debugLayer.getClientArea());
        debugBackgroundFigure.setBackgroundColor(DiagramColorConstants.diagramLightRed);
        debugLayer.add(debugBackgroundFigure);
        debugBackgroundFigure.setVisible(false);
        layeredPane.add(debugLayer, DECORATION_UNPRINTABLE_LAYER);
        layeredPane.add(new BorderItemsAwareFreeFormLayer(), PRIMARY_LAYER);
        layeredPane.add(new AsteriskConnectionLayerEx(), CONNECTION_LAYER);
        layeredPane.add(new FreeformLayer(), DECORATION_PRINTABLE_LAYER);
        return layeredPane;
    }

    public void showDebugBackground(boolean done) {
        if (done) debugBackgroundFigure.setBackgroundColor(DiagramColorConstants.diagramGray); else debugBackgroundFigure.setBackgroundColor(DiagramColorConstants.diagramLightRed);
        debugBackgroundFigure.setVisible(true);
    }

    public void hideDebugBackground() {
        debugBackgroundFigure.setVisible(false);
    }

    public class AsteriskConnectionLayerEx extends ConnectionLayerEx {

        private ConnectionRouter rectilinearRouter;

        @Override
        public ConnectionRouter getRectilinearRouter() {
            if (rectilinearRouter == null) {
                rectilinearRouter = new SafiRectilinearRouter((HandlerEditPart) getContents());
            }
            return rectilinearRouter;
        }

        @Override
        public ConnectionRouter getConnectionRouter() {
            return null;
        }

        public void moveToFront(Connection conn) {
            ConnectionRouter router = conn.getConnectionRouter();
            if (router instanceof SafiRectilinearRouter) {
                remove(conn);
                add(conn);
                conn.setConnectionRouter(router);
            }
        }

        static final int FLAG_REALIZED = 1 << 31;

        public void figureAdd(IFigure figure, Object constraint, int index) {
            if (children == Collections.EMPTY_LIST) children = new ArrayList(2);
            if (index < -1 || index > children.size()) throw new IndexOutOfBoundsException("Index does not exist");
            for (IFigure f = this; f != null; f = f.getParent()) if (figure == f) throw new IllegalArgumentException("Figure being added introduces cycle");
            if (figure.getParent() != null) figure.getParent().remove(figure);
            if (index == -1) children.add(figure); else children.add(index, figure);
            figure.setParent(this);
            LayoutManager layoutManager = getLayoutManager();
            if (layoutManager != null) layoutManager.setConstraint(figure, constraint);
            revalidate();
            if (getFlag(FLAG_REALIZED)) figure.addNotify();
            figure.repaint();
        }

        public void figureRemove(IFigure figure) {
            if ((figure.getParent() != this)) throw new IllegalArgumentException("Figure is not a child");
            if (getFlag(FLAG_REALIZED)) figure.removeNotify();
            LayoutManager layoutManager = getLayoutManager();
            if (layoutManager != null) layoutManager.remove(figure);
            figure.erase();
            figure.setParent(null);
            children.remove(figure);
            revalidate();
        }
    }

    class AsteriskRectilinearRouter extends BorderItemRectilinearRouter {

        @Override
        protected PointList calculateBendPoints(Connection conn) {
            IFigure source = conn.getSourceAnchor().getOwner();
            IFigure target = conn.getTargetAnchor().getOwner();
            if (isAvoidingObstructions(conn)) {
                PointList points = new PointList();
                points = SafiRouterHelper.getInstance().routeAroundObstructions(conn);
                return points;
            }
            return super.calculateBendPoints(conn);
        }

        @Override
        public void routeLine(Connection conn, int nestedRoutingDepth, PointList newLine) {
            super.routeLine(conn, nestedRoutingDepth, newLine);
        }

        @Override
        protected int getBorderFigurePosition(IFigure borderFigure) {
            int side = super.getBorderFigurePosition(borderFigure);
            if (borderFigure instanceof OutputFigure) return PositionConstants.EAST;
            return side;
        }

        @Override
        protected Rectangle getObstacle(IFigure figure, Connection conn, boolean isBorderItem) {
            return super.getObstacle(figure, conn, isBorderItem);
        }

        @Override
        protected IFigure getBorderItemParent(IFigure figure) {
            IFigure parent = super.getBorderItemParent(figure);
            return parent;
        }
    }

    @Override
    protected IFigure createFigure() {
        return super.createFigure();
    }

    public IFigure getDebugBackgroundFigure() {
        return debugBackgroundFigure;
    }
}
