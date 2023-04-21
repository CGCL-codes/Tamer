package de.uni_trier.st.nevada.view.central;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Dimension2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import de.uni_trier.st.nevada.analysis.AnalysisMetric;
import de.uni_trier.st.nevada.analysis.GraphSequenceMetric;
import de.uni_trier.st.nevada.analysis.networkmetric.NetworkPatternMetric;
import de.uni_trier.st.nevada.analysis.nodemetric.NodeMetric;
import de.uni_trier.st.nevada.analysis.property.PropertyMetric;
import de.uni_trier.st.nevada.graphs.PresentGraph;
import de.uni_trier.st.nevada.graphs.Int.Edge;
import de.uni_trier.st.nevada.graphs.Int.Graph;
import de.uni_trier.st.nevada.graphs.Int.Node;
import de.uni_trier.st.nevada.util.Math2D;
import de.uni_trier.st.nevada.view.NEVADAView;
import de.uni_trier.st.nevada.view.LineMetricPanel;

public class GraphAnimationView extends JComponent implements Runnable {

    public enum ArrowType {

        NoArrow, StdArrow, ThinArrow
    }

    enum UI {

        Standard, Dark
    }

    public enum LabelType {

        Dynamic, HalfSize, None, NormalSize, Short
    }

    public enum NodeMetricType {

        None, NormalSize, HalfSize
    }

    public enum VisType {

        Standard, Nested
    }

    private static final long serialVersionUID = 1L;

    private Thread animationThread;

    private GraphAnimationController controller;

    private ArrowType currentArrowType;

    private int currentEdgeType;

    private int doneSteps;

    private Set<Node> drawLabel;

    private int drawValue;

    private GraphUI graphUI;

    private double labelDistance;

    private LabelType labelType;

    private NodeMetricType nodeMetricType;

    private final int maxSteps = 30;

    private MouseController mouseController;

    private Set<Node> selectedNodes;

    private Set<Edge> selectedEdges;

    public static Node mouseOverNode = null;

    private int speed;

    private double thick;

    private Set<Node> toBeLabelled;

    private Interpolator transparencyInterpolator;

    private Interpolator animationInterpolator;

    private Interpolator colorInterpolator;

    private boolean painting;

    private Image lock;

    /**
	 * Edges move from source to target when animating?
	 */
    private boolean walkingEdges = false;

    private double zoomValue;

    private VisType visType;

    private AffineTransform transformation;

    private Set<AnalysisMetric> updatedMetrics;

    private NodeMetric nodeMetric;

    private NetworkPatternMetric networkPatternMetric;

    private GraphSequenceMetric sequenceMetric;

    private PropertyMetric propertyMetric;

    private Set<Node> nodeHighlightNetworkPattern;

    private Set<Edge> edgeHighlightNetworkPattern;

    private boolean networkPatternDirty;

    private boolean nodeMetricNormalized;

    private float nodeMetricFontSize;

    private boolean nodeMetricDirected;

    private boolean showNodeHighlightNetworkPattern;

    private boolean showEdgeHighlightNetworkPattern;

    private boolean edgeFadeNetworkPattern;

    private boolean networkMetricDirected;

    private double highlightIntensity;

    private int digits;

    private boolean showSequenceMetric;

    private LinkedList<Node> nodesInFocus;

    private int maxNodesInFocus;

    private Map<Node, Color> colorFocusMap;

    private LineMetricPanel metricPanel;

    private NEVADAView.MenuSetting currentMenuSetting;

    /**
	 * Variables that store the amount the current viewed graph should be moved in x- and y-direction with the middle mouse-button.
	 */
    public int moveX = 0;

    public int moveY = 0;

    public GraphAnimationView(final GraphAnimationController controller) {
        this.controller = controller;
        this.drawValue = 100;
        this.zoomValue = 1.0;
        this.transformation = new AffineTransform(this.zoomValue, 0.0, 0.0, this.zoomValue, 0.0, 0.0);
        this.selectedNodes = new HashSet<Node>();
        this.selectedEdges = new HashSet<Edge>();
        this.doneSteps = 0;
        this.animationThread = null;
        this.mouseController = new MouseController(this, this.controller, this.transformation);
        this.speed = 70;
        this.addMouseListener(this.mouseController);
        this.addMouseMotionListener(this.mouseController);
        this.addMouseWheelListener(this.mouseController);
        this.currentEdgeType = 2;
        this.currentArrowType = ArrowType.NoArrow;
        this.labelType = LabelType.None;
        this.toBeLabelled = new HashSet<Node>();
        this.labelDistance = 75.0;
        this.drawLabel = new HashSet<Node>();
        this.thick = 1.5;
        this.graphUI = new StandardGraphUI(this);
        this.transparencyInterpolator = new BernsteinInterpolator();
        this.animationInterpolator = new BezierInterpolator();
        this.colorInterpolator = new BernsteinInterpolator();
        this.painting = false;
        this.selected = false;
        this.lock = this.getToolkit().getImage("de/uni_trier/st/nevada/view/central/lock.png");
        this.nodeMetricType = NodeMetricType.None;
        this.nodeMetricNormalized = false;
        this.nodeMetricFontSize = 11.0f;
        this.nodeHighlightNetworkPattern = new LinkedHashSet<Node>();
        this.edgeHighlightNetworkPattern = new LinkedHashSet<Edge>();
        this.showEdgeHighlightNetworkPattern = false;
        this.showNodeHighlightNetworkPattern = false;
        this.networkMetricDirected = true;
        this.nodeMetricDirected = true;
        this.highlightIntensity = 0.2;
        this.digits = 4;
        this.showSequenceMetric = false;
        this.updatedMetrics = new HashSet<AnalysisMetric>();
        this.nodesInFocus = new LinkedList<Node>();
        this.colorFocusMap = new HashMap<Node, Color>();
        this.maxNodesInFocus = 5;
        this.metricPanel = LineMetricPanel.getInstance();
        this.metricPanel.setCurrentView(this);
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    }

    /**
	 * Sets the UI for this view
	 * @param ui
	 */
    void setUI(UI ui) {
        switch(ui) {
            case Dark:
                {
                    this.graphUI = new ColorGraphUI(this);
                    break;
                }
            case Standard:
                {
                    this.graphUI = new StandardGraphUI(this);
                    break;
                }
        }
        repaint();
    }

    void setNodeMetric(NodeMetric metric) {
        this.nodeMetric = metric;
        if (metric != null && !this.updatedMetrics.contains(metric)) {
            metric.update();
            this.updatedMetrics.add(metric);
        }
    }

