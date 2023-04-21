package com.kescom.matrix.core.compute;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import org.hibernate.criterion.DetachedCriteria;

public class SeriesQuerySeriesBatchLongCompute extends SeriesBatchLongCompute {

    private DetachedCriteria seriesCriteria;

    private int seriesCountEstimate;

    protected Map<String, String> getSeriesTaskArgs() {
        Map<String, String> args = new LinkedHashMap<String, String>();
        args.putAll(super.getSeriesTaskArgs());
        args.put("seriesCountEstimate", Integer.toString(seriesCountEstimate));
        return args;
    }

    public void setSeriesCountEstimate(int seriesCountEstimate) {
        this.seriesCountEstimate = seriesCountEstimate;
    }

    public void setSeriesCriteria(DetachedCriteria seriesCriteria) {
        this.seriesCriteria = seriesCriteria;
    }

    protected Serializable getInputObject() {
        if (seriesCriteria instanceof Serializable) return (Serializable) seriesCriteria; else return null;
    }
}
