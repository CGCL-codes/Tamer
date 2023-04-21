package ru.cos.sim.visualizer.traffic.parser.trace.location;

import org.jdom.Element;
import ru.cos.sim.visualizer.traffic.parser.Parser;
import ru.cos.sim.visualizer.traffic.parser.trace.Link;
import ru.cos.sim.visualizer.traffic.parser.utils.ItemParser;

public class LinkLocation extends Location {

    protected static enum Fields {

        LinkId, SegmentId, LaneId, Position
    }

    protected int linkId;

    protected Link link;

    public LinkLocation(Element e) {
        super(Type.Link);
        read(e);
    }

    protected LinkLocation(Type type) {
        super(type);
    }

    public LinkLocation(int linkId) {
        super(Type.Link);
        this.linkId = linkId;
    }

    public LinkLocation(int linkId, float position) {
        super(Type.Link, position);
        this.linkId = linkId;
    }

    public LinkLocation(Type type, int linkId) {
        super(type);
        this.linkId = linkId;
    }

    public LinkLocation(Type type, int linkId, float position) {
        super(type, position);
        this.linkId = linkId;
    }

    public int getLinkId() {
        return linkId;
    }

    public Link getLink() {
        return link;
    }

    public void setLinkId(int linkId) {
        this.linkId = linkId;
    }

    public void setLink(Link link) {
        this.link = link;
    }

    public void read(Element e) {
        this.linkId = ItemParser.getInteger(e, Fields.LinkId.name());
        if (e.getChild(Fields.Position.name(), Parser.getCurrentNamespace()) != null) {
            this.position = ItemParser.getFloat(e, Fields.Position.name());
            this.hasPosition = true;
        }
    }
}
