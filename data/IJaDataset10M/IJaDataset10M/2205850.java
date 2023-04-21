package org.expasy.jpl.msmatch.model;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.apache.commons.collections15.Transformer;
import org.expasy.jpl.commons.collection.graph.GraphFilterer;
import edu.uci.ics.jung.algorithms.cluster.WeakComponentClusterer;
import edu.uci.ics.jung.algorithms.filters.FilterUtils;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Pair;

/**
 * This class handles conflicts in {@code PeakListMatcher}.
 * 
 * <h4>Algorithm</h4>
 * 
 * <pre>
 * DATA
 * graph: the input graph to solve conflicts from
 * wcc: set of nodes
 * se: set of selected edges
 * sg: subgraph of graph
 * e: edge
 * outgraph: the output graph without conflicts returned by the algorithm
 * 
 * BEGIN
 * graph <- input param
 * Compute the weak connected components (wcc) of graph of matches
 * se <- empty list of future selected edges
 * FOR each wcc DO
 *   sg <- subgraph(graph, component)
 *   WHILE sg has edges DO
 *     IF sg.nodes count > 2 THEN
 *       e <- get the min value edge
 *       IF e is valid THEN
 *         add e in se
 *       FI
 *       remove incident nodes of e in sg
 *       remove disconnected nodes in sg
 *     FI
 *   ELIHW
 * ROF
 * outgraph <- partialgraph(graph, se)
 * return outgraph
 * END
 *         
 * Example:
 * 
 * g = {
 * 0 1 2 3 4 5 6
 * |/    |/|   |
 * 7     8 9   10
 * Edges:
 * 1:d(0,7)=0.09
 * 2:d(1,7)=0.00
 * 3:d(3,8)=0.00
 * 4:d(4,8)=0.56
 * 5:d(4,9)=0.58
 * 6:d(6,10)=0.0
 * }
 * 
 *       : 3 wccs=(0,1,7), (3,4,8,9), (6,10)
 *       : sg = subgraph(g, wccs[0])
 *       : sq.edgecount > 0 => true
 * sg={0 1
 *     |/
 *     7}  
 *       : min(d(0,7), d(1,7))-> d(1,7)
 *       : remove nodes 1 and 7
 * sg={0}
 *       : remove disconnected nodes
 * sg={}
 *       : sq.edgecount > 0 => false
 *       : sg = subgraph(g, wccs[1])
 * 	  : sq.edgecount > 0 => true
 * sg={3 4
 *     |/|
 *     8 9}
 *       : min(d(3,8), d(4,8), d(4,9))-> d(3,8)
 * 	  : remove nodes 3 and 8
 * 	  : remove disconnected nodes
 * sg={4
 *     |
 *     9}
 *  ...
 *  
 * outgraph = partialgraph(graph, se={1-7, 3-8, 4-9, 6-10})
 * 
 * 0 1 2 3 4 5 6
 *  /    | |   |
 * 7     8 9   10
 * </pre>
 * 
 * @author nikitin
 * 
 * @version 1.0.0
 * 
 */
public class ConflictSolver implements Transformer<Graph<Integer, String>, Graph<Integer, String>> {

    private static final ConflictSolver INSTANCE = new ConflictSolver();

    /** the clusterer searches weak connected components in graph */
    protected WeakComponentClusterer<Integer, String> wcClusterer;

    private ConflictSolver() {
        this.wcClusterer = new WeakComponentClusterer<Integer, String>();
    }

    public static ConflictSolver getInstance() {
        return INSTANCE;
    }

    /**
	 * Return true if a conflict is detected.
	 * 
	 * @param matches the peak matches.
	 * @return true if has conflicts.
	 */
    public boolean hasConflicts(Graph<Integer, String> matches) {
        Set<Set<Integer>> wcs = wcClusterer.transform(matches);
        for (Set<Integer> s : wcs) {
            if (s.size() > 2) {
                return true;
            }
        }
        return false;
    }

