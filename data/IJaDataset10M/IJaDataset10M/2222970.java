package org.echarts.servlet.sip.features.rerouteUponFailure;

import javax.servlet.sip.SipServletRequest;
import javax.servlet.sip.URI;

/** Specifies the alternate address for routing upon failure.
 */
public interface RerouteUponFailureFSMToJava {

    /** 
	 * Returns the desired alternate address for routing upon failure.
	 * @param initialRequest the incoming request that created this instance
	 * @return URI for alternate routing
	 */
    public URI getAlternateAddress(SipServletRequest initialRequest);
}
