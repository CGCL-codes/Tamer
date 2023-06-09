package ch.ethz.sg.cuttlefish.gui2;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.swing.JPanel;
import org.apache.commons.collections15.Predicate;
import org.apache.commons.collections15.Transformer;
import ch.ethz.sg.cuttlefish.gui.mouse.MouseMenus;
import ch.ethz.sg.cuttlefish.gui.mouse.PopupMousePlugin;
import ch.ethz.sg.cuttlefish.gui2.INetworkBrowser;
import ch.ethz.sg.cuttlefish.gui2.tasks.SetLayoutWorker;
import ch.ethz.sg.cuttlefish.layout.ARF2Layout;
import ch.ethz.sg.cuttlefish.layout.FixedLayout;
import ch.ethz.sg.cuttlefish.layout.KCoreLayout;
import ch.ethz.sg.cuttlefish.layout.WeightedARF2Layout;
import ch.ethz.sg.cuttlefish.misc.Edge;
import ch.ethz.sg.cuttlefish.misc.EdgeFactory;
import ch.ethz.sg.cuttlefish.misc.Observer;
import ch.ethz.sg.cuttlefish.misc.Subject;
import ch.ethz.sg.cuttlefish.misc.Vertex;
import ch.ethz.sg.cuttlefish.misc.VertexFactory;
import ch.ethz.sg.cuttlefish.networks.BrowsableForestNetwork;
import ch.ethz.sg.cuttlefish.networks.BrowsableNetwork;
import ch.ethz.sg.cuttlefish.networks.CxfNetwork;
import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout2;
import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.RadialTreeLayout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout2;
import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Context;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.EditingModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.picking.ShapePickSupport;
import edu.uci.ics.jung.visualization.transform.MutableTransformer;

