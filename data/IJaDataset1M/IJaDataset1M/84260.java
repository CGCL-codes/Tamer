package ru.cos.sim.road.init.data;

import ru.cos.sim.road.node.TransitionRule;

/**
 * Node location data.
 * @author zroslaw
 */
public class NodeLocationData extends LocationData {

    protected int nodeId;

    protected int transitionRuleId;

    public NodeLocationData() {
    }

    public NodeLocationData(TransitionRule transitionRule) {
        this.nodeId = transitionRule.getNode().getId();
        this.transitionRuleId = transitionRule.getId();
    }

    @Override
    public final LocationType getLocationType() {
        return LocationType.NodeLocation;
    }

    public int getNodeId() {
        return nodeId;
    }

    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
    }

    public int getTransitionRuleId() {
        return transitionRuleId;
    }

    public void setTransitionRuleId(int transitionRuleId) {
        this.transitionRuleId = transitionRuleId;
    }
}
