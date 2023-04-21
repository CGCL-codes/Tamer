package org.matsim.router.util;

import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;
import org.apache.log4j.Logger;
import org.matsim.network.Link;
import org.matsim.network.NetworkLayer;
import org.matsim.network.Node;

/**
 * Pre-processes a given network, gathering information which can be used by
 * {@link org.matsim.router.AStarLandmarks} when computing least-cost paths
 * between a start and an end node. Specifically, designates some
 * nodes in the network that act as landmarks and computes the distance
 * from and to each node in the network to each of the landmarks.
 *
 * @author lnicolas
 */
public class PreProcessLandmarks extends PreProcessEuclidean {

    private final Rectangle2D.Double travelZone;

    private final int landmarkCount;

    private Node[] landmarks;

    private static final Logger log = Logger.getLogger(PreProcessLandmarks.class);

    public PreProcessLandmarks(final TravelMinCostI costFunction) {
        this(costFunction, new Rectangle2D.Double());
    }

    public PreProcessLandmarks(final TravelMinCostI costFunction, final int landmarkCount) {
        this(costFunction, new Rectangle2D.Double(), landmarkCount);
    }

    /**
	 * @param costFunction
	 * @param travelZone The area within which the landmarks should lie. If you know the
	 * plans before routing, you could invoke
	 * {@link org.matsim.plans.algorithms.FromToSummary} as a preprocess and
	 * pass the travel zone to this constructor. Narrowing the zone where the landmarks should
	 * be put normally improves the routing speed of {@link org.matsim.router.AStarLandmarks}.
	 */
    public PreProcessLandmarks(final TravelMinCostI costFunction, final Rectangle2D.Double travelZone) {
        this(costFunction, travelZone, 16);
    }

    /**
	 * @param costFunction
	 * @param travelZone The area within which the landmarks should lie. If you know the
	 * plans before routing, you could invoke
	 * {@link org.matsim.plans.algorithms.FromToSummary} as a preprocess and
	 * pass the travel zone to this constructor. Narrowing the zone where the landmarks should
	 * be put normally improves the routing speed of {@link org.matsim.router.AStarLandmarks}.
	 * @param landmarkCount
	 */
    public PreProcessLandmarks(final TravelMinCostI costFunction, final Rectangle2D.Double travelZone, final int landmarkCount) {
        super(costFunction);
        this.travelZone = travelZone;
        this.landmarkCount = landmarkCount;
    }

    @Override
    public void run(final NetworkLayer network) {
        super.run(network);
        log.info("Putting landmarks on network...");
        long now = System.currentTimeMillis();
        LandmarkerPieSlices landmarker = new LandmarkerPieSlices(this.landmarkCount, this.travelZone);
        landmarker.run(network);
        log.info("done in " + (System.currentTimeMillis() - now) + " ms");
        log.info("Calculating distance from each node to each of the " + this.landmarkCount + " landmarks...");
        now = System.currentTimeMillis();
        this.landmarks = landmarker.getLandmarks();
        for (int i = 0; i < this.landmarks.length; i++) {
            expandLandmark(this.landmarks[i], i);
        }
        for (Node node : network.getNodes().values()) {
            LandmarksRole r = (LandmarksRole) getRole(node);
            r.updateMinMaxTravelTimes();
        }
        for (Node node : network.getNodes().values()) {
            LandmarksRole r = (LandmarksRole) getRole(node);
            for (int i = 0; i < this.landmarks.length; i++) {
                if (r.getMinLandmarkTravelTime(i) > r.getMaxLandmarkTravelTime(i)) {
                    log.info("Min > max for node " + node.getId() + " and landmark " + i);
                }
            }
        }
        log.info("done in " + (System.currentTimeMillis() - now) + " ms");
    }

    void expandLandmark(final Node startNode, final int landmarkIndex) {
        LandmarksTravelTimeComparator comparator = new LandmarksTravelTimeComparator(this.roleIndex, landmarkIndex);
        PriorityQueue<Node> pendingNodes = new PriorityQueue<Node>(100, comparator);
        LandmarksRole role = (LandmarksRole) getRole(startNode);
        role.setToLandmarkTravelTime(landmarkIndex, 0.0);
        role.setFromLandmarkTravelTime(landmarkIndex, 0.0);
        pendingNodes.add(startNode);
        while (pendingNodes.isEmpty() == false) {
            Node node = pendingNodes.poll();
            double toTravTime = ((LandmarksRole) getRole(node)).getToLandmarkTravelTime(landmarkIndex);
            expandLinks(landmarkIndex, pendingNodes, node.getInLinks().values(), toTravTime, false);
            double fromTravTime = ((LandmarksRole) getRole(node)).getFromLandmarkTravelTime(landmarkIndex);
            expandLinks(landmarkIndex, pendingNodes, node.getOutLinks().values(), fromTravTime, true);
        }
    }