    void setNodeMetricType(GraphAnimationView.NodeMetricType type) {
        this.nodeMetricType = type;
    }

    void setNormalized(boolean b) {
        this.nodeMetricNormalized = b;
    }

    void setNodeMetricDirected(boolean b) {
        this.nodeMetricDirected = b;
        this.updateMetric();
    }

    void setMetricFontSize(float f) {
        this.nodeMetricFontSize = f;
    }

    void addNodeInFocus(Node n) {
        if (this.metricPanel.getCurrentView() != this) return;
        if (this.nodesInFocus.contains(n)) {
            this.nodesInFocus.remove(n);
            this.colorFocusMap.remove(n);
            this.metricPanel.removeNode(n);
            return;
        }
        this.nodesInFocus.add(n);
        System.out.println("++nodesInFocus.size() = " + this.nodesInFocus.size());
        this.colorFocusMap.put(n, this.metricPanel.addNode(n));
        if (this.nodesInFocus.size() > this.maxNodesInFocus) {
            this.metricPanel.removeNode(this.nodesInFocus.getFirst());
            this.colorFocusMap.remove(this.nodesInFocus.getFirst());
            this.nodesInFocus.removeFirst();
        }
    }

    public void clearNodesInFocus() {
        this.nodesInFocus.clear();
        this.metricPanel.clearNodes();
    }

    public LinkedList<Node> getNodesInFocus() {
        return this.nodesInFocus;
    }

    void setMaxNodesInFocus(int max) {
        if (max > 0) this.maxNodesInFocus = max; else return;
    }

    void setCurrentIndex(int i) {
        this.metricPanel.setCurrentIndex(i);
    }

    NodeMetric getNodeMetric() {
        return this.nodeMetric;
    }

    void setEdgeFadeNetworkPattern(boolean b) {
        this.edgeFadeNetworkPattern = b;
    }

    void updateMetric() {
        this.updatedMetrics.clear();
        if (this.nodeMetric != null) {
            updatedMetrics.add(this.nodeMetric);
            this.nodeMetric.update();
        }
        if (this.networkPatternMetric != null) {
            updatedMetrics.add(this.networkPatternMetric);
            this.networkPatternMetric.update();
        }
        if (this.propertyMetric != null) {
            updatedMetrics.add(this.propertyMetric);
            this.propertyMetric.update();
        }
        if (this.sequenceMetric != null) {
            updatedMetrics.add(this.sequenceMetric);
            this.sequenceMetric.update();
        }
        if (this.metricPanel != null) this.metricPanel.update();
        this.networkPatternDirty = true;
    }

    public void resetMetricPanel() {
        this.metricPanel.setSequence(this.controller.getGraphs());
    }

    public LineMetricPanel getMetricPanel() {
        return this.metricPanel;
    }

    void reset() {
        this.nodeHighlightNetworkPattern.clear();
        this.edgeHighlightNetworkPattern.clear();
        this.nodeMetric = null;
        this.selectedEdges.clear();
        this.selectedNodes.clear();
    }

    void refreshMetrics() {
        this.networkPatternDirty = true;
    }

    void setNetworkPatternMetric(NetworkPatternMetric metric) {
        this.networkPatternMetric = metric;
        if (metric != null & !this.updatedMetrics.contains(metric)) {
            this.networkPatternMetric.update();
            this.updatedMetrics.add(metric);
            this.networkPatternDirty = true;
        }
    }

    void setNetworkMetricDirected(boolean b) {
        this.networkMetricDirected = b;
        this.networkPatternDirty = true;
    }

    void setGraphSequenceMetric(GraphSequenceMetric metric) {
        this.sequenceMetric = metric;
        if (metric != null & !this.updatedMetrics.contains(metric)) {
            this.sequenceMetric.update();
            this.updatedMetrics.add(metric);
        }
    }

    void setSequenceHighlight(boolean b) {
        this.showSequenceMetric = b;
        this.nodeMetricType = GraphAnimationView.NodeMetricType.None;
    }

    void setHighlightIntensity(double d) {
        if ((d <= 1.0) && (d >= 0.0)) this.highlightIntensity = d;
    }

    NetworkPatternMetric getNetworkPatternMetric() {
        return this.networkPatternMetric;
    }

    void setNodeHighlight(boolean b) {
        this.showNodeHighlightNetworkPattern = b;
    }

    void setEdgeHighlight(boolean b) {
        this.showEdgeHighlightNetworkPattern = b;
    }

    void setDigits(int d) {
        this.digits = d;
    }

    /**
	 * Returns if an animation is running on this view.
	 * @return <code>true</code> during animation
	 */
    boolean animationRunning() {
        if (this.animationThread == null) {
            return false;
        }
        return this.animationThread.isAlive();
    }

    /**
	 * Clears the selected Nodes
	 *
	 */
    void clearSelection() {
        this.selectedNodes.clear();
    }

    void clearEdgeSelection() {
        this.selectedEdges.clear();
    }

    public int getDrawOptionsValue() {
        return this.drawValue;
    }

    public GraphAnimationController getController() {
        return this.controller;
    }

    @Override
    public Dimension getPreferredSize() {
        final Rectangle2D sizeRect = new Rectangle2D.Double(0.0, 0.0, 0.0, 0.0);
        if (this.controller.getCurrentGraph() != null) {
            for (final Node n : this.controller.getCurrentGraph().getVisibleNodes()) {
                sizeRect.add(n.getPosition(this.controller.getCurrentGraph()).getX() + n.getDimension(this.controller.getCurrentGraph()).getWidth(), n.getPosition(this.controller.getCurrentGraph()).getY() + n.getDimension(this.controller.getCurrentGraph()).getHeight());
            }
            for (final Edge e : this.controller.getCurrentGraph().getVisibleEdges()) {
                for (final Point2D p : e.getPointVector(this.controller.getCurrentGraph())) {
                    sizeRect.add(p);
                }
            }
        }
        return new Dimension((int) (sizeRect.getWidth() * this.zoomValue), (int) (sizeRect.getHeight() * this.zoomValue));
    }

    Set<Node> getSelectedNodes() {
        return Collections.unmodifiableSet(this.selectedNodes);
    }

    Set<Edge> getSelectedEdges() {
        return Collections.unmodifiableSet(this.selectedEdges);
    }

    public double getThickness() {
        return this.thick;
    }

