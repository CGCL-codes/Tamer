package org.punkgrok.flightrecorder.mock;

import org.apache.commons.logging.Log;

/**
 * User: Roman Florian Zabicki III
 * Date: May 25, 2009
 * Time: 11:50:13 PM
 */
public class MockApacheCommonsLog implements Log {

    private String toString;

    public MockApacheCommonsLog() {
        toString = "";
    }

    public boolean isDebugEnabled() {
        return true;
    }

    public boolean isErrorEnabled() {
        return true;
    }

    public boolean isFatalEnabled() {
        return true;
    }

    public boolean isInfoEnabled() {
        return true;
    }

    public boolean isTraceEnabled() {
        return true;
    }

    public boolean isWarnEnabled() {
        return true;
    }

    public void trace(Object o) {
    }

    public void trace(Object o, Throwable throwable) {
    }

    public void debug(Object o) {
        toString += o;
    }

    public void debug(Object o, Throwable throwable) {
        toString += o;
        toString += throwable;
    }

    public void info(Object o) {
        toString += o;
    }

    public void info(Object o, Throwable throwable) {
        toString += o;
    }

    public void warn(Object o) {
        toString += o;
    }

    public void warn(Object o, Throwable throwable) {
        toString += o;
        toString += throwable;
    }

    public void error(Object o) {
        toString += o;
    }

    public void error(Object o, Throwable throwable) {
        toString += o;
        toString += throwable;
    }

    public void fatal(Object o) {
        toString += o;
    }

    public void fatal(Object o, Throwable throwable) {
        toString += o;
        toString += throwable;
    }

    public String toString() {
        return toString;
    }
}
