package org.matsim.utils.vis.otfvis.opengl.queries;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Map;
import org.matsim.events.Events;
import org.matsim.gbl.Gbl;
import org.matsim.interfaces.core.v01.Link;
import org.matsim.interfaces.core.v01.Population;
import org.matsim.mobsim.queuesim.QueueLink;
import org.matsim.mobsim.queuesim.QueueNetwork;
import org.matsim.utils.collections.QuadTree;
import org.matsim.utils.collections.QuadTree.Executor;
import org.matsim.utils.collections.QuadTree.Rect;
import org.matsim.utils.geometry.CoordImpl;
import org.matsim.utils.vis.otfvis.data.OTFDataWriter;
import org.matsim.utils.vis.otfvis.data.OTFServerQuad;
import org.matsim.utils.vis.otfvis.gui.OTFVisConfig;
import org.matsim.utils.vis.otfvis.interfaces.OTFDrawer;
import org.matsim.utils.vis.otfvis.interfaces.OTFQuery;
import org.matsim.utils.vis.otfvis.opengl.layer.SimpleStaticNetLayer;

public class QueryLinkId implements OTFQuery {

    private static final long serialVersionUID = -1389950511283282110L;

    private double sx;

    private double sy;

    private double width = 0;

    private double height = 0;

    public Map<CoordImpl, String> linkIds = new HashMap<CoordImpl, String>();

    public QueryLinkId(double x, double y) {
        this.sx = x;
        this.sy = y;
    }

    public QueryLinkId(Rectangle2D.Double rect) {
        this.sx = rect.x;
        this.sy = rect.y;
        this.width = rect.width;
        this.height = rect.height;
    }

    public QueryLinkId(Rect rect) {
        this.sx = rect.minX;
        this.sy = rect.minY;
        this.width = rect.maxX - sx;
        this.height = rect.maxY - sy;
    }

    public void draw(OTFDrawer drawer) {
    }

    class AddIdStringExecutor implements Executor<OTFDataWriter> {

        private final boolean nearestOnly;

        private double minDist = Double.POSITIVE_INFINITY;

        private static final double epsilon = 0.0001;

        private final double cellWidth;

        public AddIdStringExecutor(boolean nearestOnly) {
            this.nearestOnly = nearestOnly;
            cellWidth = ((OTFVisConfig) Gbl.getConfig().getModule("otfvis")).getLinkWidth();
        }

        public void execute(double x, double y, OTFDataWriter writer) {
            Object src = writer.getSrc();
            if (src instanceof QueueLink) {
                Link link = ((QueueLink) src).getLink();
                double alpha = 0.6;
                double fromX = link.getFromNode().getCoord().getX();
                double fromY = link.getFromNode().getCoord().getY();
                double middleX = alpha * fromX + (1.0 - alpha) * link.getToNode().getCoord().getX();
                double middleY = alpha * fromY + (1.0 - alpha) * link.getToNode().getCoord().getY();
                if (nearestOnly) {
                    double xDist = middleX - sx;
                    double yDist = middleY - sy;
                    double dist = Math.sqrt(xDist * xDist + yDist * yDist);
                    if (dist <= minDist) {
                        if (minDist - dist > epsilon) linkIds.clear();
                        minDist = dist;
                        Point2D.Float anchor = SimpleStaticNetLayer.SimpleQuadDrawer.calcOrtho(fromX, fromY, middleX, middleY, cellWidth / 2.);
                        linkIds.put(new CoordImpl(middleX + anchor.x, middleY + anchor.y), link.getId().toString());
                    }
                } else {
                    Point2D.Float anchor = SimpleStaticNetLayer.SimpleQuadDrawer.calcOrtho(fromX, fromY, middleX, middleY, cellWidth / 2.);
                    linkIds.put(new CoordImpl(middleX + anchor.x, middleY + anchor.y), link.getId().toString());
                }
            }
        }
    }

    public void query(QueueNetwork net, Population plans, Events events, OTFServerQuad quad) {
        double regionWidth = (quad.getMaxEasting() - quad.getMinEasting()) * 0.1;
        double regionHeight = (quad.getMaxNorthing() - quad.getMinNorthing()) * 0.1;
        QuadTree.Rect rect;
        double qsx = sx - quad.getMinEasting();
        double qsy = sy - quad.getMinNorthing();
        if (width == 0) rect = new QuadTree.Rect(qsx - regionWidth, qsy - regionHeight, qsx + regionWidth, qsy + regionHeight); else rect = new QuadTree.Rect(qsx, qsy, qsx + width, qsy + height);
        quad.execute(rect, new AddIdStringExecutor(width == 0));
    }

    public void remove() {
    }

    public boolean isAlive() {
        return false;
    }

    public Type getType() {
        return OTFQuery.Type.OTHER;
    }

    public void setId(String id) {
    }
}
