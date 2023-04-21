package com.volantis.mcs.management.tracking.jmx;

import com.volantis.mcs.management.tracking.PageDetails;

/**
 * Interface that provides the Management bean interface for PageTracking.
 * @volantis-api-include-in PublicAPI
 */
public interface PageTrackerMBean {

    /**
     * Management operation that allows the queue of <code>PageDetails</code>
     * objects, which are being tracked, to be flushed.
     */
    public void flushPageDetails();

    /**
     * Returns an array of the <code>PageDetails</code> obejcts that have been
     * tracked.
     */
    public PageDetails[] retrievePageDetails();
}
