package edu.umd.cs.viviz.layout;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FilenameFilter;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import prefuse.Display;
import prefuse.Visualization;
import prefuse.action.Action;
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
import prefuse.controls.SubtreeDragControl;
import prefuse.controls.WheelZoomControl;
import prefuse.controls.ZoomControl;
import prefuse.controls.ZoomToFitControl;
import prefuse.data.Graph;
import prefuse.data.Schema;
import prefuse.data.Table;
import prefuse.data.Tuple;
import prefuse.data.event.TupleSetListener;
import prefuse.data.io.GraphMLReader;
import prefuse.data.tuple.TupleSet;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.EdgeRenderer;
import prefuse.render.LabelRenderer;
import prefuse.render.Renderer;
import prefuse.util.ColorLib;
import prefuse.util.GraphLib;
import prefuse.util.GraphicsLib;
import prefuse.util.PrefuseLib;
import prefuse.util.display.DisplayLib;
import prefuse.util.display.ItemBoundsListener;
import prefuse.util.force.ForceSimulator;
import prefuse.util.io.IOLib;
import prefuse.util.ui.JForcePanel;
import prefuse.util.ui.JValueSlider;
import prefuse.util.ui.UILib;
import prefuse.visual.AggregateItem;
import prefuse.visual.VisualGraph;
import prefuse.visual.VisualItem;
import prefuse.visual.expression.InGroupPredicate;
import prefuse.visual.tuple.TableNodeItem;
import prefuse.data.io.DelimitedTextTableReader;
import prefuse.Constants;
import prefuse.data.Node;
import prefuse.data.Edge;
import prefuse.data.Tree;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import edu.umd.cs.viviz.data.*;
import edu.umd.cs.viviz.gui.MainWindow;
import edu.umd.cs.viviz.gui.SplashScreen;

