package com.od.jtimeseries.server.servermetrics;

import com.od.jtimeseries.component.managedmetric.AbstractManagedMetric;
import com.od.jtimeseries.component.util.cache.TimeSeriesCache;
import com.od.jtimeseries.context.TimeSeriesContext;
import com.od.jtimeseries.identifiable.Identifiable;
import com.od.jtimeseries.source.Counter;
import com.od.jtimeseries.util.time.Time;
import com.od.jtimeseries.util.time.TimePeriod;
import static com.od.jtimeseries.capture.function.CaptureFunctions.MEAN_COUNT_OVER;
import static com.od.jtimeseries.capture.function.CaptureFunctions.LATEST;

/**
 * Created by IntelliJ IDEA.
 * User: nick
 * Date: 25-Nov-2009
 * Time: 19:33:20
 * To change this template use File | Settings | File Templates.
 */
public class MemoryCacheRequestCountMetric extends AbstractManagedMetric {

    private static final String id = "MemoryCacheRequestCount";

    private String parentContextPath;

    private TimeSeriesCache cache;

    private TimePeriod timePeriod;

    public MemoryCacheRequestCountMetric(String parentContextPath, TimeSeriesCache cache) {
        this(parentContextPath, cache, DEFAULT_TIME_PERIOD_FOR_SERVER_METRICS);
    }

    public MemoryCacheRequestCountMetric(String parentContextPath, TimeSeriesCache cache, TimePeriod timePeriod) {
        this.parentContextPath = parentContextPath;
        this.cache = cache;
        this.timePeriod = timePeriod;
    }

    protected String getSeriesPath() {
        return parentContextPath + Identifiable.NAMESPACE_SEPARATOR + id;
    }

    public void doInitializeMetric(TimeSeriesContext rootContext, String path) {
        Counter c = rootContext.createCounterSeries(path, "Number of requests to memory cache", LATEST(timePeriod), MEAN_COUNT_OVER(Time.seconds(1), timePeriod));
        cache.setCacheRequestCounter(c);
    }
}
