package org.mobicents.ext.javax.sip.dns;

import javax.sip.SipException;
import javax.sip.SipStack;
import javax.sip.address.Hop;
import javax.sip.address.SipURI;
import javax.sip.address.URI;
import javax.sip.header.RouteHeader;
import javax.sip.message.Request;
import gov.nist.core.CommonLogger;
import gov.nist.core.InternalErrorHandler;
import gov.nist.core.LogWriter;
import gov.nist.core.StackLogger;
import gov.nist.javax.sip.header.RequestLine;
import gov.nist.javax.sip.header.Route;
import gov.nist.javax.sip.header.RouteList;
import gov.nist.javax.sip.message.SIPRequest;
import gov.nist.javax.sip.stack.DefaultRouter;

/**
 * This custom router is the same as the DefaultRouter from jain sip except that it remove the first route if it contains
 * DNS_ROUTE param in the Route' SIP URI
 * 
 * @author jean.deruelle@gmail.com
 *
 */
public class DNSAwareRouter extends DefaultRouter {

    public static final String DNS_ROUTE = "dns_route";

    private static StackLogger logger = CommonLogger.getLogger(DNSAwareRouter.class);

    public DNSAwareRouter(SipStack sipStack, String defaultRoute) {
        super(sipStack, defaultRoute);
    }

    @Override
    public Hop getNextHop(Request request) throws SipException {
        SIPRequest sipRequest = (SIPRequest) request;
        RequestLine requestLine = sipRequest.getRequestLine();
        if (requestLine == null) {
            return getOutboundProxy();
        }
        javax.sip.address.URI requestURI = requestLine.getUri();
        if (requestURI == null) throw new IllegalArgumentException("Bad message: Null requestURI");
        RouteList routes = sipRequest.getRouteHeaders();
        if (routes != null) {
            Route route = (Route) routes.getFirst();
            URI uri = route.getAddress().getURI();
            if (uri.isSipURI()) {
                SipURI sipUri = (SipURI) uri;
                if (sipUri.getParameter(DNS_ROUTE) != null) {
                    if (logger.isLoggingEnabled(LogWriter.TRACE_DEBUG)) logger.logDebug("Removing Route added by container to conform to RFC 3263 " + route);
                    sipRequest.removeFirst(RouteHeader.NAME);
                }
                if (!sipUri.hasLrParam()) {
                    fixStrictRouting(sipRequest);
                    if (logger.isLoggingEnabled(LogWriter.TRACE_DEBUG)) logger.logDebug("Route post processing fixed strict routing");
                }
                Hop hop = createHop(sipUri, request);
                if (logger.isLoggingEnabled(LogWriter.TRACE_DEBUG)) logger.logDebug("NextHop based on Route:" + hop);
                return hop;
            } else {
                throw new SipException("First Route not a SIP URI");
            }
        } else if (requestURI.isSipURI() && ((SipURI) requestURI).getMAddrParam() != null) {
            Hop hop = createHop((SipURI) requestURI, request);
            if (logger.isLoggingEnabled(LogWriter.TRACE_DEBUG)) logger.logDebug("Using request URI maddr to route the request = " + hop.toString());
            return hop;
        } else if (getOutboundProxy() != null) {
            if (logger.isLoggingEnabled(LogWriter.TRACE_DEBUG)) logger.logDebug("Using outbound proxy to route the request = " + getOutboundProxy().toString());
            return getOutboundProxy();
        } else if (requestURI.isSipURI()) {
            Hop hop = createHop((SipURI) requestURI, request);
            if (hop != null && logger.isLoggingEnabled(LogWriter.TRACE_DEBUG)) logger.logDebug("Used request-URI for nextHop = " + hop.toString()); else if (logger.isLoggingEnabled(LogWriter.TRACE_DEBUG)) {
                logger.logDebug("returning null hop -- loop detected");
            }
            return hop;
        } else {
            InternalErrorHandler.handleException("Unexpected non-sip URI", this.logger);
            return null;
        }
    }
}
