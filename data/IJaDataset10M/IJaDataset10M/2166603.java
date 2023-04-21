package com.aelitis.azureus.core.speedmanager.impl.v2;

import com.aelitis.azureus.core.util.average.AverageFactory;
import com.aelitis.azureus.core.util.average.Average;
import com.aelitis.azureus.core.speedmanager.SpeedManagerPingSource;

/**
 * Keeps the ping time stats for a single source. Should calculate averages for the data.
 *
 */
public class PingSourceStats {

    final SpeedManagerPingSource source;

    double currPing;

    Average shortTerm = AverageFactory.MovingImmediateAverage(3);

    Average medTerm = AverageFactory.MovingImmediateAverage(6);

    Average longTerm = AverageFactory.MovingImmediateAverage(10);

    Average forChecks = AverageFactory.MovingImmediateAverage(100);

    public PingSourceStats(SpeedManagerPingSource _source) {
        source = _source;
    }

    public void madeChange() {
    }

    public void addPingTime(int ping) {
        currPing = (double) ping;
        shortTerm.update((double) ping);
        medTerm.update((double) ping);
        longTerm.update((double) ping);
    }

    /**
     * Speculative method to see if it can determine a trend. The larger the number
     * the stronger the trend.
     * @return current - interger. A positive number is an increasing trend. A negative number is a decreasing trend.
     */
    public int getTrend() {
        int retVal = 0;
        if (currPing < 0.0) {
            retVal--;
        } else {
            if (currPing < shortTerm.getAverage()) {
                retVal++;
            } else {
                retVal--;
            }
            if (currPing < medTerm.getAverage()) {
                retVal++;
            } else {
                retVal--;
            }
            if (currPing < longTerm.getAverage()) {
                retVal++;
            } else {
                retVal--;
            }
        }
        if (shortTerm.getAverage() < medTerm.getAverage()) {
            retVal++;
        } else {
            retVal--;
        }
        if (shortTerm.getAverage() < longTerm.getAverage()) {
            retVal++;
        } else {
            retVal--;
        }
        if (medTerm.getAverage() < longTerm.getAverage()) {
            retVal++;
        } else {
            retVal--;
        }
        final int ABSOLUTE_GOOD_PING_VALUE = 30;
        if (currPing < ABSOLUTE_GOOD_PING_VALUE) {
            retVal++;
        }
        if (shortTerm.getAverage() < ABSOLUTE_GOOD_PING_VALUE) {
            retVal++;
        }
        if (medTerm.getAverage() < ABSOLUTE_GOOD_PING_VALUE) {
            retVal++;
        }
        if (longTerm.getAverage() < ABSOLUTE_GOOD_PING_VALUE) {
            retVal++;
        }
        final int ABSOLUTE_BAD_PING_VALUE = 300;
        if (currPing > ABSOLUTE_BAD_PING_VALUE) {
            retVal--;
        }
        if (shortTerm.getAverage() > ABSOLUTE_BAD_PING_VALUE) {
            retVal--;
        }
        if (medTerm.getAverage() > ABSOLUTE_BAD_PING_VALUE) {
            retVal--;
        }
        if (longTerm.getAverage() > ABSOLUTE_BAD_PING_VALUE) {
            retVal--;
        }
        return retVal;
    }

    /**
     * Get the long-term average.
     * @return Average - longTerm
     */
    public Average getLongTermAve() {
        return longTerm;
    }

    /**
     * Get the average that should be used for checking ping times.
     * @return - ping time of history.
     */
    public Average getHistory() {
        return forChecks;
    }
}
