package de.tu_darmstadt.informatik.rbg.klein.osgi.navigator;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import org.openrdf.elmo.sesame.roles.SesameEntity;
import prefuse.Constants;
import prefuse.Display;
import prefuse.Visualization;
import prefuse.action.ActionList;
import prefuse.action.RepaintAction;
import prefuse.action.animate.ColorAnimator;
import prefuse.action.animate.FontAnimator;
import prefuse.action.animate.PolarLocationAnimator;
import prefuse.action.animate.QualityControlAnimator;
import prefuse.action.animate.VisibilityAnimator;
import prefuse.action.assignment.ColorAction;
import prefuse.action.assignment.FontAction;
import prefuse.action.assignment.StrokeAction;
import prefuse.action.filter.GraphDistanceFilter;
import prefuse.action.layout.graph.RadialTreeLayout;
import prefuse.activity.SlowInSlowOutPacer;
import prefuse.controls.DragControl;
import prefuse.controls.FocusControl;
import prefuse.controls.PanControl;
import prefuse.controls.ZoomControl;
import prefuse.controls.ZoomToFitControl;
import prefuse.data.Graph;
import prefuse.render.AbstractShapeRenderer;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.EdgeRenderer;
import prefuse.render.LabelRenderer;
import prefuse.util.ColorLib;
import prefuse.util.FontLib;
import prefuse.visual.VisualItem;
import prefuse.visual.expression.HoverPredicate;
import prefuse.visual.expression.InGroupPredicate;
import prefuse.visual.tuple.TableNodeItem;
import de.tu_darmstadt.informatik.rbg.klein.osgi.navigator.actions.FinalLocationAnimator;
import de.tu_darmstadt.informatik.rbg.klein.osgi.navigator.actions.SequentialActionList;

/**
 * 
 * @author Tim Klein
 * @version 0.1
 *
 */
public class PrefuseManager {

    public static final String GROUP_GRAPH = "graph";

    public static final String GROUP_GRAPH_NODES = "graph.nodes";

    public static final String GROUP_GRAPH_EDGES = "graph.edges";

    public static final String GROUP_GRAPH_SELECTED = "graph.selected";

    static final String PROPERTY_ENTITY = "entity";

    Graph graph = null;

    Visualization visualization = null;

    Display display = null;

    Viewer viewer = null;

    GraphDistanceFilter distanceFilter = null;

    /**
	 * Prepares all needed modules.
	 * 
	 */
    public void prepare() {
        graph = createGraph();
        visualization = createVisualization();
        display = createDisplay();
        createView();
    }

    /**
	 * Update to set a new graph.
	 * 
	 */
    public void update() {
        graph = createGraph();
        visualization = createVisualization();
        display.setVisualization(visualization);
        viewer.update();
    }

    /**
	 * Create a new Graph.
	 * 
	 */
    public Graph createGraph() {
        Graph tempGraph = null;
        tempGraph = new Graph(false);
        tempGraph.addColumn(PROPERTY_ENTITY, SesameEntity.class, null);
        tempGraph.getEdgeTable().addColumn("type", String.class, null);
        return tempGraph;
    }

