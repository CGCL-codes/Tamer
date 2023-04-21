package examples;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JSplitPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.MouseEvent;
import prefuse.Display;
import prefuse.Visualization;
import prefuse.action.ActionList;
import prefuse.action.RepaintAction;
import prefuse.action.assignment.ColorAction;
import prefuse.action.filter.GraphDistanceFilter;
import prefuse.action.layout.graph.ForceDirectedLayout;
import prefuse.activity.Activity;
import prefuse.controls.ControlAdapter;
import prefuse.controls.DragControl;
import prefuse.controls.FocusControl;
import prefuse.controls.NeighborHighlightControl;
import prefuse.controls.PanControl;
import prefuse.controls.WheelZoomControl;
import prefuse.controls.ZoomControl;
import prefuse.controls.ZoomToFitControl;
import prefuse.data.Edge;
import prefuse.data.Graph;
import prefuse.data.Node;
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
import prefuse.util.ui.JForcePanel;
import prefuse.util.ui.JPrefuseApplet;
import prefuse.util.ui.JValueSlider;
import prefuse.util.ui.UILib;
import prefuse.visual.NodeItem;
import prefuse.visual.VisualGraph;
import prefuse.visual.VisualItem;

/**
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class GraphViewToolTips extends JPrefuseApplet {

    private static final String graph = "graph";

    private static final String nodes = "graph.nodes";

    private static final String edges = "graph.edges";

    public void init() {
        UILib.setPlatformLookAndFeel();
        JComponent graphview = demo("/socialnet.xml", "name");
        this.getContentPane().add(graphview);
    }

    public static JComponent demo(String datafile, String label) {
        Graph g = null;
        if (datafile == null) {
            g = GraphLib.getGrid(15, 15);
        } else {
            try {
                g = new GraphMLReader().readGraph(datafile);
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
        return demo(g, label);
    }

    public static JComponent demo(Graph g, String label) {
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
        int maxhops = 4, hops = 4;
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
        fsim.getForces()[0].setParameter(0, -1.2f);
        ActionList animate = new ActionList(Activity.INFINITY);
        animate.add(fdl);
        animate.add(fill);
        animate.add(new RepaintAction());
        vis.putAction("draw", draw);
        vis.putAction("layout", animate);
        vis.runAfter("draw", "layout");
        Display display = new Display(vis);
        display.setSize(500, 500);
        display.setForeground(Color.GRAY);
        display.setBackground(Color.WHITE);
        display.addControlListener(new FocusControl(1));
        display.addControlListener(new DragControl());
        display.addControlListener(new PanControl());
        display.addControlListener(new ZoomControl());
        display.addControlListener(new WheelZoomControl());
        display.addControlListener(new ZoomToFitControl());
        display.addControlListener(new NeighborHighlightControl());
        display.addControlListener(new HoverTooltip());
        display.setForeground(Color.GRAY);
        display.setBackground(Color.WHITE);
        final JForcePanel fpanel = new JForcePanel(fsim);
        final JValueSlider slider = new JValueSlider("Distance", 0, maxhops, hops);
        slider.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                filter.setDistance(slider.getValue().intValue());
                vis.run("draw");
            }
        });
        slider.setBackground(Color.WHITE);
        slider.setPreferredSize(new Dimension(300, 30));
        slider.setMaximumSize(new Dimension(300, 30));
        Box cf = new Box(BoxLayout.Y_AXIS);
        cf.add(slider);
        cf.setBorder(BorderFactory.createTitledBorder("Connectivity Filter"));
        fpanel.add(cf);
        fpanel.add(Box.createVerticalGlue());
        JSplitPane split = new JSplitPane();
        split.setLeftComponent(display);
        split.setRightComponent(fpanel);
        split.setOneTouchExpandable(true);
        split.setContinuousLayout(false);
        split.setDividerLocation(530);
        split.setDividerLocation(800);
        NodeItem focus = (NodeItem) vg.getNode(0);
        PrefuseLib.setX(focus, null, 400);
        PrefuseLib.setY(focus, null, 250);
        focusGroup.setTuple(focus);
        return split;
    }
}

abstract class PrefuseTooltip {

    protected javax.swing.JPanel popup;

    protected int startDelay, stopDelay;

    protected javax.swing.Timer startShowingTimer, stopShowingTimer;

    protected javax.swing.JComponent owner;

    protected boolean isSticky;

    public PrefuseTooltip(javax.swing.JComponent owner) {
        this(owner, 1000, 1000);
    }

    public PrefuseTooltip(javax.swing.JComponent owner, int startDelay, int stopDelay) {
        this.owner = owner;
        this.startDelay = startDelay;
        this.stopDelay = stopDelay;
        this.isSticky = false;
        startShowingTimer = new javax.swing.Timer(startDelay, new ShowTimerAction(true));
        stopShowingTimer = new javax.swing.Timer(stopDelay, new ShowTimerAction(false));
        startShowingTimer.setRepeats(false);
        stopShowingTimer.setRepeats(false);
    }

    public void startShowing(int x, int y) {
        java.awt.Component contents = getContents();
        contents.addMouseListener(new PrefuseTooltipListener());
        popup = new javax.swing.JPanel(new java.awt.BorderLayout(), true);
        popup.setVisible(false);
        popup.setLocation(x, y);
        popup.setSize(contents.getPreferredSize());
        popup.add(contents, java.awt.BorderLayout.CENTER);
        owner.add(popup);
        startShowingTimer.start();
    }

    public void stopShowing() {
        if (PrefuseTooltip.this.popup.isVisible() && !isSticky) {
            stopShowingTimer.start();
        } else {
            startShowingTimer.stop();
        }
    }

    public void startShowingImmediately() {
        stopShowingTimer.stop();
        if (!PrefuseTooltip.this.popup.isVisible()) {
            startShowingTimer.stop();
            bringToFront();
            popup.setVisible(true);
        }
    }

    public void stopShowingImmediately() {
        startShowingTimer.stop();
        if (PrefuseTooltip.this.popup.isVisible() && !isSticky) {
            stopShowingTimer.stop();
            popup.setVisible(false);
        }
    }

    public void setSticky(boolean isSticky) {
        this.isSticky = isSticky;
    }

    public boolean isSticky() {
        return isSticky;
    }

    public void bringToFront() {
        popup.getParent().setComponentZOrder(popup, 0);
    }

    protected class ShowTimerAction implements java.awt.event.ActionListener {

        protected boolean showTimer;

        protected ShowTimerAction(boolean showTimer) {
            this.showTimer = showTimer;
        }

        public void actionPerformed(java.awt.event.ActionEvent e) {
            if (showTimer) {
                PrefuseTooltip.this.startShowingImmediately();
            } else {
                PrefuseTooltip.this.stopShowingImmediately();
            }
        }
    }

    protected class PrefuseTooltipListener extends java.awt.event.MouseAdapter {

        public void mouseEntered(MouseEvent e) {
            stopShowingTimer.stop();
        }

        public void mouseExited(MouseEvent e) {
            if (((java.awt.Component) e.getSource()).getMousePosition() != null) {
                return;
            }
            PrefuseTooltip.this.stopShowing();
        }
    }

    protected abstract java.awt.Component getContents();
}

class HoverTooltip extends ControlAdapter {

    PrefuseTooltip activeTooltip;

    public void itemExited(VisualItem item, MouseEvent e) {
        if (activeTooltip != null) {
            activeTooltip.stopShowing();
        }
    }

    public void itemEntered(VisualItem item, MouseEvent e) {
        if (item instanceof Node) {
            showNodeTooltip(item, e);
        } else if (item instanceof Edge) {
            showEdgeTooltip(item, e);
        }
    }

    public void itemPressed(VisualItem item, java.awt.event.MouseEvent e) {
        if (activeTooltip != null) {
            activeTooltip.stopShowingImmediately();
        }
    }

    public void itemReleased(VisualItem item, java.awt.event.MouseEvent e) {
        if (item instanceof Node) {
            showNodeTooltip(item, e);
        } else if (item instanceof Edge) {
            showEdgeTooltip(item, e);
        }
    }

    protected void showNodeTooltip(VisualItem item, java.awt.event.MouseEvent e) {
        Visualization v = item.getVisualization();
        showTooltip(new GraphViewNodeTooltip((Display) e.getSource(), ((prefuse.data.Node) v.getSourceTuple(item)).getString("name"), ((prefuse.data.Node) v.getSourceTuple(item)).getString("gender")), item, e);
    }

    protected void showEdgeTooltip(VisualItem item, java.awt.event.MouseEvent e) {
    }

    protected void showTooltip(PrefuseTooltip ptt, VisualItem item, java.awt.event.MouseEvent e) {
        if (activeTooltip != null) {
            activeTooltip.stopShowingImmediately();
        }
        activeTooltip = ptt;
        activeTooltip.startShowing(e.getX(), e.getY());
    }
}

class GraphViewNodeTooltip extends PrefuseTooltip {

    protected javax.swing.JLabel nameLabel;

    protected javax.swing.JPanel contentsPanel;

    protected String gender;

    GraphViewNodeTooltip(javax.swing.JComponent owner, String name, String gender) {
        super(owner);
        contentsPanel = new javax.swing.JPanel();
        nameLabel = new javax.swing.JLabel();
        this.gender = gender;
        nameLabel.setText(name);
        contentsPanel.add(nameLabel);
        contentsPanel.add(new javax.swing.JButton(new javax.swing.AbstractAction("Context Menu") {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                System.out.println("Gender for " + GraphViewNodeTooltip.this.nameLabel.getText() + ": " + GraphViewNodeTooltip.this.gender);
            }
        }));
        contentsPanel.setBorder(BorderFactory.createLineBorder(Color.gray));
        contentsPanel.setBackground(new Color(255, 250, 205));
    }

    public java.awt.Component getContents() {
        return contentsPanel;
    }
}
