package info.goldenorb.widgets;

import java.awt.Color;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import javax.swing.JComponent;
import javax.swing.JPanel;
import prefuse.Display;
import prefuse.Visualization;
import prefuse.action.ActionList;
import prefuse.action.RepaintAction;
import prefuse.action.assignment.ColorAction;
import prefuse.action.filter.GraphDistanceFilter;
import prefuse.action.layout.graph.ForceDirectedLayout;
import prefuse.activity.Activity;
import prefuse.controls.DragControl;
import prefuse.controls.FocusControl;
import prefuse.controls.NeighborHighlightControl;
import prefuse.controls.PanControl;
import prefuse.controls.WheelZoomControl;
import prefuse.controls.ZoomControl;
import prefuse.controls.ZoomToFitControl;
import prefuse.data.Graph;
import prefuse.data.Tuple;
import prefuse.data.event.TupleSetListener;
import prefuse.data.io.GraphMLReader;
import prefuse.data.tuple.TupleSet;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.LabelRenderer;
import prefuse.util.ColorLib;
import prefuse.util.GraphLib;
import prefuse.util.PrefuseLib;
import prefuse.util.force.ForceSimulator;
import prefuse.util.ui.JPrefuseApplet;
import prefuse.util.ui.UILib;
import prefuse.visual.NodeItem;
import prefuse.visual.VisualGraph;
import prefuse.visual.VisualItem;

/**
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class ClusterGraph extends JPrefuseApplet {

    private static final long serialVersionUID = 1L;

    private static final String graph = "graph";

    private static final String nodes = "graph.nodes";

    private static final String edges = "graph.edges";

    private int centerNode = 2;

    private float force = -5f;

    private String path = "/InstitutoStela/work/framework/GoldenOrb/data/";

    private String file = "graphml_is.xml";

    private int type = 2;

    public void init() {
        UILib.setPlatformLookAndFeel();
        JComponent graphview = null;
        if (type == 1) {
            graphview = demo(path + file, "name");
        } else {
            graphview = demo(this.getParameter("xml"), "name");
        }
        this.getContentPane().add(graphview);
    }

    public JComponent demo(String datafile, String label) {
        Graph g = null;
        if (datafile == null) {
            g = GraphLib.getGrid(15, 15);
        } else {
            try {
                if (this.type == 1) {
                    InputStream is = new BufferedInputStream(new FileInputStream(datafile));
                    g = new GraphMLReader().readGraph(is);
                } else {
                    ByteArrayInputStream bais = new ByteArrayInputStream(datafile.getBytes("UTF-8"));
                    g = new GraphMLReader().readGraph(bais);
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
        return demo(g, label);
    }

    public JComponent demo(Graph g, String label) {
        final Visualization vis = new Visualization();
        VisualGraph vg = vis.addGraph(graph, g);
        vis.setValue(edges, null, VisualItem.INTERACTIVE, Boolean.FALSE);
        TupleSet focusGroup = vis.getGroup(Visualization.FOCUS_ITEMS);
        focusGroup.addTupleSetListener(new TupleSetListener() {

            public void tupleSetChanged(TupleSet ts, Tuple[] add, Tuple[] rem) {
                for (int i = 0; i < rem.length; ++i) ((VisualItem) rem[i]).setFixed(false);
                for (int i = 0; i < add.length; ++i) {
                    ((VisualItem) add[i]).setFixed(false);
                    ((VisualItem) add[i]).setFixed(true);
                }
                vis.run("draw");
            }
        });
        LabelRenderer tr = new LabelRenderer(label);
        tr.setRoundedCorner(8, 8);
        vis.setRendererFactory(new DefaultRendererFactory(tr));
        int hops = 4;
        final GraphDistanceFilter filter = new GraphDistanceFilter(graph, hops);
        ActionList draw = new ActionList();
        draw.add(filter);
        draw.add(new ColorAction(nodes, VisualItem.FILLCOLOR, ColorLib.rgb(200, 200, 255)));
        draw.add(new ColorAction(nodes, VisualItem.STROKECOLOR, 0));
        draw.add(new ColorAction(nodes, VisualItem.TEXTCOLOR, ColorLib.rgb(0, 0, 0)));
        draw.add(new ColorAction(edges, VisualItem.FILLCOLOR, ColorLib.gray(200)));
        draw.add(new ColorAction(edges, VisualItem.STROKECOLOR, ColorLib.gray(200)));
        ColorAction fill = new ColorAction(nodes, VisualItem.FILLCOLOR, ColorLib.rgb(200, 200, 255));
        fill.add("_fixed", ColorLib.rgb(255, 100, 100));
        fill.add("_highlight", ColorLib.rgb(255, 200, 125));
        ForceDirectedLayout fdl = new ForceDirectedLayout(graph);
        ForceSimulator fsim = fdl.getForceSimulator();
        fsim.getForces()[0].setParameter(0, force);
        fdl.setMaxTimeStep(20);
        ActionList animate = new ActionList(Activity.INFINITY);
        animate.add(fdl);
        animate.add(fill);
        animate.add(new RepaintAction());
        vis.putAction("draw", draw);
        vis.putAction("layout", animate);
        vis.runAfter("draw", "layout");
        Display display = new Display(vis);
        display.setSize(1300, 900);
        display.setForeground(Color.GRAY);
        display.setBackground(Color.WHITE);
        display.addControlListener(new FocusControl(1));
        display.addControlListener(new DragControl());
        display.addControlListener(new PanControl());
        display.addControlListener(new ZoomControl());
        display.addControlListener(new WheelZoomControl());
        display.addControlListener(new ZoomToFitControl());
        display.addControlListener(new NeighborHighlightControl());
        display.setForeground(Color.GRAY);
        display.setBackground(Color.WHITE);
        JPanel ss = new JPanel();
        ss.add(display);
        NodeItem focus = (NodeItem) vg.getNode(centerNode);
        PrefuseLib.setX(focus, null, 400);
        PrefuseLib.setY(focus, null, 250);
        focusGroup.setTuple(focus);
        return ss;
    }
}
