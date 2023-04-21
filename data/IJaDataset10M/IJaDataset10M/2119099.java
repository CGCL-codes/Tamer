package net.sf.magicmap.client.model.location.jung;

import net.sf.magicmap.client.model.node.IMagicEdge;
import net.sf.magicmap.client.model.node.Node;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.graph.impl.DirectedSparseEdge;

public class JungEdge extends DirectedSparseEdge implements IMagicEdge {

    public JungEdge(Vertex arg0, Vertex arg1) {
        super(arg0, arg1);
    }

    public Node getSourceNode() {
        return (Node) getSource().getUserDatum(LayoutSettings.NODE_KEY);
    }

    public Node getTargetNode() {
        return (Node) getDest().getUserDatum(LayoutSettings.NODE_KEY);
    }
}
