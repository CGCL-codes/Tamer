package org.matsim.core.mobsim.qsim.qnetsimengine;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.matsim.api.core.v01.Coord;
import org.matsim.api.core.v01.Id;
import org.matsim.core.utils.geometry.CoordImpl;
import org.matsim.core.utils.geometry.CoordUtils;

/**
 * @author dgrether
 *
 */
public class OTFLinkWLanes implements Serializable {

    private Point2D.Double linkStart = null;

    private Point2D.Double linkEnd = null;

    private Point2D.Double normalizedLinkVector;

    private Point2D.Double linkOrthogonalVector;

    private double numberOfLanes = 1.0;

    private int maximalAlignment = 0;

    private Map<String, OTFLane> laneData = null;

    private String id = null;

    private double linkWidth;

    private Point2D.Double linkStartCenterPoint = null;

    private Point2D.Double linkEndCenterPoint = null;

    private Map<String, OTFSignal> signals = null;

    private ArrayList<Id> toLinkIds;

    private transient List<OTFLinkWLanes> toLinks = null;

    private CoordImpl startCoord;

    private CoordImpl endCoord;

    private double euklideanDistance;

    public OTFLinkWLanes(String id) {
        this.id = id;
    }

    public String getLinkId() {
        return this.id;
    }

    public void setNormalizedLinkVector(Point2D.Double v) {
        this.normalizedLinkVector = v;
    }

    public void setLinkOrthogonalVector(Point2D.Double v) {
        this.linkOrthogonalVector = v;
    }

    public Point2D.Double getLinkStart() {
        return linkStart;
    }

    public Point2D.Double getLinkEnd() {
        return linkEnd;
    }

    public Double getNormalizedLinkVector() {
        return normalizedLinkVector;
    }

    public Double getLinkOrthogonalVector() {
        return linkOrthogonalVector;
    }

    public void setNumberOfLanes(double nrLanes) {
        this.numberOfLanes = nrLanes;
    }

    public double getNumberOfLanes() {
        return this.numberOfLanes;
    }

    public void setMaximalAlignment(int maxAlign) {
        this.maximalAlignment = maxAlign;
    }

    public int getMaximalAlignment() {
        return this.maximalAlignment;
    }

    public void addLaneData(OTFLane laneData) {
        if (this.laneData == null) {
            this.laneData = new HashMap<String, OTFLane>();
        }
        this.laneData.put(laneData.getId(), laneData);
    }

    public Map<String, OTFLane> getLaneData() {
        return this.laneData;
    }

    public void addSignal(OTFSignal signal) {
        if (this.signals == null) {
            this.signals = new HashMap<String, OTFSignal>();
        }
        this.signals.put(signal.getId(), signal);
    }

    public Map<String, OTFSignal> getSignals() {
        return this.signals;
    }

    public void setLinkWidth(double linkWidth) {
        this.linkWidth = linkWidth;
    }

    public double getLinkWidth() {
        return this.linkWidth;
    }

    public void setLinkStartEndPoint(Double linkStart, Double linkEnd) {
        this.linkStart = linkStart;
        this.linkEnd = linkEnd;
        this.calcCoords();
    }

    public void setLinkStartCenterPoint(Double linkStartCenter) {
        this.linkStartCenterPoint = linkStartCenter;
    }

    public void setLinkEndCenterPoint(Double linkEndCenter) {
        this.linkEndCenterPoint = linkEndCenter;
    }

    public Coord getLinkStartCoord() {
        return this.startCoord;
    }

    public Coord getLinkEndCoord() {
        return this.endCoord;
    }

    private void calcCoords() {
        this.startCoord = new CoordImpl(linkStart.x, linkStart.y);
        this.endCoord = new CoordImpl(linkEnd.x, linkEnd.y);
        this.euklideanDistance = CoordUtils.calcDistance(startCoord, endCoord);
    }

    public double getEuklideanDistance() {
        return euklideanDistance;
    }

    public Point2D.Double getLinkStartCenterPoint() {
        return this.linkStartCenterPoint;
    }

    public Point2D.Double getLinkEndCenterPoint() {
        return this.linkEndCenterPoint;
    }

    public void addToLink(OTFLinkWLanes link) {
        if (this.toLinks == null) {
            this.toLinks = new ArrayList<OTFLinkWLanes>();
        }
        this.toLinks.add(link);
    }

    public List<OTFLinkWLanes> getToLinks() {
        return this.toLinks;
    }

    public void addToLinkId(Id toLinkId) {
        if (this.toLinkIds == null) this.toLinkIds = new ArrayList<Id>();
        this.toLinkIds.add(toLinkId);
    }

    public List<Id> getToLinkIds() {
        return toLinkIds;
    }
}
