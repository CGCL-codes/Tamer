package fluid.tree;

import fluid.ir.IRLocation;
import fluid.ir.IRNode;

/** Class for events where edges of locus are modified.
 * @see ChildEdgeEvent
 * @see ParentEdgeEvent
 */
public class NodeEdgeEvent extends NodeEvent {

    private final IRLocation location;

    private final IRNode edgeNode;

    public NodeEdgeEvent(DigraphInterface dig, IRNode node, IRLocation loc, IRNode edge) {
        super(dig, node);
        location = loc;
        edgeNode = edge;
    }

    public IRLocation getLocation() {
        return location;
    }

    public IRNode getEdge() {
        return edgeNode;
    }
}
