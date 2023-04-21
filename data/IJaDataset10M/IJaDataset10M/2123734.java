package com.od.jtimeseries.server.servermetrics;

import com.od.jtimeseries.context.TimeSeriesContext;
import com.od.jtimeseries.timeseries.IdentifiableTimeSeries;
import com.od.jtimeseries.util.time.TimePeriod;

/**
 * Created by IntelliJ IDEA.
 * User: nick
 * Date: 23-Nov-2009
 * Time: 21:39:49
 * To change this template use File | Settings | File Templates.
 */
public interface ServerMetric {

    /**
     * Called by the server to ask the metric to initialize itself
     * Typically this will involve creating one or more new timeseries
     */
    public void initializeMetrics(TimeSeriesContext rootContext);
}
