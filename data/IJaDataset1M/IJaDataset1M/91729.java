package com.vividsolutions.jtstest.testbuilder;

import java.text.NumberFormat;
import java.util.List;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.util.Assert;
import com.vividsolutions.jtstest.testbuilder.model.*;
import com.vividsolutions.jtstest.testbuilder.ui.*;
import com.vividsolutions.jtstest.testbuilder.ui.style.AWTUtil;
import com.vividsolutions.jtstest.testbuilder.ui.tools.*;
import com.vividsolutions.jtstest.testbuilder.ui.render.*;
import com.vividsolutions.jtstest.testbuilder.ui.render.GeometryPainter;

/**
 * Panel which displays rendered geometries.
 * 
 * @version 1.7
 */
public class GeometryEditPanel extends JPanel {

    private TestBuilderModel tbModel;

    private DrawingGrid grid = new DrawingGrid();

    private GridRenderer gridRenderer;

    boolean stateAddingPoints = false;

    Coordinate markPoint;

    Point2D lastPt = new Point2D.Double();

    private Tool currentTool = null;

    private Viewport viewport = new Viewport(this);

    private RenderManager renderMgr;

    BorderLayout borderLayout1 = new BorderLayout();

    GeometryPopupMenu menu = new GeometryPopupMenu();

