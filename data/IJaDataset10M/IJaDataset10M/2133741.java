package xbrowser.logger;

import xbrowser.XProjectConstants;

public class XLogMode {

    public XLogMode() {
        this(XProjectConstants.LOG_MODE_NONE);
    }

    public XLogMode(int log_mode) {
        switch(log_mode) {
            case XProjectConstants.LOG_MODE_NONE:
                setLogMessages(false);
                setLogDebugs(false);
                setLogWarnings(false);
                setLogErrors(false);
                break;
            case XProjectConstants.LOG_MODE_BRIEF:
                setLogMessages(false);
                setLogDebugs(false);
                setLogWarnings(true);
                setLogErrors(true);
                break;
            case XProjectConstants.LOG_MODE_VERBOSE:
                setLogMessages(true);
                setLogDebugs(true);
                setLogWarnings(true);
                setLogErrors(true);
                break;
        }
    }

    public void setLogMessages(boolean b) {
        logMessages = b;
    }

    public boolean getLogMessages() {
        return logMessages;
    }

    public void setLogDebugs(boolean b) {
        logDebugs = b;
    }

    public boolean getLogDebugs() {
        return logDebugs;
    }

    public void setLogWarnings(boolean b) {
        logWarnings = b;
    }

    public boolean getLogWarnings() {
        return logWarnings;
    }

    public void setLogErrors(boolean b) {
        logErrors = b;
    }

    public boolean getLogErrors() {
        return logErrors;
    }

    public boolean isOff() {
        return (!logMessages && !logDebugs && !logWarnings && !logErrors);
    }

    private boolean logMessages = false;

    private boolean logDebugs = false;

    private boolean logWarnings = false;

    private boolean logErrors = false;
}