/**
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class TreeLayout extends JPanel {

    /**
     * 
     */
    private static final int WINDOW_DISTANCES = 2000;

    /**
     * 
     */
    private static final String IMAGES_DIR = "data\\spacetree\\images\\";

    /**
     * 
     */
    static String GUI_FILE = "data\\spacetree\\SpaceTree.GUI";

    /**
     * 
     */
    static String EFG_FILE = "data\\spacetree\\SpaceTree.EFG";

    private static final long serialVersionUID = 1L;

    static final JFileChooser fc = new JFileChooser();

    private static final String graph = "graph";

    private static final String nodes = "graph.nodes";

    private static final String edges = "graph.edges";

    private Visualization m_vis;

    static edu.umd.cs.viviz.data.Data data;

    static Map<edu.umd.cs.viviz.data.Node, Node> mp;

    static Map<VisualItem, edu.umd.cs.viviz.data.Node> reverse_mp;

    static Map<VisualItem, edu.umd.cs.viviz.data.Edge> edge_reverse_mp;

    Data graphData;

    private int edgeType = prefuse.Constants.EDGE_TYPE_CURVE;

    private int arrowType = prefuse.Constants.EDGE_ARROW_FORWARD;

    private final Color[] edgeColors = { Color.RED, Color.YELLOW, Color.GREEN, Color.BLUE };

    static float upperLeftX, upperLeftY, bottomRightX, bottomRightY;

    private static final int winSizeX = 700, winSizeY = 700;

    public static final Schema LABEL_SCHEMA = new Schema();

    static {
        LABEL_SCHEMA.addColumn("image", String.class, "");
        LABEL_SCHEMA.addColumn("label", String.class, "");
    }

    public void preLoadImage(Graph g, LabelRenderer nodeR) {
        ArrayList<Node> arr = new ArrayList();
        for (int i = 0; i < g.getNodeCount(); i++) {
            arr.add(g.getNode(i));
        }
        Iterator<Node> nodeStart = arr.iterator();
        nodeR.getImageFactory().preloadImages(nodeStart, "image");
        arr.clear();
    }

    public TreeLayout(Graph g, String label) {
        super(new BorderLayout());
        m_vis = new Visualization();
        LabelRenderer nodeRenderer = new LabelRenderer(label, "image");
        nodeRenderer.setVerticalAlignment(Constants.BOTTOM);
        nodeRenderer.setImagePosition(Constants.TOP);
        nodeRenderer.setHorizontalPadding(0);
        nodeRenderer.setVerticalPadding(0);
        nodeRenderer.setMaxImageDimensions(500, 500);
        EdgeRenderer m_edgeRenderer = new EdgeRenderer(Constants.EDGE_TYPE_LINE, Constants.EDGE_ARROW_FORWARD);
        m_edgeRenderer.setArrowHeadSize(10, 10);
        DefaultRendererFactory render = new DefaultRendererFactory(nodeRenderer, m_edgeRenderer);
        m_vis.setRendererFactory(render);
        setGraph(g, label);
        TupleSet focusGroup = m_vis.getGroup(Visualization.FOCUS_ITEMS);
        focusGroup.addTupleSetListener(new TupleSetListener() {

            public void tupleSetChanged(TupleSet ts, Tuple[] add, Tuple[] rem) {
                for (int i = 0; i < rem.length; ++i) ((VisualItem) rem[i]).setFixed(false);
                for (int i = 0; i < add.length; ++i) {
                    ((VisualItem) add[i]).setFixed(false);
                    ((VisualItem) add[i]).setFixed(true);
                }
                if (ts.getTupleCount() == 0) {
                    ts.addTuple(rem[0]);
                    ((VisualItem) rem[0]).setFixed(false);
                }
                m_vis.run("draw");
            }
        });
        int hops = 30;
        final GraphDistanceFilter filter = new GraphDistanceFilter(graph, hops);
        ColorAction fill = new ColorAction(nodes, VisualItem.FILLCOLOR, ColorLib.rgba(VivizConstants.nFillClr.getRed(), VivizConstants.nFillClr.getGreen(), VivizConstants.nFillClr.getBlue(), 200));
        fill.add(VisualItem.FIXED, ColorLib.rgb(VivizConstants.nFixedClr.getRed(), VivizConstants.nFixedClr.getGreen(), VivizConstants.nFixedClr.getBlue()));
        fill.add(VisualItem.HIGHLIGHT, ColorLib.rgb(VivizConstants.nHigClr.getRed(), VivizConstants.nHigClr.getGreen(), VivizConstants.nHigClr.getBlue()));
        ActionList draw = new ActionList();
        draw.add(filter);
        draw.add(fill);
        draw.add(new ColorAction(nodes, VisualItem.STROKECOLOR, 0));
        draw.add(new ColorAction(nodes, VisualItem.TEXTCOLOR, ColorLib.rgb(0, 0, 0)));
        draw.add(new ColorAction(edges, VisualItem.FILLCOLOR, ColorLib.gray(0)));
        draw.add(new ColorAction(edges, VisualItem.STROKECOLOR, ColorLib.rgb(0, 0, 0)));
        ActionList animate = new ActionList(Activity.INFINITY);
        animate.add(fill);
        animate.add(new RepaintAction());
        m_vis.putAction("draw", draw);
        m_vis.putAction("layout", animate);
        m_vis.runAfter("draw", "layout");
        float boundWidth, boundHeight;
        float scaleFactorX, scaleFactorY, scaleFactor;
        boundWidth = bottomRightX - upperLeftX;
        boundHeight = bottomRightY - upperLeftX;
        scaleFactorX = winSizeX / (boundWidth + 300);
        scaleFactorY = winSizeY / (boundHeight + 300);
        scaleFactor = (scaleFactorX > scaleFactorY) ? scaleFactorY : scaleFactorX;
        System.out.println("ScaleFactor" + String.valueOf(scaleFactor));
        Display display = new Display(m_vis);
        display.setSize(winSizeX, winSizeY);
        display.zoom(new Point2D.Float(winSizeX / 2, winSizeY / 2), scaleFactor);
        display.pan(-20, -20);
        display.setForeground(Color.GRAY);
        display.setBackground(Color.WHITE);
        display.addControlListener(new DragControl());
        display.addControlListener(new PanControl());
        display.addControlListener(new ZoomControl());
        display.addControlListener(new WheelZoomControl());
        display.addControlListener(new ZoomToFitControl());
        display.addControlListener(new NeighborHighlightControl());
        display.addControlListener(new TreeLayoutControl());
        display.setForeground(Color.GRAY);
        display.setBackground(Color.WHITE);
        JPanel fpanel = new JPanel();
        final JValueSlider slider = new JValueSlider("Filter", 0, hops, hops);
        slider.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                filter.setDistance(slider.getValue().intValue());
                m_vis.run("draw");
            }
        });
        slider.setBackground(Color.WHITE);
        slider.setPreferredSize(new Dimension(300, 30));
        slider.setMaximumSize(new Dimension(300, 30));
        Box cf = new Box(BoxLayout.Y_AXIS);
        cf.add(slider);
        cf.setBorder(BorderFactory.createTitledBorder("Control Panel"));
        fpanel.add(cf);
        fpanel.add(Box.createVerticalGlue());
        JButton bRefresh = new JButton("Refresh");
        cf.add(bRefresh);
        JSplitPane split = new JSplitPane();
        split.setLeftComponent(display);
        split.setRightComponent(fpanel);
        split.setOneTouchExpandable(true);
        split.setContinuousLayout(false);
        split.setDividerLocation(700);
        m_vis.run("draw");
        enableForceDirected(true);
        enableForceDirected(false);
        add(split);
    }

    public void repaintImmediate() {
        m_vis.run("draw");
    }

    public void setTransparency(int alpha) {
        ColorAction fill = new ColorAction(nodes, VisualItem.FILLCOLOR, ColorLib.rgba(VivizConstants.nFillClr.getRed(), VivizConstants.nFillClr.getGreen(), VivizConstants.nFillClr.getBlue(), alpha));
        fill.add(VisualItem.FIXED, ColorLib.rgb(VivizConstants.nFixedClr.getRed(), VivizConstants.nFixedClr.getGreen(), VivizConstants.nFixedClr.getBlue()));
        fill.add(VisualItem.HIGHLIGHT, ColorLib.rgb(VivizConstants.nHigClr.getRed(), VivizConstants.nHigClr.getGreen(), VivizConstants.nHigClr.getBlue()));
        ActionList animate = (ActionList) m_vis.getAction("layout");
        for (int i = animate.size() - 1; i >= 0; i--) {
            animate.remove(i);
        }
        animate.add(fill);
        animate.add(new RepaintAction());
        m_vis.putAction("layout", animate);
    }

    public void enableForceDirected(Boolean b) {
    }

    public void setHideImage(Boolean enable) {
        DefaultRendererFactory drf = (DefaultRendererFactory) m_vis.getRendererFactory();
        Renderer nodeR = drf.getDefaultRenderer();
        if (enable == true) {
            ((LabelRenderer) nodeR).setImageField("gg");
        } else {
            ((LabelRenderer) nodeR).setImageField("image");
        }
        m_vis.repaint();
    }

    public void setGraph(Graph g, String label) {
        int counter;
        DefaultRendererFactory drf = (DefaultRendererFactory) m_vis.getRendererFactory();
        m_vis.removeGroup(graph);
        VisualGraph vg = m_vis.addGraph(graph, g);
        m_vis.setValue(edges, null, VisualItem.INTERACTIVE, Boolean.TRUE);
        VisualItem f = (VisualItem) vg.getNode(0);
        m_vis.getGroup(Visualization.FOCUS_ITEMS).setTuple(f);
        LinkedList<edu.umd.cs.viviz.data.Node> nodeList = data.getNodes();
        Set<edu.umd.cs.viviz.data.Edge> edgeList = data.getEdges();
        reverse_mp = new HashMap<VisualItem, edu.umd.cs.viviz.data.Node>();
        edge_reverse_mp = new HashMap<VisualItem, edu.umd.cs.viviz.data.Edge>();
        counter = 0;
        for (edu.umd.cs.viviz.data.Edge edge : edgeList) {
            VisualItem v = (VisualItem) vg.getEdge(counter);
            edge_reverse_mp.put(v, edge);
            counter++;
        }
        counter = 0;
        for (edu.umd.cs.viviz.data.Node node : nodeList) {
            VisualItem v = (VisualItem) vg.getNode(counter);
            reverse_mp.put(v, node);
            edu.umd.cs.viviz.data.Node tmpNode = reverse_mp.get(vg.getNode(counter));
            System.out.println("bb" + tmpNode.getID());
            PrefuseLib.setX(v, null, node.getWindowIndex() * WINDOW_DISTANCES + (node.getIndex() - node.getSiblings() / 2) * 100);
            PrefuseLib.setY(v, null, node.getWidgetLevel() * 200);
            counter++;
        }
        f.setFixed(false);
    }

    public static Graph CreateWidgetMapGraph(String datafile) {
        int counter;
        int numNode, numImg;
        Graph g = new Graph(true);
        LinkedList<edu.umd.cs.viviz.data.Node> nodeList = data.getNodes();
        Set<edu.umd.cs.viviz.data.Edge> edgeList = data.getEdges();
        g.getNodeTable().addColumns(LABEL_SCHEMA);
        counter = 0;
        float maxX = -100000, maxY = -100000, minX = 100000, minY = 100000;
        mp = new HashMap<edu.umd.cs.viviz.data.Node, Node>();
        Node[] graphNode = new Node[nodeList.size()];
        for (edu.umd.cs.viviz.data.Node node : nodeList) {
            graphNode[counter] = g.addNode();
            graphNode[counter].setString("label", node.getID());
            if (node.getX() > maxX) maxX = node.getX();
            if (node.getX() < minX) minX = node.getX();
            if (node.getY() > maxY) maxY = node.getY();
            if (node.getY() < minY) minY = node.getY();
            mp.put(node, graphNode[counter]);
            System.out.println(node.getType() + " x: " + String.valueOf(node.getX()) + " y: " + String.valueOf(node.getY()));
            String filename = new String(IMAGES_DIR + node.getID() + ".png");
            graphNode[counter].setString("image", filename);
            counter = counter + 1;
        }
        upperLeftX = minX;
        upperLeftY = minY;
        bottomRightX = maxX;
        bottomRightY = maxY;
        for (edu.umd.cs.viviz.data.Edge edge : edgeList) {
            Node tNode1, tNode2;
            edu.umd.cs.viviz.data.Node source = edge.getSource();
            edu.umd.cs.viviz.data.Node target = edge.getTarget();
            tNode1 = mp.get(source);
            tNode2 = mp.get(target);
            g.addEdge(tNode1, tNode2);
        }
        return g;
    }

    public static void ReadGUIData() {
        Parser parser = new Parser();
        data = parser.parse(GUI_FILE, EFG_FILE);
    }

    public static void main(String[] args) {
        UILib.setPlatformLookAndFeel();
        String datafile = "resources\\widget_images.txt";
        String label = "label";
        if (args.length > 1) {
            datafile = args[0];
            label = args[1];
        }
        JFrame frame = demo(datafile, label);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static JFrame demo() {
        return demo((String) null, "label");
    }

    public static JFrame demo(String datafile, String label) {
        Graph g = null;
        if (datafile == null) {
            g = GraphLib.getGrid(15, 15);
            label = "label";
        } else {
            try {
                ReadGUIData();
                g = CreateWidgetMapGraph(datafile);
                label = "label";
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
        return demo(g, label);
    }

    public static JFrame demo(Graph g, String label) {
        final TreeLayout view = new TreeLayout(g, label);
        final JFrame frame = new JFrame("ViViz - Visualize the Visualization");
        JMenu dataMenu = new JMenu("Data");
        JMenuItem loadGUI = new JMenuItem("Open GUI...");
        loadGUI.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int returnVal = fc.showOpenDialog(frame);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    GUI_FILE = fc.getSelectedFile().getAbsolutePath();
                } else {
                }
            }
        });
        dataMenu.add(loadGUI);
        JMenuItem loadEFG = new JMenuItem("Open EFG...");
        loadEFG.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int returnVal = fc.showOpenDialog(frame);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    EFG_FILE = fc.getSelectedFile().getAbsolutePath();
                } else {
                }
            }
        });
        dataMenu.add(loadEFG);
        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        dataMenu.add(exit);
        JMenuBar menubar = new JMenuBar();
        menubar.add(dataMenu);
        frame.setJMenuBar(menubar);
        frame.setContentPane(view);
        frame.pack();
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter() {

            public void windowActivated(WindowEvent e) {
                view.m_vis.run("layout");
            }

            public void windowDeactivated(WindowEvent e) {
                view.m_vis.cancel("layout");
            }
        });
        return frame;
    }

    /**
     * Swing menu action that loads a graph into the graph viewer.
     */
    public abstract static class GraphMenuAction extends AbstractAction {

        private TreeLayout m_view;

        public GraphMenuAction(String name, String accel, TreeLayout view) {
            m_view = view;
            this.putValue(AbstractAction.NAME, name);
            this.putValue(AbstractAction.ACCELERATOR_KEY, KeyStroke.getKeyStroke(accel));
        }

        public void actionPerformed(ActionEvent e) {
            m_view.setGraph(getGraph(), "label");
        }

        protected abstract Graph getGraph();
    }

    public static class OpenGraphAction extends AbstractAction {

        private TreeLayout m_view;

        public OpenGraphAction(TreeLayout view) {
            m_view = view;
            this.putValue(AbstractAction.NAME, "Open File...");
            this.putValue(AbstractAction.ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl O"));
        }

        public void actionPerformed(ActionEvent e) {
            Graph g = IOLib.getGraphFile(m_view);
            if (g == null) return;
            String label = getLabel(m_view, g);
            if (label != null) {
                m_view.setGraph(g, label);
            }
        }

        public static String getLabel(Component c, Graph g) {
            Table t = g.getNodeTable();
            int cc = t.getColumnCount();
            String[] names = new String[cc];
            for (int i = 0; i < cc; ++i) names[i] = t.getColumnName(i);
            final String[] label = new String[1];
            while (c != null && !(c instanceof JFrame)) {
                c = c.getParent();
            }
            final JDialog dialog = new JDialog((JFrame) c, "Choose Label Field", true);
            final JButton ok = new JButton("OK");
            ok.setEnabled(false);
            ok.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    dialog.setVisible(false);
                }
            });
            JButton cancel = new JButton("Cancel");
            cancel.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    label[0] = null;
                    dialog.setVisible(false);
                }
            });
            final JList list = new JList(names);
            list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            list.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

                public void valueChanged(ListSelectionEvent e) {
                    int sel = list.getSelectedIndex();
                    if (sel >= 0) {
                        ok.setEnabled(true);
                        label[0] = (String) list.getModel().getElementAt(sel);
                    } else {
                        ok.setEnabled(false);
                        label[0] = null;
                    }
                }
            });
            JScrollPane scrollList = new JScrollPane(list);
            JLabel title = new JLabel("Choose a field to use for node labels:");
            Box bbox = new Box(BoxLayout.X_AXIS);
            bbox.add(Box.createHorizontalStrut(5));
            bbox.add(Box.createHorizontalGlue());
            bbox.add(ok);
            bbox.add(Box.createHorizontalStrut(5));
            bbox.add(cancel);
            bbox.add(Box.createHorizontalStrut(5));
            JPanel panel = new JPanel(new BorderLayout());
            panel.add(title, BorderLayout.NORTH);
            panel.add(scrollList, BorderLayout.CENTER);
            panel.add(bbox, BorderLayout.SOUTH);
            panel.setBorder(BorderFactory.createEmptyBorder(5, 2, 2, 2));
            dialog.setContentPane(panel);
            dialog.pack();
            dialog.setLocationRelativeTo(c);
            dialog.setVisible(true);
            dialog.dispose();
            return label[0];
        }
    }

    public static class FitOverviewListener implements ItemBoundsListener {

        private Rectangle2D m_bounds = new Rectangle2D.Double();

        private Rectangle2D m_temp = new Rectangle2D.Double();

        private double m_d = 15;

        public void itemBoundsChanged(Display d) {
            d.getItemBounds(m_temp);
            GraphicsLib.expand(m_temp, 25 / d.getScale());
            double dd = m_d / d.getScale();
            double xd = Math.abs(m_temp.getMinX() - m_bounds.getMinX());
            double yd = Math.abs(m_temp.getMinY() - m_bounds.getMinY());
            double wd = Math.abs(m_temp.getWidth() - m_bounds.getWidth());
            double hd = Math.abs(m_temp.getHeight() - m_bounds.getHeight());
            if (xd > dd || yd > dd || wd > dd || hd > dd) {
                m_bounds.setFrame(m_temp);
                DisplayLib.fitViewToBounds(d, m_bounds, 0);
            }
        }
    }
}
