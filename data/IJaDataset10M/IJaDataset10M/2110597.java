package at.ac.tuwien.ifs.alviz.smallworld.app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.geom.Point2D;
import java.util.*;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.grlea.log.SimpleLogger;
import at.ac.tuwien.ifs.alviz.AlVizClsesPanel;
import at.ac.tuwien.ifs.alviz.align.data.AlignmentData;
import at.ac.tuwien.ifs.alviz.smallworld.action.ClusterBounds;
import at.ac.tuwien.ifs.alviz.smallworld.action.DendroLayout;
import at.ac.tuwien.ifs.alviz.smallworld.action.KillYourParents;
import at.ac.tuwien.ifs.alviz.smallworld.action.LinLogAction;
import at.ac.tuwien.ifs.alviz.smallworld.action.SWClusterAction;
import at.ac.tuwien.ifs.alviz.smallworld.layout.DOALayout;
import at.ac.tuwien.ifs.alviz.smallworld.layout.VoroNetLayout;
import edu.berkeley.guir.prefuse.Display;
import edu.berkeley.guir.prefuse.util.display.ToolTipManager;
import edu.berkeley.guir.prefuse.ItemRegistry;
import edu.berkeley.guir.prefuse.action.RepaintAction;
import edu.berkeley.guir.prefuse.activity.ActionList;
import edu.berkeley.guir.prefuse.graph.Graph;
import edu.berkeley.guir.prefuse.graph.Node;
import edu.berkeley.guir.prefuse.graph.io.GraphReader;
import edu.berkeley.guir.prefuse.render.DefaultRendererFactory;
import at.ac.tuwien.ifs.alviz.smallworld.control.AnchorAlwaysUpdateControl;
import at.ac.tuwien.ifs.alviz.smallworld.render.AlVizDisplay;
import at.ac.tuwien.ifs.alviz.smallworld.render.AlVizFrameRenderer;
import at.ac.tuwien.ifs.alviz.smallworld.render.MultipassDisplay;
import at.ac.tuwien.ifs.alviz.smallworld.render.TubeRenderer;
import at.ac.tuwien.ifs.alviz.smallworld.render.VoroHackRenderer;
import at.ac.tuwien.ifs.alviz.smallworld.render.VoroNetRenderer;
import at.ac.tuwien.ifs.alviz.smallworld.types.AlVizCluster;
import at.ac.tuwien.ifs.alviz.smallworld.types.BasicGraphReader;
import at.ac.tuwien.ifs.alviz.smallworld.types.DefaultVoroNode;
import at.ac.tuwien.ifs.alviz.smallworld.types.GMLGraphReader;
import at.ac.tuwien.ifs.alviz.smallworld.types.ProgressUpdate;
import at.ac.tuwien.ifs.alviz.smallworld.types.SmallWorldComparator;
import at.ac.tuwien.ifs.alviz.smallworld.types.AlVizGraph;

/**
 * Test application for Small World Visualization components
 *
 * @author Stephen
 */
public class SmallWorldFrame extends JPanel implements ProgressUpdate, ComponentListener, ChangeListener, ActionListener, ItemListener {

    public static final String GRAPH_FRIENDSTER = "/friendster.xml";

    public static final String GRAPH_TERROR = "/terror.xml";

    public static final int PASSES = 10;

    private KillYourParents m_patricide = null;

    private LinLogAction m_linlog = null;

    private SWClusterAction m_clust = null;

    private JComboBox m_graphs = null;

    private JCheckBox m_quality = null;

    private TubeRenderer m_tube = null;

    private LoadThread m_lt = null;

    private JProgressBar progress = null;

    private JSlider slider = null;

    private JSlider slider_inner = null;

    private JSlider slider_outer = null;

    private ItemRegistry m_registry;

    private AlVizDisplay m_display;

    private ToolTipManager m_tooltip = null;

    private DOALayout m_doa_layout;

    private ActionList m_implicit;

    private ActionList m_refit;

