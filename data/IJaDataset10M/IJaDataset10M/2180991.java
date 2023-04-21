package edu.whitman.halfway.util.graphs;

import edu.whitman.halfway.util.IntegerBinaryHeap;
import cern.colt.list.IntArrayList;
import org.apache.log4j.Logger;

/**
   A simple implementation of Dijkstra's Alg.
   Given a graph and a destination node or set of goal nodes , provides:
     1) distance from each node to the nearest goal
     2) for each node, the node to go to next (succ) on a shortest path to a goal

*/
public class DijkstraAlg {

    IntegerBinaryHeap heap;

    DirectedGraph graph;

    IntArrayList goalList;

    int n;

    private static Logger log = Logger.getLogger(DijkstraAlg.class);

    double[] pathLength;

    int[] succ;

    /** Constructs a problem on the given graph, with the given source */
    public DijkstraAlg(DirectedGraph g, int goal) {
        this.graph = g;
        n = graph.numNodes();
        heap = new IntegerBinaryHeap(n);
        goalList = new IntArrayList(1);
        goalList.add(goal);
        pathLength = null;
        succ = null;
    }

    /** Constructs a problem on the given graph, with the array of sourcesgiven sources */
    public DijkstraAlg(DirectedGraph g, IntArrayList goals) {
        this.graph = g;
        n = graph.numNodes();
        heap = new IntegerBinaryHeap(n);
        this.goalList = goals;
        pathLength = null;
        succ = null;
        if (goalList.size() == 0) {
            throw new IllegalArgumentException("You must specify at least one goal node.");
        }
    }

    /** Returns an array where succ[s] is the next vertex after s on a
     * shortest path to some goal */
    public int[] getSucc() {
        if (succ == null) solve();
        return succ;
    }

    public double[] getDistances() {
        if (pathLength == null) solve();
        return pathLength;
    }

    private void solve() {
        succ = new int[n];
        for (int i = 0; i < n; i++) {
            heap.insert(i, Double.MAX_VALUE);
            succ[i] = -1;
        }
        for (int i = 0; i < goalList.size(); i++) {
            heap.decreaseKey(goalList.get(i), 0);
        }
        while (!heap.isEmpty()) {
            int u = heap.deleteMin();
            double td = heap.getPriority(u);
            DirectedGraph.Edge[] edgeArray = graph.getArcsIn(u);
            int na = edgeArray.length;
            for (int a = 0; a < na; a++) {
                DirectedGraph.Edge edge = edgeArray[a];
                double w = edge.getWeight();
                if (log.isDebugEnabled() && w < 0) log.debug("Got edge weight " + w + " on edge " + edge);
                double posNewDist = td + w;
                if (heap.getPriority(edge.source) > posNewDist) {
                    succ[edge.source] = u;
                    heap.decreaseKey(edge.source, posNewDist);
                }
            }
        }
        pathLength = (double[]) heap.getPriorities().clone();
    }
}
