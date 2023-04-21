package ru.cos.sim.road.node;

import java.util.HashMap;
import java.util.Map;
import ru.cos.sim.road.link.Link;

/**
 * Destination node. Point in the road network where vehicles 
 * finish their trips and disappears from the model.
 * @author zroslaw
 */
public class DestinationNode extends Node {

    /**
	 * Link that comes to this destination node.
	 */
    protected Link incomingLink;

    protected Map<Integer, TransitionRule> tRules = new HashMap<Integer, TransitionRule>();

    /**
	 * Constructor by node agentId
	 * @param agentId
	 */
    public DestinationNode(int id) {
        super(id);
    }

    @Override
    public final NodeType getNodeType() {
        return NodeType.DestinationNode;
    }

    public Link getIncomingLink() {
        return incomingLink;
    }

    public void setIncomingLink(Link incomingLink) {
        this.incomingLink = incomingLink;
    }

    public Map<Integer, TransitionRule> geTtRules() {
        return tRules;
    }

    public void setTRules(Map<Integer, TransitionRule> tRules) {
        this.tRules = tRules;
    }
}
