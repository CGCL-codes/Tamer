package nl.vu.cs.pato.gui;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.decorators.EdgeStringer;
import edu.uci.ics.jung.graph.decorators.StringLabeller;
import edu.uci.ics.jung.graph.decorators.VertexColorFunction;
import edu.uci.ics.jung.graph.impl.DirectedSparseEdge;
import edu.uci.ics.jung.graph.impl.SparseGraph;
import edu.uci.ics.jung.graph.impl.SparseVertex;
import edu.uci.ics.jung.graph.impl.UndirectedSparseEdge;
import edu.uci.ics.jung.visualization.FRLayout;
import edu.uci.ics.jung.visualization.PluggableRenderer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import nl.vu.cs.pato.net.Arc;
import nl.vu.cs.pato.net.Edge;
import nl.vu.cs.pato.net.Network;
import nl.vu.cs.pato.net.Vertex;

/**
 * A graph frame for showing the graph that corresponds to a network.
 *
 * @version $Rev: 235 $
 * @author Maarten Menken
 */
public class GraphFrame extends javax.swing.JFrame {

    private class VertexColorer implements VertexColorFunction {

        private Map<edu.uci.ics.jung.graph.Vertex, Color> vertexForeColors = new HashMap<edu.uci.ics.jung.graph.Vertex, Color>();

        private Map<edu.uci.ics.jung.graph.Vertex, Color> vertexBackColors = new HashMap<edu.uci.ics.jung.graph.Vertex, Color>();

        /**
         * Sets the fore and back color for the specified vertex.
         *
         * @param vertex a vertex
         * @param foreColor the fore color
         * @param backColor the back color
         */
        public void setColor(edu.uci.ics.jung.graph.Vertex vertex, Color foreColor, Color backColor) {
            vertexForeColors.put(vertex, foreColor);
            vertexBackColors.put(vertex, backColor);
        }

        /**
         * Gets the fore color for the specified vertex.
         *
         * @param vertex a vertex
         * @return the fore color
         */
        public Color getForeColor(edu.uci.ics.jung.graph.Vertex vertex) {
            return vertexForeColors.get(vertex);
        }

        /**
         * Gets the back color for the specified vertex.
         *
         * @param vertex a vertex
         * @return the back color
         */
        public Color getBackColor(edu.uci.ics.jung.graph.Vertex vertex) {
            return vertexBackColors.get(vertex);
        }

        /**
         * Clears all information about the colors of the vertices.
         */
        public void clear() {
            vertexForeColors.clear();
            vertexBackColors.clear();
        }
    }

    ;

    private class EdgeLabeller implements EdgeStringer {

        private Map<edu.uci.ics.jung.graph.Edge, String> edgeLabels = new HashMap<edu.uci.ics.jung.graph.Edge, String>();

        /**
         * Gets the label associated with a particular edge.
         *
         * @param edge an edge
         * @return the label
         */
        public String getLabel(edu.uci.ics.jung.graph.Edge edge) {
            return edgeLabels.get(edge);
        }

        /**
         * Associates an edge with a label. This method overwrites any previous labels on this edge.
         *
         * @param edge an edge
         * @param label a label
         */
        public void setLabel(edu.uci.ics.jung.graph.Edge edge, String label) {
            edgeLabels.put(edge, label);
        }

        /**
         * Clears all information about the labels of the edges.
         */
        public void clear() {
            edgeLabels.clear();
        }
    }

    ;

    private static final Color[] COLORS = { Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.CYAN, Color.MAGENTA, Color.ORANGE, Color.PINK, Color.DARK_GRAY, Color.GRAY, Color.LIGHT_GRAY, Color.BLACK, Color.WHITE };

    private static final long LOWER_TEMPERATURE_INTERVAL = 5000l;

    private static final Object[] LOWER_TEMPERATURE_DIALOG_OPTIONS = { "Yes", "Yes, and don't ask again for one minute", "No" };

    private static final long serialVersionUID = 1L;

    private static Logger myLogger = Logger.getLogger(GraphFrame.class.getName());

    private MainWindow mainWindow;

    private Graph graph = new SparseGraph();

    private FRLayout layout = new FRLayout(graph);

    private PluggableRenderer renderer = new PluggableRenderer();

    private VisualizationViewer visualizationViewer = new VisualizationViewer(layout, renderer);

    private int colorIndex = 0;

    private VertexColorer vertexColors = new VertexColorer();

    private StringLabeller vertexLabels = StringLabeller.getLabeller(graph);

    private EdgeLabeller edgeLabels = new EdgeLabeller();

    /**
     * Creates a graph frame.
     *
     * @param mainWindow the main window of the Pato GUI
     */
    public GraphFrame(MainWindow mainWindow) {
        initComponents();
        this.mainWindow = mainWindow;
        renderer.setVertexColorFunction(vertexColors);
        renderer.setVertexStringer(vertexLabels);
        renderer.setEdgeStringer(edgeLabels);
        visualizationViewer.setBackground(Color.WHITE);
        graphScrollPane.setViewportView(visualizationViewer);
    }

    private void pauseVisualization() {
        visualizationViewer.suspend();
    }

    private void resumeVisualization() {
        layout.update();
        visualizationViewer.unsuspend();
        visualizationViewer.repaint();
    }

