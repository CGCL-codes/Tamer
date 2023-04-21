package net.sf.forecash.commons.log4j;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.ILogListener;
import org.eclipse.core.runtime.IStatus;

public class Log4jListener implements ILogListener {

    @Override
    public void logging(IStatus status, String plugin) {
        Logger log = Logger.getLogger(status.getPlugin());
        writeLog(0, status, log);
    }

    protected Level getPriority(IStatus status) {
        switch(status.getSeverity()) {
            case IStatus.INFO:
                return Level.INFO;
            case IStatus.WARNING:
                return Level.WARN;
            case IStatus.ERROR:
                return Level.ERROR;
            default:
                return Level.DEBUG;
        }
    }

    protected void writeLog(int depth, IStatus status, Logger log) {
        writeEntry(depth, status, log);
        IStatus[] children = status.getChildren();
        if (children != null) {
            for (int i = 0; i < children.length; i++) {
                writeLog(depth + 1, children[i], log);
            }
        }
    }

    protected void writeEntry(int depth, IStatus status, Logger log) {
        String message;
        if (depth == 0) {
            message = String.format("!ENTRY %s (code %d)", status.getMessage(), status.getCode());
        } else {
            message = String.format("!SUBENTRY %d %s (code %d)", depth, status.getMessage(), status.getCode());
        }
        log.log(getPriority(status), message, status.getException());
    }
}
