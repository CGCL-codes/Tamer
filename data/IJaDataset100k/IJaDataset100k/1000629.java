package gov.nist.siplite.address;

import gov.nist.siplite.message.*;
import gov.nist.siplite.*;
import java.util.*;

/**
 * Path for outbound message routing.
 */
public interface Router {

    /**
     * Return a linked list of addresses corresponding to a requestURI.
     * This is called for sending out outbound messages for which we do
     * not directly have the request URI. The implementaion function
     * is expected to return a linked list of addresses to which the
     * request is forwarded. The implementation may use this method
     * to perform location searches etc.
     *
     * @param sipRequest is the message to route.
     * @param isDialog target URI is taken from route list inside of dialog,
     * else it is taken from request URI
     * @return enumeration of next hops
     */
    public Enumeration getNextHops(Request sipRequest, boolean isDialog);

    /**
     * Sets the outbound proxy.
     * @param outboundProxy the outbound proxy address
     */
    public void setOutboundProxy(String outboundProxy);

    /**
     * Sets the sip stack.
     * @param sipStack the SIP stack context
     */
    public void setSipStack(SipStack sipStack);

    /**
     * Gets the outbound proxy.
     * @return the outbounc proxy address
     */
    public Hop getOutboundProxy();
}
