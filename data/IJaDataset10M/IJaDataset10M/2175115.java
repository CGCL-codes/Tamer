package org.apache.ws.jaxme.logging;

import org.apache.log4j.Level;

/**
 * @author <a href="mailto:joe@ispsoft.de">Jochen Wiedmann</a>
 */
public class Log4jLogger implements Logger {

    private final org.apache.log4j.Logger logger;

    public Log4jLogger(String pCName) {
        logger = org.apache.log4j.Logger.getLogger(pCName);
    }

    protected String format(String pMethodName, String pMsg, Object[] pDetails) {
        final StringBuffer sb = new StringBuffer(pMethodName);
        sb.append(": ").append(pMsg);
        if (pDetails != null) {
            for (int i = 0; i < pDetails.length; i++) {
                sb.append(", ");
                sb.append(pDetails[i]);
            }
        }
        return sb.toString();
    }

    protected String format(String pMethodName, Object[] pDetails) {
        final StringBuffer sb = new StringBuffer(pMethodName);
        if (pDetails != null) {
            String add = ": ";
            for (int i = 0; i < pDetails.length; i++) {
                sb.append(add);
                add = ", ";
                sb.append(pDetails[i]);
            }
        }
        return sb.toString();
    }

    public void entering(String pMethodName, Object[] pDetails) {
        if (logger.isDebugEnabled()) {
            logger.debug(format(pMethodName + " ->", pDetails));
        }
    }

    public void entering(String pMethodName) {
        if (logger.isDebugEnabled()) {
            logger.debug(pMethodName + " ->");
        }
    }

    public void entering(String pMethodName, Object pDetails) {
        if (logger.isDebugEnabled()) {
            logger.debug(pMethodName + " ->: " + pDetails);
        }
    }

    public void exiting(String pMethodName, Object[] pDetails) {
        if (logger.isDebugEnabled()) {
            logger.debug(format(pMethodName + " <-", pDetails));
        }
    }

    public void exiting(String pMethodName) {
        if (logger.isDebugEnabled()) {
            logger.debug(pMethodName + " <-");
        }
    }

    public void exiting(String pMethodName, Object pDetails) {
        if (logger.isDebugEnabled()) {
            logger.debug(pMethodName + " <-: " + pDetails);
        }
    }

    public void throwing(String pMethodName, Throwable pThrowable) {
        logger.error(pMethodName, pThrowable);
    }

    public boolean isFinestEnabled() {
        return logger.isDebugEnabled();
    }

    public void finest(String pMethodName, String pMsg, Object[] pDetails) {
        if (logger.isDebugEnabled()) {
            logger.debug(format(pMethodName, pMsg, pDetails));
        }
    }

    public void finest(String pMethodName, String pMsg) {
        if (logger.isDebugEnabled()) {
            logger.debug(pMethodName + ": " + pMsg);
        }
    }

    public void finest(String pMethodName, String pMsg, Object pDetails) {
        if (logger.isDebugEnabled()) {
            logger.debug(pMethodName + ": " + pMsg + ", " + pDetails);
        }
    }

    public void finer(String pMethodName, String pMsg, Object[] pDetails) {
        finest(pMethodName, pMsg, pDetails);
    }

    public boolean isFinerEnabled() {
        return isFinestEnabled();
    }

    public void finer(String pMethodName, String pMsg) {
        finest(pMethodName, pMsg);
    }

    public void finer(String pMethodName, String pMsg, Object pDetails) {
        finest(pMethodName, pMsg, pDetails);
    }

    public boolean isFineEnabled() {
        return isFinestEnabled();
    }

    public void fine(String pMethodName, String pMsg, Object[] pDetails) {
        finest(pMethodName, pMsg, pDetails);
    }

    public void fine(String pMethodName, String pMsg) {
        finest(pMethodName, pMsg);
    }

    public void fine(String pMethodName, String pMsg, Object pDetails) {
        finest(pMethodName, pMsg, pDetails);
    }

    public boolean isInfoEnabled() {
        return logger.isInfoEnabled();
    }

    public void info(String pMethodName, String pMsg, Object[] pDetails) {
        if (logger.isInfoEnabled()) {
            logger.info(format(pMethodName, pMsg, pDetails));
        }
    }

    public void info(String pMethodName, String pMsg) {
        if (logger.isInfoEnabled()) {
            logger.info(pMethodName + ": " + pMsg);
        }
    }

    public void info(String pMethodName, String pMsg, Object pDetails) {
        if (logger.isInfoEnabled()) {
            logger.info(pMethodName + ": " + pMsg + ", " + pDetails);
        }
    }

    public boolean isWarnEnabled() {
        return logger.isEnabledFor(Level.WARN);
    }

    public void warn(String pMethodName, String pMsg, Object[] pDetails) {
        if (logger.isEnabledFor(Level.WARN)) {
            logger.warn(format(pMethodName, pMsg, pDetails));
        }
    }

    public void warn(String pMethodName, String pMsg) {
        if (logger.isEnabledFor(Level.WARN)) {
            logger.warn(pMethodName + ": " + pMsg);
        }
    }

    public void warn(String pMethodName, String pMsg, Object pDetails) {
        if (logger.isEnabledFor(Level.WARN)) {
            logger.warn(pMethodName + ": " + pMsg + ", " + pDetails);
        }
    }

    public boolean isErrorEnabled() {
        return logger.isEnabledFor(Level.ERROR);
    }

    public void error(String pMethodName, String pMsg, Object[] pDetails) {
        if (logger.isEnabledFor(Level.ERROR)) {
            logger.error(format(pMethodName, pMsg, pDetails));
        }
    }

    public void error(String pMethodName, String pMsg) {
        if (logger.isEnabledFor(Level.ERROR)) {
            logger.error(pMethodName + ": " + pMsg);
        }
    }

    public void error(String pMethodName, String pMsg, Object pDetails) {
        if (logger.isEnabledFor(Level.ERROR)) {
            logger.error(pMethodName + ": " + pMsg + ", " + pDetails);
        }
    }
}