    /**
	 * Create a new Visualization.
	 * 
	 */
    public Visualization createVisualization() {
        Visualization visualization = null;
        LabelRenderer nodeRenderer = null;
        EdgeRenderer edgeRenderer = null;
        LabelRenderer edgeLabelRenderer = null;
        DefaultRendererFactory rendererFactory = null;
        visualization = new Visualization();
        visualization.add("graph", graph);
        visualization.setInteractive(GROUP_GRAPH_EDGES, null, false);
        nodeRenderer = new OsgiLabelRenderer();
        nodeRenderer.setRenderType(AbstractShapeRenderer.RENDER_TYPE_DRAW_AND_FILL);
        nodeRenderer.setHorizontalAlignment(Constants.CENTER);
        nodeRenderer.setRoundedCorner(8, 8);
        edgeRenderer = new EdgeRenderer();
        edgeLabelRenderer = new OsgiLabelRenderer();
        rendererFactory = new DefaultRendererFactory(nodeRenderer);
        rendererFactory.add(new InGroupPredicate(GROUP_GRAPH_EDGES), edgeRenderer);
        visualization.setRendererFactory(rendererFactory);
        ColorAction nodeColor = null;
        ColorAction textColor = null;
        ColorAction edgeColor = null;
        StrokeAction edgeStroke = null;
        StrokeAction nodeStroke = null;
        ColorAction nodeStrokeColor = null;
        FontAction fonts = null;
        RadialTreeLayout graphLayout = null;
        float[] dash1 = { 6.0f, 3.0f };
        float[] dash2 = { 6.0f, 2.0f, 1.0f, 2.0f };
        nodeColor = new ColorAction("graph.nodes", VisualItem.FILLCOLOR, ColorLib.gray(200, 255));
        nodeColor.add(new HoverPredicate(), ColorLib.gray(230, 255));
        nodeColor.add(new InGroupPredicate(Visualization.FOCUS_ITEMS), ColorLib.rgb(0, 229, 0));
        nodeColor.add(new NodeStylePredicate("Component"), ColorLib.gray(150, 255));
        nodeColor.add(new NodeStylePredicate("Interface"), ColorLib.gray(255, 255));
        nodeColor.add(new NodeStylePredicate("Property"), ColorLib.gray(200, 255));
        textColor = new ColorAction("graph.nodes", VisualItem.TEXTCOLOR, ColorLib.gray(0));
        textColor.add(new HoverPredicate(), ColorLib.rgb(255, 0, 0));
        edgeColor = new ColorAction("graph.edges", VisualItem.STROKECOLOR, ColorLib.rgb(200, 200, 200));
        edgeColor.add(new EdgeColorPredicate("maybe"), ColorLib.rgb(150, 150, 150));
        edgeColor.add(new EdgeColorPredicate("is"), ColorLib.rgb(170, 0, 0));
        edgeColor.add(new EdgeColorPredicate("has"), ColorLib.rgb(0, 0, 0));
        edgeStroke = new StrokeAction("graph.edges");
        edgeStroke.add(new EdgeStrokePredicate("maybe"), new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1.0f, dash1, 0.0f));
        edgeStroke.add(new EdgeStrokePredicate("is"), new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1.0f, dash2, 0.0f));
        nodeStroke = new StrokeAction("graph.nodes");
        nodeStroke.setDefaultStroke(new BasicStroke(1.0f));
        nodeStroke.add(new NodeStylePredicate("Interface"), new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1.0f, dash2, 0.0f));
        nodeStrokeColor = new ColorAction("graph.nodes", VisualItem.STROKECOLOR);
        fonts = new FontAction("graph.nodes", FontLib.getFont("Tahoma", 10));
        distanceFilter = new GraphDistanceFilter(GROUP_GRAPH, 10);
        ActionList recolor = null;
        ActionList repaint = null;
        ActionList animatePaint = null;
        ActionList filter = null;
        ActionList animate = null;
        ActionList filterAnimate = null;
        recolor = new ActionList();
        recolor.add(nodeColor);
        recolor.add(textColor);
        visualization.putAction("recolor", recolor);
        repaint = new ActionList();
        repaint.add(recolor);
        repaint.add(new RepaintAction());
        visualization.putAction("repaint", repaint);
        animatePaint = new ActionList(400);
        animatePaint.add(new ColorAnimator("graph.nodes"));
        animatePaint.add(new RepaintAction());
        visualization.putAction("animatePaint", animatePaint);
        graphLayout = new RadialTreeLayout("graph");
        graphLayout.setAutoScale(true);
        visualization.putAction("graphLayout", graphLayout);
        filter = new ActionList();
        filter.add(new TreeRootAction("graph"));
        filter.add(distanceFilter);
        filter.add(graphLayout);
        filter.add(fonts);
        filter.add(textColor);
        filter.add(nodeColor);
        filter.add(edgeColor);
        filter.add(edgeStroke);
        filter.add(nodeStroke);
        filter.add(nodeStrokeColor);
        visualization.putAction("filter", filter);
        animate = new ActionList(1250);
        animate.setPacingFunction(new SlowInSlowOutPacer());
        animate.add(new QualityControlAnimator());
        animate.add(new VisibilityAnimator());
        animate.add(new PolarLocationAnimator());
        animate.add(new FinalLocationAnimator());
        animate.add(new FontAnimator());
        animate.add(new RepaintAction());
        visualization.putAction("animate", animate);
        visualization.putAction("filterAnimate", new SequentialActionList());
        filterAnimate = new ActionList(1250);
        filterAnimate.add(filter);
        filterAnimate.add(animate);
        visualization.putAction("filterAnimate", filterAnimate);
        visualization.addFocusGroup(GROUP_GRAPH_SELECTED);
        visualization.setInteractive(GROUP_GRAPH_EDGES, null, false);
        return visualization;
    }

    /**
	 * Sets the distance filter and runs the visualization.
	 * 
	 */
    public void setDistanceFilter(int distance) {
        if (graph.getNodeCount() > 0) {
            distanceFilter.setDistance(distance);
            visualization.run("filter");
            visualization.run("animate");
        }
    }

    /**
	 * Create a new display.
	 * 
	 */
    public Display createDisplay() {
        display = new Display(visualization);
        display.setForeground(Color.BLACK);
        display.setBackground(Color.WHITE);
        display.addControlListener(new FocusControl(1, "filterAnimate"));
        display.addControlListener(new DragControl());
        display.addControlListener(new PanControl());
        display.addControlListener(new ZoomControl());
        display.addControlListener(new ZoomToFitControl());
        display.registerKeyboardAction(new PrefuseActionListener(this.visualization, "filter"), "left-to-right", KeyStroke.getKeyStroke("ctrl 1"), JComponent.WHEN_FOCUSED);
        display.registerKeyboardAction(new PrefuseActionListener(this.visualization, "animate"), "left-to-right", KeyStroke.getKeyStroke("ctrl 2"), JComponent.WHEN_FOCUSED);
        return display;
    }

    /**
	 * Create a new view.
	 * 
	 */
    public void createView() {
        viewer.setDisplay(display);
        viewer.setPrefuseManager(this);
    }

    /**
	 * Run the visualization.
	 * 
	 */
    public void runVisualization() {
        prepareFocusItem(visualization);
        visualization.run("filter");
        visualization.run("animate");
    }

    /**
	 * Get the graph.
	 * 
	 */
    public Graph getGraph() {
        return graph;
    }

    /**
	 * Set the graph.
	 * 
	 */
    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    /**
	 * Get the Visualization.
	 * 
	 */
    public Visualization getVisualization() {
        return visualization;
    }

    /**
	 * Set the View.
	 * 
	 */
    public void setViewer(Viewer viewer) {
        this.viewer = viewer;
    }

    /**
	 * Set a focus item.
	 * 
	 */
    private static void prepareFocusItem(Visualization visualization) {
        Iterator i = visualization.getGroup("graph.nodes").tuples();
        boolean found = false;
        TableNodeItem item = null;
        while (i.hasNext() && !found) {
            Object temp = i.next();
            if (temp instanceof TableNodeItem) {
                found = true;
                item = (TableNodeItem) temp;
            }
        }
        if (item != null) {
            visualization.getFocusGroup(Visualization.FOCUS_ITEMS).addTuple(item);
            item.setFillColor(ColorLib.rgb(100, 200, 100));
        }
    }

    public class PrefuseActionListener implements ActionListener {

        protected Visualization visualization = null;

        protected String action = null;

        public PrefuseActionListener(Visualization visualization, String action) {
            this.visualization = visualization;
            this.action = action;
        }

        public void actionPerformed(ActionEvent e) {
            this.visualization.run(this.action);
        }
    }
}
