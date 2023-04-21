package org.objectstyle.cayenne.graph;

/**
 * A GraphDiff representing a change in node ID.
 * 
 * @since 1.2
 * @author Andrus Adamchik
 */
public class NodeIdChangeOperation extends NodeDiff {

    protected Object newNodeId;

    public NodeIdChangeOperation(Object nodeId, Object newNodeId) {
        super(nodeId);
        this.newNodeId = newNodeId;
    }

    public NodeIdChangeOperation(Object nodeId, Object newNodeId, int diffId) {
        super(nodeId, diffId);
        this.newNodeId = newNodeId;
    }

    public void apply(GraphChangeHandler tracker) {
        tracker.nodeIdChanged(nodeId, newNodeId);
    }

    public void undo(GraphChangeHandler tracker) {
        tracker.nodeIdChanged(newNodeId, nodeId);
    }
}
