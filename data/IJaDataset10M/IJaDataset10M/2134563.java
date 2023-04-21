package org.echarts.servlet.sip.utilities;

import java.util.ListIterator;
import javax.servlet.sip.Address;
import javax.servlet.sip.URI;
import javax.servlet.sip.SipServletRequest;

/**
 * Modifies a SipServletRequest before sending it out.
 */
public class RequestModifier extends MessageModifier {

    static final String rcsid = "$Name:  $ $Id: RequestModifier.java 1832 2011-10-04 17:53:12Z yotommy $";

    /**
	 * Provide a modified From address to use in outgoing SipServletRequest.
	 * Base class returns null (no change).
	 * @param req request on which to base modified From address
	 * @return desired From address, or null for no change
	 */
    public Address getModifiedFromAddress(SipServletRequest req) {
        return null;
    }

    /**
	 * Provide a modified To address to use in outgoing SipServletRequest.
	 * Base class returns null (no change).
	 * @param req request on which to base modified To address
	 * @return desired To address, or null for no change
	 */
    public Address getModifiedToAddress(SipServletRequest req) {
        return null;
    }

    /**
	 * Provide a modified Request-URI to use in outgoing SipServletRequest.
	 * Base class returns null (no change).
	 * @param req request on which to base modified Request-URI
	 * @return desired Request-URI, or null if no change
	 */
    public URI getModifiedRequestURI(SipServletRequest req) {
        return null;
    }

    /** Provide modified Route headers to use in outgoing SipServletRequest. Base class returns null (no change).
	 * @param req request to be modified
	 * @return desired Route headers, or null if no change
	 */
    public ListIterator getModifiedRoutes(SipServletRequest req) {
        return null;
    }
}
