package org.echarts.servlet.sip.appRouter.DFCRouterImpl;

import javax.servlet.sip.*;
import java.util.logging.Logger;

/**
 * This class represents an originating address as derived from a SipServletRequest. For
 * the DFC router implementation, the originating address consists of the From header value
 * in the SIP request message. Thus, the address includes the display-name if present, the URI, and all
 * the parameters.
 */
public class OriginatingAddress implements org.echarts.servlet.sip.appRouter.DFCRouterImpl.Address {

    private static Logger logger;

    static {
        logger = Logger.getLogger("org.echarts.servlet.sip.appRouter.DFCRouterImpl");
    }

    private static final long serialVersionUID = 1289372944879749238L;

    private javax.servlet.sip.Address sipServletAddress;

    public OriginatingAddress(SipServletRequest r) {
        sipServletAddress = r.getFrom();
        logger.fine("Build new originating address: <" + sipServletAddress.toString() + ">");
    }

    /**
	 * Return a String that represents a version of the originating address that can be compared
	 * with other originating addresses to determine a match. Currently, this method removes
	 * the "tag=..." and "transport=..." parameters from the URI. A useful enhancement would be to
	 * have some properties defined somewhere that would dictate what other URI parameters, if any,
	 * that should similarly be excluded from consideration when comparing two originating addresses.
	 */
    public String toCompareString() {
        String cookedFromHeaderValue = "";
        String[] parameters = this.toString().split(";");
        cookedFromHeaderValue += parameters[0];
        for (int i = 1; i < parameters.length; i++) {
            String requestURIParam = parameters[i];
            String tokenValue[] = requestURIParam.split("=", 2);
            if (tokenValue[0].equals("transport")) continue;
            if (tokenValue[0].equals("tag")) continue;
            cookedFromHeaderValue += ";" + parameters[i];
        }
        return cookedFromHeaderValue;
    }

    /**
	 * Returns a string representation of the originating address
	 */
    public String toString() {
        return sipServletAddress.toString();
    }

    /**
	 * Determines whether this originating address differs from the string representation of
	 * another originating address passed as a parameter.
	 *
	 * @param oldAddressName    The string representation of an originating address that this address
	 *                          to be compared to
	 * @return                  <code>true</code> if this originating address has changed (is different
	 *                          from) the old address, otherwise <code>false</code>
	 */
    public boolean hasChanged(String oldAddressName) {
        return (!this.toCompareString().equals(oldAddressName));
    }

    /**
	 * Return the subscriber URI associated with this originating address.
	 */
    public URI getSubscriberURI() {
        return sipServletAddress.getURI();
    }
}
