package ru.cos.sim.engine;

import java.util.HashSet;
import java.util.Set;
import ru.cos.cs.agents.framework.Agent;
import ru.cos.cs.agents.framework.Universe;
import ru.cos.sim.road.RoadNetwork;
import ru.cos.sim.road.RoadTrajectory;
import ru.cos.sim.road.exceptions.RoadNetworkException;
import ru.cos.sim.road.init.data.LinkLocationData;
import ru.cos.sim.road.init.data.LocationData;
import ru.cos.sim.road.init.data.NodeLocationData;
import ru.cos.sim.road.link.Link;
import ru.cos.sim.road.link.Segment;
import ru.cos.sim.road.node.Node;
import ru.cos.sim.road.node.RegularNode;

/**
 * 
 * @author zroslaw
 */
public class RoadNetworkUniverse implements Universe {

    protected RoadNetwork roadNetwork;

    protected Set<Agent> newbornAgents = new HashSet<Agent>();

    public RoadNetworkUniverse(RoadNetwork roadNetwork) {
        this.roadNetwork = roadNetwork;
    }

    @Override
    public void act(float dt) {
        clock.act(dt);
    }

    @Override
    public Set<Agent> lookupNewborns() {
        Set<Agent> result = newbornAgents;
        newbornAgents = new HashSet<Agent>();
        return result;
    }

    public void addNewborns(Set<Agent> newborns) {
        this.newbornAgents.addAll(newborns);
    }

    public void addNewborn(Agent newborn) {
        this.newbornAgents.add(newborn);
    }

    public RoadNetwork getRoadNetwork() {
        return roadNetwork;
    }

    public Node getNode(int nodeId) {
        return roadNetwork.getNode(nodeId);
    }

    public Link getLink(int linkId) {
        return roadNetwork.getLink(linkId);
    }

    /**
	 * Find particular road trajectory by location instance
	 * @param locationData 
	 * @return location instance
	 */
    public RoadTrajectory getTrajectory(LocationData locationData) {
        RoadTrajectory result;
        switch(locationData.getLocationType()) {
            case NodeLocation:
                NodeLocationData nodeLocationData = (NodeLocationData) locationData;
                RegularNode node = (RegularNode) getNode(nodeLocationData.getNodeId());
                result = node.getTransitionRule(nodeLocationData.getTransitionRuleId());
                break;
            case LinkLocation:
                LinkLocationData linkLocationData = (LinkLocationData) locationData;
                Link link = getLink(linkLocationData.getLinkId());
                Segment segment = link.getSegment(linkLocationData.getSegmentId());
                result = segment.getLane(linkLocationData.getLaneIndex());
                break;
            default:
                throw new RoadNetworkException("Unexpected location type " + locationData.getLocationType());
        }
        return result;
    }

    private static class ClockImpl implements Clock {

        private float time = 0;

        public void act(float dt) {
            time += dt;
        }

        @Override
        public float getCurrentTime() {
            return time;
        }
    }

    ;

    private final ClockImpl clock = new ClockImpl();

    public Clock getClock() {
        return clock;
    }
}