    double getZoomValue() {
        return this.zoomValue;
    }

    public boolean mustDrawLabel(final Node n) {
        return (this.drawLabel.contains(n));
    }

    @Override
    public void paintComponent(final Graphics gc) {
        this.painting = true;
        final Graphics2D g = (Graphics2D) gc;
        this.paintOnDevice(g, this.getWidth(), this.getHeight());
        this.painting = false;
    }

    /**
	 * Starts painting the graphs
	 * @param g
	 * @param width
	 * @param height
	 */
    public void paintOnDevice(final Graphics2D g, final int width, final int height) {
        if (this.currentMenuSetting == NEVADAView.MenuSetting.NetworkPattern && this.networkPatternDirty) {
            this.nodeHighlightNetworkPattern.clear();
            this.edgeHighlightNetworkPattern.clear();
            if (this.networkPatternMetric != null) {
                for (Graph graph : this.networkPatternMetric.findPatterns(this.controller.getCurrentGraph(), this.networkMetricDirected)) {
                    this.nodeHighlightNetworkPattern.addAll(graph.getNodes());
                    this.edgeHighlightNetworkPattern.addAll(graph.getEdges());
                }
            }
            this.networkPatternDirty = false;
        }
        if (this.drawValue > 20) {
            g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
            if (this.drawValue > 40) {
                g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
                if (this.drawValue > 60) {
                    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    if (this.drawValue > 80) {
                        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
                    }
                }
            }
        }
        if (this.controller.getCurrentSight() != null) this.graphUI.paintBackgroundUI(g, width, height, this.controller.getCurrentGraph().getName(), this.controller.getCurrentSight().getName(), selected, this.controller.getScrollPane().getViewport().getViewRect()); else {
            this.graphUI.paintBackgroundUI(g, width, height, this.controller.getCurrentGraph() == null ? "" : this.controller.getCurrentGraph().getName(), "Original", selected, this.controller.getScrollPane().getViewport().getViewRect());
        }
        g.scale(this.zoomValue, this.zoomValue);
        this.paintGraph(g);
        if ((this.mouseController.getLastVisited() != null) && (this.mouseController.getSrcCandidate() != null)) {
            this.paintInteraction(g);
        }
        if ((this.mouseController.getStartPoint() != null) && (this.mouseController.getLastVisited() != null)) {
            this.paintRectangle(g);
        }
        g.scale(1 / this.zoomValue, 1 / this.zoomValue);
        this.graphUI.paintForeground(g, this.controller.getScrollPane().getViewport().getViewRect(), lock);
    }

    /**
	 * The animation loop
	 */
    public void run() {
        this.controller.getXpView().disableAll();
        while (this.controller.getNextGraph() != null) {
            while (this.doneSteps < this.maxSteps) {
                this.controller.updateView();
                ++this.doneSteps;
                try {
                    Thread.sleep(100 - this.speed);
                    while (this.painting) {
                        Thread.sleep(5);
                    }
                } catch (final Exception e) {
                }
            }
            this.repaint();
            this.animationThread = null;
            this.doneSteps = 0;
            this.controller.animationDone();
        }
        this.controller.getXpView().enableAll();
    }

    /**
	 * Select Node n with two flags
	 * @param n the Node selected
	 * @param clear <code>true</code> remote all other nodes from selection, <code>false</code> add this node to selection
	 * @param toggel <code>true</code> if node is already selected, remove it from selection
	 */
    void select(final Node n, boolean clear, boolean toggel) {
        if (this.selectedNodes.contains(n) && toggel) this.selectedNodes.remove(n); else {
            if (clear) this.selectedNodes.clear();
            this.selectedNodes.add(n);
        }
    }

    /**
	 * Sets the current VisType
	 * @param type
	 */
    void setVisType(VisType type) {
        this.visType = type;
    }

    VisType getVisType() {
        return this.visType;
    }

    void unselectNode(final Node n) {
        this.selectedNodes.remove(n);
    }

    /**
	 * @param clear
	 *            determins whether preselected nodes are cleared or not
	 */
    void selectSet(final Set<Node> s, boolean clear) {
        if (clear) this.selectedNodes.clear();
        for (Iterator<Node> it = s.iterator(); it.hasNext(); ) select(it.next(), false, false);
    }

    /**
	 * Selects all edges within rectangle rect.
	 * @param g the current graph
	 * @param rect the selection rectangle
	 * @param clear <code>true</code> remove all other edges from selection
	 */
    void selectEdges(Graph g, Rectangle2D rect, boolean clear) {
        if (clear) this.selectedEdges.clear();
        for (final Edge e : g.getVisibleEdges()) {
            Rectangle2D b = e.getPath(g).getBounds2D();
            if (b.getWidth() == 0) b = new Rectangle2D.Double(b.getX(), b.getY(), 1.0, b.getHeight());
            if (b.getHeight() == 0) b = new Rectangle2D.Double(b.getX(), b.getY(), b.getWidth(), 1.0);
            if (rect.contains(b)) this.selectedEdges.add(e);
        }
    }

    void addToSelectedEdges(Edge edge) {
        if (!this.selectedEdges.contains(edge)) this.selectedEdges.add(edge); else this.selectedEdges.remove(edge);
    }

    boolean removeFromSelectedEdges(Edge edge) {
        return this.selectedEdges.remove(edge);
    }

    void unselectNodes(final Set<Node> s) {
        this.selectedNodes.removeAll(s);
    }

    void unselectEdges(final Set<Edge> s) {
        this.selectedEdges.removeAll(s);
    }

    void setAnimationSpeed(final int value) {
        assert ((value >= 0) && (value <= 100)) : value;
        this.speed = value;
    }

    void setAskLabel(final boolean al) {
        this.mouseController.setAskLabel(al);
    }

    public void setCurrentArrowType(final ArrowType type) {
        this.currentArrowType = type;
        this.repaint();
    }

    public void setCurrentEdgeType(final int type) {
        this.currentEdgeType = type;
        this.repaint();
    }

    public void setDrawOptionsValue(final int v) {
        this.drawValue = v;
        this.repaint();
    }

    public void setDynaValue(final int value) {
        this.labelDistance = value;
    }

    public void setLabelType(final LabelType type) {
        this.labelType = type;
        this.repaint();
    }

    public void setNumericalLabels(final boolean n) {
        this.mouseController.setNumericalLabels(n);
    }

    public void setThickness(final double t) {
        this.thick = t;
        this.repaint();
    }

