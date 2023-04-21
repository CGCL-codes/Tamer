package nz.ac.massey.cs.barrio.clusterer;

import java.util.HashMap;
import java.util.List;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Edge;

public interface Clusterer {

    /**
	 * Removes one or several edges from the graph that have the same highest betweenness value.
	 * @param graph the graph
	 */
    public void cluster(Graph graph);

    /**
	 * Retrieves the list of all edges that were removed by method "cluster(Graph graph)". The edges returned are stored in order in which they were removed 
	 * @return java.util.List list of the removed edges. 
	 */
    public List<Edge> getEdgesRemoved();

    /**
	 * Assigns String attachment of the name of the cluster to the vertex objects for the purpose of identity. 
	 * Cluster names are generated as follows: "cluster-0", "cluster-1", ..., "cluster-n"
	 * @param graph the graph
	 * @return the map of cluster names mapped with the cluster size.
	 */
    public HashMap<String, Integer> nameClusters(Graph graph);
}
