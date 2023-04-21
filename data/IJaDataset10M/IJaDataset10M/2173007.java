package org.matsim.vis.snapshotwriters;

import org.matsim.api.core.v01.Coord;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Link;
import org.matsim.core.network.LinkImpl;
import org.matsim.core.utils.geometry.CoordUtils;

/**
 * @author nagel
 *
 */
public class AgentSnapshotInfoFactory {

    private static final double TWO_PI = 2.0 * Math.PI;

    private static final double PI_HALF = Math.PI / 2.0;

    private SnapshotLinkWidthCalculator linkWidthCalculator;

    public AgentSnapshotInfoFactory(SnapshotLinkWidthCalculator widthCalculator) {
        this.linkWidthCalculator = widthCalculator;
    }

    public AgentSnapshotInfo createAgentSnapshotInfo(Id agentId, double easting, double northing, double elevation, double azimuth) {
        PositionInfo info = new PositionInfo();
        info.setId(agentId);
        info.setEasting(easting);
        info.setNorthing(northing);
        info.setAzimuth(azimuth);
        return info;
    }

    public AgentSnapshotInfo createAgentSnapshotInfo(Id agentId, Link link, double distanceOnLink, int lane) {
        PositionInfo info = new PositionInfo();
        info.setId(agentId);
        double euklidean;
        if (link instanceof LinkImpl) {
            euklidean = ((LinkImpl) link).getEuklideanDistance();
        } else {
            euklidean = CoordUtils.calcDistance(link.getFromNode().getCoord(), link.getToNode().getCoord());
        }
        calculateAndSetPosition(info, link.getFromNode().getCoord(), link.getToNode().getCoord(), distanceOnLink, link.getLength(), euklidean, lane);
        return info;
    }

    /**
	 * Static creator based on Coord
	 * @param curveLength lengths are usually different (usually longer) than the euclidean distances between the startCoord and endCoord
	 */
    public AgentSnapshotInfo createAgentSnapshotInfo(Id agentId, Coord startCoord, Coord endCoord, double distanceOnLink, Integer lane, double curveLength, double euclideanLength) {
        PositionInfo info = new PositionInfo();
        info.setId(agentId);
        calculateAndSetPosition(info, startCoord, endCoord, distanceOnLink, curveLength, euclideanLength, lane);
        return info;
    }

    /**
	 * 
	 * @param lane may be null
	 */
    private final void calculateAndSetPosition(PositionInfo info, Coord startCoord, Coord endCoord, double distanceOnVector, double lengthOfCurve, double euclideanLength, Integer lane) {
        double dx = -startCoord.getX() + endCoord.getX();
        double dy = -startCoord.getY() + endCoord.getY();
        double theta = 0.0;
        if (dx > 0) {
            theta = Math.atan(dy / dx);
        } else if (dx < 0) {
            theta = Math.PI + Math.atan(dy / dx);
        } else {
            if (dy > 0) {
                theta = PI_HALF;
            } else {
                theta = -PI_HALF;
            }
        }
        if (theta < 0.0) theta += TWO_PI;
        double correction = 0.;
        if (lengthOfCurve != 0) {
            correction = euclideanLength / lengthOfCurve;
        }
        double lanePosition = 0;
        if (lane != null) {
            lanePosition = this.linkWidthCalculator.calculateLanePosition(lane);
        }
        info.setEasting(startCoord.getX() + (Math.cos(theta) * distanceOnVector * correction) + (Math.sin(theta) * lanePosition * correction));
        info.setNorthing(startCoord.getY() + Math.sin(theta) * distanceOnVector * correction - Math.cos(theta) * lanePosition * correction);
        info.setAzimuth(theta / TWO_PI * 360.);
    }
}