    /**
	 * Enables or disables walking edges.
	 * @param walking
	 */
    public void setWalkingEdges(final boolean walking) {
        this.walkingEdges = walking;
    }

    /**
	 * Locks or unlocks the workspace i.e. disables or enables mouseevents on it
	 * @param b
	 */
    void setWorkspaceLock(final boolean b) {
        if (b == true) {
            this.controller.getConfig().set("lock", "true");
        } else {
            this.controller.getConfig().set("lock", "false");
        }
        this.mouseController.setLock(b);
    }

    boolean isWorkspaceLocked() {
        return this.mouseController.getLock();
    }

    void setZoomOptionsValue(final double z) {
        this.zoomValue = z;
        this.transformation.setTransform(this.zoomValue, 0.0, 0.0, this.zoomValue, 0.0, 0.0);
        this.controller.updateView();
    }

    void setCurrentMenuSetting(NEVADAView.MenuSetting m) {
        this.currentMenuSetting = m;
    }

    NEVADAView.MenuSetting getCurrentMenuSetting() {
        return this.currentMenuSetting;
    }

    /**
	 * Tries to centrate on mouse coursor during zoom
	 */
    void modifyZoomOptionsValue(final double value, Point2D p) {
        Rectangle rect = this.controller.getScrollPane().getViewport().getViewRect();
        double w = p.getX() - rect.getX();
        double h = p.getY() - rect.getY();
        Point2D p1 = p;
        double z = (value / 25.0) * this.zoomValue;
        if (this.zoomValue - z < 0.01) {
            this.zoomValue = 0.01;
        } else {
            if (this.zoomValue - z > 100) {
                this.zoomValue = 100;
            } else this.zoomValue = this.zoomValue - z;
        }
        this.transformation.setTransform(this.zoomValue, 0.0, 0.0, this.zoomValue, 0.0, 0.0);
        Point2D p2 = transformation.transform(p1, null);
        double w2 = p2.getX() - w;
        if (w2 < 0) w2 = 0;
        double h2 = p2.getY() - h;
        if (h2 < 0) h2 = 0;
        rect.setLocation((int) (w2), (int) (h2));
        this.controller.getScrollPane().getViewport().scrollRectToVisible(rect);
        this.controller.updateView();
    }

    /**
	 * Shows labels around point pt
	 * @param pt
	 */
    void showLabels(final Point2D pt) {
        this.toBeLabelled.clear();
        if (this.controller.getCurrentGraph() != null) {
            for (final Node n : this.controller.getCurrentGraph().getVisibleNodes()) {
                if (n.getPosition(this.controller.getCurrentGraph()).distance(pt) < this.labelDistance) {
                    this.toBeLabelled.add(n);
                }
            }
        }
    }

    /**
	 * Starts animation
	 *
	 */
    void startThread() {
        if (this.animationThread == null) {
            this.animationThread = new Thread(this);
            this.animationThread.start();
        }
    }

    void toggleLabel(final Node n) {
        if (this.drawLabel.contains(n)) {
            this.drawLabel.remove(n);
        } else {
            this.drawLabel.add(n);
        }
    }

    private Point2D computeDockPoint(final List<Point2D> points) {
        final Point2D retVal = new Point2D.Double(0.0, 0.0);
        double distance = 0.0;
        if (points.size() == 0) {
            assert false;
            return null;
        }
        Point2D tPoint = points.get(0);
        for (int i = 1; i < points.size(); ++i) {
            distance += tPoint.distance(points.get(i));
            tPoint = points.get(i);
        }
        int ctr = 0;
        tPoint = points.get(ctr);
        double cDist = 0.0;
        while ((++ctr < points.size()) && (cDist <= distance / 2.0)) {
            cDist += tPoint.distance(points.get(ctr));
        }
        --ctr;
        if (ctr == 0) {
            retVal.setLocation(points.get(0));
        } else {
            retVal.setLocation((points.get(ctr).getX() + points.get(ctr - 1).getX()) / 2.0, (points.get(ctr).getY() + points.get(ctr - 1).getY()) / 2.0);
        }
        return retVal;
    }

    /**
	 * @param e
	 * @param pointVector
	 */
    private void createEdgePath(final List<Point2D> pointVector, Path2D path, final Path2D arrow) {
        switch(this.currentEdgeType) {
            case 0:
                path.moveTo((float) pointVector.get(0).getX(), (float) pointVector.get(0).getY());
                Point2D p1 = Math2D.linearInterpolation(pointVector.get(0), pointVector.get(1), 0.5);
                path.lineTo((float) p1.getX(), (float) p1.getY());
                for (int i = 1; i < pointVector.size() - 1; ++i) {
                    p1 = Math2D.linearInterpolation(pointVector.get(i), pointVector.get(i + 1), 0.5);
                    path.quadTo((float) pointVector.get(i).getX(), (float) pointVector.get(i).getY(), (float) p1.getX(), (float) p1.getY());
                }
                path.lineTo((float) pointVector.get(pointVector.size() - 1).getX(), (float) pointVector.get(pointVector.size() - 1).getY());
                break;
            case 1:
                de.uni_trier.st.nevada.view.graphics.EdgeUtilizer.createPath(pointVector, path);
                break;
            case 2:
                path.moveTo((float) pointVector.get(0).getX(), (float) pointVector.get(0).getY());
                for (final Point2D p : pointVector) {
                    path.lineTo((float) p.getX(), (float) p.getY());
                }
                break;
            case 3:
                float startx = (float) pointVector.get(0).getX();
                float starty = (float) pointVector.get(0).getY();
                float destx = (float) pointVector.get(pointVector.size() - 1).getX();
                float desty = (float) pointVector.get(pointVector.size() - 1).getY();
                path.moveTo(0.0f, 0.0f);
                path.quadTo(0.1f, -0.5f, 0.0f, -1.0f);
                final AffineTransform lEdgeTransform = new AffineTransform();
                double radius = Math.sqrt(Math.pow((desty - starty), 2.0) + Math.pow((destx - startx), 2.0));
                lEdgeTransform.translate(startx, starty);
                lEdgeTransform.rotate(-Math.atan2(startx - destx, starty - desty));
                lEdgeTransform.scale(radius, radius);
                path.transform(lEdgeTransform);
                break;
            default:
                path = null;
        }
        final Point2D lPrevPoint = pointVector.get(pointVector.size() - 2);
        final Point2D lEndPoint = pointVector.get(pointVector.size() - 1);
        switch(this.currentArrowType) {
            case StdArrow:
                arrow.moveTo(-6.0f, 8.0f);
                arrow.lineTo(0.0f, 0.0f);
                arrow.lineTo(6.0f, 8.0f);
                arrow.closePath();
                break;
            case NoArrow:
                break;
            case ThinArrow:
                arrow.moveTo(-6.0f, 8.0f);
                arrow.lineTo(0.0f, 0.0f);
                arrow.lineTo(6.0f, 8.0f);
                break;
            default:
        }
        final AffineTransform lArrowTransform = new AffineTransform();
        lArrowTransform.translate(lEndPoint.getX(), lEndPoint.getY());
        lArrowTransform.rotate(-Math.atan2(lPrevPoint.getX() - lEndPoint.getX(), lPrevPoint.getY() - lEndPoint.getY()));
        arrow.transform(lArrowTransform);
    }