    private ActionList m_layout;

    private ActionList preproc;

    private AnchorAlwaysUpdateControl m_control = null;

    private Object syncObjectM_control = new Object();

    private AlVizClsesPanel clstree = null;

    private AlignmentData alignmentData = null;

    private String namespace = null;

    private Map<String, AlVizCluster> nodeMap = new HashMap<String, AlVizCluster>();

    private int id = 0;

    private static final SimpleLogger log = new SimpleLogger(SmallWorldFrame.class);

    public void startSetup() {
        m_patricide.run(m_registry, 1.00);
        m_linlog.run(m_registry, 1.00);
        m_refit.runNow();
    }

    public void finishSetup() {
        synchronized (this.syncObjectM_control) {
            m_control = new AnchorAlwaysUpdateControl(m_doa_layout, m_layout);
            if (this.alignmentData != null) this.m_control.setAlignmentData(this.alignmentData);
        }
        if (clstree != null) {
            m_control.setAlVizClsesPanel(clstree);
            m_control.setParent(this);
            clstree.setSmallWorldRegistry(m_registry);
        }
        m_display.addControlListener(m_control);
    }

    public SmallWorldFrame(int id, Graph graph, boolean fish) {
        this.id = id;
        do_show(graph, fish);
    }

    public void setAlVizClsesPanel(AlVizClsesPanel al) {
        clstree = al;
        if (m_control != null) {
            m_control.setAlVizClsesPanel(al);
        }
        m_display.setAlVizClsesPanel(al);
        al.setAlVizDisplay(m_display);
    }

