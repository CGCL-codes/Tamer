package org.aiotrade.platform.core.netbeans;

import org.aiotrade.platform.core.LogManager;
import org.openide.ErrorManager;

/**
 *
 * @author Caoyuan Deng
 */
public class NetBeansLogManager extends LogManager {

    ErrorManager errorManager = ErrorManager.getDefault();

    public NetBeansLogManager() {
    }

    public void log(int severity, String message) {
        errorManager.log(severity, message);
    }

    public void log(String message) {
        errorManager.log(message);
    }

    public void info(String message) {
        errorManager.log(LogManager.INFORMATIONAL, message);
    }

    public boolean isDebugEnabled() {
        return true;
    }

    public void debug(String message) {
        errorManager.log(LogManager.INFORMATIONAL, message);
    }

    public void debug(Throwable t) {
        errorManager.notify(LogManager.INFORMATIONAL, t);
    }

    public void error(String message) {
        errorManager.log(LogManager.ERROR, message);
    }

    public void error(Throwable t) {
        errorManager.notify(LogManager.ERROR, t);
    }

    public void notify(int severity, Throwable t) {
        errorManager.notify(severity, t);
    }

    public void notify(Throwable t) {
        errorManager.notify(t);
    }
}
