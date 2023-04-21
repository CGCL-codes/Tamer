package cz.razor.dzemuj.clustering;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.border.BevelBorder;
import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.Transformer;
import org.freehep.util.export.ExportDialog;
import com.rapidminer.gui.graphs.BasicVertexShapeTransformer;
import com.rapidminer.gui.graphs.ExtendedVertexShapeTransformer;
import com.rapidminer.gui.graphs.GraphCreator;
import com.rapidminer.gui.graphs.GraphViewer;
import com.rapidminer.gui.graphs.LayoutSelection;
import com.rapidminer.gui.graphs.actions.PickingModeAction;
import com.rapidminer.gui.graphs.actions.TransformingModeAction;
import com.rapidminer.gui.graphs.actions.ZoomInAction;
import com.rapidminer.gui.graphs.actions.ZoomOutAction;
import com.rapidminer.gui.tools.ExtendedJScrollPane;
import com.rapidminer.gui.tools.ExtendedToolBar;
import com.rapidminer.gui.tools.IconSize;
import com.rapidminer.gui.tools.SwingTools;
import cz.razor.dzemuj.Texts;
import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.graph.DelegateTree;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.Tree;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.MultiLayerTransformer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.GraphMouseListener;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.decorators.ConstantDirectionalEdgeValueTransformer;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.GradientEdgePaintTransformer;
import edu.uci.ics.jung.visualization.layout.LayoutTransition;
import edu.uci.ics.jung.visualization.renderers.BasicEdgeLabelRenderer;
import edu.uci.ics.jung.visualization.renderers.BasicVertexRenderer;
import edu.uci.ics.jung.visualization.renderers.EdgeLabelRenderer;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import edu.uci.ics.jung.visualization.renderers.VertexLabelRenderer;
import edu.uci.ics.jung.visualization.util.Animator;

/**
 * The basic graph viewer component for graphs.
 *
 * @author Ingo Mierswa
 * @version $Id: GraphViewer.java,v 1.14 2007/07/06 21:15:16 ingomierswa Exp $
 */
public class ClusterAppletGraphViewer<V, E> extends GraphViewer<V, E> {

    private static final long serialVersionUID = -7501422172633548861L;

    public static final int MARGIN = 10;

    public static final Font EDGE_FONT = new Font("SansSerif", Font.PLAIN, 10);

    public static final Font VERTEX_BOLD_FONT = new Font("SansSerif", Font.BOLD, 11);

    public static final Font VERTEX_PLAIN_FONT = new Font("SansSerif", Font.PLAIN, 11);

    private static final String INSTRUCTIONS = Texts.getString("JUNG_graphControlHelp");

    private final Action transformAction = new TransformingModeAction<V, E>(this, IconSize.SMALL);

    private final Action pickingAction = new PickingModeAction<V, E>(this, IconSize.SMALL);

    private VisualizationViewer<V, E> vv;

    private transient Layout<V, E> layout;

    private transient GraphCreator<V, E> graphCreator;

    private LayoutSelection<V, E> layoutSelection;

    private transient ScalingControl scaler = new CrossoverScalingControl();

    private transient DefaultModalGraphMouse graphMouse = new DefaultModalGraphMouse();

    private boolean showEdgeLabels = true;

    private boolean showVertexLabels = true;

    private transient JSplitPane objectViewerSplitPane;

    private transient ModalGraphMouse.Mode currentMode = ModalGraphMouse.Mode.TRANSFORMING;

