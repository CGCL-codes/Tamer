package edu.tufts.cs.geometry;

import java.util.*;
import javax.vecmath.*;

/**
 * A <code>ProximityGraph</code> represents a real proximity graph for a 
 * set of points. It is defined by a set of edges.
 */
public abstract class ProximityGraph extends ComputationUnit implements Iterable<int[]> {

    int edgeCount = 0;

    private Vector<Set<Integer>> edges = null;

    private Set<int[]> edgeList = null;

    GMatrix data = null;

    public ProximityGraph() {
    }

    public void setData(GMatrix data) {
        this.data = data;
        edgeCount = 0;
        edges = new Vector<Set<Integer>>(data.getNumRow());
        for (int i = 0; i < data.getNumRow(); i++) edges.add(new HashSet<Integer>());
        edgeList = new HashSet<int[]>();
    }

    /**
	 * Compute the graph from a initialized set of points. Typically,
	 * this is the slowest part of the calculation.
	 */
    public abstract void computeGraph();

    /**
	 * 
	 * @param vindex
	 * @return
	 */
    public int[] getConnectedVertices(int vindex) {
        Set<Integer> connected = edges.get(vindex);
        int[] retval = new int[connected.size()];
        int i = 0;
        for (Integer cvindex : connected) retval[i++] = cvindex;
        return retval;
    }

    /**
	 * Return the number of edges in the graph
	 * @return the number of edges in the graph
	 */
    public int getEdgeCount() {
        return edgeList.size();
    }

    public Iterator<int[]> iterator() {
        return edgeList.iterator();
    }

    void addEdge(int indexA, int indexB) {
        Set<Integer> set;
        set = edges.get(indexA);
        set.add(indexB);
        set = edges.get(indexB);
        int setsize = set.size();
        set.add(indexA);
        if (setsize < set.size()) edgeList.add(new int[] { indexA, indexB });
    }

    void removeEdge(int indexA, int indexB) {
        Set<Integer> set;
        set = edges.get(indexA);
        set.remove(indexB);
        set = edges.get(indexB);
        set.remove(indexA);
        Set<int[]> newEL = new HashSet<int[]>();
        for (int[] edge : edgeList) {
            if ((edge[0] == indexA) && (edge[1] == indexB)) continue;
            if ((edge[1] == indexA) && (edge[0] == indexB)) continue;
            newEL.add(edge);
        }
        edgeList = newEL;
    }
}
