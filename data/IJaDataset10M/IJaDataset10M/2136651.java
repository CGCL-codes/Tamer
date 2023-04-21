package org.mobicents.javax.servlet.sip.dns;

import javax.servlet.sip.SipURI;
import javax.servlet.sip.URI;

/**
 * Allows for an application to perform DNS queries to modify the SIP Message before it is sent out.<br/>
 * To get the DNSResolver from your application just use 
 * <pre>
 * DNSResolver dnsResolver = (DNSResolver) getServletContext().getAttribute("org.mobicents.servlet.sip.DNS_RESOLVER");
 * </pre>
 * 
 * @author jean.deruelle@gmail.com
 *
 */
public interface DNSResolver {

    /**
	 * <p>From the uri passed in parameter, try to find the corresponding SipURI.
	 * If the uri in parameter is already a SipURI without a user=phone param, it is just returned
	 * If the uri in parameter is a TelURL or SipURI with a user=phone param, the phone number is converted to a domain name
	 * then a corresponding NAPTR DNS lookup is done to find the SipURI</p>
	 * 
	 * <p> Usage Example </p>
	 * <pre>
	 * 	DNSResolver dnsResolver = (DNSResolver) getServletContext().getAttribute("org.mobicents.servlet.sip.DNS_RESOLVER");
	 * try {
	 *		URI uri = sipFactory.createURI("tel:+358-555-1234567");
	 *		SipURI sipURI = dnsResolver.getSipURI(uri);
	 * } catch (ServletParseException e) {
	 * 		logger.error("Impossible to create the tel URL", e);
	 * }
	 * </pre>
	 * 
	 * @param uri the uri used to find the corresponding SipURI
	 * @return the SipURI found through ENUM methods or the uri itself if the uri is already a SipURI without a user=phone param
	 */
    SipURI getSipURI(URI uri);
}