    /**
	 * Return the number of conflicts.
	 * 
	 * @param matches the peak matches.
	 * @return the number of conflicts.
	 */
    public int getNumOfConflicts(Graph<Integer, String> matches) {
        Set<Set<Integer>> wcs = wcClusterer.transform(matches);
        int count = 0;
        for (Set<Integer> s : wcs) {
            if (s.size() > 2) {
                count++;
            }
        }
        return count;
    }

    /**
	 * Solve conflicts and update the graph of matches.
	 * 
	 * <h4>warning (bug to fix)</h4>
	 * <p>
	 * TODO: change the edge coding algorithm (a bug appears when size of
	 * spectrum group > 36). Before the fix is done, try to better filter your
	 * spectra.
	 * </p>
	 * 
	 * @param matcher the matcher with infos on spectra.
	 * @param graph the graph to handle conflicts on.
	 * @return a graph without conflicts.
	 * 
	 * @throws MatchConflictRuntimeException if conflicts cannot be solved.
	 */
    public Graph<Integer, String> transform(Graph<Integer, String> graph) {
        GraphFilterer<Integer, String> filterer = GraphFilterer.newInstance(graph);
        Set<Set<Integer>> clusters = wcClusterer.transform(graph);
        Set<String> selectedEdges = new HashSet<String>();
        for (Set<Integer> clusterSet : clusters) {
            if (clusterSet.size() > 2) {
                Graph<Integer, String> sg = FilterUtils.createInducedSubgraph(clusterSet, graph);
                HashSet<String> selectedClusterEdges = new HashSet<String>();
                while (sg.getEdges().size() > 0) {
                    String edge = getNextUnconflictedEdge(sg);
                    if (isValidEdgeCandidate(edge, graph, selectedClusterEdges)) {
                        selectedClusterEdges.add(edge);
                    }
                    Pair<Integer> nodes = sg.getEndpoints(edge);
                    sg.removeVertex(nodes.getFirst());
                    sg.removeVertex(nodes.getSecond());
                    Set<Integer> nodes2remove = new HashSet<Integer>();
                    for (Integer vertex : sg.getVertices()) {
                        if (sg.degree(vertex) == 0) {
                            nodes2remove.add(vertex);
                        }
                    }
                    for (Integer vertex : nodes2remove) {
                        sg.removeVertex(vertex);
                    }
                }
                selectedEdges.addAll(selectedClusterEdges);
            } else if (clusterSet.size() == 2) {
                Iterator<Integer> iter = clusterSet.iterator();
                selectedEdges.add(graph.findEdge(iter.next(), iter.next()));
            }
        }
        return filterer.getPartialGraph(selectedEdges, false);
    }

    private boolean isValidEdgeCandidate(String candidateEdge, Graph<Integer, String> g, Set<String> selectedEdges) {
        Pair<Integer> candidateNodes = g.getEndpoints(candidateEdge);
        for (String selectedEdge : selectedEdges) {
            Pair<Integer> selectedNodes = g.getEndpoints(selectedEdge);
            boolean isDiffSp1Pos = ((candidateNodes.getFirst() - selectedNodes.getFirst()) > 0) ? true : false;
            boolean isDiffSp2Pos = ((candidateNodes.getSecond() - selectedNodes.getSecond()) > 0) ? true : false;
            if (isDiffSp1Pos != isDiffSp2Pos) {
                return false;
            }
        }
        return true;
    }

    private String getNextUnconflictedEdge(Graph<Integer, String> conflictualSubgraph) {
        String selectedEdge = null;
        double min = Double.MAX_VALUE;
        for (String edge : conflictualSubgraph.getEdges()) {
            double dist = Double.parseDouble(edge.split("=")[1]);
            if (dist < min) {
                selectedEdge = edge;
                min = dist;
            }
        }
        return selectedEdge;
    }
}
