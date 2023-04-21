package org.dbe.composer.wfengine.bpel.impl.list;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.dbe.composer.wfengine.bpel.impl.queue.SdlMessageReceiver;

/**
 * Wraps a listing of queued message receivers.
 */
public class SdlMessageReceiverListResult implements Serializable {

    /** Total rows that matched selection criteria.  This number may be greater than the number of results in this listing. */
    protected int mTotalRows;

    /** The matching message receivers. */
    protected SdlMessageReceiver[] mResults;

    /** Mapping of process ids to location paths. */
    protected Map mLocationIdtoLocationPathMap;

    /**
     * Constructor.
     * @param aTotalRows Total rows that matched selection criteria.  This number may be greater than the number of results in this listing.
     * @param aReceivers The matching message receivers.
     */
    public SdlMessageReceiverListResult(int aTotalRows, List aReceivers) {
        mTotalRows = aTotalRows;
        mResults = (SdlMessageReceiver[]) aReceivers.toArray(new SdlMessageReceiver[aReceivers.size()]);
        mLocationIdtoLocationPathMap = new HashMap();
    }

    /**
     * Accessor for total rows.
     */
    public int getTotalRows() {
        return mTotalRows;
    }

    /**
     * Accessor for message receivers.
     */
    public SdlMessageReceiver[] getResults() {
        return mResults;
    }

    /**
     * Add a location id to location path mapping.
     * @param aLocationId The location path id.
     * @param aLocation The location xpath.
     */
    public void addPathMapping(int aLocationId, String aLocation) {
        mLocationIdtoLocationPathMap.put(new Integer(aLocationId), aLocation);
    }

    /**
     * Returns the matching location path for this process id.
     * @param aLocationPath
     */
    public String getLocationPath(int aLocationId) {
        return (String) mLocationIdtoLocationPathMap.get(new Integer(aLocationId));
    }

    /**
     * Returns true if there are no queued message receivers.
     */
    public boolean isEmpty() {
        return mResults == null || mResults.length == 0;
    }
}