    private void expandLinks(final int landmarkIndex, final PriorityQueue<Node> nodes, final Collection<? extends Link> links, final double travTime, final boolean expandFromLandmark) {
        LandmarksRole role;
        Iterator<?> iter = links.iterator();
        while (iter.hasNext()) {
            Link l = (Link) iter.next();
            Node n;
            if (expandFromLandmark == true) {
                n = l.getToNode();
            } else {
                n = l.getFromNode();
            }
            double linkTravTime = this.costFunction.getLinkMinimumTravelCost(l);
            role = (LandmarksRole) getRole(n);
            double totalTravelTime = travTime + linkTravTime;
            if (expandFromLandmark == true) {
                if (role.getFromLandmarkTravelTime(landmarkIndex) > totalTravelTime) {
                    role.setFromLandmarkTravelTime(landmarkIndex, totalTravelTime);
                    nodes.add(n);
                }
            } else {
                if (role.getToLandmarkTravelTime(landmarkIndex) > totalTravelTime) {
                    role.setToLandmarkTravelTime(landmarkIndex, totalTravelTime);
                    nodes.add(n);
                }
            }
        }
    }

    public Node[] getLandmarks() {
        return this.landmarks.clone();
    }

    @Override
    DeadEndRole getRole(final Node n) {
        LandmarksRole r = (LandmarksRole) n.getRole(this.roleIndex);
        if (r == null) {
            r = new LandmarksRole(this.landmarkCount);
            n.setRole(this.roleIndex, r);
        }
        return r;
    }

    public class LandmarksRole extends DeadEndRole {

        double[] landmarkTravelTime1;

        double[] landmarkTravelTime2;

        LandmarksRole(final int landmarkCount) {
            this.landmarkTravelTime2 = new double[landmarkCount];
            this.landmarkTravelTime1 = new double[landmarkCount];
            for (int i = 0; i < this.landmarkTravelTime2.length; i++) {
                this.landmarkTravelTime2[i] = Double.POSITIVE_INFINITY;
                this.landmarkTravelTime1[i] = Double.POSITIVE_INFINITY;
            }
        }

        void setToLandmarkTravelTime(final int landmarkIndex, final double travelTime) {
            this.landmarkTravelTime2[landmarkIndex] = travelTime;
        }

        void setFromLandmarkTravelTime(final int landmarkIndex, final double travelTime) {
            this.landmarkTravelTime1[landmarkIndex] = travelTime;
        }

        double getToLandmarkTravelTime(final int landmarkIndex) {
            return this.landmarkTravelTime2[landmarkIndex];
        }

        double getFromLandmarkTravelTime(final int landmarkIndex) {
            return this.landmarkTravelTime1[landmarkIndex];
        }

        void updateMinMaxTravelTimes() {
            for (int i = 0; i < this.landmarkTravelTime1.length; i++) {
                setTravelTimes(i, this.landmarkTravelTime2[i], this.landmarkTravelTime1[i]);
            }
        }

        public void setTravelTimes(final int landmarkIndex, final double travelTime1, final double travelTime2) {
            if (travelTime1 > travelTime2) {
                this.landmarkTravelTime2[landmarkIndex] = travelTime1;
                this.landmarkTravelTime1[landmarkIndex] = travelTime2;
            } else {
                this.landmarkTravelTime1[landmarkIndex] = travelTime1;
                this.landmarkTravelTime2[landmarkIndex] = travelTime2;
            }
        }

        public double getMinLandmarkTravelTime(final int landmarkIndex) {
            return this.landmarkTravelTime1[landmarkIndex];
        }

        public double getMaxLandmarkTravelTime(final int landmarkIndex) {
            return this.landmarkTravelTime2[landmarkIndex];
        }
    }

    /**
	 * Sorts the Nodes ascending according to their ToLandmarkTravelTime. We assume that the
	 * ToLandmarkTravelTime is somewhat similar to the FromLandmarkTravelTime,
	 * so ordering the nodes according to one of these values produces a similar
	 * ordered list as if to order them according to the other value...
	 *
	 * @author lnicolas
	 */
    public static class LandmarksTravelTimeComparator implements Comparator<Node>, Serializable {

        private static final long serialVersionUID = 1L;

        private final int roleIndex;

        private final int landmarkIndex;

        public LandmarksTravelTimeComparator(final int roleIndex, final int landmarkIndex) {
            this.roleIndex = roleIndex;
            this.landmarkIndex = landmarkIndex;
        }

        public int compare(final Node n1, final Node n2) {
            double c1 = ((LandmarksRole) n1.getRole(this.roleIndex)).getToLandmarkTravelTime(this.landmarkIndex);
            double c2 = ((LandmarksRole) n2.getRole(this.roleIndex)).getToLandmarkTravelTime(this.landmarkIndex);
            if (c1 < c2) {
                return -1;
            }
            if (c1 > c2) {
                return +1;
            }
            return n1.compareTo(n2);
        }
    }
}
