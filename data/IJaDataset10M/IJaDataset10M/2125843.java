package ti.constraint;

import ti.event.Event;

/**
 * A constraint based on source node-id
 */
public class NodeIdConstraint extends Constraint {

    private int nodeId;

    public NodeIdConstraint(int nodeId) {
        this.nodeId = nodeId;
    }

    public String getCategoryName() {
        return null;
    }

    public String getTrigger() {
        String source = null;
        return Integer.toString(nodeId);
    }

    public boolean pass(Event evt) {
        return evt.getSourceNodeId() == nodeId;
    }
}
