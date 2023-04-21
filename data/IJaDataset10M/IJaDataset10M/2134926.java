package org.ccnx.ccn;

import org.ccnx.ccn.protocol.ContentObject;
import org.ccnx.ccn.protocol.Interest;

/**
 * A listener used to receive callbacks when data arrives matching one of our
 * asynchronously-expressed Interests (expressed with CCNBase#expressInterest(Interest, CCNInterestListener)).
 * Once the listener is called with matching data, the Interest is canceled. As a convenience,
 * the listener can return a new Interest, which will be expressed on its behalf, using
 * it as the callback listener when data is returned in response. This new Interest can be
 * the same as the previous Interest, derived from it, or completely unrelated. Since data
 * consumes Interest, there can only be a single response for one Interest expression.
 * 
 * @see CCNBase
 */
public interface CCNInterestListener {

    /**
	 * Callback called when we get new results for our query.
	 * @param data the ContentObject that matched our Interest
	 * @param interest Interest that was satisfied
	 * @return new Interest to be expressed
	 */
    public Interest handleContent(ContentObject data, Interest interest);
}
