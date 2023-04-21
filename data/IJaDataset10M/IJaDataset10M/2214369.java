package com.od.jtimeseries.timeseries.aggregation;

import com.od.jtimeseries.timeseries.TimeSeries;
import com.od.jtimeseries.timeseries.function.aggregate.AggregateFunction;
import com.od.jtimeseries.timeseries.impl.DefaultIdentifiableTimeSeries;
import com.od.jtimeseries.timeseries.impl.DefaultTimeSeries;

/**
 * Created by IntelliJ IDEA.
 * User: Nick Ebbutt
 * Date: 21-Jan-2009
 * Time: 16:31:08
 */
public class DefaultAggregatedIdentifiableTimeSeries extends DefaultIdentifiableTimeSeries implements AggregatedIdentifiableTimeSeries {

    private DefaultAggregatedTimeSeries series;

    public DefaultAggregatedIdentifiableTimeSeries(String id, String description, AggregateFunction aggregateFunction) {
        super(id, description, new DefaultTimeSeries());
        this.series = new DefaultAggregatedTimeSeries(this, aggregateFunction);
    }

    public synchronized void addTimeSeries(TimeSeries... timeSeries) {
        series.addTimeSeries(timeSeries);
    }
}
