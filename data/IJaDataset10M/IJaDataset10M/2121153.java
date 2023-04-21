package ch.iserver.ace.net.discovery.dnssd;

import org.apache.log4j.Logger;
import com.apple.dnssd.DNSRecord;
import com.apple.dnssd.DNSSDException;
import com.apple.dnssd.DNSSDRegistration;

/**
 * <code>DNSSDCall</code> implementation for a DNSSD TXT update call. This call updates the
 * TXT record contents for the local user. Other users browsing in the local area network will
 * receive a notification of the updated TXT record. 
 * The <code>TXTUpdate</code> call is currently used for user name changes only.
 * 
 * <p>Note: executing <code>TXTUpdate</code> many times may result in inconsistent TXT record
 * contents at the peer users because the mDNSResponder service uses a cache which will not update
 * very frequently and the service also implements a DOS like security-algorithm for TXT update flooding.</p>
 * 
 * @see ch.iserver.ace.net.discovery.dnssd.DNSSDCall
 */
public class TXTUpdate extends DNSSDCall {

    private Logger LOG = Logger.getLogger(TXTUpdate.class);

    /**
	 * The DNSSDRegistration reference
	 */
    private DNSSDRegistration registration;

    /**
	 * The raw TXT record data
	 */
    private byte[] rawTXT;

    /**
	 * Creates a new TXTUpdate DNSSD call.
	 * 
	 * @param registration	the registration object to get the TXT record reference from
	 * @param rawTXT			the raw TXT record data 
	 */
    public TXTUpdate(DNSSDRegistration registration, byte[] rawTXT) {
        this.registration = registration;
        this.rawTXT = rawTXT;
    }

    /**
	 * @see ch.iserver.ace.net.discovery.dnssd.DNSSDCall#makeCall()
	 */
    protected Object makeCall() throws DNSSDCallException {
        try {
            DNSRecord record = registration.getTXTRecord();
            record.update(0, rawTXT, 0);
            return null;
        } catch (DNSSDException de) {
            throw new DNSSDCallException(de);
        }
    }

    /**
	 * {@inheritDoc}
	 */
    protected Logger getLogger() {
        return LOG;
    }
}
