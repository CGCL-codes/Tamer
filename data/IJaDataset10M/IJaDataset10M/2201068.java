package com.bayareasoftware.chartengine.chart.jfree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jfree.data.general.Dataset;
import com.bayareasoftware.chartengine.model.ChartConstants;
import com.bayareasoftware.chartengine.model.ChartInfo;
import com.bayareasoftware.chartengine.model.SeriesDescriptor;
import com.bayareasoftware.chartengine.model.SimpleProps;

/**
 * Map series to the appropriate dataset.  Series share a dataset 
 * if they share 1) renderer 2) range axis
 * otherwise, different datasets
 */
class DSMap implements ChartConstants {

    private String defaultRenderType;

    private Map<SeriesKey, Dataset> series2ds = new HashMap<SeriesKey, Dataset>();

    private List<Dataset> dsList = new ArrayList<Dataset>();

    private List<SeriesKey> dsKeys = new ArrayList<SeriesKey>();

    public DSMap(Producer prod, ChartInfo ci) {
        int numSeries = ci.getSeriesCount();
        this.defaultRenderType = ci.getRenderType();
        for (int i = 0; i < numSeries; i++) {
            SeriesDescriptor sd = ci.getSeriesDescriptor(i);
            SeriesKey sk = seriesKey(sd);
            if (series2ds.get(sk) == null) {
                addDataset(sk, prod.createDataset(ci, sd));
            }
        }
    }

    private boolean isDefaultRenderType(String r) {
        if (r != null) {
            return r.equals(defaultRenderType);
        }
        return true;
    }

    public Dataset getDatasetForSeries(SeriesDescriptor sd) {
        Dataset ret = null;
        SeriesKey sk = seriesKey(sd);
        ret = series2ds.get(sk);
        return ret;
    }

    public void setDatasetForSeries(SeriesDescriptor sd, Dataset ds) {
        Dataset old = this.getDatasetForSeries(sd);
        for (int i = 0; i < dsList.size(); i++) {
            if (old == dsList.get(i)) {
                dsList.set(i, ds);
                break;
            }
        }
        SeriesKey sk = seriesKey(sd);
        series2ds.put(sk, ds);
    }

    public String getRendererForDataset(int index) {
        return dsKeys.get(index).rendererType;
    }

    public SimpleProps getRendererPropsForDataset(int index) {
        return dsKeys.get(index).rendererProps;
    }

    public int getDatasetCount() {
        return dsList.size();
    }

    public Dataset getDataset(int index) {
        return dsList.get(index);
    }

    public int getRangeAxisForDataset(int index) {
        return dsKeys.get(index).axis;
    }

    public int getIndexOfDataset(Dataset ds) {
        for (int i = 0; i < dsList.size(); i++) {
            if (ds == dsList.get(i)) {
                return i;
            }
        }
        return -1;
    }

    private SeriesKey seriesKey(SeriesDescriptor sd) {
        int axis = sd.getAxisIndex();
        if (axis >= 0 && axis < MAX_RANGE_AXES) {
        } else {
            axis = 0;
        }
        String rt = sd.getRenderer();
        if (isDefaultRenderType(rt)) {
            rt = null;
        }
        return new SeriesKey(rt, axis, sd.getRendererProps());
    }

    private void addDataset(SeriesKey sk, Dataset ds) {
        series2ds.put(sk, ds);
        dsList.add(ds);
        dsKeys.add(sk);
    }

    private class SeriesKey {

        String rendererType;

        final int axis;

        SimpleProps rendererProps;

        SeriesKey(String r, int ax, SimpleProps rp) {
            this.axis = ax;
            this.rendererType = (r != null ? r : "");
            this.rendererProps = rp;
        }

        @Override
        public int hashCode() {
            return rendererType.hashCode() ^ axis;
        }

        @Override
        public boolean equals(Object o) {
            SeriesKey sk = (SeriesKey) o;
            if (axis != sk.axis) {
                return false;
            }
            return rendererType.equals(sk.rendererType);
        }

        @Override
        public String toString() {
            return "[SK: render=" + rendererType + " axis=" + axis + "]";
        }
    }

    private static void p(String s) {
        System.out.println("[DSMap] " + s);
    }
}
