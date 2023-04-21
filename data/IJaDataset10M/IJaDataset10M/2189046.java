package org.jostraca.util;

/** Accepts messages for display to user and ignores them.
 */
public class NullUserMessageHandler implements UserMessageHandler {

    public void setThreshold(int pThreshold) {
    }

    public int getThreshold() {
        return DEBUG;
    }

    public String getThresholdAsString() {
        return NAME_THRESHOLD[DEBUG];
    }

    public void add(int pType, String pMsg) {
    }

    public void add(int pType, String pPrefix, String pMsg) {
    }

    public void debug(String pMsg) {
    }

    public void info(String pMsg) {
    }

    public void warn(String pMsg) {
    }

    public void error(String pMsg) {
    }

    public void fatal(String pMsg) {
    }

    public void debug(String pMsg, Throwable pT) {
    }

    public void info(String pMsg, Throwable pT) {
    }

    public void warn(String pMsg, Throwable pT) {
    }

    public void error(String pMsg, Throwable pT) {
    }

    public void fatal(String pMsg, Throwable pT) {
    }

    public void debug(Throwable pT) {
    }

    public void info(Throwable pT) {
    }

    public void warn(Throwable pT) {
    }

    public void error(Throwable pT) {
    }

    public void fatal(Throwable pT) {
    }

    public void debug(String pPrefix, String pMsg) {
    }

    public void info(String pPrefix, String pMsg) {
    }

    public void warn(String pPrefix, String pMsg) {
    }

    public void error(String pPrefix, String pMsg) {
    }

    public void fatal(String pPrefix, String pMsg) {
    }
}
