package com.bitgate.util.db;

import java.util.HashMap;
import com.bitgate.util.debug.Debug;

/**
 * This class is used to keep a list of the database pools that have permanently failed to connect to their destinations.
 * This is used so that pool connections are not attempted to re-connect when they have already tried several times, slowing
 * down the system's performance.  Instead of continuously trying to reconnect to a pool, this caching mechanism serves as
 * a purpose for keeping track of those pools which have already failed to connect.  This class uses a default instance,
 * which is used for system-wide caching.
 *
 * @author Kenji Hollis &lt;kenji@nuklees.com&gt;
 * @version $Id: //depot/nuklees/util/db/PoolTable.java#5 $
 */
public class PoolTable {

    private static final PoolTable _default = new PoolTable();

    private HashMap<String, String> failedPools;

    /**
     * Constructor.
     */
    public PoolTable() {
        failedPools = new HashMap<String, String>();
    }

    /**
     * Returns the default instance of this class.
     *
     * @return <code>static PoolTable</code> object.
     */
    public static PoolTable getDefault() {
        return _default;
    }

    /**
     * Clears out all failed pools from the list.
     */
    public void restart() {
        Debug.inform("Removed " + failedPools.size() + " failed pool(s).");
        failedPools.clear();
    }

    /**
     * Adds a failed pool name to the list.
     *
     * @param name The pool name to add.
     */
    public void addFailure(String name) {
        failedPools.put(name.toLowerCase(), "1");
    }

    /**
     * Returns whether or not a failure has occurred with a pool.
     *
     * @param name The pool name to look up.
     * @return <code>true</code> if seen in the list, <code>false</code> otherwise.
     */
    public boolean getFailure(String name) {
        if (failedPools.get(name.toLowerCase()) != null) {
            return true;
        }
        return false;
    }
}
