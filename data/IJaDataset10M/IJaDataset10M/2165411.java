package ev.utility;

import giny.model.Edge;
import giny.model.Node;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import multilevelLayoutPlugin.NodePositionManager;
import org.miv.graphstream.graph.Graph;
import org.miv.graphstream.graph.implementations.DefaultGraph;
import org.miv.util.SingletonException;
import cytoscape.CyNetwork;

public class GraphUtility {

    public static List<Node> getCloseNodes(final Node source, final Iterator<Node> nodes, final NodePositionManager posManager, final double radius) {
        final List<Node> results = new ArrayList<Node>();
        while (nodes.hasNext()) {
            final Node u = nodes.next();
            if (u != source && distance(u, source, posManager) <= radius) {
                results.add(u);
            }
        }
        return results;
    }

    private static double distance(final Node u, final Node source, final NodePositionManager posManager) {
        final double x1 = posManager.getX(u.getRootGraphIndex());
        final double y1 = posManager.getY(u.getRootGraphIndex());
        final double x2 = posManager.getX(u.getRootGraphIndex());
        final double y2 = posManager.getY(u.getRootGraphIndex());
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    public static float meanNodeDegree(final CyNetwork net) {
        final Graph g = convertToGraphStreamGraph(net);
        return g.algorithm().getAverageDegree();
    }

    public static double clusteringCoefficient(final CyNetwork net) {
        final Graph g = convertToGraphStreamGraph(net);
        final double[] ccs = g.algorithm().getClusteringCoefficients();
        return Matrix.average(ccs);
    }

    public static float networkDensity(final CyNetwork net) {
        final Graph g = convertToGraphStreamGraph(net);
        return g.algorithm().getDensity();
    }

    public static float networkDegreeAverageDeviation(final CyNetwork net) {
        final Graph g = convertToGraphStreamGraph(net);
        return g.algorithm().getDegreeAverageDeviation();
    }

    public static Graph convertToGraphStreamGraph(final CyNetwork net) {
        final Graph graph = new DefaultGraph();
        for (final Object o : net.edgesList()) {
            final Edge edge = (Edge) o;
            final Node n1 = edge.getSource();
            final Node n2 = edge.getTarget();
            try {
                graph.addNode(n1.toString());
            } catch (final SingletonException ignored) {
            }
            try {
                graph.addNode(n2.toString());
            } catch (final SingletonException ignored) {
            }
            graph.addEdge(edge.toString(), n1.toString(), n2.toString());
        }
        return graph;
    }
}
