package ru.cos.sim.road.node;

import ru.cos.cs.lengthy.Fork;
import ru.cos.cs.lengthy.Join;
import ru.cos.sim.road.AbstractRoadTrajectory;
import ru.cos.sim.road.link.Lane;
import ru.cos.sim.road.link.Link;

/**
 * 
 * @author zroslaw
 */
public class TransitionRule extends AbstractRoadTrajectory {

    /**
	 * Transition rule unique agentId in the node
	 */
    protected int id;

    /**
	 * Parent node
	 */
    protected Node node;

    /**
	 * Constructor
	 * @param agentId agentId of the transition rule
	 * @param length of the transition rule
	 */
    public TransitionRule(int id, float length) {
        super(length);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    /**
	 * Retrieve parent node
	 * @return parent node
	 */
    public Node getNode() {
        return node;
    }

    /**
	 * Set parent node
	 * @param node the node to set
	 */
    public void setNode(Node node) {
        this.node = node;
    }

    @Override
    public final RoadTrajectoryType getRoadTrajectoryType() {
        return RoadTrajectoryType.TransitionRule;
    }

    public Link getNextLink() {
        NodeJoin nodeJoin = (NodeJoin) getNext();
        Lane nextLane = (Lane) nodeJoin.getNext();
        return nextLane.getLink();
    }

    public Lane getDestinationLane() {
        return (Lane) ((NodeJoin) next).getNext();
    }

    @Override
    public boolean isTransitionRules() {
        return true;
    }

    public Lane getOutgoingLane() {
        return (Lane) ((Join) next).getNext();
    }

    public Lane getIncomingLane() {
        return (Lane) ((Fork) prev).getPrev();
    }
}
