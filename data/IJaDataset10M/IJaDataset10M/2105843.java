package com.ynhenc.gis.ui.viewer_01.map_viewer;

import java.awt.Dimension;
import com.ynhenc.comm.GisComLib;
import com.ynhenc.gis.GisRegistry;
import com.ynhenc.gis.model.map.*;
import com.ynhenc.gis.model.shape.GeoPoint;
import com.ynhenc.gis.model.shape.Mbr;
import com.ynhenc.gis.model.shape.PntShort;
import com.ynhenc.gis.model.shape.Projection;

public class MapController_03_Web extends GisComLib {

    public Mbr getTopLevelMbr() {
        return this.topLevelMbr;
    }

    private int getZoomLevelMax() {
        return this.zoomLevelList.getZoomLevelMax();
    }

    private Projection getProjection() {
        return this.projection;
    }

    public final Projection setMapCenter(Mbr mbr, PntShort mapCenter) {
        return this.setMapCenter(mbr, mapCenter.getX(), mapCenter.getY());
    }

    public final Projection setMapCenter(Mbr mbr, double mapX, double mapY) {
        double width = mbr.getWidth();
        double height = mbr.getHeight();
        double minx = mapX - width / 2.0;
        double miny = mapY - height / 2.0;
        double maxx = mapX + width / 2.0;
        double maxy = mapY + height / 2.0;
        Mbr newMBR = new Mbr(minx, miny, maxx, maxy);
        Projection projection = this.getProjection();
        Dimension pixelSize = projection.getPixelSize();
        Projection projectionNew = Projection.getProject(newMBR, pixelSize);
        return projectionNew;
    }

    public final Projection setPan(Mbr mbr, int dx, int dy) {
        Projection projection = this.getProjection();
        double scale = projection.getScale();
        double sx = dx / scale;
        double sy = -dy / scale;
        double minx = mbr.getMinX() + sx;
        double miny = mbr.getMinY() + sy;
        double maxx = mbr.getMaxX() + sx;
        double maxy = mbr.getMaxY() + sy;
        Mbr newMBR = new Mbr(minx, miny, maxx, maxy);
        Dimension pixelSize = projection.getPixelSize();
        Projection projectionNew = Projection.getProject(newMBR, pixelSize);
        return projectionNew;
    }

    public final Projection setViewFullExtent() {
        this.debug("Setting View All Box .....");
        Projection projectionNew = this.setZoomLevel(this.getZoomLevelMax());
        this.debug("Done Setting View All Box.");
        return projectionNew;
    }

    public final Projection setZoomLevel(int zoomLevelCurr) {
        final int zoomLevelMax = this.getZoomLevelMax();
        if (true) {
            zoomLevelCurr = zoomLevelCurr < 0 ? 0 : zoomLevelCurr;
            zoomLevelCurr = zoomLevelCurr > zoomLevelMax ? zoomLevelMax : zoomLevelCurr;
        }
        if (true) {
            final Projection projection = this.getProjection();
            final ZoomLevelList zoomLevelList = this.zoomLevelList;
            Dimension pixelSize = projection.getPixelSize();
            final Mbr mbrTopLevel = this.getTopLevelMbr();
            Mbr mbrNew = null;
            if (mbrTopLevel == null) {
            } else if (zoomLevelCurr == zoomLevelMax) {
                mbrNew = mbrTopLevel;
            } else {
                LevelScale levelScale = zoomLevelList.getLevelScale(zoomLevelCurr);
                double sw = mbrTopLevel.getWidth() * levelScale.getScale();
                double sh = mbrTopLevel.getHeight() * levelScale.getScale();
                Mbr mbr = projection.getMbr();
                PntShort cenS = mbr.getCentroid();
                double scx = cenS.x;
                double scy = cenS.y;
                double minx = scx - sw / 2.0F;
                double miny = scy - sh / 2.0F;
                double maxx = scx + sw / 2.0F;
                double maxy = scy + sh / 2.0F;
                mbrNew = new Mbr(minx, miny, maxx, maxy);
                this.debug("MBR NEW = " + mbrNew);
            }
            if (mbrNew != null) {
                Projection projectionNew = Projection.getProject(mbrNew, pixelSize);
                return projectionNew;
            }
        }
        return null;
    }

    private double getLevelDist(double dist, final int n, final int N) {
        if (true) {
            if (n == N) {
                return dist;
            } else {
                double q = n * Math.sin((Math.PI / 2.0) * (n + 1.0) / (N + 1.0));
                return dist * Math.pow(2, q) / Math.pow(2, N);
            }
        } else if (true) {
            return dist * Math.pow(2, n) / Math.pow(2, N);
        } else if (true) {
            return dist * (n + 1) / (N + 1);
        } else {
            return dist * Math.sin(Math.PI * (n + 1.0) / (2.0 * N + 2.0));
        }
    }

    public MapController_03_Web(Projection projection, Mbr topLevelMbr, ZoomLevelList zoomLevelList) {
        super();
        this.projection = projection;
        this.topLevelMbr = topLevelMbr;
        this.zoomLevelList = zoomLevelList;
    }

    private Projection projection;

    private Mbr topLevelMbr;

    private ZoomLevelList zoomLevelList;
}
