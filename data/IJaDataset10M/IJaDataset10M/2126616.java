package org.matsim.core.mobsim.qsim.qnetsimengine;

import org.matsim.api.core.v01.Scenario;
import org.matsim.vis.snapshotwriters.AgentSnapshotInfoFactory;

/**
 * Calculates the positions of all vehicles on this link according to the queue-logic: Vehicles are placed on the link
 * according to the ratio between the free-travel time and the time the vehicles are already on the link. If they could have
 * left the link already (based on the time), the vehicles start to build a traffic-jam (queue) at the end of the link.
 
 * @author dgrether
 * @author nagel
 *
 */
public class QueueAgentSnapshotInfoBuilder extends AbstractAgentSnapshotInfoBuilder {

    public QueueAgentSnapshotInfoBuilder(Scenario scenario, AgentSnapshotInfoFactory agentSnapshotInfoFactory) {
        super(scenario, agentSnapshotInfoFactory);
    }

    @Override
    public double calculateVehicleSpacing(double linkLength, double numberOfVehiclesOnLink, double storageCapacity, double bufferStorageCapacity) {
        double vehLen = Math.min(linkLength / (storageCapacity + bufferStorageCapacity), linkLength / (numberOfVehiclesOnLink));
        return vehLen;
    }

    @Override
    public double calculateDistanceOnVectorFromFromNode(double length, double spacing, double lastDistanceFromFNode, double now, double freespeedTraveltime, double travelTime) {
        double distanceFromFNode;
        if (freespeedTraveltime == 0.0) {
            distanceFromFNode = 0.;
        } else {
            distanceFromFNode = (travelTime / freespeedTraveltime) * length;
            if (distanceFromFNode < 0.) {
                distanceFromFNode = 0.;
            }
        }
        if (Double.isNaN(lastDistanceFromFNode)) {
            lastDistanceFromFNode = length;
        }
        if (distanceFromFNode >= lastDistanceFromFNode - spacing) {
            distanceFromFNode = lastDistanceFromFNode - spacing;
        }
        return distanceFromFNode;
    }
}
