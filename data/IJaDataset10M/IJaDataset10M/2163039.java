package com.indigen.victor.core;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import com.indigen.victor.actions.VictorAction;

/**
 * A Progress object is used to hold progress indication for an operation that lasts long.
 * It is used in conjunction with a Mozilla UI component to display the progress
 * indication
 * @author mig
 *
 */
public class Progress extends LogEnabled {

    /**
	 * Opening timeout for the progress protocol
	 */
    static final long OPENING_TIMEOUT = 60000;

    /**
	 * Colsing timeout for the progress protocol
	 */
    static final long CLOSING_TIMEOUT = 60000;

    /**
	 * did the client opened the progress
	 */
    boolean clientOpened = false;

    /**
	 * did the client closed the progress
	 */
    boolean clientClosed = false;

    /**
	 * did the server opened the progress
	 */
    boolean serverOpened = false;

    /**
	 * did the server closed the progress
	 */
    boolean serverClosed = false;

    /**
	 * progress unique identifier
	 */
    String id;

    /**
	 * the maximum value for the progress indicator
	 */
    int max = 0;

    /**
	 * the current value for the progress indicator
	 */
    int current = 0;

    /**
	 * the time when this progress object has been created
	 */
    long creationTime;

    /**
	 * the time when this progress object has been first closed
	 */
    long closingTime;

    /**
	 * Object creation
	 * @param id the progress identifier
	 */
    Progress(String id) {
        this.id = id;
        creationTime = System.currentTimeMillis();
    }

    /**
	 * Return a Map containing the progress object values
	 * @return
	 */
    Map getStatus() {
        Map status = new Hashtable();
        status.put("id", id);
        status.put("closing", "" + serverClosed);
        status.put("maximum", "" + max);
        status.put("current", "" + current);
        return status;
    }

    /**
	 * Return the number of milliseconds since when this object has been created
	 * @return
	 */
    long getAge() {
        return System.currentTimeMillis() - creationTime;
    }

    /**
	 * Return the number of milliseconds since when this object has been first closed
	 * @return
	 */
    long getClosingTime() {
        return System.currentTimeMillis() - closingTime;
    }

    /**
	 * Close the progress indicator from the server operation
	 */
    public void close() {
        closingTime = System.currentTimeMillis();
        setServerClosed(true);
    }

    /**
	 * Called when the client request a close of the progress indicator
	 */
    void clientClose() {
        closingTime = System.currentTimeMillis();
        setClientClosed(true);
    }

    /**
	 * Remove progress objects whose timeouts expired 
	 * @param action
	 */
    static void purgeProgresses(VictorAction action) {
        List tobePurged = new Vector();
        Map indicators = getIndicators(action);
        Iterator i = indicators.keySet().iterator();
        while (i.hasNext()) {
            String id = (String) i.next();
            Progress progress = (Progress) indicators.get(id);
            if (progress.isServerOpened() == false && progress.getAge() > OPENING_TIMEOUT) progress.close();
            if (progress.isServerClosed() == true) {
                if (progress.isClientClosed() == false && progress.getClosingTime() > CLOSING_TIMEOUT) {
                    progress.setClientClosed(true);
                }
                if (progress.isClientClosed()) {
                    tobePurged.add(id);
                }
            }
        }
        i = tobePurged.iterator();
        while (i.hasNext()) {
            String id = (String) i.next();
            indicators.remove(id);
        }
    }

    /**
	 * Get a progress object given its identifier. If no object is found with this ID,
	 * a new one is created
	 * @param action the action the request takes place in
	 * @param id the Progress object identifier
	 * @return a progress object
	 */
    public static Progress getProgress(VictorAction action, String id) {
        synchronized (action.getLock()) {
            purgeProgresses(action);
            Map indicsMap = getIndicators(action);
            Progress progress = (Progress) indicsMap.get(id);
            if (progress == null) {
                progress = new Progress(id);
                indicsMap.put(id, progress);
            }
            progress.setServerOpened(true);
            return progress;
        }
    }

    /**
	 * Get all the existing progress objects
	 * @param action
	 * @return
	 */
    static Map getIndicators(VictorAction action) {
        Map indicsMap = (Map) action.getSession().getAttribute("progress-indicators");
        if (indicsMap == null) {
            indicsMap = new Hashtable();
            action.getSession().setAttribute("progress-indicators", indicsMap);
        }
        return indicsMap;
    }