    /**
	 * @param pointVector
	 * @param src
	 * @param dest
	 */
    private void createInterpolation(final List<Point2D> pointVector, final List<Point2D> src, final List<Point2D> dest) {
        for (int i = 0; i < Math.max(src.size(), dest.size()); ++i) {
            Point2D srcP;
            Point2D destP;
            if (i < src.size()) {
                srcP = src.get(i);
            } else {
                srcP = src.get(src.size() - 1);
            }
            if (i < dest.size()) {
                destP = dest.get(i);
            } else {
                destP = dest.get(dest.size() - 1);
            }
            pointVector.add(Math2D.linearInterpolation(srcP, destP, this.animationInterpolator.transformValue(this.doneSteps / (double) this.maxSteps, false)));
        }
    }

    private Dimension2D getViewDimension(final Node n) {
        Dimension2D dimension;
        final Dimension2D src = n.getDimension(this.controller.getCurrentGraph());
        Dimension2D dest;
        if (this.controller.getNextGraph() == null) {
            dest = null;
        } else {
            dest = n.getDimension(this.controller.getNextGraph());
        }
        if ((src != null) && (dest != null)) {
            dimension = Math2D.linearInterpolation(n.getDimension(this.controller.getCurrentGraph()), n.getDimension(this.controller.getNextGraph()), this.doneSteps / (double) this.maxSteps);
        } else {
            dimension = (src == null ? dest : src);
        }
        return dimension;
    }

    /**
	 * Returns current position. Delivers null, if object is not visible!
	 *
	 * @param n
	 * @return
	 */
    private Point2D getViewLocation(final Node n) {
        Point2D location;
        Point2D dest;
        final Point2D src = n.getPosition(this.controller.getCurrentGraph());
        if (this.controller.getNextGraph() == null) {
            dest = null;
        } else {
            dest = n.getPosition(this.controller.getNextGraph());
        }
        if ((src != null) && (dest != null)) {
            location = Math2D.linearInterpolation(n.getPosition(this.controller.getCurrentGraph()), n.getPosition(this.controller.getNextGraph()), this.animationInterpolator.transformValue(this.doneSteps / (double) this.maxSteps, false));
        } else {
            location = (src == null ? dest : src);
        }
        return location;
    }

    private List<Point2D> makeNiceAnimation(final List<Point2D> pts, final boolean fadeOut) {
        if (this.walkingEdges) {
            final List<Point2D> retVal = new LinkedList<Point2D>();
            final double index = Math.pow(((double) this.doneSteps / (double) (this.maxSteps + 1)), (fadeOut ? 0.5 : 5.00)) * pts.size();
            final double cIndex = Math.round(Math.floor(index));
            final double dist = index - cIndex;
            if (fadeOut) {
                Point2D newPt;
                if (cIndex + 1 < pts.size()) {
                    newPt = new Point2D.Double(dist * pts.get((int) cIndex + 1).getX() + (1.0 - dist) * pts.get((int) cIndex).getX(), dist * pts.get((int) cIndex + 1).getY() + (1.0 - dist) * pts.get((int) cIndex).getY());
                } else {
                    newPt = pts.get((int) cIndex);
                }
                for (int i = 0; i < pts.size(); ++i) {
                    if (i <= cIndex) {
                        retVal.add(newPt);
                    } else {
                        retVal.add(pts.get(i));
                    }
                }
            } else {
                Point2D newPt;
                if (cIndex + 1 < pts.size()) {
                    newPt = new Point2D.Double(dist * pts.get((int) cIndex + 1).getX() + (1.0 - dist) * pts.get((int) cIndex).getX(), dist * pts.get((int) cIndex + 1).getY() + (1.0 - dist) * pts.get((int) cIndex).getY());
                } else {
                    newPt = pts.get((int) cIndex);
                }
                for (int i = 0; i < pts.size(); ++i) {
                    if (i <= cIndex) {
                        retVal.add(pts.get(i));
                    } else {
                        retVal.add(newPt);
                    }
                }
            }
            assert (retVal.size() == pts.size()) : retVal.size() - pts.size();
            return retVal;
        }
        return pts;
    }

