package com.cube42.util.logging;

/**
 * Basic LogEntryFilter that lets all types of entries pass
 *
 * @author Matt Paulin
 * @version $id$
 */
public class AllPassFilter implements LogEntryFilter {

    /**
     * Allows network traces to be collected
     *
     * @return  True will allow network traces to be collected
     */
    public boolean allowNetTrace() {
        return true;
    }

    /**
     * Allows creation of all fatal log entries
     *
     * @return  True
     */
    public boolean allowFatal() {
        return true;
    }

    /**
     * Allows creation of all error log entries
     *
     * @return  True
     */
    public boolean allowError() {
        return true;
    }

    /**
     * Allows creation of all warning log entries
     *
     * @return  True
     */
    public boolean allowWarning() {
        return true;
    }

    /**
     * Allows creation of all info log entries
     *
     * @return  True
     */
    public boolean allowInfo() {
        return true;
    }

    /**
     * Allows creation of all data log entries
     *
     * @return  True
     */
    public boolean allowDebug() {
        return true;
    }
}
