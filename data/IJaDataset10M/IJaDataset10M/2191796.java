package net.wgen.op;

import net.wgen.op.logging.TraceKey;
import net.wgen.op.db.CallExecutor;
import net.wgen.op.db.DatabaseCall;
import org.apache.log4j.Logger;
import org.example.project.ProjectModule;
import java.util.Random;

/**
 * @author Paul Feuer, Wireless Generation, Inc.
 * @version $Id: SleepyOp.java 8 2007-01-17 15:37:22Z paulfeuer $
 */
public class SleepyOp extends Op {

    private static final long serialVersionUID = -5868159598247540909L;

    protected Long _sleepyTime = new Long(0);

    private String _serverData = null;

    public SleepyOp(TraceKey traceKey, Long sleepyTime) {
        super(traceKey);
        _sleepyTime = sleepyTime;
    }

    public String getServerData() {
        return _serverData;
    }

    public void setSleepyTime(Long sleepyTime) {
        _sleepyTime = sleepyTime;
    }

    protected void _execute(CallExecutor exec) throws OpException {
        _serverData = "I have a random number: " + new Random(_sleepyTime.longValue()).nextDouble();
        Logger.getLogger(getClass()).info(getTraceKey() + " " + _serverData);
        Logger.getLogger(getClass()).info(getTraceKey() + " about to sleep " + _sleepyTime);
        try {
            Thread.sleep(_sleepyTime.longValue());
        } catch (InterruptedException ex) {
            throw new OpException(this, ex);
        }
        Logger.getLogger(getClass()).info(getTraceKey() + " is DONE sleeping " + _sleepyTime + "!");
    }
}
