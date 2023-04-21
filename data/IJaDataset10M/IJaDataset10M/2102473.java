package net.wgen.op.event;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import net.wgen.op.util.AssertionUtils;
import net.wgen.op.logging.RequestExecutionInfo;
import net.wgen.op.logging.Snapshot;
import net.wgen.op.logging.TraceKey;

/**
 * @author Paul Feuer, Wireless Generation, Inc.
 * @version $Id: RequestEventHandler.java 22 2007-06-26 01:11:24Z paulfeuer $
 */
public class RequestEventHandler implements EventHandler<RequestFinishedEvent> {

    private Logger _logger = Logger.getLogger("Stat");

    private String DELIMITER = EventLayoutCommon.DELIMITER;

    public RequestEventHandler() {
        setLogger("Stat");
    }

    public void setLogger(String loggerPath) {
        _logger = Logger.getLogger(loggerPath);
    }

    public void handleEvent(Level loggerLevel, RequestFinishedEvent e) {
        String message = formatMessage(e);
        if (message != null) _logger.log(loggerLevel, message);
    }

    protected String formatMessage(RequestFinishedEvent e) {
        TraceKey traceKey = e.getTraceKey();
        AssertionUtils.assertNotNull(traceKey, "traceKey");
        AssertionUtils.assertNotNull(traceKey.getModule(), "traceKey.getModule()");
        AssertionUtils.assertNotNull(traceKey.getExecInfo(), "traceKey.getExecInfo()");
        long t2 = System.currentTimeMillis();
        traceKey.getExecInfo().setTotalTime(t2 - traceKey.getStartTime());
        RequestExecutionInfo info = traceKey.getExecInfo();
        String request = traceKey.getRequestUrl();
        if (request != null && EventLayoutCommon.SKIP_LIST.contains(request)) {
            return null;
        }
        Snapshot.aggregateRequest(traceKey);
        String module = traceKey.getModule().getName();
        String subModule = traceKey.getSubModule() != null ? traceKey.getSubModule().getName() : "";
        int numCxUsed = info.getTotalConnectionsUsed();
        int numOpsUsed = info.getTotalOpsExecuted();
        int dbCallsMade = info.getTotalDatabaseCallsMade();
        int rowsUsed = info.getTotalRowsRetrieved();
        long totalTime = info.getTotalTime();
        long timeInDb = info.getTotalTimeInDatabase();
        long timeGetCx = info.getTotalTimeGettingCx();
        String isError = info.isError() ? "ERR" : "OK";
        String errMsg = (info.getErrMsg() != null ? info.getErrMsg() : "");
        StringBuffer buf = new StringBuffer(256);
        EventLayoutCommon.startMessage(buf, e);
        buf.append(module).append(DELIMITER);
        buf.append(subModule).append(DELIMITER);
        buf.append(isError).append(DELIMITER);
        buf.append(totalTime).append(" ms").append(DELIMITER);
        buf.append(rowsUsed).append(" rows").append(DELIMITER);
        buf.append(numCxUsed).append(" cx").append(DELIMITER);
        buf.append(numOpsUsed).append(" ops").append(DELIMITER);
        buf.append(dbCallsMade).append(" calls").append(DELIMITER);
        buf.append(timeInDb).append(" inDB").append(DELIMITER);
        buf.append(timeGetCx).append(" getCx").append(DELIMITER);
        buf.append(request).append(DELIMITER);
        buf.append(errMsg).append(DELIMITER);
        return buf.toString().replace('\n', ' ');
    }
}
