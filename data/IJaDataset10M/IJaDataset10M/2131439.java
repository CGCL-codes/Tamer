package com.newisys.joveutils.logging;

import com.newisys.dv.DV;
import com.newisys.joveutils.external.JunoSupport;
import com.newisys.verilog.util.Bit;
import com.newisys.verilog.util.BitVector;

public final class LogGlobals {

    private LogGlobals() {
    }

    public static BitVector getTime64() {
        return new BitVector(64, DV.simulation.getSimTime());
    }

    public static String secsToStr(final Integer time) {
        final String secsToStr;
        final Integer hours = JunoSupport.divide(time, 3600);
        final Integer mins = JunoSupport.divide(JunoSupport.mod(time, 3600), 60);
        final Integer secs = JunoSupport.mod(time, 60);
        secsToStr = JunoSupport.psprintf("%d:%s%d:%s%d", hours, JunoSupport.less(mins, 10) == Bit.ONE ? "0" : "", mins, JunoSupport.less(secs, 10) == Bit.ONE ? "0" : "", secs);
        return secsToStr;
    }

    public static LogSeverity strToSev(final String str) {
        LogSeverity strToSev = LogSeverity.UNDEFINED;
        if (JunoSupport.equals(str, "debug2")) strToSev = LogSeverity.LS_DEBUG2; else if (JunoSupport.equals(str, "debug1")) strToSev = LogSeverity.LS_DEBUG1; else if (JunoSupport.equals(str, "debug")) strToSev = LogSeverity.LS_DEBUG; else if (JunoSupport.equals(str, "userInfo")) strToSev = LogSeverity.LS_USER_INFO; else if (JunoSupport.equals(str, "config")) strToSev = LogSeverity.LS_CONFIG; else if (JunoSupport.equals(str, "warning")) strToSev = LogSeverity.LS_WARNING; else if (JunoSupport.equals(str, "error")) strToSev = LogSeverity.LS_ERROR; else if (JunoSupport.equals(str, "fatal")) strToSev = LogSeverity.LS_FATAL; else if (JunoSupport.equals(str, "system")) strToSev = LogSeverity.LS_SYSTEM; else JunoSupport.error("Unknown log severity %s!\n", str);
        return strToSev;
    }

    public static LogInterface newChildLogInterface(String name, final LogInterface parentLogIntf, final String prefix, Object creator) {
        final LogInterface newChildLogInterface;
        final LogInterface logIntf;
        if (parentLogIntf != null) {
            String parentName = parentLogIntf.getSource();
            if (!name.contains(parentName)) {
                name = JunoSupport.concat(parentName, ".", name);
            }
        }
        logIntf = new LogInterface(name, creator);
        if (parentLogIntf != null) {
            logIntf.copySettings(parentLogIntf);
        }
        if (prefix != null) {
            logIntf.loadSettings(prefix);
        }
        newChildLogInterface = logIntf;
        return newChildLogInterface;
    }

    public static String quoteMsg(String msg) {
        final String quoteMsg;
        if (JunoSupport.search(msg, ",") >= 0 && JunoSupport.search(msg, "\\\"") < 0) {
            msg = JunoSupport.concat("\\\"", msg, "\\\"");
        }
        quoteMsg = msg;
        return quoteMsg;
    }

    /**
     * Creates a LogInterface and logs a fatal message.
     * @param msg - the message displayed in the log
     */
    public static void fatalError(final String msg) {
        final LogInterface logIntf = new LogInterface("log", LogGlobals.class);
        logIntf.logMsg(LogSeverity.LS_FATAL, msg);
    }

    public static void assertFailed(final String msg, final String file, final int line, final String expr) {
        fatalError(JunoSupport.psprintf(msg, file, line, expr));
    }
}