public class NetworkPanel extends JPanel implements Subject, ItemListener, INetworkBrowser, Runnable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private BrowsableNetwork network = null;

    private VisualizationViewer<Vertex, Edge> visualizationViewer = null;

    private EditingModalGraphMouse<Vertex, Edge> graphMouse;

    private Layout<Vertex, Edge> layout = null;

    private String currentLayout = null;

    private String layoutType = null;

    private List<Observer> observers = null;

    private StatusBar statusBar = null;

    private SetLayoutWorker setLayoutWorker = null;

    private int width;

    private int height;

    private VertexFactory vertexFactory = null;

    private EdgeFactory edgeFactory = null;

    public NetworkPanel() {
        super();
        width = 1096;
        height = 200;
        initialize();
    }

    public NetworkPanel(int width, int height) {
        super();
        this.width = width;
        this.height = height;
        initialize();
    }

    /**
	 * Getter for JUNG's VisualizationViewer, creating it if it does not exist
	 * @return VisualizationViewer	
	 */
    public VisualizationViewer<Vertex, Edge> getVisualizationViewer() {
        if (visualizationViewer == null) {
            if (layout == null) {
                synchronized (this) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            visualizationViewer = new VisualizationViewer<Vertex, Edge>(layout, new Dimension(width, height));
            visualizationViewer.setBackground(Color.WHITE);
        }
        return visualizationViewer;
    }

    public StatusBar getStatusBar() {
        if (statusBar == null) statusBar = new StatusBar();
        return statusBar;
    }

    public String getCurrentLayout() {
        return currentLayout;
    }

    private void initialize() {
        observers = new LinkedList<Observer>();
        BrowsableNetwork temp = new BrowsableNetwork();
        this.setNetwork(temp);
        this.setLayout(new BorderLayout());
        this.add(getStatusBar(), BorderLayout.SOUTH);
        this.setSize(width, height);
        this.add(getVisualizationViewer(), BorderLayout.CENTER);
        vertexFactory = new VertexFactory();
        edgeFactory = new EdgeFactory();
        graphMouse = new EditingModalGraphMouse<Vertex, Edge>(getVisualizationViewer().getRenderContext(), vertexFactory, edgeFactory);
        graphMouse.getAnnotatingPlugin().setAnnotationColor(getForeground());
        graphMouse.setMode(ModalGraphMouse.Mode.TRANSFORMING);
        graphMouse.remove(graphMouse.getPopupEditingPlugin());
        PopupMousePlugin<Vertex, Edge> popupMouse = new PopupMousePlugin<Vertex, Edge>(this);
        popupMouse.setVertexPopup(new MouseMenus.VertexMenu());
        popupMouse.setEdgePopup(new MouseMenus.EdgeMenu(null));
        graphMouse.add(popupMouse);
        RenderContext<Vertex, Edge> renderContext = getVisualizationViewer().getRenderContext();
        Transformer<Vertex, Shape> vertexShapeTransformer = new Transformer<Vertex, Shape>() {

            public Shape transform(Vertex vertex) {
                return vertex.getShape();
            }
        };
        Transformer<Vertex, String> vertexLabelTransformer = new Transformer<Vertex, String>() {

            public String transform(Vertex vertex) {
                if (network instanceof CxfNetwork) if (((CxfNetwork) network).hideVertexLabels()) return null;
                return vertex.getLabel();
            }
        };
        Transformer<Vertex, Stroke> vertexStrokeTransformer = new Transformer<Vertex, Stroke>() {

            public Stroke transform(Vertex vertex) {
                return new BasicStroke(new Double(vertex.getWidth()).intValue());
            }
        };
        Transformer<Vertex, Paint> vertexPaintTransformer = new Transformer<Vertex, Paint>() {

            public Paint transform(Vertex vertex) {
                return vertex.getFillColor();
            }
        };
        Transformer<Vertex, Paint> vertexBorderTransformer = new Transformer<Vertex, Paint>() {

            public Paint transform(Vertex vertex) {
                return vertex.getColor();
            }
        };
        Predicate<Context<Graph<Vertex, Edge>, Vertex>> vertexIncludePredicate = new Predicate<Context<Graph<Vertex, Edge>, Vertex>>() {

            public boolean evaluate(Context<Graph<Vertex, Edge>, Vertex> context) {
                Vertex vertex = context.element;
                return !vertex.isExcluded();
            }
        };
        renderContext.setVertexShapeTransformer(vertexShapeTransformer);
        renderContext.setVertexLabelTransformer(vertexLabelTransformer);
        renderContext.setVertexDrawPaintTransformer(vertexBorderTransformer);
        renderContext.setVertexIncludePredicate(vertexIncludePredicate);
        renderContext.setLabelOffset(20);
        renderContext.setVertexStrokeTransformer(vertexStrokeTransformer);
        renderContext.setVertexFillPaintTransformer(vertexPaintTransformer);
        Transformer<Edge, Paint> edgePaintTransformer = new Transformer<Edge, Paint>() {

            public Paint transform(Edge edge) {
                return edge.getColor();
            }
        };
        Transformer<Edge, String> edgeLabelTransformer = new Transformer<Edge, String>() {

            public String transform(Edge edge) {
                if (network instanceof CxfNetwork) if (((CxfNetwork) network).hideEdgeLabels()) return null;
                return edge.getLabel();
            }
        };
        Transformer<Context<Graph<Vertex, Edge>, Edge>, Shape> edgeShapeTransformer = new Transformer<Context<Graph<Vertex, Edge>, Edge>, Shape>() {

            public Shape transform(Context<Graph<Vertex, Edge>, Edge> context) {
                if (network instanceof CxfNetwork) {
                    if (((CxfNetwork) network).getEdgeShape() == "line") {
                        return new EdgeShape.Line<Vertex, Edge>().transform(context);
                    }
                } else if (network instanceof BrowsableForestNetwork) {
                    if (((BrowsableForestNetwork) network).getEdgeShape() == "line") {
                        return new EdgeShape.Line<Vertex, Edge>().transform(context);
                    }
                }
                return new EdgeShape.QuadCurve<Vertex, Edge>().transform(context);
            }
        };
        renderContext.setEdgeShapeTransformer(edgeShapeTransformer);
        Transformer<Edge, Stroke> edgeStrokeTransformer = new Transformer<Edge, Stroke>() {

            public Stroke transform(Edge edge) {
                return new BasicStroke(new Double(edge.getWidth()).intValue());
            }
        };
        Predicate<Context<Graph<Vertex, Edge>, Edge>> edgeIncludePredicate = new Predicate<Context<Graph<Vertex, Edge>, Edge>>() {

            public boolean evaluate(Context<Graph<Vertex, Edge>, Edge> context) {
                Edge edge = context.element;
                return !edge.isExcluded();
            }
        };
        renderContext.setEdgeDrawPaintTransformer(edgePaintTransformer);
        renderContext.setArrowFillPaintTransformer(edgePaintTransformer);
        renderContext.setArrowDrawPaintTransformer(edgePaintTransformer);
        renderContext.setEdgeLabelTransformer(edgeLabelTransformer);
        renderContext.setEdgeStrokeTransformer(edgeStrokeTransformer);
        renderContext.setEdgeIncludePredicate(edgeIncludePredicate);
        getVisualizationViewer().setPickSupport(new ShapePickSupport<Vertex, Edge>(getVisualizationViewer()));
        getVisualizationViewer().setGraphMouse(graphMouse);
        getVisualizationViewer().getPickedVertexState().addItemListener(this);
        getVisualizationViewer().setDoubleBuffered(true);
    }

    @Override
    public Layout<Vertex, Edge> getNetworkLayout() {
        return layout;
    }

    @Override
    public void repaintViewer() {
        getVisualizationViewer().repaint();
    }

    /**
	 * Getter for the network
	 * @return DirectedSparseGraph network in use in CuttleFish
	 */
    public BrowsableNetwork getNetwork() {
        return network;
    }

    @Override
    public void setNetwork(BrowsableNetwork network) {
        this.network = network;
        if (layout == null) {
            setLayout("ARFLayout");
            while (layout == null) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } else if (layout instanceof TreeLayout) {
            network = new BrowsableForestNetwork(network);
            layout.setGraph(network);
        }
        if (layout != null && layout.getGraph() != network) layout.setGraph(network);
        if ((layout != null && layout instanceof ARF2Layout) && (((ARF2Layout<Vertex, Edge>) layout).getMaxUpdates() < getNetwork().getVertexCount())) ((ARF2Layout<Vertex, Edge>) layout).setMaxUpdates(getNetwork().getVertexCount());
        network.init();
        refreshAnnotations();
    }

    @Override
    public void refreshAnnotations() {
        network.updateAnnotations();
        network.applyShadows();
        System.gc();
        this.repaintViewer();
    }

    @Override
    public void onNetworkChange() {
        if (layout instanceof ARF2Layout) {
            ((ARF2Layout<Vertex, Edge>) layout).step();
            ((ARF2Layout<Vertex, Edge>) layout).resetUpdates();
        } else if (layout instanceof WeightedARF2Layout) {
            ((WeightedARF2Layout<Vertex, Edge>) layout).step();
            ((WeightedARF2Layout<Vertex, Edge>) layout).resetUpdates();
        }
        if (layout instanceof FixedLayout) {
            ((FixedLayout<Vertex, Edge>) layout).update();
        }
        if (layout instanceof TreeLayout || layout instanceof RadialTreeLayout || layout instanceof CircleLayout || layout instanceof KCoreLayout) {
            stopLayout();
            setNetwork(((BrowsableForestNetwork) getNetwork()).getOriginalNetwork());
            resumeLayout();
        }
        this.repaintViewer();
    }

    @Override
    public void stopLayout() {
        for (Vertex v : getNetwork().getVertices()) {
            layout.lock(v, true);
            if (v.isFixed()) layout.setLocation(v, v.getPosition());
        }
    }

    @Override
    public void resumeLayout() {
        setLayout(layoutType);
        for (Vertex v : getNetwork().getVertices()) if (!v.isFixed()) layout.lock(v, false); else layout.setLocation(v, v.getPosition());
    }

    @Override
    public void setLayout(String selectedLayout) {
        if (setLayoutWorker != null) {
            while (!setLayoutWorker.isDone()) {
                setLayoutWorker.cancel(true);
            }
            setLayoutWorker = null;
        }
        setLayoutWorker = new SetLayoutWorker(selectedLayout, this);
        setLayoutWorker.execute();
    }

    public void setLayoutByName(String selectedLayout) {
        layoutType = selectedLayout;
        Layout<Vertex, Edge> newLayout = null;
        currentLayout = selectedLayout;
        if (selectedLayout.equalsIgnoreCase("ARFLayout")) {
            newLayout = new ARF2Layout<Vertex, Edge>(getNetwork(), ((BrowsableNetwork) getNetwork()).isIncremental(), layout);
            ((ARF2Layout<Vertex, Edge>) newLayout).setMaxUpdates(Integer.MAX_VALUE);
            if (((ARF2Layout<Vertex, Edge>) newLayout).getMaxUpdates() < getNetwork().getVertexCount()) ((ARF2Layout<Vertex, Edge>) newLayout).setMaxUpdates(getNetwork().getVertexCount());
        }
        if (selectedLayout.equalsIgnoreCase("WeightedARFLayout")) {
            newLayout = new WeightedARF2Layout<Vertex, Edge>(getNetwork(), ((BrowsableNetwork) getNetwork()).isIncremental(), layout);
            ((WeightedARF2Layout<Vertex, Edge>) newLayout).setMaxUpdates(Integer.MAX_VALUE);
            if (((WeightedARF2Layout<Vertex, Edge>) newLayout).getMaxUpdates() < getNetwork().getVertexCount()) ((WeightedARF2Layout<Vertex, Edge>) newLayout).setMaxUpdates(getNetwork().getVertexCount());
        }
        if (selectedLayout.equalsIgnoreCase("SpringLayout")) {
            newLayout = new SpringLayout2<Vertex, Edge>(getNetwork());
            ((SpringLayout2<Vertex, Edge>) newLayout).setForceMultiplier(10);
        }
        if (selectedLayout.equalsIgnoreCase("Kamada-Kawai")) {
            newLayout = new KKLayout<Vertex, Edge>(getNetwork());
            ((KKLayout<Vertex, Edge>) newLayout).setExchangeVertices(false);
            ((KKLayout<Vertex, Edge>) newLayout).setDisconnectedDistanceMultiplier(3);
            ((KKLayout<Vertex, Edge>) newLayout).setLengthFactor(0.15);
        }
        if (selectedLayout.equalsIgnoreCase("Fruchterman-Reingold")) {
            newLayout = new FRLayout2<Vertex, Edge>(getNetwork());
        }
        if (selectedLayout.equalsIgnoreCase("ISOMLayout")) {
            newLayout = new ISOMLayout<Vertex, Edge>(getNetwork());
        }
        if (selectedLayout.equalsIgnoreCase("CircleLayout")) newLayout = new CircleLayout<Vertex, Edge>(getNetwork());
        if (selectedLayout.equalsIgnoreCase("Fixed")) newLayout = new FixedLayout<Vertex, Edge>(getNetwork(), layout);
        if (selectedLayout.equalsIgnoreCase("KCore")) newLayout = new KCoreLayout<Vertex, Edge>(getNetwork(), layout);
        if (selectedLayout.equalsIgnoreCase("TreeLayout")) {
            BrowsableForestNetwork network = new BrowsableForestNetwork((BrowsableNetwork) getNetwork());
            setNetwork(network);
            newLayout = new TreeLayout<Vertex, Edge>(network);
        }
        if (selectedLayout.equalsIgnoreCase("RadialTreeLayout")) {
            BrowsableForestNetwork network = new BrowsableForestNetwork((BrowsableNetwork) getNetwork());
            setNetwork(network);
            newLayout = new RadialTreeLayout<Vertex, Edge>(network);
        }
        layout = newLayout;
        for (Vertex v : getNetwork().getVertices()) if (v.isFixed()) layout.lock(v, true);
        getVisualizationViewer().setGraphLayout(layout);
        centerGraph();
        centerGraph();
        this.repaintViewer();
        for (Observer o : observers) o.update(this);
    }

    /**
	 * This method finds the location of the graph relative
	 * to the viewer and shifts is so that it appears in the
	 * center of JUNG's VisualizationViewer.
	 */
    private void centerGraph() {
        VisualizationViewer<Vertex, Edge> vv = getVisualizationViewer();
        MutableTransformer layout2 = vv.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.LAYOUT);
        double top = Double.MAX_VALUE;
        double bottom = Double.MAX_VALUE;
        double left = Double.MAX_VALUE;
        double right = Double.MAX_VALUE;
        for (Vertex v : getNetwork().getVertices()) {
            Point2D p;
            if (layout instanceof AbstractLayout) p = ((AbstractLayout<Vertex, Edge>) layout).transform(v); else p = ((TreeLayout<Vertex, Edge>) layout).transform(v);
            Point2D invP = layout2.transform(p);
            if (top < invP.getY() || top == Double.MAX_VALUE) top = invP.getY();
            if (bottom > invP.getY() || bottom == Double.MAX_VALUE) bottom = invP.getY();
            if (left > invP.getX() || left == Double.MAX_VALUE) left = invP.getX();
            if (right < invP.getX() || right == Double.MAX_VALUE) right = invP.getX();
        }
        if (getMouse() != null) {
            double scale = 0;
            do {
                scale = vv.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.VIEW).getScale();
                getMouse().mouseWheelMoved(new MouseWheelEvent(getVisualizationViewer(), 0, 0, 0, getVisualizationViewer().getWidth() / 2, getVisualizationViewer().getHeight() / 2, 1, false, 1, -1, -1));
            } while (scale >= 1);
        }
        vv.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.VIEW).setScale(1, 1, new Point((int) (getVisualizationViewer().getCenter().getX() - (right + left) / 2), (int) (getVisualizationViewer().getCenter().getY() - (top + bottom) / 2)));
        double deltaX = getVisualizationViewer().getCenter().getX() - (right + left) / 2;
        double deltaY = getVisualizationViewer().getCenter().getY() - (top + bottom) / 2;
        if (!Double.isInfinite(deltaX) && !Double.isInfinite(deltaY) && !Double.isNaN(deltaX) && !Double.isNaN(deltaY)) {
            layout2.translate(deltaX, deltaY);
        }
    }

    /**
	 * Getter for the graph mouse associated to the panel
	 * @return EditingModalGraphMouse automatically created by JUNG2.0
	 */
    public EditingModalGraphMouse<Vertex, Edge> getMouse() {
        return graphMouse;
    }

    @Override
    public BufferedImage getSnapshot() {
        Dimension size = getVisualizationViewer().getSize();
        BufferedImage img = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = img.createGraphics();
        getVisualizationViewer().paint(g2);
        return img;
    }

    @Override
    public Set<Vertex> getPickedVertices() {
        return getVisualizationViewer().getPickedVertexState().getPicked();
    }

    @Override
    public Set<Edge> getPickedEdges() {
        return getVisualizationViewer().getPickedEdgeState().getPicked();
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        refreshAnnotations();
    }

    @Override
    public void run() {
        while (true) ;
    }

    @Override
    public void addObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }
}
