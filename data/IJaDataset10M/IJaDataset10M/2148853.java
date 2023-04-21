package net.walend.measured.test;

import net.walend.measured.PathMeter;

/**
This simple path meter returns 53 to leave a node, 59 to cross an edge and 61 to enter a node.
<p>
@author <a href="http://walend.net">David Walend</a> <a href="mailto:dfw1@cornell.edu">dfw1@cornell.edu</a>
*/
public class TestPathMeter implements PathMeter {

    public TestPathMeter() {
    }

    /**
The cost to cross an edge.

@param edge is the edge object. In a generic-edge path or digraph, this will be GENERICEDGE.
     */
    public double costToCross(Object fromNode, Object toNode, Object edge) {
        return 59;
    }

    /**
The cost to cross an edge.

@param edge is the edge object. In a generic-edge path or digraph, this will be GENERICEDGE.
     */
    public int intCostToCross(Object fromNode, Object toNode, Object edge) {
        return 59;
    }

    /**
Returns true if this PathMeter can return negative weight edges, false if not. Shortest path algorithms use this method to trigger the bellman-ford algorithm to detect negative weight cycles. If you're not sure, return true.
     */
    public boolean negativeWeightEdges() {
        return false;
    }
}
