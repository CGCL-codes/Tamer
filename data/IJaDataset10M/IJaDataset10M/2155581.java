package org.timepedia.chronoscope.client.data;

import org.timepedia.chronoscope.client.data.tuple.Tuple2D;

/**
 * @author chad takahashi
 */
public final class RenderedPoint implements Tuple2D {

    private double domain = -1;

    private double range = -1;

    private double x = -1;

    private double y = -1;

    private int datasetIndex;

    private int domainIndex;

    private int dim;

    public String toString() {
        String ret = "";
        ret += " datasetIndex:" + datasetIndex;
        ret += " domainIndex:" + domainIndex;
        ret += " dim:" + dim;
        ret += " domain:" + domain;
        ret += ", range:" + range;
        ret += ", x:" + x;
        ret += ", y:" + y;
        return ret;
    }

    public RenderedPoint(int datasetIndex, int domainIndex, int dim, double domain, double range, double x, double y) {
        this.datasetIndex = datasetIndex;
        this.domainIndex = domainIndex;
        this.dim = dim;
        this.domain = domain;
        this.range = range;
        this.x = x;
        this.y = y;
    }

    public int getDatasetIndex() {
        return datasetIndex;
    }

    public int getDomainIndex() {
        return domainIndex;
    }

    public int getDimension() {
        return dim;
    }

    public double getRange(int rangeTupleIndex) {
        return range;
    }

    public int size() {
        return (x > -1 && y > -1 && domain > -1) ? 1 : 0;
    }

    public double getDomain() {
        return domain;
    }

    public double getRange0() {
        return range;
    }

    public double getPlotX() {
        return x;
    }

    public double getPlotY() {
        return y;
    }
}
