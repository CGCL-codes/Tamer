package org.sulweb.infustore;

import java.util.HashMap;
import org.sulweb.infumon.common.Pump;

/**
 *
 * @author lucio
 */
public class PendingOfflineStates {

    private static PendingOfflineStates instance;

    /** Creates a new instance of PendingOfflineStates */
    private HashMap<Pump, Integer> states;

    private PendingOfflineStates() {
        states = new HashMap<Pump, Integer>();
    }

    public static PendingOfflineStates getInstance() {
        if (instance == null) instance = new PendingOfflineStates();
        return instance;
    }

    public synchronized int getPendingCycles(Pump p) {
        int result = 0;
        if (states.containsKey(p)) result = states.get(p).intValue();
        return result;
    }

    public synchronized void increasePendingCycles(Pump p) {
        int current = getPendingCycles(p);
        states.put(p, current + 1);
    }

    public synchronized void resetPendingCycles(Pump p) {
        states.remove(p);
    }
}
