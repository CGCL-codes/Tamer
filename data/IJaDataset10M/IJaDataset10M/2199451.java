package com.od.jtimeseries.ui.visualizer.download.panel;

import com.od.jtimeseries.context.TimeSeriesContext;
import com.od.jtimeseries.ui.visualizer.displaypattern.DisplayNameCalculator;
import com.od.jtimeseries.ui.visualizer.download.panel.FindRemoteTimeSeriesQuery;
import com.od.jtimeseries.ui.timeseries.RemoteChartingTimeSeries;
import com.od.jtimeseries.util.time.Time;
import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: Nick Ebbutt
 * Date: 12-Jan-2009
 * Time: 16:18:37
 */
public class AddRemoteSeriesQuery {

    private TimeSeriesContext parent;

    private URL remoteContextUrl;

    private DisplayNameCalculator displayNameCalculator;

    public AddRemoteSeriesQuery(TimeSeriesContext destinationContext, URL remoteContextUrl, DisplayNameCalculator displayNameCalculator) {
        this.parent = destinationContext;
        this.remoteContextUrl = remoteContextUrl;
        this.displayNameCalculator = displayNameCalculator;
    }

    public void runQuery() throws Exception {
        FindRemoteTimeSeriesQuery findAllTimeSeries = new FindRemoteTimeSeriesQuery(remoteContextUrl);
        findAllTimeSeries.runQuery();
        for (FindRemoteTimeSeriesQuery.RemoteTimeSeries timeSeriesResult : findAllTimeSeries.getResult()) {
            createTimeSeries(timeSeriesResult);
        }
    }

    private void createTimeSeries(FindRemoteTimeSeriesQuery.RemoteTimeSeries timeSeriesResult) {
        TimeSeriesContext c = parent.createContextForPath(timeSeriesResult.getParentPath());
        RemoteChartingTimeSeries series = new RemoteChartingTimeSeries(timeSeriesResult.getId(), timeSeriesResult.getDescription(), timeSeriesResult.getSeriesURL(), Time.minutes(5), 1);
        series.putAllProperties(timeSeriesResult.getSummaryStatsProperties());
        series.setNeverLoadRemoteSeriesData(true);
        c.addChild(series);
        displayNameCalculator.setDisplayName(series);
    }
}
