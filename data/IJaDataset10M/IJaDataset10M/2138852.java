package org.matsim.core.mobsim.qsim.qnetsimengine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Link;
import org.matsim.core.events.AgentWait2LinkEventImpl;
import org.matsim.core.events.LinkEnterEventImpl;
import org.matsim.core.mobsim.qsim.interfaces.MobsimVehicle;
import org.matsim.lanes.data.v20.LaneData20;
import org.matsim.lanes.data.v20.LaneData20MeterFromLinkEndComparator;
import org.matsim.lanes.data.v20.LanesToLinkAssignment20;
import org.matsim.vis.snapshotwriters.AgentSnapshotInfo;
import org.matsim.vis.snapshotwriters.SnapshotLinkWidthCalculator;
import org.matsim.vis.snapshotwriters.VisData;

/**
 * Please read the docu of QBufferItem, QLane, QLinkInternalI (arguably to be renamed
 * into something like AbstractQLink) and QLinkImpl jointly. kai, nov'11
 * 
 * @author dstrippgen
 * @author dgrether
 * @author mrieser
 *
 * A QueueLink can consist of one or more QueueLanes, which may have the following layout
 * (Dashes stand for the borders of the QueueLink, equal signs (===) depict one lane,
 * plus signs (+) symbolize a decision point where one lane splits into several lanes) :
 * <pre>
 * ----------------
 * ================
 * ----------------
 *</pre>
 *
 *<pre>
 * ----------------
 *          =======
 * ========+
 *          =======
 * ----------------
 *</pre>
 *
 *<pre>
 * ----------------
 *         							========
 * =======+========
 *         							========
 * ----------------
 * </pre>
 *
 *
 * The following layouts are not allowed:
 * <pre>
 * ----------------
 * ================
 * ================
 * ----------------
 *</pre>
 *
 *<pre>
 * ----------------
 * =======
 *        +========
 * =======
 * ----------------
 * </pre>
 *
 * Queue Model Link implementation with the following properties:
 * <ul>
 *   <li>The queue behavior itself is simulated by one or more instances of QueueLane</li>
 *   <li>Each QueueLink has at least one QueueLane. All QueueVehicles added to the QueueLink
 *   are placed on this instance.
 *    </li>
 *   <li>All QueueLane instances which are connected to the ToQueueNode are
 *   held in the attribute toNodeQueueLanes</li>
 *   <li>QueueLink is active as long as at least one
 * of its QueueLanes is active.</li>
 * </ul>
 */
public class QLinkLanesImpl extends AbstractQLink {

    private static final QLane.FromLinkEndComparator fromLinkEndComparator = new QLane.FromLinkEndComparator();

    /**
	 * Reference to the QueueNode which is at the end of each QueueLink instance
	 */
    private final QNode toQueueNode;

    /**
	 * The QueueLane instance which always exists.
	 */
    private QLane firstLane;

    /**
	 * A List holding all QueueLane instances of the QueueLink
	 */
    private List<QLane> queueLanes;

    /**
	 * If more than one QueueLane exists this list holds all QueueLanes connected to
	 * the (To)QueueNode of the QueueLink
	 */
    private List<QLane> toNodeQueueLanes = null;

    private boolean active = false;

    private VisData visdata = null;

    private LanesToLinkAssignment20 lanesToLinkAssignment;

    /**
	 * Initializes a QueueLink with one QueueLane.
	 * @param link2
	 * @param queueNetwork
	 * @param toNode
	 * @see NetsimLink#createLanes(List)
	 */
    QLinkLanesImpl(final Link link2, QNetwork network, final QNode toNode, LanesToLinkAssignment20 lanesToLinkAssignment) {
        super(link2, network);
        this.toQueueNode = toNode;
        this.queueLanes = new ArrayList<QLane>();
        this.toNodeQueueLanes = new ArrayList<QLane>();
        this.lanesToLinkAssignment = lanesToLinkAssignment;
        this.createLanes(lanesToLinkAssignment.getLanes());
        this.visdata = this.new VisDataImpl();
    }