    private void paintEdge(final Graphics2D g, final Edge e) {
        List<Point2D> pointVector = new ArrayList<Point2D>();
        List<Point2D> src = null;
        List<Point2D> dest = null;
        Graph cg = null, ng = null;
        if ((cg = this.controller.getCurrentGraph()) == null) {
            return;
        }
        if (cg.getVisibleEdges().contains(e)) {
            src = e.getPointVector(cg);
        }
        if (!((ng = this.controller.getNextGraph()) == null) && (ng.getVisibleEdges().contains(e))) {
            dest = e.getPointVector(ng);
        }
        if ((src != null) && (dest != null)) {
            this.createInterpolation(pointVector, src, dest);
        } else {
            pointVector = (src == null ? dest : src);
        }
        if (pointVector == null) {
            return;
        }
        float thickness = (float) this.thick;
        if ((e.getThickness(cg) > 0.0) || (e.getThickness(ng) > 0.0)) {
            float newThickness = (float) e.getThickness(cg);
            float newThickness2 = (float) e.getThickness(ng);
            newThickness = (newThickness > 0.0f ? newThickness : 0.0f);
            newThickness2 = (newThickness2 > 0.0f ? newThickness2 : 0.0f);
            newThickness = (1.0f - (float) this.doneSteps / (float) this.maxSteps) * newThickness + ((float) this.doneSteps / (float) this.maxSteps) * newThickness2;
            thickness = newThickness;
        } else if (e.getProperty("thickness") != null) {
            try {
                thickness = (float) Double.parseDouble(e.getProperty("thickness"));
            } catch (final NumberFormatException exn) {
            }
        }
        final Path2D edgeShape = new Path2D.Float();
        final Path2D arrowShape = new Path2D.Float();
        Point2D labelPoint = null;
        double drawingIntensity = 1.0;
        if ((src != null) && ((this.doneSteps == 0) || (dest == null))) {
            src = this.makeNiceAnimation(src, true);
            this.createEdgePath(src, edgeShape, arrowShape);
            labelPoint = this.computeDockPoint(src);
            e.setLabelPoint(labelPoint);
            drawingIntensity = this.transparencyInterpolator.transformValue(this.doneSteps / (double) this.maxSteps, false);
            if (this.drawValue < 25.0) {
                drawingIntensity = (drawingIntensity > 0.5 ? 1.0 : 0.0);
            }
        } else if ((this.doneSteps > 0) && (src != null) && (dest != null)) {
            this.createEdgePath(pointVector, edgeShape, arrowShape);
            labelPoint = this.computeDockPoint(pointVector);
            e.setLabelPoint(labelPoint);
            drawingIntensity = 1.0;
        } else if ((dest != null) && (src == null)) {
            dest = this.makeNiceAnimation(dest, false);
            this.createEdgePath(dest, edgeShape, arrowShape);
            labelPoint = this.computeDockPoint(dest);
            e.setLabelPoint(labelPoint);
            drawingIntensity = this.transparencyInterpolator.transformValue(this.doneSteps / (double) this.maxSteps, true);
            if (this.drawValue < 25.0) {
                drawingIntensity = (drawingIntensity > 0.5 ? 1.0 : 0.0);
            }
        }
        Color paintColor = Color.black;
        if (e.getEdgeColor(cg) != null) {
            paintColor = e.getEdgeColor(cg);
        } else if (e.getProperty("color.edge") != null) {
            paintColor = Color.decode(e.getProperty("color.edge"));
        }
        if (e.getArrowColor(cg) != null) {
            paintColor = e.getArrowColor(cg);
        }
        if (e.getProperty("color.arrow") != null) {
            paintColor = Color.decode(e.getProperty("color.arrow"));
        }
        Color paintColorTemp;
        if (this.controller.getNextGraph() != null) {
            Color nextCol;
            if (ng != null && e.getEdgeColor(ng) != null) {
                double d = colorInterpolator.transformValue(this.doneSteps / (double) this.maxSteps, true);
                nextCol = e.getEdgeColor(ng);
                paintColorTemp = new Color((int) ((1 - d) * paintColor.getRed() + d * nextCol.getRed()), (int) ((1 - d) * paintColor.getGreen() + d * nextCol.getGreen()), (int) ((1 - d) * paintColor.getBlue() + d * nextCol.getBlue()));
            } else paintColorTemp = paintColor;
        } else paintColorTemp = paintColor;
        Color arrowPaintColorTemp;
        if (ng != null) {
            Color nextCol;
            if (ng != null && e.getEdgeColor(ng) != null) {
                double d = colorInterpolator.transformValue(this.doneSteps / (double) this.maxSteps, true);
                nextCol = e.getEdgeColor(ng);
                arrowPaintColorTemp = new Color((int) ((1 - d) * paintColor.getRed() + d * nextCol.getRed()), (int) ((1 - d) * paintColor.getGreen() + d * nextCol.getGreen()), (int) ((1 - d) * paintColor.getBlue() + d * nextCol.getBlue()));
            } else arrowPaintColorTemp = paintColor;
        } else arrowPaintColorTemp = paintColor;
        e.putPath(cg, edgeShape);
        if (this.currentMenuSetting == NEVADAView.MenuSetting.NetworkPattern) {
            if (this.edgeFadeNetworkPattern && this.networkPatternMetric != null) {
                if (!this.nodeHighlightNetworkPattern.contains(e.getSource()) || !this.nodeHighlightNetworkPattern.contains(e.getTarget())) drawingIntensity = this.highlightIntensity;
            }
            if (this.showEdgeHighlightNetworkPattern && this.networkPatternMetric != null) {
                if (this.edgeHighlightNetworkPattern.contains(e)) this.graphUI.paintEdgeUI(g, edgeShape, arrowShape, drawingIntensity, thickness, paintColorTemp, arrowPaintColorTemp, this.selectedEdges.contains(e)); else this.graphUI.paintEdgeUI(g, edgeShape, arrowShape, this.highlightIntensity, thickness, paintColorTemp, arrowPaintColorTemp, this.selectedEdges.contains(e));
            } else {
                this.graphUI.paintEdgeUI(g, edgeShape, arrowShape, drawingIntensity, thickness, paintColorTemp, arrowPaintColorTemp, this.selectedEdges.contains(e));
            }
        } else this.graphUI.paintEdgeUI(g, edgeShape, arrowShape, drawingIntensity, thickness, paintColorTemp, arrowPaintColorTemp, this.selectedEdges.contains(e));
        String label = e.getLabel(cg);
        if (label != null && !label.equals("") && de.uni_trier.st.nevada.view.EdgeMenu.showLabels()) {
            this.graphUI.paintEdgeDockUI(g, labelPoint, label, drawingIntensity);
        } else if (de.uni_trier.st.nevada.view.EdgeMenu.showLabels()) {
            this.graphUI.paintEdgeDockUI(g, labelPoint, "X", drawingIntensity);
        }
    }

    private void paintGraph(final Graphics2D g) {
        final Set<Node> drawableNodeSet = new HashSet<Node>();
        final Set<Edge> drawableEdgeSet = new HashSet<Edge>();
        if (this.controller.getCurrentGraph() != null) {
            drawableNodeSet.addAll(this.controller.getCurrentGraph().getVisibleNodes());
            drawableEdgeSet.addAll(this.controller.getCurrentGraph().getVisibleEdges());
        }
        if (this.controller.getNextGraph() != null) {
            drawableNodeSet.addAll(this.controller.getNextGraph().getVisibleNodes());
            drawableEdgeSet.addAll(this.controller.getNextGraph().getVisibleEdges());
        }
        for (final Node n : drawableNodeSet) {
            this.paintNodeMetric(g, n);
            this.paintNode(g, n);
        }
        if (de.uni_trier.st.nevada.view.central.GraphAnimationView.mouseOverNode != null) {
            Color col = new Color(255, 0, 0, 28);
            String hexColor = "0x" + Integer.toHexString((col.getRGB() & 0xffffff) | 0x1000000).substring(1);
            de.uni_trier.st.nevada.view.central.GraphAnimationView.mouseOverNode.setProperty("color.inside", hexColor);
            this.paintNodeWithID(g, de.uni_trier.st.nevada.view.central.GraphAnimationView.mouseOverNode.getID());
            de.uni_trier.st.nevada.view.central.GraphAnimationView.mouseOverNode.setProperty("color.inside", "0xFFFFFF");
        }
        for (final Edge e : drawableEdgeSet) {
            this.paintEdge(g, e);
        }
        for (final Node n : drawableNodeSet) {
            if (n.getProperty("label") != null) {
                this.paintLabel(g, n);
            }
        }
    }