    /**
	 * The client requested a progress opening
	 * @param action
	 * @param id
	 */
    public static void clientOpen(VictorAction action, String id) {
        synchronized (action.getLock()) {
            Map indicsMap = getIndicators(action);
            Progress progress = (Progress) indicsMap.get(id);
            if (progress == null) {
                progress = new Progress(id);
                indicsMap.put(id, progress);
            }
            progress.setClientOpened(true);
        }
    }

    /**
	 * Get a list of all progress object values
	 * @param action
	 * @return
	 */
    public static List getStatus(VictorAction action) {
        synchronized (action.getLock()) {
            List indics = new Vector();
            Map indicsMap = getIndicators(action);
            Iterator i = indicsMap.keySet().iterator();
            while (i.hasNext()) {
                String id = (String) i.next();
                Progress progress = (Progress) indicsMap.get(id);
                if (progress.isClientOpened() && !progress.isClientClosed()) {
                    indics.add(progress.getStatus());
                }
            }
            return indics;
        }
    }

    /**
	 * Client requested closure of a progress object
	 * @param action
	 * @param id
	 */
    public static void clientClose(VictorAction action, String id) {
        synchronized (action.getLock()) {
            Map indicsMap = getIndicators(action);
            Progress progress = (Progress) indicsMap.get(id);
            if (progress != null) {
                progress = new Progress(id);
                progress.setClientClosed(true);
            }
        }
    }

    /**
	 * Did client requested closure for this progress object
	 * @return
	 */
    public boolean isClientClosed() {
        return clientClosed;
    }

    /**
	 * Client request closure on this progress object
	 * @param clientClosed
	 */
    public void setClientClosed(boolean clientClosed) {
        this.clientClosed = clientClosed;
    }

    /**
	 * Has this progress been opened by client
	 * @return
	 */
    public boolean isClientOpened() {
        return clientOpened;
    }

    /**
	 * Set the client opened flag
	 * @param clientOpened
	 */
    public void setClientOpened(boolean clientOpened) {
        this.clientOpened = clientOpened;
    }

    /**
	 * Get current value for this progress indicator
	 * @return
	 */
    public int getCurrent() {
        return current;
    }

    /**
	 * Assign the current value for this indicator
	 * @param current
	 */
    public void setCurrent(int current) {
        this.current = current;
    }

    /**
	 * Get the maximum value for this progress indicator
	 * @return
	 */
    public int getMax() {
        return max;
    }

    /**
	 * Assign the maximum value for this progress indicator
	 * @param max
	 */
    public void setMax(int max) {
        this.max = max;
    }

    /**
	 * Did server requested closure of this progress object
	 * @return
	 */
    public boolean isServerClosed() {
        return serverClosed;
    }

    /**
	 * Server requested closure for this progress object
	 * @param serverClosed
	 */
    public void setServerClosed(boolean serverClosed) {
        this.serverClosed = serverClosed;
    }

    /**
	 * Did server opened this progress object
	 * @return
	 */
    public boolean isServerOpened() {
        return serverOpened;
    }

    /**
	 * Set the server opened flag
	 * @param serverOpened
	 */
    public void setServerOpened(boolean serverOpened) {
        this.serverOpened = serverOpened;
    }

    /**
	 * Get progress object identifier
	 * @return
	 */
    public String getId() {
        return id;
    }

    /**
	 * Increment the current value by one
	 *
	 */
    public void incrementCurrent() {
        incrementCurrent(1);
    }

    /**
	 * Increment the current value by the given integer
	 * @param incr
	 */
    public void incrementCurrent(int incr) {
        current += incr;
    }

    /**
	 * Increment maximum value by one
	 *
	 */
    public void incrementMax() {
        incrementMax(1);
    }

    /**
	 * Increment maximum value by given integer
	 * @param incr
	 */
    public void incrementMax(int incr) {
        max += incr;
    }

    /**
	 * Decrement current value by one
	 *
	 */
    public void decrementCurrent() {
        decrementCurrent(1);
    }

    /**
	 * Decrement current value by given integer
	 * @param decr
	 */
    public void decrementCurrent(int decr) {
        current -= decr;
    }

    /**
	 * Decrement maximum value by one
	 *
	 */
    public void decrementMax() {
        decrementMax(1);
    }

    /**
	 * Decrement maximum value by given integer
	 * @param decr
	 */
    public void decrementMax(int decr) {
        max -= decr;
    }
}
