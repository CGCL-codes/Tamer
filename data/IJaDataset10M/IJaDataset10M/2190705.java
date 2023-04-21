package org.matsim.lanes.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.core.basic.v01.IdImpl;
import org.matsim.lanes.data.v11.Lane;
import org.matsim.lanes.data.v11.LaneDefinitions;
import org.matsim.lanes.data.v11.LanesToLinkAssignment;
import org.matsim.lanes.data.v20.LaneData20;
import org.matsim.lanes.data.v20.LaneData20MeterFromLinkEndComparator;
import org.matsim.lanes.data.v20.LaneDefinitionsFactory20;
import org.matsim.lanes.data.v20.LaneDefinitions20;
import org.matsim.lanes.data.v20.LaneDefinitions20Impl;
import org.matsim.lanes.utils.LanesCapacityCalculator;
import org.matsim.signalsystems.CalculateAngle;

/**
 * Converts LaneDefinitions that have been read from a xml file in the lanedefinitions_v1.1.xsd
 * to LaneDefinitions that have all attributes set used in the lanedefinitions_v2.0.xsd file format.
 * 
 * In the v1.1 format only the Lanes at the end of a link are specified but not the ones at the 
 * beginning of the link that lead to the Lanes at the end of the link. Furthermore there is no
 * explicit U-Turn functionality expected to be modeled in the v1.1 format. Also not existing in
 * v1.1 is information about the topological order of the Lanes on the link. All this information is 
 * computed heuristically by this converter.
 * 
 * This means:
 * <ul>
 *   <li>One or more Lanes are created that lead from the beginning of the link to the Lanes at
 *   the end of the link.</li>
 *   <li>Based on the geometry information in the network graph topology information is added.</li>
 *   <li>To the lane which is the most left one (looking south to north on the link) a additional out 
 *   link is added to enable U-Turn funcitonality.</li>
 * </ul>
 * 
 * @author dgrether
 */
public class LaneDefinitionsV11ToV20Conversion {

    public LaneDefinitions20 convertTo20(LaneDefinitions lanedefs11, Network network) {
        LaneDefinitions20 lanedefs20 = new LaneDefinitions20Impl();
        LaneDefinitionsFactory20 lanedefs20fac = lanedefs20.getFactory();
        org.matsim.lanes.data.v20.LanesToLinkAssignment20 l2lnew;
        LaneData20 lanev20;
        Link link;
        LanesCapacityCalculator capacityCalculator = new LanesCapacityCalculator();
        for (LanesToLinkAssignment l2lv11 : lanedefs11.getLanesToLinkAssignments().values()) {
            l2lnew = lanedefs20fac.createLanesToLinkAssignment(l2lv11.getLinkId());
            link = network.getLinks().get(l2lv11.getLinkId());
            lanedefs20.addLanesToLinkAssignment(l2lnew);
            for (Lane lanev11 : l2lv11.getLanes().values()) {
                lanev20 = lanedefs20fac.createLane(lanev11.getId());
                l2lnew.addLane(lanev20);
                lanev20.setNumberOfRepresentedLanes(lanev11.getNumberOfRepresentedLanes());
                lanev20.setStartsAtMeterFromLinkEnd(lanev11.getStartsAtMeterFromLinkEnd());
                for (Id toLinkId : lanev11.getToLinkIds()) {
                    lanev20.addToLinkId(toLinkId);
                }
                capacityCalculator.calculateAndSetCapacity(lanev20, true, link, network);
            }
            List<LaneData20> sortedLanes = new ArrayList<LaneData20>(l2lnew.getLanes().values());
            Collections.sort(sortedLanes, new LaneData20MeterFromLinkEndComparator());
            LaneData20 longestLane = sortedLanes.get(sortedLanes.size() - 1);
            String originalLaneIdString = link.getId().toString() + ".ol";
            LaneData20 originalLane = lanedefs20fac.createLane(new IdImpl(originalLaneIdString));
            originalLane.setNumberOfRepresentedLanes(link.getNumberOfLanes());
            originalLane.setStartsAtMeterFromLinkEnd(link.getLength());
            originalLane.addToLaneId(longestLane.getId());
            capacityCalculator.calculateAndSetCapacity(originalLane, false, link, network);
            l2lnew.addLane(originalLane);
            LaneData20 lastLane = originalLane;
            LaneData20 secondLongestLane;
            LaneData20 intermediateLane;
            Id intermediateLaneId;
            int intermediateLanesCounter = 1;
            for (int i = sortedLanes.size() - 2; i >= 0; i--) {
                secondLongestLane = sortedLanes.get(i);
                if (longestLane.getStartsAtMeterFromLinkEnd() > secondLongestLane.getStartsAtMeterFromLinkEnd()) {
                    intermediateLaneId = new IdImpl(intermediateLanesCounter + ".cl");
                    intermediateLanesCounter++;
                    intermediateLane = lanedefs20fac.createLane(intermediateLaneId);
                    intermediateLane.setStartsAtMeterFromLinkEnd(longestLane.getStartsAtMeterFromLinkEnd());
                    intermediateLane.setNumberOfRepresentedLanes(link.getNumberOfLanes());
                    intermediateLane.addToLaneId(secondLongestLane.getId());
                    capacityCalculator.calculateAndSetCapacity(intermediateLane, false, link, network);
                    l2lnew.addLane(intermediateLane);
                    lastLane.addToLaneId(intermediateLaneId);
                    lastLane = intermediateLane;
                    longestLane = secondLongestLane;
                } else if (longestLane.getStartsAtMeterFromLinkEnd() == secondLongestLane.getStartsAtMeterFromLinkEnd()) {
                    lastLane.addToLaneId(secondLongestLane.getId());
                } else {
                    throw new RuntimeException("Illegal sort order");
                }
            }
            int mostRight = l2lv11.getLanes().size() / 2;
            SortedMap<Double, Link> outLinksByAngle = CalculateAngle.getOutLinksSortedByAngle(link);
            LaneData20 newLane;
            Set<Lane> assignedLanes = new HashSet<Lane>();
            for (Link outlink : outLinksByAngle.values()) {
                for (Lane oldLane : l2lv11.getLanes().values()) {
                    if (assignedLanes.contains(oldLane)) {
                        continue;
                    }
                    newLane = l2lnew.getLanes().get(oldLane.getId());
                    if (assignedLanes.isEmpty()) {
                        this.addUTurn(link, newLane);
                    }
                    if (newLane.getToLinkIds().contains(outlink.getId())) {
                        newLane.setAlignment(mostRight);
                        assignedLanes.add(oldLane);
                        mostRight--;
                        if ((mostRight == 0) && (l2lv11.getLanes().size() % 2 == 0)) {
                            mostRight--;
                        }
                    }
                }
            }
        }
        return lanedefs20;
    }

    private void addUTurn(Link link, LaneData20 newLane) {
        for (Link outLink : link.getToNode().getOutLinks().values()) {
            if ((outLink.getToNode().equals(link.getFromNode()))) {
                newLane.addToLinkId(outLink.getId());
            }
        }
    }
}
