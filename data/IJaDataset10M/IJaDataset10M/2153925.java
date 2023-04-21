package org.rakiura.cpn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Represents basic timed simulator. Simulated time in this simulator
 * do not run continuously, it steps only when there is no enabled
 * transitions, and it skips to the next smallest token timestamp. All
 * operation and the state of timestamps should be performed via this
 * class methods.
 * 
 *<br><br>
 * TimedSimulator.java<br>
 * Created: Thu Sep 12 09:46:39 2002<br>
 *
 *@author  <a href="mariusz@rakiura.org">Mariusz Nowostawski</a>
 *@version $Revision: 1.8 $ $Date: 2008/10/31 23:43:11 $
 *@since 3.0
 */
public class TimedSimulator extends BasicSimulator {

    /** Timestamps holder. */
    private final Map timestamps = new HashMap();

    /** Timed tokens hash. */
    private final Map timedTokens = new HashMap();

    /** Simulated clock. */
    private long time = 0;

    public TimedSimulator(final Net aNet) {
        super(aNet);
    }

    /**
   * Sets the timestamp value for a given token.
   *@param aToken token
   *@param aTimestamp timestamp value
   */
    public void setTimestamp(final Object aToken, final long aTimestamp) {
        setTimestamp(aToken, new Long(aTimestamp));
    }

    /**
   * Sets the timestamp value for a given token. Currently the implementation 
   * allows the user to change the timestamp of a given timed token during
   * runtime - use it with care.
   *@param aToken token
   *@param aTimestamp timestamp value
   */
    public void setTimestamp(final Object aToken, final Long aTimestamp) {
        this.timestamps.put(aToken, aTimestamp);
    }

    /**
   * Sets the timestamp value for a given token 
   * based on the current clock and the given interval. The implementation 
   * of this method is equivalent to the call <pre>
   * setTimestamp(getTime() + anInterval) </pre>
   *@param aToken token
   *@param anInterval a time interval value
   */
    public void setRelativeTimestamp(final Object aToken, final long anInterval) {
        setTimestamp(aToken, this.time + anInterval);
    }

    /**
   * Returns the timestamp value for a given token.
   *@param aToken token
   *@return the timestamp value for a given token.
   */
    public long getTimestamp(final Object aToken) {
        final Long l = (Long) this.timestamps.get(aToken);
        if (l != null) return l.longValue();
        return 0;
    }

    /**
   * Returns the simulated time value.
   *@return simulated time value.
   */
    public long getTime() {
        return this.time;
    }

    public boolean step() {
        removeDisabledTokens();
        processNetStructure();
        addDisabledTokens();
        if (occurrence().size() > 0) {
            ((Transition) occurrence().get(0)).fire();
            notifyPlaces();
            incrementSimulatedClock();
            removeDisabledTokens();
            processNetStructure();
            addDisabledTokens();
            if (occurrence().size() > 0) return true;
        }
        while (occurrence().size() == 0 && this.timedTokens.size() > 0) {
            this.time++;
            incrementSimulatedClock();
            notifyPlaces();
            removeDisabledTokens();
            processNetStructure();
            addDisabledTokens();
        }
        return (occurrence().size() > 0);
    }

    /**
   * Notify all the places about timed tokens changing their locations (places). 
   */
    private void notifyPlaces() {
        final Iterator iter = this.timedTokens.keySet().iterator();
        while (iter.hasNext()) {
            final Object token = iter.next();
            final List list = new ArrayList();
            list.add(token);
            final Place place = (Place) this.timedTokens.get(token);
            place.fireTokensAddedEvent(list);
        }
    }

    private void incrementSimulatedClock() {
        final long newTime = findNextTimestamp();
        if (newTime > this.time) this.time = newTime;
        final Iterator iter = this.timestamps.keySet().iterator();
        while (iter.hasNext()) {
            final Object token = iter.next();
            final long t = ((Long) this.timestamps.get(token)).longValue();
            if (t < this.time) this.timedTokens.remove(token);
        }
    }

    private long findNextTimestamp() {
        long result = this.time;
        final Iterator iter = this.timestamps.keySet().iterator();
        if (iter.hasNext()) {
            final Object token = iter.next();
            result = ((Long) this.timestamps.get(token)).longValue();
        }
        while (iter.hasNext()) {
            final Object token = iter.next();
            final long t = ((Long) this.timestamps.get(token)).longValue();
            if (t > this.time && t < result) result = t;
        }
        return result;
    }

    /**
   * Special utility method. Adds all previously removed disabled
   * timed tokens. Note, this method to work properly must be
   * proceeded by {@link #removeDisabledTokens}. This method is to be
   * used by users manually testing transition enablement. Use it with
   * care.
   */
    public void addDisabledTokens() {
        final Iterator iter = this.timedTokens.keySet().iterator();
        while (iter.hasNext()) {
            final Object token = iter.next();
            final Place place = (Place) this.timedTokens.get(token);
            place.addTokenQuietly(token);
        }
    }

    /**
   * Special utility method. Adds all previously removed disabled
   * timed tokens. Note, this method to work properly must be
   * followed by {@link #addDisabledTokens}, otherwise all the 
   * disabled tokens will not be present in any of the net
   * Places. This method is to be used by users manually testing
   * transition enablement.  Use it with care.
   */
    public void removeDisabledTokens() {
        buildTimedTokens();
        final Iterator iter = this.timedTokens.keySet().iterator();
        while (iter.hasNext()) {
            final Object token = iter.next();
            final Place place = (Place) this.timedTokens.get(token);
            place.removeTokenQuietly(token);
        }
    }

    private void buildTimedTokens() {
        this.timedTokens.clear();
        final Place[] p = net().getAllPlaces();
        for (int i = 0; i < p.length; i++) {
            final Iterator t = p[i].getTokens().iterator();
            while (t.hasNext()) {
                final Object token = t.next();
                if (this.timestamps.get(token) != null) {
                    final long timestamp = ((Long) this.timestamps.get(token)).longValue();
                    if (timestamp > this.time) {
                        this.timedTokens.put(token, p[i]);
                    }
                }
            }
        }
    }
}
