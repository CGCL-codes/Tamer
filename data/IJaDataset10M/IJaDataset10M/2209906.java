package com.rapidace.visualization.graphs;

import edu.uci.ics.jung.graph.SparseMultigraph;

;

public class DMLSparseGraph<V, E> extends SparseMultigraph<V, E> {

    /**
	 * 
	 */
    private static final long serialVersionUID = -3952662012726496395L;

    private String globalGraphName;

    /**
	 * 
	 */
    public DMLSparseGraph() {
        super();
        setGraphName("");
    }

    public void setGraphName(String graphName) {
        globalGraphName = graphName;
    }

    public String getGraphName() {
        return (globalGraphName);
    }

    public String toString() {
        if (globalGraphName.length() > 0) return (globalGraphName);
        return (super.toString());
    }
}
