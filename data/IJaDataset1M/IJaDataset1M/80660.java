package edu.whitman.halfway.util.graphs;

import org.apache.log4j.Logger;
import salvo.jesus.graph.VertexImpl;
import salvo.jesus.graph.Vertex;
import salvo.jesus.graph.DirectedWeightedEdgeImpl;
import salvo.jesus.graph.DirectedGraphImpl;
import salvo.jesus.graph.visual.*;
import salvo.jesus.graph.visual.layout.StraightLineLayout;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Dimension;
import javax.swing.JFrame;

/** Misc static methods for working with graphs.*/
public class GraphUtil {

    private static Logger log = Logger.getLogger(GraphUtil.class);

    public static salvo.jesus.graph.Graph getOpenGraph(DirectedGraph graph, double[] vLabel, double[] eLabel) {
        int n = graph.numNodes();
        String[] vStr = null;
        if (vLabel != null) {
            vStr = new String[n];
            for (int i = 0; i < n; i++) {
                vStr[i] = Double.toString(vLabel[i]);
            }
        }
        int ne = graph.numEdges();
        String[] eStr = null;
        if (eLabel != null) {
            eStr = new String[ne];
            for (int i = 0; i < ne; i++) {
                eStr[i] = Double.toString(eLabel[i]);
            }
        }
        return getOpenGraph(graph, vStr, eStr);
    }

    public static salvo.jesus.graph.Graph getOpenGraph(DirectedGraph graph) {
        return getOpenGraph(graph, (String[]) null, null);
    }

    /** Creates an OpenJGraph Graph from the given graph, labelling
     * each vertex with the given label*/
    public static salvo.jesus.graph.Graph getOpenGraph(DirectedGraph graph, String[] vLabel, String[] eLabel) {
        if (eLabel != null && eLabel.length != graph.numEdges()) {
            throw new IllegalArgumentException("Error, invalid eLabel length " + eLabel.length);
        } else if (vLabel != null && vLabel.length != graph.numNodes()) {
            throw new IllegalArgumentException("Error, invalid vLabel length " + vLabel.length);
        }
        try {
            int n = graph.numNodes();
            DirectedGraphImpl openGraph = new DirectedGraphImpl();
            Vertex[] v = new Vertex[n];
            for (int i = 0; i < n; i++) {
                v[i] = new VertexImpl();
                if (vLabel != null && vLabel[i] != null) {
                    v[i].setLabel(vLabel[i]);
                } else {
                    v[i].setLabel(Integer.toString(i));
                }
                log.assertLog(v[i].getLabel() != null, "Error, null label on " + v[i]);
                openGraph.add(v[i]);
            }
            int ne = graph.numEdges();
            for (int i = 0; i < ne; i++) {
                DirectedGraph.Edge e = graph.getEdgeByIndex(i);
                log.assertLog(e != null, "Error, got null edge for " + i);
                DirectedWeightedEdgeImpl edge = new DirectedWeightedEdgeImpl(v[e.source], v[e.sink], e.getWeight());
                edge.setFollowVertexLabel(false);
                if (eLabel != null && eLabel[e.index] != null) {
                    edge.setLabel(eLabel[e.index]);
                } else {
                    edge.setLabel(Double.toString(e.getWeight()));
                }
                log.assertLog(edge.getLabel() != null, "Error, null label on " + edge);
                openGraph.addEdge(edge);
            }
            return openGraph;
        } catch (Exception e) {
            log.error("Error, caught graph exception " + e, e);
            return null;
        }
    }

    public static void displayGraph(salvo.jesus.graph.Graph graph, String title) {
        GraphFrame frame = new GraphFrame(graph);
        frame.setTitle(title);
        frame.setVisible(true);
    }

    public static void displayGraph(salvo.jesus.graph.Graph graph) {
        displayGraph(graph, "");
    }

    static class GraphFrame extends JFrame {

        GraphScrollPane gpanel;

        public GraphFrame(salvo.jesus.graph.Graph graph) {
            gpanel = new GraphScrollPane(new VisualGraph(graph));
            this.getContentPane().add(gpanel);
            gpanel.setGraphLayoutManager(new StraightLineLayout(gpanel.getVisualGraph()));
            this.addWindowListener(new WindowAdapter() {

                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            });
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            Dimension frameSize = new Dimension((int) (screenSize.width / 1.5), (int) (screenSize.height / 1.5));
            this.setSize(frameSize);
            this.setLocation((int) (screenSize.getWidth() - frameSize.getWidth()) / 2, (int) (screenSize.getHeight() - frameSize.getHeight()) / 2);
        }
    }
}
