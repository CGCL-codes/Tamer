package org.mitre.lattice.graph;

import org.jgraph.JGraph;
import org.jgraph.graph.CellMapper;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.GraphModel;
import org.jgraph.graph.VertexView;
import org.mitre.mrald.util.Config;

/**
 *  Class that contains any functions that deal with Graphing of the Lattice
 *  objects, using JGraph as a graphical interface.
 *
 *@author     ghamilton
 *@created    December 12, 2003
 */
public class MraldGraph extends JGraph {

    public MraldGraph() {
        this(null);
    }

    public MraldGraph(GraphModel model) {
        this(model, null);
    }

    public MraldGraph(GraphModel model, GraphLayoutCache view) {
        super(model, view, null);
    }

    protected VertexView createVertexView(JGraph graph, CellMapper cm, Object cell) {
        return Config.getLatticeFactory().createView(cell, this, cm);
    }
}
