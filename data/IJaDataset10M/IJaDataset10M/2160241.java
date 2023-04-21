package org.apache.wsrp4j.log;

/**
 Wrapper class for a log4j logger
 */
public final class LoggerImpl implements Logger {

    private org.apache.log4j.Logger logger = null;

    private int level = Logger.ERROR;

    LoggerImpl(org.apache.log4j.Logger aLogger, int logLevel) {
        this.logger = aLogger;
        this.level = logLevel;
    }

    public boolean isLogging(int logLevel) {
        return logLevel <= level;
    }

    public void text(int logLevel, String loggingMethod, String text) {
        text(logLevel, loggingMethod, text, null);
    }

    public void text(int logLevel, String loggingMethod, String text, Object parm1) {
        text(logLevel, loggingMethod, text, new Object[] { parm1 });
    }

    public void text(int logLevel, String loggingMethod, String text, Object[] parms) {
        text(logLevel, loggingMethod, (Throwable) null, text, parms);
    }

    public void text(int logLevel, String loggingMethod, Throwable t, String text) {
        text(logLevel, loggingMethod, t, text, null);
    }

    public void text(int logLevel, String loggingMethod, Throwable t, String text, Object[] parms) {
        org.apache.log4j.Level log4jLevel = determineLog4jLevel(logLevel);
        StringBuffer msgBuffer = new StringBuffer();
        if (loggingMethod != null) {
            msgBuffer.append(loggingMethod);
            msgBuffer.append(" - ");
        }
        if (text != null) {
            msgBuffer.append(text);
        }
        if (parms != null) {
            msgBuffer.append("\nParameters:\n");
            for (int i = 0; i < parms.length; i++) {
                msgBuffer.append(parms[i]);
            }
        }
        logger.log(log4jLevel, msgBuffer.toString(), t);
    }

    public void entry(int logLevel, String loggingMethod) {
        entry(logLevel, loggingMethod, null);
    }

    public void entry(int logLevel, String loggingMethod, Object parm1) {
        entry(logLevel, loggingMethod, new Object[] { parm1 });
    }

    public void entry(int logLevel, String loggingMethod, Object[] parms) {
        text(logLevel, loggingMethod, "Entering method", parms);
    }

    public void exit(int logLevel, String loggingMethod) {
        text(logLevel, loggingMethod, "Exiting method.");
    }

    public void exit(int logLevel, String loggingMethod, byte retValue) {
        exit(logLevel, loggingMethod, new Byte(retValue));
    }

    public void exit(int logLevel, String loggingMethod, short retValue) {
        exit(logLevel, loggingMethod, new Short(retValue));
    }

    public void exit(int logLevel, String loggingMethod, int retValue) {
        exit(logLevel, loggingMethod, new Integer(retValue));
    }

    public void exit(int logLevel, String loggingMethod, long retValue) {
        exit(logLevel, loggingMethod, new Long(retValue));
    }

    public void exit(int logLevel, String loggingMethod, float retValue) {
        exit(logLevel, loggingMethod, new Float(retValue));
    }

    public void exit(int logLevel, String loggingMethod, double retValue) {
        exit(logLevel, loggingMethod, new Double(retValue));
    }

    public void exit(int logLevel, String loggingMethod, char retValue) {
        exit(logLevel, loggingMethod, new Character(retValue));
    }

    public void exit(int logLevel, String loggingMethod, boolean retValue) {
        exit(logLevel, loggingMethod, Boolean.valueOf(retValue));
    }

    public void exit(int logLevel, String loggingMethod, Object retValue) {
        text(logLevel, loggingMethod, "Exiting method. Returned value: {0}", retValue);
    }

    public void stackTrace(int logLevel, String loggingMethod, String text) {
        text(logLevel, loggingMethod, new Throwable("Stacktrace"), text);
    }

    private org.apache.log4j.Level determineLog4jLevel(int wsrp4jLevel) {
        if (wsrp4jLevel < Logger.ERROR) {
            return org.apache.log4j.Level.OFF;
        } else if (wsrp4jLevel < Logger.WARN) {
            return org.apache.log4j.Level.ERROR;
        } else if (wsrp4jLevel < Logger.INFO) {
            return org.apache.log4j.Level.WARN;
        } else if (wsrp4jLevel < Logger.TRACE_LOW) {
            return org.apache.log4j.Level.INFO;
        } else if (wsrp4jLevel < Logger.TRACE_HIGH) {
            return org.apache.log4j.Level.DEBUG;
        } else {
            return org.apache.log4j.Level.DEBUG;
        }
    }
}
