package be.fedict.eid.applet.service.signer.facets;

import java.security.cert.X509Certificate;
import java.util.List;

/**
 * Interface for a service that retrieves revocation data about some given
 * certificate chain.
 * 
 * @author Frank Cornelis
 * 
 */
public interface RevocationDataService {

    /**
	 * Gives back the revocation data corresponding with the given certificate
	 * chain.
	 * 
	 * @param certificateChain
	 * @return
	 */
    RevocationData getRevocationData(List<X509Certificate> certificateChain);
}
