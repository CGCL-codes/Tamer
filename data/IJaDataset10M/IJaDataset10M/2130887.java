package net.java.sip.communicator.impl.protocol.sip;

import gov.nist.javax.sip.stack.*;
import java.util.*;
import javax.sip.*;
import javax.sip.address.*;
import javax.sip.header.*;
import javax.sip.message.*;
import net.java.sip.communicator.util.*;

/**
 * An implementation of the <tt>Router</tt> interface wrapping around JAIN-SIP
 * RI <tt>DefaultRouter</tt> in order to be able to change the outbound proxy
 * depending on the account which sent the request.
 * 
 * @author Sebastien Mazy
 */
public class ProxyRouter implements Router {

    /**
     * Logger for this class.
     */
    private static final Logger logger = Logger.getLogger(ProxyRouter.class);

    /**
     * The running JAIN-SIP stack.
     */
    private final SipStack stack;

    /**
     * Used to cache the <tt>DefaultRouter</tt>s. One <tt>DefaultRouter</tt> per
     * outbound proxy.
     */
    private final Map<String, Router> routerCache = new HashMap<String, Router>();

    /**
     * The jain-sip router to use for accounts that do not have a proxy or as a
     * default. Do not use this attribute directly but getDefaultRouter() (lazy
     * initialization)
     */
    private Router defaultRouter = null;

    /**
     * Simple constructor. Ignores the <tt>defaultRoute</tt> parameter.
     *
     * @param stack the currently running stack.
     * @param defaultRoute ignored parameter.
     */
    public ProxyRouter(SipStack stack, String defaultRoute) {
        if (stack == null) throw new IllegalArgumentException("stack shouldn't be null!");
        this.stack = stack;
    }

    /**
     * Returns the next hop for this <tt>Request</tt>.
     *
     * @param request <tt>Request</tt> to find the next hop.
     * @return the next hop for the <tt>request</tt>.
     */
    public Hop getNextHop(Request request) throws SipException {
        return this.getRouterFor(request).getNextHop(request);
    }

    /**
     * Returns the next hops for this <tt>Request</tt>.
     *
     * @param request <tt>Request</tt> to find the next hops.
     * @return the next hops for the <tt>request</tt>.
     */
    @Deprecated
    public ListIterator getNextHops(Request request) {
        return this.getRouterFor(request).getNextHops(request);
    }

    /**
     * Returns the outbound proxy for this <tt>Router</tt>.
     *
     * @return the outbound proxy for this <tt>Router</tt>.
     */
    public Hop getOutboundProxy() {
        logger.fatal("If you see this then please please describe your SIP " + "setup and send the following stack trace to" + "dev@sip-communicator.dev.java.net", new Exception());
        return null;
    }

    /**
     * Retrieves a DefaultRouter whose default route is the outbound proxy of
     * the account which sent the <tt>request</tt>, or a default one.
     *
     * @param request the <tt>Request</tt> which to retrieve a <tt>Router</tt>
     *            for.
     * @return a <tt>Router</tt> with the outbound proxy set for this
     *         <tt>request</tt> if needed, or a default router
     */
    private Router getRouterFor(Request request) {
        Object service = SipApplicationData.getApplicationData(request, SipApplicationData.KEY_SERVICE);
        if (service instanceof ProtocolProviderServiceSipImpl) {
            String proxy = ((ProtocolProviderServiceSipImpl) service).getOutboundProxyString();
            if (proxy == null) return this.getDefaultRouter();
            Router router = routerCache.get(proxy);
            if (router == null) {
                router = new DefaultRouter(stack, proxy);
                routerCache.put(proxy, router);
            }
            return router;
        }
        ToHeader to = (ToHeader) request.getHeader(ToHeader.NAME);
        if (to.getTag() == null) logger.error("unable to identify the service which created this " + "out-of-dialog request");
        return this.getDefaultRouter();
    }

    /**
     * Returns and create if needed a default router (no outbound proxy)
     *
     * @return a router with no outbound proxy set
     */
    private Router getDefaultRouter() {
        if (this.defaultRouter == null) this.defaultRouter = new DefaultRouter(stack, null);
        return this.defaultRouter;
    }
}
