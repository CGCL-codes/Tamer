package org.wfp.vam.intermap.kernel.map.mapServices.wmc.om;

import org.wfp.vam.intermap.kernel.map.mapServices.wms.schema.type.WMSBaseBoundingBox;

/**
 * @author ETj
 */
public class WMCBoundingBox implements WMSBaseBoundingBox {

    private String _srs = null;

    private double _minx = Double.NaN;

    private double _miny = Double.NaN;

    private double _maxx = Double.NaN;

    private double _maxy = Double.NaN;

    private WMCBoundingBox() {
    }

    public static WMCBoundingBox newInstance() {
        return new WMCBoundingBox();
    }

    public void setSRS(String srs) {
        _srs = srs;
    }

    public String getSRS() {
        return _srs;
    }

    public void setMinx(double minx) {
        _minx = minx;
    }

    public double getMinx() {
        return _minx;
    }

    public void setMiny(double miny) {
        _miny = miny;
    }

    public double getMiny() {
        return _miny;
    }

    public void setMaxx(double maxx) {
        _maxx = maxx;
    }

    public double getMaxx() {
        return _maxx;
    }

    public void setMaxy(double maxy) {
        _maxy = maxy;
    }

    public double getMaxy() {
        return _maxy;
    }
}