    public void do_show(Graph g, boolean fish) {
        try {
            m_registry = new ItemRegistry(g);
            m_registry.addItemClass(ItemRegistry.DEFAULT_NODE_CLASS, DefaultVoroNode.class);
            m_registry.setItemComparator(new SmallWorldComparator());
            VoroHackRenderer vnetR = new VoroHackRenderer(PASSES);
            m_tube = new TubeRenderer();
            m_tube.setTubes(false);
            DefaultRendererFactory rfac = new DefaultRendererFactory();
            rfac.addRenderer(ItemRegistry.DEFAULT_NODE_CLASS, vnetR);
            rfac.setEdgeRenderer(m_tube);
            m_registry.setRendererFactory(rfac);
            m_patricide = new KillYourParents();
            m_linlog = new LinLogAction(this);
            ClusterBounds bounds = new ClusterBounds();
            m_clust = new SWClusterAction();
            m_clust.setMode(true);
            m_refit = new ActionList(m_registry);
            m_refit.add(bounds);
            m_refit.add(m_clust);
            m_implicit = new ActionList(m_registry);
            m_implicit.add(bounds);
            m_implicit.add(m_clust);
            m_implicit.add(new DendroLayout());
            m_doa_layout = new DOALayout();
            m_doa_layout.setLayoutAnchor(new Point2D.Double(0, 0));
            m_doa_layout.setTube(m_tube);
            m_doa_layout.setFisheye(fish);
            m_layout = new ActionList(m_registry);
            m_layout.add(m_doa_layout);
            m_layout.add(new RepaintAction());
            m_layout.runAfter(m_refit);
            m_display = new AlVizDisplay(m_registry, PASSES);
            m_display.setSize(600, 600);
            m_display.setBackground(Color.WHITE);
            m_display.setHighQuality(true);
            m_doa_layout.setEdges(true);
            m_tube.setTubes(true);
            progress = new JProgressBar(JProgressBar.VERTICAL);
            slider = new JSlider(JSlider.VERTICAL, 0, 100, (int) (100. * m_doa_layout.getConstantDOA()));
            slider.addChangeListener(this);
            slider.setValue(0);
            m_display.updateClusterColors();
            JPanel proslide = new JPanel();
            proslide.setLayout(new GridLayout(1, 2));
            proslide.add(slider);
            proslide.add(progress);
            JPanel meta = new JPanel();
            meta.setLayout(new BorderLayout());
            m_quality = new JCheckBox("HQ", true);
            m_quality.addItemListener(this);
            meta.add(m_quality, BorderLayout.NORTH);
            meta.add(proslide, BorderLayout.CENTER);
            JPanel meta_panel = new JPanel();
            meta_panel.setLayout(new GridLayout(1, 1));
            JPanel lower_panel = new JPanel();
            lower_panel.setLayout(new GridLayout(1, 1));
            setLayout(new BorderLayout(5, 5));
            add(m_display, BorderLayout.CENTER);
            add(meta, BorderLayout.EAST);
            setVisible(true);
            m_display.addComponentListener(this);
            m_lt = new LoadThread(this);
            m_lt.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update() {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                progress.setValue(progress.getValue() + 1);
                progress.setString("" + progress.getValue() + "%");
                repaint();
            }
        });
    }

    public void componentHidden(ComponentEvent e) {
    }

    public void componentMoved(ComponentEvent e) {
    }

    public void componentResized(ComponentEvent e) {
        m_layout.runAfter(m_refit);
        m_refit.runNow();
    }

    public void componentShown(ComponentEvent e) {
    }

    public void stateChanged(ChangeEvent e) {
        if (e.getSource() == slider) {
            if (m_registry != null) {
                m_doa_layout.setConstantDOA(slider.getValue() * 0.01);
                m_layout.runNow();
                m_display.updateClusterColors();
                log.info("Graph" + this.id + "|Clustering|" + slider.getValue());
            }
        }
        if (e.getSource() == slider_inner) {
            if (m_registry != null) {
                m_doa_layout.ZERO_RADIUS = slider_inner.getValue();
                m_layout.runNow();
            }
        }
        if (e.getSource() == slider_outer) {
            if (m_registry != null) {
                m_doa_layout.DOA_RADIUS = slider_outer.getValue();
                m_layout.runNow();
            }
        }
    }

    public class LoadThread extends Thread {

        SmallWorldFrame m_small_world = null;

        public boolean already_started = false;

        public LoadThread(SmallWorldFrame small_world) {
            super();
            m_small_world = small_world;
        }

        public void run() {
            m_small_world.startSetup();
            if (!already_started) {
                m_small_world.finishSetup();
                already_started = true;
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equalsIgnoreCase("no edges")) {
            m_doa_layout.setEdges(false);
            m_layout.runNow();
        }
        if (e.getActionCommand().equalsIgnoreCase("line edges")) {
            m_doa_layout.setEdges(true);
            m_tube.setTubes(false);
            m_layout.runNow();
        }
        if (e.getActionCommand().equalsIgnoreCase("tube edges")) {
            m_doa_layout.setEdges(true);
            m_tube.setTubes(true);
            m_layout.runNow();
        }
        if (e.getActionCommand().equalsIgnoreCase("average link")) {
            m_linlog.setDirty(true);
            m_clust.setMode(true);
            m_layout.runAfter(m_refit);
            m_lt = new LoadThread(this);
            m_lt.already_started = true;
            progress.setValue(0);
            m_lt.start();
        }
        if (e.getActionCommand().equalsIgnoreCase("newman")) {
            m_clust.setMode(false);
            m_layout.runAfter(m_implicit);
            m_implicit.runNow();
        }
    }

    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() == m_quality) {
            if (e.getStateChange() == ItemEvent.DESELECTED) {
                m_display.setHighQuality(false);
                m_layout.runNow();
            } else {
                m_display.setHighQuality(true);
                m_layout.runNow();
            }
        }
    }

    public DOALayout getDOALayout() {
        return this.m_doa_layout;
    }

    public AlignmentData getAlignmentData() {
        return alignmentData;
    }

    public void setAlignmentData(AlignmentData alignmentData) {
        this.alignmentData = alignmentData;
        synchronized (this.syncObjectM_control) {
            if (this.m_control != null) {
                this.m_control.setAlignmentData(this.alignmentData);
            }
        }
    }
}
