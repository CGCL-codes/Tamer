package game.visualizations.structure;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Paint;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Set;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationImageServer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import edu.uci.ics.jung.visualization.renderers.VertexLabelRenderer;
import game.classifier.GameClassifierContainer;
import game.classifiers.Classifier;
import game.model.GameModelContainer;
import game.models.ModelLearnable;
import game.visualizations.Visualization;
import game.visualizations.config.GameModelStructureVisualizationConfig;
import game.visualizations.config.VisualizationConfig;
import javax.swing.JComponent;
import javax.swing.JLabel;
import org.apache.commons.collections15.Transformer;
import com.rapidminer.RapidMiner;
import com.rapidminer.gui.graphs.GraphViewer;
import com.rapidminer.gui.tools.SwingTools;

/**
 * A visualization of the structure of GAME model.
 * 
 * @author Jan Fabian
 *
 */
public class GameModelStructureVisualization extends Visualization {

    VisualizationViewer<Object, Integer> chart = null;

    JComponent controls = null;

    GameStructureGraphViewer viewer = null;

    Object root = null;

    public GameModelStructureVisualization(Object o) {
        this(o, true, true);
    }

    public GameModelStructureVisualization(Object o, boolean addOutput) {
        this(o, true, addOutput);
    }

    public void setNodeSize(Dimension size) {
        viewer.setNodeSize(size);
    }

    public GameModelStructureVisualization(Object o, boolean expandClassifiers, boolean addOutput) {
        GameStructureGraphCreator creator = null;
        if (o instanceof GameModelContainer) {
            o = ((GameModelContainer) o).getModel().get(0).getModel();
        } else if (o instanceof GameClassifierContainer) {
            o = ((GameClassifierContainer) o).getConnectableClassifier().getClassifier();
        }
        if (o instanceof Classifier) {
            creator = new GameStructureGraphCreator((Classifier) o, expandClassifiers, addOutput);
        } else if (o instanceof ModelLearnable) {
            creator = new GameStructureGraphCreator((ModelLearnable) o, addOutput);
        }
        root = o;
        if (creator != null) {
            viewer = new GameStructureGraphViewer(creator);
        } else {
            viewer = new GameStructureGraphViewer(new GameStructureGraphCreator());
        }
        if (viewer != null) {
            try {
                chart = viewer.getVisualizationComponent();
                controls = viewer.createControlPanel();
                chart.addMouseListener(new MouseAdapter() {

                    @Override
                    public void mouseReleased(MouseEvent arg1) {
                        setChanged();
                        notifyObservers();
                        clearChanged();
                    }
                });
            } catch (Exception ex) {
                throw new RuntimeException("Error creating Game model structure visualization.");
            }
        }
    }

    public void pickRoot() {
        chart.getPickedVertexState().pick(root, true);
    }

    public void setDefaultLayout() {
        viewer.setDefaultLayout();
    }

    public void togglePaintVertexLabels(boolean toggle) {
        viewer.toggleGamePaintVertexLabels(toggle);
    }

    public void setPickingMode() {
        viewer.setPickingMode();
    }

    public Object getSelectedModel() {
        Set<Object> picked = chart.getPickedVertexState().getPicked();
        if (picked.size() == 1) {
            return picked.toArray()[0];
        }
        return null;
    }

    public Object[] getSelectedModels() {
        return chart.getPickedVertexState().getPicked().toArray();
    }

    @Override
    public JComponent getChart() {
        return chart;
    }

    @Override
    public VisualizationConfig getConfig() {
        return new GameModelStructureVisualizationConfig();
    }

    @Override
    public JComponent getControls() {
        return controls;
    }

    @Override
    public void setConfig(VisualizationConfig cfg) {
        if (!(cfg instanceof GameModelStructureVisualizationConfig)) throw new IllegalArgumentException("Invalid config: " + cfg.getClass().getName());
    }

    @Override
    public String toString() {
        return "Structure";
    }

    @Override
    public Class<? extends VisualizationConfig> getConfigClass() {
        return GameModelStructureVisualizationConfig.class;
    }

    public Image getImage(Dimension size) {
        setNodeSize(size);
        final GameStructureGraphCreator graphCreator = viewer.getGameGraphCreator();
        graphCreator.setAdditionalDisplay(GameStructureGraphCreator.CHART);
        Graph<Object, Integer> graph = graphCreator.createGraph();
        GameLayout layout = new GameLayout(viewer);
        VisualizationImageServer<Object, Integer> vv = new VisualizationImageServer<Object, Integer>(layout, layout.getSize());
        vv.setBackground(Color.white);
        vv.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line<Object, Integer>());
        vv.getRenderContext().setEdgeDrawPaintTransformer(new Transformer<Integer, Paint>() {

            public Paint transform(Integer edge) {
                double edgeStrength = graphCreator.getEdgeStrength(edge);
                int value = (int) Math.max(140, Math.min(230, 230 - edgeStrength * 90));
                return new Color(value, value, value);
            }
        });
        vv.getRenderContext().setArrowDrawPaintTransformer(new Transformer<Integer, Paint>() {

            public Paint transform(Integer edge) {
                double edgeStrength = graphCreator.getEdgeStrength(edge);
                int value = (int) Math.max(140, Math.min(230, 230 - edgeStrength * 90));
                return new Color(value, value, value).darker();
            }
        });
        vv.getRenderContext().setArrowFillPaintTransformer(new Transformer<Integer, Paint>() {

            public Paint transform(Integer edge) {
                double edgeStrength = graphCreator.getEdgeStrength(edge);
                int value = (int) Math.max(140, Math.min(230, 230 - edgeStrength * 90));
                return new Color(value, value, value);
            }
        });
        vv.getRenderContext().setVertexFontTransformer(new Transformer<Object, Font>() {

            public Font transform(Object vertex) {
                if (graphCreator.isBold(vertex)) return GraphViewer.VERTEX_BOLD_FONT; else return GraphViewer.VERTEX_PLAIN_FONT;
            }
        });
        vv.getRenderContext().setVertexLabelTransformer(new Transformer<Object, String>() {

            public String transform(Object object) {
                return graphCreator.getVertexName(object);
            }
        });
        Transformer<Object, Paint> paintTransformer = graphCreator.getVertexPaintTransformer(vv);
        if (paintTransformer == null) {
            paintTransformer = new Transformer<Object, Paint>() {

                public Paint transform(Object vertex) {
                    return SwingTools.LIGHT_BLUE;
                }
            };
        }
        vv.getRenderContext().setVertexFillPaintTransformer(paintTransformer);
        vv.getRenderContext().setVertexShapeTransformer(new GameStructureVertexShapeTransformer(graphCreator));
        Renderer.Vertex<Object, Integer> vertexRenderer = graphCreator.getVertexRenderer();
        if (vertexRenderer != null) vv.getRenderer().setVertexRenderer(vertexRenderer);
        vv.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);
        Renderer.VertexLabel<Object, Integer> customVertexLabelRenderer = graphCreator.getVertexLabelRenderer();
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
        return vv.getImage(new Point2D.Double(layout.getSize().width / 2, layout.getSize().height / 2), layout.getSize());
    }
}