    public ClusterAppletGraphViewer(final GraphCreator<V, E> graphCreator) {
        super();
        this.graphCreator = graphCreator;
        DelegateForest<String, String> graph = new DelegateForest<String, String>();
        setLayout(new BorderLayout());
        DelegateTree<V, E> tree = (DelegateTree<V, E>) graphCreator.createGraph();
        graph.addTree((Tree<String, String>) tree);
        layout = (Layout<V, E>) new TreeLayout<String, String>(graph);
        vv = new VisualizationViewer<V, E>(layout) {

            private static final long serialVersionUID = 8247229781249216143L;

            private boolean initialized = false;

            /** Necessary in order to re-change layout after first painting (starting position and size). */
            public void paint(Graphics g) {
                super.paint(g);
                if (!initialized) {
                    initialized = true;
                    updateLayout();
                    if (objectViewerSplitPane != null) {
                        objectViewerSplitPane.setDividerLocation(0.9);
                    }
                }
            }
        };
        vv.setBackground(Color.white);
        vv.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line<V, E>());
        vv.getRenderContext().setEdgeFillPaintTransformer(new GradientEdgePaintTransformer<V, E>(SwingTools.LIGHTEST_BLUE, SwingTools.LIGHT_BLUE, vv));
        vv.getRenderContext().setEdgeDrawPaintTransformer(new GradientEdgePaintTransformer<V, E>(SwingTools.LIGHT_BLUE, SwingTools.DARK_BLUE, vv));
        vv.getRenderContext().setEdgeFontTransformer(new Transformer<E, Font>() {

            public Font transform(E arg0) {
                return EDGE_FONT;
            }
        });
        vv.getRenderContext().setArrowFillPaintTransformer(new Transformer<E, Paint>() {

            public Paint transform(E arg0) {
                return SwingTools.LIGHT_BLUE;
            }
        });
        vv.getRenderContext().setArrowDrawPaintTransformer(new Transformer<E, Paint>() {

            public Paint transform(E arg0) {
                return SwingTools.DARK_BLUE;
            }
        });
        vv.getRenderContext().setEdgeLabelClosenessTransformer(new ConstantDirectionalEdgeValueTransformer<V, E>(0.5d, 0.5d));
        int labelOffset = graphCreator.getLabelOffset();
        if (labelOffset >= 0) vv.getRenderContext().setLabelOffset(labelOffset);
        vv.getRenderContext().setEdgeLabelTransformer(new Transformer<E, String>() {

            public String transform(E object) {
                return graphCreator.getEdgeName(object);
            }
        });
        Renderer.EdgeLabel<V, E> edgeLabelRenderer = graphCreator.getEdgeLabelRenderer();
        if (edgeLabelRenderer != null) vv.getRenderer().setEdgeLabelRenderer(edgeLabelRenderer);
        vv.getRenderContext().setEdgeLabelRenderer(new EdgeLabelRenderer() {

            private JLabel renderer = new JLabel();

            public <T> Component getEdgeLabelRendererComponent(JComponent parent, Object value, Font font, boolean isSelected, T edge) {
                this.renderer.setFont(font);
                if (graphCreator.isEdgeLabelDecorating()) {
                    this.renderer.setOpaque(true);
                    renderer.setBackground(Color.WHITE);
                }
                this.renderer.setText(value.toString());
                return this.renderer;
            }

            /** Let the graph model decide. */
            public boolean isRotateEdgeLabels() {
                return graphCreator.isRotatingEdgeLabels();
            }

            /** Does nothing. */
            public void setRotateEdgeLabels(boolean rotate) {
            }
        });
        vv.getRenderContext().setVertexFontTransformer(new Transformer<V, Font>() {

            public Font transform(V vertex) {
                if (graphCreator.isBold(vertex)) return VERTEX_BOLD_FONT; else return VERTEX_PLAIN_FONT;
            }
        });
        vv.getRenderContext().setVertexLabelTransformer(new Transformer<V, String>() {

            public String transform(V object) {
                return graphCreator.getVertexName(object);
            }
        });
        vv.getRenderContext().setVertexFillPaintTransformer(new Transformer<V, Paint>() {

            public Paint transform(V vertex) {
                if (vv.getPickedVertexState().isPicked(vertex)) return SwingTools.makeYellowPaint(50, 50); else return SwingTools.makeBluePaint(50, 50);
            }
        });
        vv.getRenderContext().setVertexDrawPaintTransformer(new Transformer<V, Paint>() {

            public Paint transform(V vertex) {
                if (vv.getPickedVertexState().isPicked(vertex)) return SwingTools.DARKEST_YELLOW; else return SwingTools.DARKEST_BLUE;
            }
        });
        this.vv.setVertexToolTipTransformer(new Transformer<V, String>() {

            public String transform(V vertex) {
                return graphCreator.getVertexToolTip(vertex);
            }
        });
        vv.getRenderContext().setVertexShapeTransformer(new ExtendedVertexShapeTransformer<V>(graphCreator));
        Renderer.Vertex<V, E> vertexRenderer = graphCreator.getVertexRenderer();
        if (vertexRenderer != null) vv.getRenderer().setVertexRenderer(vertexRenderer);
        setDefaultLabelPosition();
        Renderer.VertexLabel<V, E> customVertexLabelRenderer = graphCreator.getVertexLabelRenderer();
        if (customVertexLabelRenderer != null) vv.getRenderer().setVertexLabelRenderer(customVertexLabelRenderer);
        vv.getRenderContext().setVertexLabelRenderer(new VertexLabelRenderer() {

            private JLabel label = new JLabel();

            public <T> Component getVertexLabelRendererComponent(JComponent parent, Object object, Font font, boolean isSelection, T vertex) {
                label.setFont(font);
                if (object != null) {
                    label.setText(object.toString());
                } else {
                    label.setText("");
                }
                return label;
            }
        });
        JSplitPane controlsSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        controlsSplitPane.setOneTouchExpandable(true);
        JPanel mainPanel = new JPanel(new BorderLayout());
        if (graphCreator.getObjectViewer() == null) {
            vv.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
            add(vv, BorderLayout.CENTER);
        } else {
            JComponent viewer = graphCreator.getObjectViewer().getViewerComponent();
            if (viewer != null) {
                viewer.setBorder(null);
                objectViewerSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
                objectViewerSplitPane.add(vv, 0);
                objectViewerSplitPane.add(viewer, 1);
                objectViewerSplitPane.setOneTouchExpandable(true);
                objectViewerSplitPane.setResizeWeight(0.8);
                controlsSplitPane.setRightComponent(objectViewerSplitPane);
            } else {
                vv.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
                controlsSplitPane.setRightComponent(vv);
            }
        }
        JComponent controls = createControlPanel();
        controlsSplitPane.setLeftComponent(new ExtendedJScrollPane(controls));
        controlsSplitPane.setDividerLocation(165 + controlsSplitPane.getInsets().left);
        add(controlsSplitPane);
        this.showEdgeLabels = !graphCreator.showEdgeLabelsDefault();
        togglePaintEdgeLabels();
        this.showVertexLabels = !graphCreator.showVertexLabelsDefault();
        togglePaintVertexLabels();
    }

    private JComponent createControlPanel() {
        vv.setGraphMouse(graphMouse);
        vv.addKeyListener(graphMouse.getModeKeyListener());
        graphMouse.setMode(ModalGraphMouse.Mode.TRANSFORMING);
        transformAction.setEnabled(false);
        pickingAction.setEnabled(true);
        vv.addGraphMouseListener(new GraphMouseListener<V>() {

            public void graphClicked(V vertex, MouseEvent arg1) {
            }

            public void graphPressed(V arg0, MouseEvent arg1) {
            }

            public void graphReleased(V vertex, MouseEvent arg1) {
                if (currentMode.equals(ModalGraphMouse.Mode.TRANSFORMING)) {
                    if (graphCreator.getObjectViewer() != null) {
                        vv.getPickedVertexState().clear();
                        vv.getPickedVertexState().pick(vertex, true);
                        graphCreator.getObjectViewer().showObject(graphCreator.getObject(vertex));
                    }
                }
            }
        });
        JPanel controls = new JPanel();
        GridBagLayout gbLayout = new GridBagLayout();
        controls.setLayout(gbLayout);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(4, 4, 4, 4);
        c.weightx = 1;
        c.weighty = 0;
        JToolBar zoomBar = new ExtendedToolBar();
        zoomBar.setLayout(new FlowLayout(FlowLayout.CENTER));
        zoomBar.add(new ZoomInAction(this, IconSize.SMALL));
        zoomBar.add(new ZoomOutAction(this, IconSize.SMALL));
        zoomBar.setBorder(BorderFactory.createTitledBorder(Texts.getString("JUNG_control_zoomTitle")));
        c.gridwidth = GridBagConstraints.REMAINDER;
        gbLayout.setConstraints(zoomBar, c);
        controls.add(zoomBar);
        JToolBar modeBar = new ExtendedToolBar();
        modeBar.setLayout(new FlowLayout(FlowLayout.CENTER));
        modeBar.add(transformAction);
        modeBar.add(pickingAction);
        modeBar.setBorder(BorderFactory.createTitledBorder(Texts.getString("JUNG_control_modeTitle")));
        c.gridwidth = GridBagConstraints.REMAINDER;
        gbLayout.setConstraints(modeBar, c);
        controls.add(modeBar);
        c.gridwidth = GridBagConstraints.REMAINDER;
        for (int i = 0; i < graphCreator.getNumberOfOptionComponents(); i++) {
            JComponent optionComponent = graphCreator.getOptionComponent(this, i);
            if (optionComponent != null) {
                c.gridwidth = GridBagConstraints.REMAINDER;
                gbLayout.setConstraints(optionComponent, c);
                controls.add(optionComponent);
            }
        }
        JButton help = new JButton(Texts.getString("JUNG_control_helpButton"));
        help.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(vv, INSTRUCTIONS);
            }
        });
        c.gridwidth = GridBagConstraints.REMAINDER;
        gbLayout.setConstraints(help, c);
        controls.add(help);
        JPanel fillPanel = new JPanel();
        c.weighty = 1.0d;
        c.gridwidth = GridBagConstraints.REMAINDER;
        gbLayout.setConstraints(fillPanel, c);
        controls.add(fillPanel);
        return controls;
    }

    public void updateLayout() {
        changeLayout(this.layout);
    }

    public void changeLayout(Layout<V, E> newLayout) {
        MultiLayerTransformer transformer = vv.getRenderContext().getMultiLayerTransformer();
        double scale = transformer.getTransformer(Layer.VIEW).getScale();
        int layoutWidth = (int) (vv.getWidth() / scale);
        int layoutHeight = (int) (vv.getHeight() / scale);
        newLayout.setSize(new Dimension(layoutWidth, layoutHeight));
        if (layout == null) {
            vv.setGraphLayout(newLayout);
        }
        this.layout = newLayout;
        vv.scaleToLayout(this.scaler);
        double viewX = transformer.getTransformer(Layer.VIEW).getTranslateX();
        double viewY = transformer.getTransformer(Layer.VIEW).getTranslateX();
        double scaleViewX = viewX * scale;
        double scaleViewY = viewY * scale;
        transformer.getTransformer(Layer.VIEW).translate(-scaleViewX, -scaleViewY);
    }

    /** VertexLabel is not parameterized in Jung. In order to avoid to make all things
     *  unchecked, the default label position setting is done in this method. */
    @SuppressWarnings("unchecked")
    private void setDefaultLabelPosition() {
        vv.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);
    }

    public void zoomIn() {
        scaler.scale(vv, 1.1f, vv.getCenter());
    }

    public void zoomOut() {
        scaler.scale(vv, 1 / 1.1f, vv.getCenter());
    }

    public void changeMode(ModalGraphMouse.Mode mode) {
        graphMouse.setMode(mode);
        this.currentMode = mode;
        if (mode.equals(ModalGraphMouse.Mode.PICKING)) {
            pickingAction.setEnabled(false);
            transformAction.setEnabled(true);
        } else {
            pickingAction.setEnabled(true);
            transformAction.setEnabled(false);
        }
    }

    private void togglePaintEdgeLabels() {
        this.showEdgeLabels = !this.showEdgeLabels;
        if (this.showEdgeLabels) {
            vv.getRenderContext().setEdgeLabelTransformer(new Transformer<E, String>() {

                public String transform(E object) {
                    return graphCreator.getEdgeName(object);
                }
            });
        } else {
            vv.getRenderContext().setEdgeLabelTransformer(new Transformer<E, String>() {

                public String transform(E object) {
                    return null;
                }
            });
        }
        vv.repaint();
    }

    private void togglePaintVertexLabels() {
        this.showVertexLabels = !this.showVertexLabels;
        if (this.showVertexLabels) {
            Renderer.Vertex<V, E> vertexRenderer = graphCreator.getVertexRenderer();
            if (vertexRenderer != null) vv.getRenderer().setVertexRenderer(vertexRenderer);
            vv.getRenderContext().setVertexShapeTransformer(new ExtendedVertexShapeTransformer<V>(graphCreator));
            vv.getRenderContext().setVertexLabelTransformer(new Transformer<V, String>() {

                public String transform(V object) {
                    return graphCreator.getVertexName(object);
                }
            });
        } else {
            vv.getRenderer().setVertexRenderer(new BasicVertexRenderer<V, E>());
            vv.getRenderContext().setVertexShapeTransformer(new BasicVertexShapeTransformer<V>());
            vv.getRenderContext().setVertexLabelTransformer(new Transformer<V, String>() {

                public String transform(V object) {
                    return null;
                }
            });
        }
        vv.repaint();
    }
}