    /**
	 * Initialize the QueueLink with more than one QueueLane
	 * @param map
	 */
    private void createLanes(Map<Id, LaneData20> map) {
        List<LaneData20> sortedLanes = new ArrayList<LaneData20>(map.values());
        Collections.sort(sortedLanes, new LaneData20MeterFromLinkEndComparator());
        Collections.reverse(sortedLanes);
        List<QLane> laneList = new LinkedList<QLane>();
        LaneData20 firstLane = sortedLanes.remove(0);
        if (firstLane.getStartsAtMeterFromLinkEnd() != this.link.getLength()) {
            throw new IllegalStateException("First Lane Id " + firstLane.getId() + " on Link Id " + this.link.getId() + "isn't starting at the beginning of the link!");
        }
        this.firstLane = new QLane(this, firstLane, true);
        laneList.add(this.firstLane);
        Stack<QLane> laneStack = new Stack<QLane>();
        while (!laneList.isEmpty()) {
            QLane lastQLane = laneList.remove(0);
            laneStack.push(lastQLane);
            lastQLane.setFireLaneEvents(true);
            this.queueLanes.add(lastQLane);
            List<Id> toLaneIds = lastQLane.getLaneData().getToLaneIds();
            double nextMetersFromLinkEnd = 0.0;
            double laneLength = 0.0;
            if (toLaneIds != null && (!toLaneIds.isEmpty())) {
                for (Id toLaneId : toLaneIds) {
                    LaneData20 currentLane = map.get(toLaneId);
                    nextMetersFromLinkEnd = currentLane.getStartsAtMeterFromLinkEnd();
                    QLane currentQLane = new QLane(this, currentLane, false);
                    laneList.add(currentQLane);
                    lastQLane.addToLane(currentQLane);
                }
                laneLength = (link.getLength() - nextMetersFromLinkEnd) - (link.getLength() - lastQLane.getLaneData().getStartsAtMeterFromLinkEnd());
                laneLength = lastQLane.getLaneData().getStartsAtMeterFromLinkEnd() - nextMetersFromLinkEnd;
                lastQLane.setEndsAtMetersFromLinkEnd(nextMetersFromLinkEnd);
            } else {
                laneLength = lastQLane.getLaneData().getStartsAtMeterFromLinkEnd();
                lastQLane.setEndsAtMetersFromLinkEnd(0.0);
                this.toNodeQueueLanes.add(lastQLane);
            }
            lastQLane.setLaneLength(laneLength);
        }
        while (!laneStack.isEmpty()) {
            QLane qLane = laneStack.pop();
            qLane.calculateCapacities();
            if (qLane.getToLanes() == null || (qLane.getToLanes().isEmpty())) {
                for (Id toLinkId : qLane.getLaneData().getToLinkIds()) {
                    qLane.addDestinationLink(toLinkId);
                }
            } else {
                for (QLane subsequentLane : qLane.getToLanes()) {
                    for (Id toLinkId : subsequentLane.getDestinationLinkIds()) {
                        qLane.addDestinationLink(toLinkId);
                    }
                }
            }
            qLane.finishInitialization();
        }
        Collections.sort(this.queueLanes, QLinkLanesImpl.fromLinkEndComparator);
    }

    List<QLane> getToNodeQueueLanes() {
        return this.toNodeQueueLanes;
    }

    @Override
    void activateLink() {
        if (!this.active) {
            network.simEngine.activateLink(this);
            this.active = true;
        }
    }

    /**
	 * Adds a vehicle to the link, called by
	 * {@link QNode#moveVehicleOverNode(QVehicle, QLane, double)}.
	 *
	 * @param veh
	 *          the vehicle
	 */
    @Override
    void addFromIntersection(final QVehicle veh) {
        double now = this.network.simEngine.getMobsim().getSimTimer().getTimeOfDay();
        activateLink();
        this.firstLane.addFromIntersection(veh);
        veh.setCurrentLink(this.getLink());
        this.network.simEngine.getMobsim().getEventsManager().processEvent(new LinkEnterEventImpl(now, veh.getDriver().getId(), this.getLink().getId(), veh.getId()));
    }

    @Override
    void clearVehicles() {
        super.clearVehicles();
        for (QLane lane : this.queueLanes) {
            lane.clearVehicles();
        }
    }

    @Override
    boolean doSimStep(double now) {
        boolean activeLane = false;
        boolean otherLaneActive = false;
        boolean activeWaitBuffer = false;
        for (QLane lane : this.queueLanes) {
            if (lane.moveLane(now)) {
                otherLaneActive = true;
            }
            if (lane.isActive()) {
                activeLane = true;
            }
        }
        activeWaitBuffer = this.moveWaitToBuffer(now);
        this.active = activeLane || otherLaneActive || activeWaitBuffer || (!this.waitingList.isEmpty());
        return this.active;
    }

    /**
	 * Move as many waiting cars to the link as it is possible
	 *
	 * @param now
	 *          the current time
	 *  @return true if at least one vehicle is moved to the buffer of this lane
	 */
    private boolean moveWaitToBuffer(final double now) {
        boolean movedAtLeastOne = false;
        while (this.firstLane.hasBufferSpace()) {
            QVehicle veh = this.waitingList.poll();
            if (veh == null) {
                return movedAtLeastOne;
            }
            movedAtLeastOne = true;
            this.network.simEngine.getMobsim().getEventsManager().processEvent(new AgentWait2LinkEventImpl(now, veh.getDriver().getId(), this.getLink().getId(), veh.getId()));
            boolean handled = this.firstLane.addTransitToBuffer(now, veh);
            if (!handled) {
                this.firstLane.addWaitToBuffer(veh, now);
            }
        }
        return movedAtLeastOne;
    }

    @Override
    boolean isNotOfferingVehicle() {
        if (this.toNodeQueueLanes == null) {
            return this.firstLane.isNotOfferingVehicle();
        }
        for (QLane lane : this.toNodeQueueLanes) {
            if (!lane.isNotOfferingVehicle()) {
                return false;
            }
        }
        return true;
    }

    @Override
    boolean hasSpace() {
        return this.firstLane.hasSpace();
    }

