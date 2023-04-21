package playground.mrieser.core.mobsim.features.refQueueNetworkFeature;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import org.apache.log4j.Logger;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.api.core.v01.network.Node;
import org.matsim.core.events.AgentStuckEventImpl;
import playground.mrieser.core.mobsim.api.MobsimVehicle;
import playground.mrieser.core.mobsim.network.api.MobsimNode;

class QueueNode implements MobsimNode {

    private static final Logger log = Logger.getLogger(QueueNode.class);

    private static final QueueLinkIdComparator linkIdComparator = new QueueLinkIdComparator();

    private final QueueNetwork network;

    private final Node node;

    private final QueueLink[] inLinks;

    private final QueueLink[] tempLinks;

    private final Random random;

    public QueueNode(final Node node, final QueueNetwork network, final Random random) {
        this.node = node;
        this.network = network;
        this.random = random;
        this.inLinks = new QueueLink[node.getInLinks().size()];
        this.tempLinks = new QueueLink[node.getInLinks().size()];
        int idx = 0;
        for (Id linkId : this.node.getInLinks().keySet()) {
            this.inLinks[idx] = this.network.getLinks().get(linkId);
            idx++;
        }
        Arrays.sort(this.inLinks, linkIdComparator);
    }

    final void moveNode(final double now) {
        int inLinksCounter = 0;
        double inLinksCapSum = 0.0;
        for (QueueLink link : this.inLinks) {
            if (link.buffer.getFirstVehicleInBuffer() != null) {
                this.tempLinks[inLinksCounter] = link;
                inLinksCounter++;
                inLinksCapSum += link.link.getCapacity(now);
            }
        }
        if (inLinksCounter == 0) {
            return;
        }
        int auxCounter = 0;
        while (auxCounter < inLinksCounter) {
            double rnd = this.random.nextDouble();
            double rndNum = rnd * inLinksCapSum;
            double selCap = 0.0;
            for (int i = 0; i < inLinksCounter; i++) {
                QueueLink link = this.tempLinks[i];
                if (link == null) {
                    continue;
                }
                selCap += link.link.getCapacity(now);
                if (selCap >= rndNum) {
                    auxCounter++;
                    inLinksCapSum -= link.link.getCapacity(now);
                    this.tempLinks[i] = null;
                    this.clearLinkBuffer(link.buffer, now);
                    break;
                }
            }
        }
    }

    private void clearLinkBuffer(final QueueBuffer buffer, final double now) {
        MobsimVehicle veh;
        while ((veh = buffer.getFirstVehicleInBuffer()) != null) {
            if (!moveVehicleOverNode(veh, buffer, now)) {
                break;
            }
        }
    }

    /**
	 * @param vehicle
	 * @param buffer
	 * @param now
	 * @return <code>true</code> if the vehicle was successfully moved over the node,
	 * 	<code>false</code> otherwise (e.g. in case where the next link is jammed)
	 */
    private boolean moveVehicleOverNode(final MobsimVehicle vehicle, final QueueBuffer buffer, final double now) {
        Id nextLinkId = vehicle.getDriver().getNextLinkId();
        if (nextLinkId != null) {
            QueueLink nextLink = this.network.getLinks().get(nextLinkId);
            this.checkNextLinkSemantics(buffer.link, nextLink, vehicle);
            if (nextLink.hasSpace()) {
                buffer.removeFirstVehicleInBuffer();
                vehicle.getDriver().notifyMoveToNextLink();
                nextLink.addVehicleFromIntersection(vehicle);
                return true;
            } else {
            }
            if ((now - buffer.getLastMovedTime()) > this.network.getStuckTime()) {
                if (this.network.isRemoveStuckVehicles()) {
                    buffer.link.removeVehicle(vehicle);
                    this.network.simEngine.getEventsManager().processEvent(new AgentStuckEventImpl(now, vehicle.getId(), buffer.link.getId(), TransportMode.car));
                } else {
                    buffer.removeFirstVehicleInBuffer();
                    vehicle.getDriver().notifyMoveToNextLink();
                    nextLink.addVehicleFromIntersection(vehicle);
                    return true;
                }
            }
            return false;
        }
        buffer.removeFirstVehicleInBuffer();
        log.error("Agent has no or wrong route! vehicleId=" + vehicle.getId() + " currentLink=" + buffer.link.getId().toString() + ". The agent is removed from the simulation.");
        return true;
    }

    private void checkNextLinkSemantics(final QueueLink currentLink, final QueueLink nextLink, final MobsimVehicle veh) {
        if (currentLink.link.getToNode() != nextLink.link.getFromNode()) {
            throw new RuntimeException("Cannot move vehicle " + veh.getId() + " from link " + currentLink.getId() + " to link " + nextLink.getId());
        }
    }

    @Override
    public Id getId() {
        return this.node.getId();
    }

    protected static class QueueLinkIdComparator implements Comparator<QueueLink>, Serializable {

        private static final long serialVersionUID = 1L;

        @Override
        public int compare(final QueueLink o1, final QueueLink o2) {
            return o1.link.getId().compareTo(o2.link.getId());
        }
    }
}