    private void paintInteraction(final Graphics2D g) {
        this.graphUI.paintInteractionUI(g, this.mouseController.getSrcCandidate().getCenter(this.controller.getCurrentGraph()), this.mouseController.getLastVisited());
    }

    private boolean selected;

    public void setSelected(boolean bol) {
        this.selected = bol;
    }

    public boolean isSelected() {
        return selected;
    }

    private void paintRectangle(final Graphics2D g) {
        if (this.mouseController.getRect() != null) this.graphUI.paintSelectionRectUI(g, this.mouseController.getRect());
    }

    private void paintNodeMetric(final Graphics2D g, final Node n) {
        if (this.currentMenuSetting == NEVADAView.MenuSetting.NodeMetric) {
            final Point2D location = this.getViewLocation(n);
            if (location == null) return;
            String output = "#";
            double d = 0.0;
            if (this.nodeMetric != null) {
                switch(this.nodeMetricType) {
                    case None:
                        output = "";
                        break;
                    case HalfSize:
                    case NormalSize:
                        if (!this.controller.animationRunning()) {
                            if (this.nodeMetricNormalized) d = this.nodeMetric.normalizedValue(this.controller.getCurrentGraph(), n, this.nodeMetricDirected); else d = this.nodeMetric.value(this.controller.getCurrentGraph(), n, this.nodeMetricDirected);
                            output = Double.toString(d);
                            if (output.length() > output.lastIndexOf(".") + this.digits + 1) output = output.substring(0, output.indexOf(".") + this.digits + 1);
                        } else output = "";
                }
            } else {
                if (this.nodeMetricType == NodeMetricType.None) output = "";
            }
            final Dimension2D nodeDim = n.getDimension(this.controller.getCurrentGraph());
            Font temp = g.getFont();
            Font f = g.getFont().deriveFont(this.nodeMetricFontSize);
            g.setFont(f);
            final int width = g.getFontMetrics().stringWidth(output);
            final int height = g.getFontMetrics().getAscent();
            this.graphUI.paintNodeMetricUI(g, location, nodeDim, output, 1.0f, width, height, this.visType, this.nodeMetricNormalized, d, (this.nodesInFocus.contains(n)));
            g.setFont(temp);
        }
    }

    /**
	 * @param g
	 * @param n
	 * @param location
	 */
    private void paintLabel(final Graphics2D g, final Node n) {
        final Point2D location = this.getViewLocation(n);
        if (location == null) {
            return;
        }
        String label = null;
        if ((this.drawLabel.contains(n)) || ((n.getProperty("label.show") != null) && (n.getProperty("label.show").equals("true")))) {
            label = n.getProperty("label");
        } else {
            switch(this.labelType) {
                case Dynamic:
                    if (!this.toBeLabelled.contains(n)) {
                        return;
                    }
                    label = n.getProperty("label");
                    break;
                case HalfSize:
                case NormalSize:
                    label = n.getProperty("label");
                    break;
                case Short:
                    if (n.getProperty("label").length() > 2) {
                        label = n.getProperty("label").substring(0, 3).concat("...");
                    } else {
                        label = n.getProperty("label");
                    }
                    break;
                case None:
                    break;
                default:
            }
        }
        if ((label != null) && (!label.equals(""))) {
            float intensity;
            if (this.controller.getNextGraph() != null) {
                if (!this.controller.getCurrentGraph().getVisibleNodes().contains(n) && this.controller.getNextGraph().getVisibleNodes().contains(n)) {
                    intensity = (float) this.transparencyInterpolator.transformValue(this.doneSteps / (double) this.maxSteps, true);
                } else if ((this.doneSteps > 0) && this.controller.getCurrentGraph().getVisibleNodes().contains(n) && !this.controller.getNextGraph().getVisibleNodes().contains(n)) {
                    intensity = (float) this.transparencyInterpolator.transformValue(this.doneSteps / (double) this.maxSteps, false);
                } else {
                    intensity = 1.0f;
                }
            } else {
                intensity = 1.0f;
            }
            Font f;
            if (n.getProperty("label.size") == null) {
                f = g.getFont().deriveFont(((this.labelType == LabelType.HalfSize) || (this.labelType == LabelType.Dynamic) ? 11.0f : 22.0f));
            } else {
                f = g.getFont().deriveFont(Float.parseFloat(n.getProperty("label.size")));
            }
            g.setFont(f);
            final int width = g.getFontMetrics().stringWidth(label);
            final int height = g.getFontMetrics().getAscent();
            final Dimension2D nodeDim = n.getDimension(this.controller.getCurrentGraph());
            this.graphUI.paintLabelUI(g, location, nodeDim, label, intensity, width, height, this.visType);
        }
    }

    private void paintNode(final Graphics2D g, final Node n) {
        Point2D currentP = null;
        Dimension2D currentD = null;
        if ((this.controller.getCurrentGraph() != null) && this.controller.getCurrentGraph().getVisibleNodes().contains(n)) {
            currentP = n.getPosition(this.controller.getCurrentGraph());
            currentD = n.getDimension(this.controller.getCurrentGraph());
        }
        Point2D nextP = null;
        Dimension2D nextD = null;
        if ((this.controller.getNextGraph() != null) && this.controller.getNextGraph().getVisibleNodes().contains(n)) {
            nextP = n.getPosition(this.controller.getNextGraph());
            nextD = n.getDimension(this.controller.getNextGraph());
        }
        final Point2D animateP = this.getViewLocation(n);
        final Dimension2D animateD = this.getViewDimension(n);
        if ((currentP != null) && ((nextP == null) || (this.doneSteps == 0))) {
            final double currentVal = this.transparencyInterpolator.transformValue(this.doneSteps / (double) this.maxSteps, false);
            this.paintSingleNode(g, n, currentP, currentD, currentVal);
        } else if ((this.doneSteps > 0) && (currentP != null) && (nextP != null)) {
            this.paintSingleNode(g, n, animateP, animateD, 1.0f);
        } else if ((nextP != null) && (currentP == null)) {
            final double nextVal = this.transparencyInterpolator.transformValue(this.doneSteps / (double) this.maxSteps, true);
            this.paintSingleNode(g, n, nextP, nextD, nextVal);
        }
    }