    @Override
    public void recalcTimeVariantAttributes(double time) {
        for (QLane ql : this.queueLanes) {
            ql.recalcTimeVariantAttributes(time);
        }
    }

    @Override
    QVehicle getVehicle(Id vehicleId) {
        QVehicle ret = super.getVehicle(vehicleId);
        if (ret != null) {
            return ret;
        }
        for (QVehicle veh : this.waitingList) {
            if (veh.getId().equals(vehicleId)) return veh;
        }
        for (QLane lane : this.queueLanes) {
            ret = lane.getVehicle(vehicleId);
            if (ret != null) {
                return ret;
            }
        }
        return ret;
    }

    @Override
    public final Collection<MobsimVehicle> getAllNonParkedVehicles() {
        Collection<MobsimVehicle> ret = new ArrayList<MobsimVehicle>(this.waitingList);
        for (QLane lane : this.queueLanes) {
            ret.addAll(lane.getAllVehicles());
        }
        return ret;
    }

    /**
	 * @return the total space capacity available on that link (includes the space on lanes if available)
	 */
    double getSpaceCap() {
        double total = 0.0;
        for (QLane ql : this.getQueueLanes()) {
            total += ql.getStorageCapacity();
        }
        return total;
    }

    /**
	 * One should think about the need for this method
	 * because it is only called by one testcase
	 * @return
	 */
    int vehOnLinkCount() {
        int count = 0;
        for (QLane ql : this.queueLanes) {
            count += ql.vehOnLinkCount();
        }
        return count;
    }

    @Override
    public Link getLink() {
        return this.link;
    }

    @Override
    public QNode getToNode() {
        return this.toQueueNode;
    }

    /**
	 * This method returns the normalized capacity of the link, i.e. the capacity
	 * of vehicles per second. It is considering the capacity reduction factors
	 * set in the config and the simulation's tick time.
	 *
	 * @return the flow capacity of this link per second, scaled by the config
	 *         values and in relation to the SimulationTimer's simticktime.
	 */
    double getSimulatedFlowCapacity() {
        return this.firstLane.getSimulatedFlowCapacity();
    }

    /**
	 * @return the QLanes of this QueueLink
	 */
    public List<QLane> getQueueLanes() {
        return this.queueLanes;
    }

    @Override
    public VisData getVisData() {
        return this.visdata;
    }

    QLane getOriginalLane() {
        return this.firstLane;
    }

    /**
	 * Inner class to capsulate visualization methods
	 * @author dgrether
	 *
	 */
    class VisDataImpl implements VisData {

        private OTFLaneModelBuilder laneModelBuilder = new OTFLaneModelBuilder();

        private OTFLinkWLanes otfLink;

        VisDataImpl() {
            double nodeOffset = QLinkLanesImpl.this.network.simEngine.getMobsim().getScenario().getConfig().otfVis().getNodeOffset();
            if (nodeOffset != 0) {
                nodeOffset = nodeOffset + 2.0;
            }
            otfLink = laneModelBuilder.createOTFLinkWLanes(QLinkLanesImpl.this, nodeOffset, QLinkLanesImpl.this.lanesToLinkAssignment);
            SnapshotLinkWidthCalculator linkWidthCalculator = QLinkLanesImpl.this.network.getLinkWidthCalculator();
            laneModelBuilder.recalculatePositions(otfLink, linkWidthCalculator);
            for (QLane ql : QLinkLanesImpl.this.queueLanes) {
                OTFLane otfLane = otfLink.getLaneData().get(ql.getId().toString());
                ql.setOTFLane(otfLane);
            }
        }

        @Override
        public Collection<AgentSnapshotInfo> getVehiclePositions(final Collection<AgentSnapshotInfo> positions) {
            AgentSnapshotInfoBuilder agentSnapshotInfoBuilder = QLinkLanesImpl.this.network.simEngine.getAgentSnapshotInfoBuilder();
            for (QLane lane : QLinkLanesImpl.this.getQueueLanes()) {
                lane.visdata.getVehiclePositions(positions);
            }
            int cnt2 = 10;
            agentSnapshotInfoBuilder.positionVehiclesFromWaitingList(positions, QLinkLanesImpl.this.link, cnt2, QLinkLanesImpl.this.waitingList);
            cnt2 = QLinkLanesImpl.this.waitingList.size();
            agentSnapshotInfoBuilder.positionAgentsInActivities(positions, QLinkLanesImpl.this.link, QLinkLanesImpl.this.getAdditionalAgentsOnLink(), cnt2);
            return positions;
        }
    }

    @Override
    double getLastMovementTimeOfFirstVehicle() {
        throw new UnsupportedOperationException("Method should not be called on this instance");
    }

    @Override
    QVehicle getFirstVehicle() {
        throw new UnsupportedOperationException("Method should not be called on this instance");
    }

    @Override
    boolean hasGreenForToLink(Id toLinkId) {
        throw new UnsupportedOperationException("Method should not be called on this instance");
    }

    @Override
    QVehicle popFirstVehicle() {
        throw new UnsupportedOperationException("Method should not be called on this instance");
    }
}
