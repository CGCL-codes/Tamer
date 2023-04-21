package puppy.graph;

import java.util.HashSet;
import edu.uci.ics.jung.algorithms.filters.KNeighborhoodFilter;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;

public class GraphExtractor {

    private KNeighborhoodFilter<DeliciousNode, DeliciousEdge> a = null;

    public GraphExtractor(int radio, HashSet<DeliciousNode> roots) {
        a = new KNeighborhoodFilter<DeliciousNode, DeliciousEdge>(roots, radio, KNeighborhoodFilter.EdgeType.IN_OUT);
    }

    public Graph<DeliciousNode, DeliciousEdge> extractGraph(UndirectedSparseGraph<DeliciousNode, DeliciousEdge> graph) {
        Graph<DeliciousNode, DeliciousEdge> temp = a.transform(graph);
        return temp;
    }
}
