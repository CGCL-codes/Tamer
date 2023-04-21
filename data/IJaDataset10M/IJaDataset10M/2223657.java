package net.wgen.op.logging;

import net.wgen.op.system.ServerEnvironment;
import net.wgen.op.event.EventLayoutCommon;
import java.util.*;
import java.text.SimpleDateFormat;
import org.apache.log4j.*;

/**
 * Aggregator of stats to log every so often for an indication of load.
 *
 * @author Paul Feuer, Wireless Generation, Inc.
 * @version $Id: Snapshot.java 22 2007-06-26 01:11:24Z paulfeuer $
 */
public class Snapshot {

    private static final Map _snapShotPerApp = new HashMap();

    public static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:S");

    private static final boolean IS_PROD = ServerEnvironment.isProduction();

    private static final long RESET_INTERVAL = 5 * 60 * 1000;

    private static final int THRESH_ROWS = (IS_PROD ? 5 : 2) * 5000;

    private static final int THRESH_CX = (IS_PROD ? 5 : 2) * 300;

    private static final int THRESH_CALLS = (IS_PROD ? 5 : 2) * 300;

    private static long _resetTime = System.currentTimeMillis();

    private static final Timer TIMER = new Timer();

    private static final Logger LOG = Logger.getLogger("Snapshot");

    ;

    static {
        TIMER.scheduleAtFixedRate(new TimerTask() {

            public void run() {
                dumpSnapshot();
            }
        }, RESET_INTERVAL, RESET_INTERVAL);
    }

    private String moduleName = null;

    private int rowsAccessed = 0;

    private int opsExecuted = 0;

    private int dbCallsExecuted = 0;

    private int connectionsUsed = 0;

    private int requestsMade = 0;

    private int dbErrorsOccurred = 0;

    private int reqErrorsOccurred = 0;

    private int databaseTime = 0;

    private int requestTime = 0;

    private int traceRequests = 0;

    private int profileRequests = 0;

    private int tempKeysUsed = 0;

    public Snapshot(String appName) {
        this.moduleName = appName;
    }

    public static void aggregateRequest(TraceKey key) {
        Snapshot snap = getSnapshot(key.getModule().getName());
        synchronized (snap) {
            RequestExecutionInfo info = key.getExecInfo();
            if (info.isError()) snap.reqErrorsOccurred++;
            snap.requestTime += info.getTotalTime();
            snap.opsExecuted += info.getTotalOpsExecuted();
            snap.connectionsUsed += info.getTotalConnectionsUsed();
            if (key.isTemporary()) snap.tempKeysUsed++; else snap.requestsMade++;
            if (key.isTracingOn()) snap.traceRequests++;
        }
    }

    public synchronized void reset() {
        rowsAccessed = 0;
        opsExecuted = 0;
        dbCallsExecuted = 0;
        connectionsUsed = 0;
        requestsMade = 0;
        tempKeysUsed = 0;
        dbErrorsOccurred = 0;
        reqErrorsOccurred = 0;
        databaseTime = 0;
        requestTime = 0;
        traceRequests = 0;
        profileRequests = 0;
    }

    public synchronized String toString() {
        StringBuffer buf = new StringBuffer(128);
        long now = System.currentTimeMillis();
        Date nowObj = new Date(now);
        Date rstObj = new Date(_resetTime);
        long timeSinceLastReset = now - _resetTime;
        buf.append(SDF.format(rstObj)).append(EventLayoutCommon.DELIMITER);
        buf.append(SDF.format(nowObj)).append(EventLayoutCommon.DELIMITER);
        buf.append(timeSinceLastReset).append(EventLayoutCommon.DELIMITER);
        buf.append(moduleName).append(EventLayoutCommon.DELIMITER);
        buf.append(rowsAccessed).append(" rows").append(EventLayoutCommon.DELIMITER);
        buf.append(opsExecuted).append(" ops").append(EventLayoutCommon.DELIMITER);
        buf.append(dbCallsExecuted).append(" calls").append(EventLayoutCommon.DELIMITER);
        buf.append(connectionsUsed).append(" cx").append(EventLayoutCommon.DELIMITER);
        buf.append(requestsMade).append(" reqs").append(EventLayoutCommon.DELIMITER);
        buf.append(tempKeysUsed).append(" tmpKeys").append(EventLayoutCommon.DELIMITER);
        buf.append(dbErrorsOccurred).append(" dbErr").append(EventLayoutCommon.DELIMITER);
        buf.append(reqErrorsOccurred).append(" reqErr").append(EventLayoutCommon.DELIMITER);
        buf.append(databaseTime).append(" dbTime").append(EventLayoutCommon.DELIMITER);
        buf.append(requestTime).append(" reqTime").append(EventLayoutCommon.DELIMITER);
        buf.append(traceRequests).append(" traceReq").append(EventLayoutCommon.DELIMITER);
        buf.append(profileRequests).append(" profReq");
        return buf.toString();
    }

    /**
     *
     * @param appName
     * @return the snapshot for the app
     */
    private static Snapshot getSnapshot(String appName) {
        synchronized (_snapShotPerApp) {
            Snapshot s = (Snapshot) _snapShotPerApp.get(appName);
            if (s == null) {
                s = new Snapshot(appName);
                _snapShotPerApp.put(appName, s);
            }
            return s;
        }
    }

    /**
     * Internal dump method.
     */
    private static synchronized void dumpSnapshot() {
        Iterator it = _snapShotPerApp.values().iterator();
        while (it.hasNext()) {
            Snapshot snap = (Snapshot) it.next();
            Level logLevel = Level.DEBUG;
            if (snap.connectionsUsed >= THRESH_CX) logLevel = Level.WARN;
            if (snap.rowsAccessed >= THRESH_ROWS) logLevel = Level.WARN;
            if (snap.dbCallsExecuted >= THRESH_CALLS) logLevel = Level.WARN;
            LOG.log(logLevel, snap.toString());
            snap.reset();
        }
        _resetTime = System.currentTimeMillis();
    }
}