    public GeometryEditPanel() {
        gridRenderer = new GridRenderer(viewport, grid);
        try {
            initUI();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        renderMgr = new RenderManager(this);
    }

    void initUI() throws Exception {
        this.addComponentListener(new java.awt.event.ComponentAdapter() {

            public void componentResized(ComponentEvent e) {
                this_componentResized(e);
            }
        });
        this.setBackground(Color.white);
        this.setBorder(BorderFactory.createLoweredBevelBorder());
        this.setLayout(borderLayout1);
        setToolTipText("");
        setBorder(BorderFactory.createEmptyBorder());
    }

    class PopupClickListener extends MouseAdapter {

        public void mousePressed(MouseEvent e) {
            if (e.isPopupTrigger()) doPopUp(e);
        }

        public void mouseReleased(MouseEvent e) {
            if (e.isPopupTrigger()) doPopUp(e);
        }

        private void doPopUp(MouseEvent e) {
            menu.show(e.getComponent(), e.getX(), e.getY());
        }
    }

    public void setModel(TestBuilderModel model) {
        this.tbModel = model;
    }

    public TestBuilderModel getModel() {
        return tbModel;
    }

    public GeometryEditModel getGeomModel() {
        return tbModel.getGeometryEditModel();
    }

    public void setGridEnabled(boolean isEnabled) {
        gridRenderer.setEnabled(isEnabled);
    }

    public Viewport getViewport() {
        return viewport;
    }

    public void updateView() {
        forceRepaint();
    }

    public void forceRepaint() {
        renderMgr.setDirty(true);
        Component source = SwingUtilities.windowForComponent(this);
        if (source == null) source = this;
        source.repaint();
    }

    private LayerList getLayerList() {
        return tbModel.getLayers();
    }

    public void setShowingInput(boolean isEnabled) {
        if (tbModel == null) return;
        getLayerList().getLayer(LayerList.LYR_A).setEnabled(isEnabled);
        getLayerList().getLayer(LayerList.LYR_B).setEnabled(isEnabled);
        forceRepaint();
    }

    public void setShowingGeometryA(boolean isEnabled) {
        if (tbModel == null) return;
        getLayerList().getLayer(LayerList.LYR_A).setEnabled(isEnabled);
        forceRepaint();
    }

    public void setShowingGeometryB(boolean isEnabled) {
        if (tbModel == null) return;
        getLayerList().getLayer(LayerList.LYR_B).setEnabled(isEnabled);
        forceRepaint();
    }

    public void setShowingResult(boolean isEnabled) {
        if (tbModel == null) return;
        getLayerList().getLayer(LayerList.LYR_RESULT).setEnabled(isEnabled);
        forceRepaint();
    }

    public void setGridSize(double gridSize) {
        grid.setGridSize(gridSize);
        forceRepaint();
    }

    public void setHighlightPoint(Coordinate pt) {
        markPoint = pt;
    }

    public boolean isAddingPoints() {
        return stateAddingPoints;
    }

    public void updateGeom() {
        renderMgr.setDirty(true);
        getGeomModel().geomChanged();
    }

    public String getToolTipText(MouseEvent event) {
        Coordinate pt = viewport.toModelCoordinate(event.getPoint());
        double toleranceInModel = AppConstants.TOLERANCE_PIXELS / getViewport().getScale();
        if (toleranceInModel <= 0.0) return null;
        return GeometryLocationsWriter.writeLocation(getLayerList(), pt, toleranceInModel);
    }

    public double getToleranceInModel() {
        return AppConstants.TOLERANCE_PIXELS / getViewport().getScale();
    }

    public String getInfo(Coordinate pt) {
        double toleranceInModel = AppConstants.TOLERANCE_PIXELS / getViewport().getScale();
        GeometryLocationsWriter writer = new GeometryLocationsWriter();
        writer.setHtml(false);
        return writer.writeLocationString(getLayerList(), pt, toleranceInModel);
    }

    public double getGridSize() {
        return grid.getGridSize();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        renderMgr.render();
        renderMgr.copyImage(g);
    }

    private static int VERTEX_SIZE = AppConstants.VERTEX_SIZE + 1;

    private static double VERTEX_SIZE_OVER_2 = VERTEX_SIZE / 2;

    private static int INNER_SIZE = VERTEX_SIZE - 2;

    private static double INNER_SIZE_OVER_2 = INNER_SIZE / 2;

    private void drawHighlightedVertices(Graphics2D g, List coords, Color clr) {
        Rectangle2D rect = new Rectangle2D.Double();
        for (int i = 0; i < coords.size(); i++) {
            Coordinate pt = (Coordinate) coords.get(i);
            Point2D p = viewport.toView(pt);
            rect.setFrame(p.getX() - VERTEX_SIZE_OVER_2, p.getY() - VERTEX_SIZE_OVER_2, VERTEX_SIZE, VERTEX_SIZE);
            g.setColor(clr);
            g.fill(rect);
            Rectangle2D rectInner = new Rectangle2D.Double(p.getX() - INNER_SIZE_OVER_2, p.getY() - INNER_SIZE_OVER_2, INNER_SIZE, INNER_SIZE);
            g.setColor(AppConstants.VERTEX_HIGHLIGHT_CLR);
            g.fill(rectInner);
        }
    }

    private void drawHighlightedVertex(Graphics2D g, Coordinate pt, Color clr) {
        Rectangle2D rect = new Rectangle2D.Double();
        Point2D p = viewport.toView(pt);
        rect.setFrame(p.getX() - VERTEX_SIZE_OVER_2, p.getY() - VERTEX_SIZE_OVER_2, VERTEX_SIZE, VERTEX_SIZE);
        g.setColor(clr);
        g.fill(rect);
        Rectangle2D rectInner = new Rectangle2D.Double(p.getX() - INNER_SIZE_OVER_2, p.getY() - INNER_SIZE_OVER_2, INNER_SIZE, INNER_SIZE);
        g.setColor(AppConstants.VERTEX_HIGHLIGHT_CLR);
        g.fill(rectInner);
    }

    private static double VERTEX_SHADOW_SIZE_OVER_2 = AppConstants.VERTEX_SHADOW_SIZE / 2;

    private void drawVertexShadow(Graphics2D g, Coordinate pt, Color clr) {
        Ellipse2D rect = new Ellipse2D.Double();
        Point2D p = viewport.toView(pt);
        rect.setFrame(p.getX() - VERTEX_SHADOW_SIZE_OVER_2, p.getY() - VERTEX_SHADOW_SIZE_OVER_2, AppConstants.VERTEX_SHADOW_SIZE, AppConstants.VERTEX_SHADOW_SIZE);
        g.setColor(clr);
        g.fill(rect);
    }

    private void drawMark(Graphics2D g) {
        if (markPoint == null) return;
        String markLabel = markPoint.x + ",  " + markPoint.y;
        int strWidth = g.getFontMetrics().stringWidth(markLabel);
        double markSize = AppConstants.HIGHLIGHT_SIZE;
        Point2D highlightPointView = viewport.toView(markPoint);
        double markX = highlightPointView.getX();
        double markY = highlightPointView.getY();
        Ellipse2D.Double shape = new Ellipse2D.Double(markX - markSize / 2, markY - markSize / 2, markSize, markSize);
        AWTUtil.setStroke(g, 4);
        g.setColor(AppConstants.HIGHLIGHT_CLR);
        g.draw(shape);
        Envelope viewEnv = viewport.getViewEnv();
        int bottomOffset = 10;
        int boxHgt = 20;
        int boxPadX = 20;
        int boxWidth = strWidth + 2 * boxPadX;
        int arrowWidth = 10;
        int arrowOffset = 2;
        int labelOffsetY = 5;
        int bottom = (int) viewEnv.getMaxY() - bottomOffset;
        int centreX = (int) (viewEnv.getMinX() + viewEnv.getMaxX()) / 2;
        int boxMinX = centreX - boxWidth / 2;
        int boxMaxX = centreX + boxWidth / 2;
        int boxMinY = bottom - boxHgt;
        int boxMaxY = bottom;
        int[] xpts = new int[] { boxMinX, centreX - arrowWidth / 2, (int) markX, centreX + arrowWidth / 2, boxMaxX, boxMaxX, boxMinX };
        int[] ypts = new int[] { boxMinY, boxMinY, (int) (markY + arrowOffset), boxMinY, boxMinY, boxMaxY, boxMaxY };
        Polygon poly = new Polygon(xpts, ypts, xpts.length);
        g.setColor(AppConstants.HIGHLIGHT_FILL_CLR);
        g.fill(poly);
        AWTUtil.setStroke(g, 1);
        g.setColor(ColorUtil.opaque(AppConstants.HIGHLIGHT_CLR));
        g.draw(poly);
        g.setColor(Color.BLACK);
        g.drawString(markLabel, centreX - strWidth / 2, boxMaxY - labelOffsetY);
    }

    /**
   * Draws a mask surround to indicate that geometry is being visually altered
   * @param g
   */
    private void drawMagnifyMask(Graphics2D g) {
        double viewWidth = viewport.getWidthInView();
        double viewHeight = viewport.getHeightInView();
        float minExtent = (float) Math.min(viewWidth, viewHeight);
        float maskWidth = (float) (minExtent * AppConstants.MASK_WIDTH_FRAC / 2);
        Area mask = new Area(new Rectangle2D.Float((float) 0, (float) 0, (float) viewWidth, (float) viewHeight));
        Area maskHole = new Area(new Rectangle2D.Float((float) maskWidth, (float) maskWidth, ((float) viewWidth) - 2 * maskWidth, ((float) viewHeight) - 2 * maskWidth));
        mask.subtract(maskHole);
        g.setColor(AppConstants.MASK_CLR);
        g.fill(mask);
    }

    public void flash(Geometry g) {
        Graphics2D gr = (Graphics2D) getGraphics();
        gr.setXORMode(Color.white);
        Stroke stroke = new BasicStroke(5);
        Geometry flashGeom = g;
        if (g instanceof com.vividsolutions.jts.geom.Point) flashGeom = flashPointGeom(g);
        try {
            GeometryPainter.paint(flashGeom, viewport, gr, Color.RED, null, stroke);
            Thread.sleep(200);
            GeometryPainter.paint(flashGeom, viewport, gr, Color.RED, null, stroke);
        } catch (Exception ex) {
        }
        gr.setPaintMode();
    }

    private Geometry flashPointGeom(Geometry g) {
        double ptRadius = viewport.toModel(4);
        return g.buffer(ptRadius);
    }

    public Point2D snapToGrid(Point2D modelPoint) {
        return grid.snapToGrid(modelPoint);
    }

    void this_componentResized(ComponentEvent e) {
        renderMgr.componentResized();
        viewport.update();
    }

    public void setCurrentTool(Tool newTool) {
        removeMouseListener(currentTool);
        removeMouseMotionListener(currentTool);
        currentTool = newTool;
        currentTool.activate();
        setCursor(currentTool.getCursor());
        addMouseListener(currentTool);
        addMouseMotionListener(currentTool);
    }

    public void zoomToGeometry(int i) {
        Geometry g = getGeomModel().getGeometry(i);
        if (g == null) return;
        zoom(g.getEnvelopeInternal());
    }

    public void zoomToInput() {
        zoom(getGeomModel().getEnvelope());
    }

    public void zoomToResult() {
        zoom(getGeomModel().getEnvelopeResult());
    }

    public void zoomToFullExtent() {
        zoom(getGeomModel().getEnvelopeAll());
    }

    public void zoom(Geometry geom) {
        if (geom == null) return;
        zoom(geom.getEnvelopeInternal());
    }

    public void zoom(Envelope zoomEnv) {
        if (zoomEnv == null) return;
        renderMgr.setDirty(true);
        if (zoomEnv.isNull()) {
            viewport.zoomToInitialExtent();
            return;
        }
        double averageExtent = (zoomEnv.getWidth() + zoomEnv.getHeight()) / 2d;
        if (averageExtent == 0.0) averageExtent = 1.0;
        double buffer = averageExtent * 0.03;
        zoomEnv.expandToInclude(zoomEnv.getMaxX() + buffer, zoomEnv.getMaxY() + buffer);
        zoomEnv.expandToInclude(zoomEnv.getMinX() - buffer, zoomEnv.getMinY() - buffer);
        viewport.zoom(zoomEnv);
    }

    public void zoom(Point center, double realZoomFactor) {
        renderMgr.setDirty(true);
        double width = getSize().width / realZoomFactor;
        double height = getSize().height / realZoomFactor;
        double bottomOfNewViewAsPerceivedByOldView = center.y + (height / 2d);
        double leftOfNewViewAsPerceivedByOldView = center.x - (width / 2d);
        Point bottomLeftOfNewViewAsPerceivedByOldView = new Point((int) leftOfNewViewAsPerceivedByOldView, (int) bottomOfNewViewAsPerceivedByOldView);
        Point2D bottomLeftOfNewViewAsPerceivedByModel = viewport.toModel(bottomLeftOfNewViewAsPerceivedByOldView);
        viewport.setScale(getViewport().getScale() * realZoomFactor);
        viewport.setViewOrigin(bottomLeftOfNewViewAsPerceivedByModel.getX(), bottomLeftOfNewViewAsPerceivedByModel.getY());
    }

    public void zoomPan(double xDisplacement, double yDisplacement) {
        renderMgr.setDirty(true);
        getViewport().setViewOrigin(getViewport().getViewOriginX() - xDisplacement, getViewport().getViewOriginY() - yDisplacement);
    }

    public String cursorLocationString(Point2D pView) {
        Point2D p = getViewport().toModel(pView);
        NumberFormat format = getViewport().getScaleFormat();
        return format.format(p.getX()) + ", " + format.format(p.getY());
    }

    public Renderer getRenderer() {
        return new GeometryEditPanelRenderer();
    }

    class GeometryEditPanelRenderer implements Renderer {

        private GeometryStretcherView stretchView = null;

        private Renderer currentRenderer = null;

        private boolean isMagnifyingTopology = false;

        private boolean isRenderingStretchVertices = false;

        public GeometryEditPanelRenderer() {
            if (tbModel.isMagnifyingTopology()) {
                stretchView = new GeometryStretcherView(getGeomModel());
                stretchView.setStretchSize(viewport.toModel(tbModel.getTopologyStretchSize()));
                stretchView.setNearnessTolerance(viewport.toModel(GeometryStretcherView.NEARNESS_TOL_IN_VIEW));
                stretchView.setEnvelope(viewport.getModelEnv());
                isMagnifyingTopology = tbModel.isMagnifyingTopology();
                isRenderingStretchVertices = stretchView.isViewPerformant();
            }
        }

        public void render(Graphics2D g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if (isMagnifyingTopology) {
                if (isRenderingStretchVertices) {
                    renderMagnifiedVertexMask(g2);
                } else {
                    renderMagnifyWarning(g2);
                }
            }
            gridRenderer.paint(g2);
            renderLayers(g2);
            if (isMagnifyingTopology && isRenderingStretchVertices) {
                renderMagnifiedVertices(g2);
            }
            drawMark(g2);
        }

        public void renderLayers(Graphics2D g) {
            LayerList layerList = getLayerList();
            int n = layerList.size();
            for (int i = 0; i < n; i++) {
                if (isMagnifyingTopology && isRenderingStretchVertices && stretchView != null && i < 2) {
                    currentRenderer = new LayerRenderer(layerList.getLayer(i), new StaticGeometryContainer(stretchView.getStretchedGeometry(i)), viewport);
                } else {
                    currentRenderer = new LayerRenderer(layerList.getLayer(i), viewport);
                }
                currentRenderer.render(g);
            }
            currentRenderer = null;
        }

        public void renderMagnifiedVertices(Graphics2D g) {
            LayerList layerList = getLayerList();
            for (int i = 0; i < 2; i++) {
                if (!layerList.getLayer(i).isEnabled()) continue;
                List stretchedVerts = stretchView.getStretchedVertices(i);
                if (stretchedVerts == null) continue;
                for (int j = 0; j < stretchedVerts.size(); j++) {
                    Coordinate p = (Coordinate) stretchedVerts.get(j);
                    drawHighlightedVertex(g, p, i == 0 ? GeometryDepiction.GEOM_A_HIGHLIGHT_CLR : GeometryDepiction.GEOM_B_HIGHLIGHT_CLR);
                }
            }
        }

        public void renderMagnifiedVertexShadows(Graphics2D g) {
            if (stretchView == null) return;
            for (int i = 0; i < 2; i++) {
                List stretchedVerts = stretchView.getStretchedVertices(i);
                if (stretchedVerts == null) continue;
                for (int j = 0; j < stretchedVerts.size(); j++) {
                    Coordinate p = (Coordinate) stretchedVerts.get(j);
                    drawVertexShadow(g, p, AppConstants.VERTEX_SHADOW_CLR);
                }
            }
        }

        public void renderMagnifiedVertexMask(Graphics2D g) {
            if (stretchView == null) return;
            Rectangle2D rect = new Rectangle2D.Float();
            rect.setFrame(0, 0, viewport.getWidthInView(), viewport.getHeightInView());
            g.setColor(AppConstants.MASK_CLR);
            g.fill(rect);
            for (int i = 0; i < 2; i++) {
                List stretchedVerts = stretchView.getStretchedVertices(i);
                if (stretchedVerts == null) continue;
                for (int j = 0; j < stretchedVerts.size(); j++) {
                    Coordinate p = (Coordinate) stretchedVerts.get(j);
                    drawVertexShadow(g, p, Color.WHITE);
                }
            }
        }

        public void renderMagnifyWarning(Graphics2D g) {
            if (stretchView == null) return;
            float maxx = (float) viewport.getWidthInView();
            float maxy = (float) viewport.getHeightInView();
            GeneralPath path = new GeneralPath();
            path.moveTo(0, 0);
            path.lineTo(maxx, maxy);
            path.moveTo(0, maxy);
            path.lineTo(maxx, 0);
            g.setColor(AppConstants.MASK_CLR);
            g.setStroke(new BasicStroke(30));
            g.draw(path);
        }

        public synchronized void cancel() {
            if (currentRenderer != null) currentRenderer.cancel();
        }
    }
}
