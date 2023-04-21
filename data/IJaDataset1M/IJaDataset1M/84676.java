package ru.cos.sim.visualizer.traffic.parser.trace.base;

import org.jdom.Element;
import ru.cos.sim.visualizer.traffic.parser.Parser;
import ru.cos.sim.visualizer.traffic.parser.trace.location.LaneLocation;
import ru.cos.sim.visualizer.traffic.parser.trace.location.NodeLocation;
import ru.cos.sim.visualizer.traffic.parser.utils.ItemParser;

public class TransitionRule extends StaffCollector {

    public static String Name = "TransitionRule";

    public static String ChapterName = "TransitionRules";

    private static enum Fields {

        length, width, sourceLaneIndex, sourceLinkId, destinationLinkId, destinationLaneIndex, BezierCurveGeometry, StopLine, Position
    }

    public static enum GeometryType {

        Bezier, Polyline
    }

    protected NodeLocation location;

    protected LaneLocation srcLane;

    protected LaneLocation destLane;

    protected float length;

    protected GeometryType type;

    protected boolean hasStopLine = false;

    protected float stopLine;

    public TransitionRule(Element e, int nodeId) {
        super(e);
        this.location = new NodeLocation(nodeId);
        this.length = ItemParser.getFloat(e, Fields.length.name());
        int laneId = ItemParser.getInteger(e, Fields.sourceLaneIndex.name());
        int linkId = ItemParser.getInteger(e, Fields.sourceLinkId.name());
        this.srcLane = new LaneLocation(linkId, -1, laneId);
        laneId = ItemParser.getInteger(e, Fields.destinationLaneIndex.name());
        linkId = ItemParser.getInteger(e, Fields.destinationLinkId.name());
        this.destLane = new LaneLocation(linkId, -1, laneId);
        this.type = getType(e);
        if (e.getChild(Fields.StopLine.name(), Parser.getCurrentNamespace()) != null) {
            hasStopLine = true;
            stopLine = ItemParser.getFloat(e.getChild(Fields.StopLine.name(), Parser.getCurrentNamespace()), Fields.Position.name());
        }
    }

    public TransitionRule(int uid) {
        super(uid);
    }

    public static GeometryType getType(Element e) {
        if (e.getChild(Fields.BezierCurveGeometry.name(), Parser.getCurrentNamespace()) != null) {
            return GeometryType.Bezier;
        }
        return GeometryType.Polyline;
    }

    public NodeLocation getLocation() {
        return location;
    }

    public LaneLocation getSrcLane() {
        return srcLane;
    }

    public LaneLocation getDestLane() {
        return destLane;
    }

    public float getLength() {
        return length;
    }

    public GeometryType getType() {
        return type;
    }

    public boolean isHasStopLine() {
        return hasStopLine;
    }

    public float getStopLine() {
        return stopLine;
    }
}