    /**
     * Removes all vertices (and edges) from the graph.
     */
    public void clearGraph() {
        pauseVisualization();
        graph.removeAllVertices();
        vertexColors.clear();
        vertexLabels.clear();
        edgeLabels.clear();
        colorIndex = 0;
        resumeVisualization();
    }

    private void lowerTemperature() {
        if (layout.isIncremental()) {
            if (layout.incrementsAreDone()) {
                layout.restart();
            }
            long nextDialogTime = System.currentTimeMillis() + LOWER_TEMPERATURE_INTERVAL;
            while (!layout.incrementsAreDone()) {
                layout.advancePositions();
                if (System.currentTimeMillis() > nextDialogTime) {
                    visualizationViewer.repaint();
                    int reply = JOptionPane.showOptionDialog(this, "Do you want to continue lowering the temperature?", "Continue lowering temperature", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, LOWER_TEMPERATURE_DIALOG_OPTIONS, LOWER_TEMPERATURE_DIALOG_OPTIONS[0]);
                    if (reply == 0) {
                        nextDialogTime = System.currentTimeMillis() + LOWER_TEMPERATURE_INTERVAL;
                    } else if (reply == 1) {
                        nextDialogTime = System.currentTimeMillis() + 60000l;
                    } else {
                        break;
                    }
                }
            }
            visualizationViewer.repaint();
        }
    }

    /**
     * Creates a graph from the specified network.
     *
     * @param network a network
     */
    public void createGraph(Network network) {
        pauseVisualization();
        int uniqueNumber;
        Color color = COLORS[colorIndex];
        Map<Vertex, edu.uci.ics.jung.graph.Vertex> vertexToJungVertex = new HashMap<Vertex, edu.uci.ics.jung.graph.Vertex>();
        for (Vertex vertex : network.getVertices()) {
            edu.uci.ics.jung.graph.Vertex jungVertex = new SparseVertex();
            graph.addVertex(jungVertex);
            uniqueNumber = 2;
            String label = vertex.getLabel();
            while (true) {
                try {
                    vertexLabels.setLabel(jungVertex, label);
                    break;
                } catch (StringLabeller.UniqueLabelException e) {
                    label = vertex.getLabel() + " (" + uniqueNumber + ")";
                    uniqueNumber++;
                }
            }
            vertexColors.setColor(jungVertex, Color.BLACK, color);
            vertexToJungVertex.put(vertex, jungVertex);
        }
        for (Arc arc : network.getArcs()) {
            Vertex fromVertex = arc.getFromVertex();
            Vertex toVertex = arc.getToVertex();
            edu.uci.ics.jung.graph.Vertex fromJungVertex = vertexToJungVertex.get(fromVertex);
            edu.uci.ics.jung.graph.Vertex toJungVertex = vertexToJungVertex.get(toVertex);
            edu.uci.ics.jung.graph.Edge jungEdge = new DirectedSparseEdge(fromJungVertex, toJungVertex);
            graph.addEdge(jungEdge);
            edgeLabels.setLabel(jungEdge, arc.getLabel());
        }
        for (Edge edge : network.getEdges()) {
            Vertex fromVertex = edge.getVertex1();
            Vertex toVertex = edge.getVertex2();
            edu.uci.ics.jung.graph.Vertex fromJungVertex = vertexToJungVertex.get(fromVertex);
            edu.uci.ics.jung.graph.Vertex toJungVertex = vertexToJungVertex.get(toVertex);
            edu.uci.ics.jung.graph.Edge jungEdge = new UndirectedSparseEdge(fromJungVertex, toJungVertex);
            graph.addEdge(jungEdge);
            edgeLabels.setLabel(jungEdge, edge.getLabel());
        }
        setNextColorIndex();
        resumeVisualization();
    }

    private void setNextColorIndex() {
        colorIndex++;
        if (colorIndex >= COLORS.length) {
            colorIndex = 0;
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        graphPanel = new javax.swing.JPanel();
        graphScrollPane = new javax.swing.JScrollPane();
        coolDownButton = new javax.swing.JButton();
        closeButton = new javax.swing.JButton();
        getContentPane().setLayout(new java.awt.GridBagLayout());
        setTitle("Graph");
        addComponentListener(new java.awt.event.ComponentAdapter() {

            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        graphPanel.setLayout(new java.awt.GridBagLayout());
        graphPanel.setBorder(new javax.swing.border.TitledBorder("Graph"));
        graphPanel.setPreferredSize(new java.awt.Dimension(800, 600));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        graphPanel.add(graphScrollPane, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(graphPanel, gridBagConstraints);
        coolDownButton.setText("Cool down");
        coolDownButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                coolDownButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(coolDownButton, gridBagConstraints);
        closeButton.setText("Close");
        closeButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(closeButton, gridBagConstraints);
        pack();
    }

    private void coolDownButtonActionPerformed(java.awt.event.ActionEvent evt) {
        lowerTemperature();
    }

    private void formComponentResized(java.awt.event.ComponentEvent evt) {
        layout.resize(visualizationViewer.getSize());
        layout.restart();
    }

    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {
        mainWindow.graphMenuItem.doClick();
    }

    private void formWindowClosing(java.awt.event.WindowEvent evt) {
        mainWindow.graphMenuItem.doClick();
    }

    private javax.swing.JButton closeButton;

    private javax.swing.JButton coolDownButton;

    private javax.swing.JPanel graphPanel;

    private javax.swing.JScrollPane graphScrollPane;
}