    private void paintNodeWithID(final Graphics2D g, String id) {
        Point2D currentP = null;
        Dimension2D currentD = null;
        Node n = null;
        boolean contains = false;
        if (this.controller.getCurrentGraph() != null) {
            for (Node nod : this.controller.getCurrentGraph().getVisibleNodes()) {
                if (nod.getID().equalsIgnoreCase(id)) {
                    n = nod;
                    contains = true;
                    currentP = nod.getPosition(this.controller.getCurrentGraph());
                    currentD = nod.getDimension(this.controller.getCurrentGraph());
                }
            }
        }
        if (contains) {
            Color col = new Color(255, 0, 0, 28);
            String hexColor = "0x" + Integer.toHexString((col.getRGB() & 0xffffff) | 0x1000000).substring(1);
            n.setProperty("color.inside", hexColor);
            this.paintSingleNode(g, n, currentP, currentD, 1.0f);
            n.setProperty("color.inside", "0xFFFFFF");
        }
    }

    private void paintSingleNode(final Graphics2D g, final Node n, final Point2D location, final Dimension2D dimension, double intensity) {
        intensity = Math.min(1.0, Math.max(intensity, 0.0));
        if (this.drawValue < 25) {
            intensity = (intensity > 0.5 ? 1.0 : 0.0);
        }
        if (n.getImageString(this.controller.getCurrentGraph()) != null) {
            final String imgString = n.getImageString(this.controller.getCurrentGraph());
            this.graphUI.paintNodeImgUI(g, location, dimension, imgString);
        } else {
            Shape e;
            if (this.visType == VisType.Nested) {
                e = new Rectangle2D.Double(location.getX(), location.getY(), dimension.getWidth(), dimension.getHeight());
            } else {
                if (de.uni_trier.st.nevada.view.NodeMenu.nodeShape == de.uni_trier.st.nevada.view.NodeMenu.NodeShape.Rectangle) {
                    e = new RoundRectangle2D.Double(location.getX(), location.getY(), dimension.getWidth(), dimension.getHeight(), dimension.getWidth() / 2, dimension.getHeight() / 2);
                } else {
                    e = new Ellipse2D.Double(location.getX(), location.getY(), dimension.getWidth(), dimension.getHeight());
                }
            }
            Color paintColor;
            if (n.getInsideColor(this.controller.getCurrentGraph()) != null) {
                paintColor = n.getInsideColor(this.controller.getCurrentGraph());
            } else if (n.getProperty("color.inside") != null) {
                paintColor = Color.decode(n.getProperty("color.inside"));
            } else {
                paintColor = Color.white;
            }
            Color outlineColor;
            if (n.getOutsideColor(this.controller.getCurrentGraph()) != null) outlineColor = n.getOutsideColor(this.controller.getCurrentGraph()); else if (n.getProperty("color.outside") != null) {
                outlineColor = Color.decode(n.getProperty("color.outside"));
            } else {
                outlineColor = Color.black;
            }
            Color temp;
            if (this.controller.getNextGraph() != null) {
                Color nextCol;
                if (this.controller.getNextGraph() != null && n.getInsideColor(this.controller.getNextGraph()) != null) {
                    double d = colorInterpolator.transformValue(this.doneSteps / (double) this.maxSteps, true);
                    nextCol = n.getInsideColor(this.controller.getNextGraph());
                    temp = new Color((int) ((1 - d) * paintColor.getRed() + d * nextCol.getRed()), (int) ((1 - d) * paintColor.getGreen() + d * nextCol.getGreen()), (int) ((1 - d) * paintColor.getBlue() + d * nextCol.getBlue()));
                } else temp = paintColor;
            } else temp = paintColor;
            boolean isInFocus = false;
            if (this.currentMenuSetting == NEVADAView.MenuSetting.NodeSequence) isInFocus = this.nodesInFocus.contains(n);
            if (this.currentMenuSetting == NEVADAView.MenuSetting.NetworkPattern) {
                if (this.showNodeHighlightNetworkPattern && this.networkPatternMetric != null) {
                    if (this.nodeHighlightNetworkPattern.contains(n)) this.graphUI.paintNodeUI(g, e, temp, outlineColor, intensity, this.visType, n.isLeaf(this.controller.getCurrentGraph()), n.isFolded(this.controller.getCurrentGraph()), this.selectedNodes.contains(n), isInFocus, Color.blue); else this.graphUI.paintNodeUI(g, e, temp, outlineColor, this.highlightIntensity, this.visType, n.isLeaf(this.controller.getCurrentGraph()), n.isFolded(this.controller.getCurrentGraph()), this.selectedNodes.contains(n), isInFocus, Color.blue);
                } else if (this.showSequenceMetric && this.sequenceMetric != null) {
                    if (this.sequenceMetric.findPattern(this.controller.getGraphs(), true).get(this.controller.getCurrentIndex()).getNodes().contains(n)) this.graphUI.paintNodeUI(g, e, temp, outlineColor, intensity, this.visType, n.isLeaf(this.controller.getCurrentGraph()), n.isFolded(this.controller.getCurrentGraph()), this.selectedNodes.contains(n), isInFocus, Color.blue); else this.graphUI.paintNodeUI(g, e, temp, outlineColor, this.highlightIntensity, this.visType, n.isLeaf(this.controller.getCurrentGraph()), n.isFolded(this.controller.getCurrentGraph()), this.selectedNodes.contains(n), isInFocus, Color.blue);
                } else this.graphUI.paintNodeUI(g, e, temp, outlineColor, intensity, this.visType, n.isLeaf(this.controller.getCurrentGraph()), n.isFolded(this.controller.getCurrentGraph()), this.selectedNodes.contains(n), isInFocus, Color.blue);
            } else this.graphUI.paintNodeUI(g, e, temp, outlineColor, intensity, this.visType, n.isLeaf(this.controller.getCurrentGraph()), n.isFolded(this.controller.getCurrentGraph()), this.selectedNodes.contains(n), isInFocus, this.colorFocusMap.get(n));
        }
    }
}
