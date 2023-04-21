package com.od.jtimeseries.server.message;

import com.od.jtimeseries.context.TimeSeriesContext;
import com.od.jtimeseries.net.udp.TimeSeriesValueMessage;
import com.od.jtimeseries.net.udp.UdpMessage;
import com.od.jtimeseries.net.udp.UdpServer;
import com.od.jtimeseries.component.util.path.PathMapper;
import com.od.jtimeseries.component.util.path.PathMappingResult;
import com.od.jtimeseries.source.Counter;
import com.od.jtimeseries.source.ValueRecorder;
import com.od.jtimeseries.timeseries.TimeSeries;
import com.od.jtimeseries.util.NamedExecutors;
import com.od.jtimeseries.util.logging.LogMethods;
import com.od.jtimeseries.util.logging.LogUtils;
import com.od.jtimeseries.util.time.Time;
import com.od.jtimeseries.util.time.TimePeriod;
import java.util.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by IntelliJ IDEA.
* User: nick
* Date: 20-May-2009
* Time: 22:11:48
* To change this template use File | Settings | File Templates.
*
* Appends a value to a series when a message is received, if necessary creating the series
* according to the context path and id received in the message.
*
* Keeps a running tally of the number of series for which data is being received, and the overall update count.
*/
public class AppendToSeriesMessageListener implements UdpServer.UdpMessageListener {

    private static LogMethods logMethod = LogUtils.getLogMethods(AppendToSeriesMessageListener.class);

    public static final TimePeriod STALE_SERIES_DELAY = Time.hours(1);

    private static volatile Counter updateMessagesReceivedCounter;

    private static volatile ValueRecorder liveSeriesTotalValueRecorder;

    private final Map<String, Long> liveSeriesLastUpdateMap = new HashMap<String, Long>();

    private TimeSeriesContext rootContext;

    private PathMapper pathMapper;

    private Set<String> loggedDeniedPaths = Collections.synchronizedSet(new HashSet<String>());

    private Set<String> loggedMigratedPaths = Collections.synchronizedSet(new HashSet<String>());

    public AppendToSeriesMessageListener(TimeSeriesContext rootContext, PathMapper pathMapper) {
        this.rootContext = rootContext;
        this.pathMapper = pathMapper;
        scheduleReportingAndCleanup(NamedExecutors.newSingleThreadScheduledExecutor("AppendToSeriesMessageListener"));
    }

    private void scheduleReportingAndCleanup(ScheduledExecutorService staleSeriesExecutor) {
        staleSeriesExecutor.scheduleAtFixedRate(new Runnable() {

            public void run() {
                long currentTime = System.currentTimeMillis();
                synchronized (liveSeriesLastUpdateMap) {
                    Iterator<Map.Entry<String, Long>> i = liveSeriesLastUpdateMap.entrySet().iterator();
                    while (i.hasNext()) {
                        Map.Entry<String, Long> e = i.next();
                        if (currentTime - e.getValue() > STALE_SERIES_DELAY.getLengthInMillis()) {
                            logMethod.logInfo("Series " + e.getKey() + " has received no updates for one hour, " + "it is likely this series is no longer being published");
                            i.remove();
                        }
                    }
                    if (liveSeriesTotalValueRecorder != null) {
                        liveSeriesTotalValueRecorder.newValue(liveSeriesLastUpdateMap.size());
                    }
                }
            }
        }, 300, 300, TimeUnit.SECONDS);
    }

    public void udpMessageReceived(UdpMessage m) {
        if (m instanceof TimeSeriesValueMessage) {
            TimeSeriesValueMessage v = (TimeSeriesValueMessage) m;
            processNewTimeSeriesValue(v);
        }
    }

    private void processNewTimeSeriesValue(TimeSeriesValueMessage v) {
        String path = v.getSeriesPath();
        PathMappingResult result = pathMapper.getPathMapping(path);
        if (result.getType() == PathMappingResult.ResultType.DENY) {
            if (loggedDeniedPaths.add(path)) {
                logMethod.logInfo("Not creating a series for path " + path + ", this is an invalid series path which is denied by path mapping rules configuration");
            }
        } else if (result.getType() == PathMappingResult.ResultType.MIGRATE) {
            if (loggedMigratedPaths.add(path)) {
                logMethod.logInfo("Series with path " + path + " will be migrated to path " + result.getNewPath() + " due to path mapping rules configuration");
            }
            path = result.getNewPath();
            findOrCreateSeriesAndAddTimepoint(v, path);
        } else if (result.getType() == PathMappingResult.ResultType.PERMIT) {
            findOrCreateSeriesAndAddTimepoint(v, path);
        }
    }

    private void findOrCreateSeriesAndAddTimepoint(TimeSeriesValueMessage v, String path) {
        try {
            TimeSeries s = rootContext.getOrCreateTimeSeries(path, v.getDescription());
            s.addItem(v.getTimeSeriesItem());
            if (updateMessagesReceivedCounter != null) {
                updateMessagesReceivedCounter.incrementCount();
            }
            synchronized (liveSeriesLastUpdateMap) {
                Long lastTimestamp = liveSeriesLastUpdateMap.get(path);
                if (lastTimestamp == null) {
                    liveSeriesLastUpdateMap.put(path, System.currentTimeMillis());
                    logMethod.logInfo("Started to receive UDP updates for series " + path + " from host " + v.getHostname() + " with address " + v.getInetAddress());
                }
            }
        } catch (Exception e) {
            logMethod.logError("Error when trying to create timeseries for UDP series " + path + " from host " + v.getInetAddress() + " with address " + v.getInetAddress());
            logMethod.logDebug("Error when trying to create timeseries", e);
        }
    }

    public static void setUpdateMessagesReceivedCounter(Counter updateMessagesReceivedCounter) {
        AppendToSeriesMessageListener.updateMessagesReceivedCounter = updateMessagesReceivedCounter;
    }

    public static void setLiveSeriesTotalValueRecorder(ValueRecorder liveSeriesTotalValueRecorder) {
        AppendToSeriesMessageListener.liveSeriesTotalValueRecorder = liveSeriesTotalValueRecorder;
    }
}
