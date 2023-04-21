package be.fedict.eid.idp.spi;

import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Set;

/**
 * The incoming request. This is used to specify the wanted authentication flow
 * and optionally authenticate the RP.
 * 
 * @author Wim Vandenhaute
 */
public class IncomingRequest {

    private final IdentityProviderFlow idpFlow;

    private final String spDomain;

    private final X509Certificate spCertificate;

    private final List<String> languages;

    private final Set<String> requiredAttributes;

    /**
	 * Main constructor
	 * 
	 * @param idpFlow
	 *            authentication flow
	 * @param spDomain
	 *            optional SP domain, <code>null</code> if empty
	 * @param spCertificate
	 *            optional SP certificate, <code>null</code> if empty
	 * @param languages
	 *            list of preferred languages if any, <code>null</code> or empty
	 *            list if none.
	 * @param requiredAttributes
	 *            optional list of required attributes to be returned in the
	 *            protocol specific response.
	 */
    public IncomingRequest(IdentityProviderFlow idpFlow, String spDomain, X509Certificate spCertificate, List<String> languages, Set<String> requiredAttributes) {
        this.idpFlow = idpFlow;
        this.spDomain = spDomain;
        this.spCertificate = spCertificate;
        this.languages = languages;
        this.requiredAttributes = requiredAttributes;
    }

    public IdentityProviderFlow getIdpFlow() {
        return idpFlow;
    }

    public String getSpDomain() {
        return spDomain;
    }

    public X509Certificate getSpCertificate() {
        return spCertificate;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public Set<String> getRequiredAttributes() {
        return requiredAttributes;
    }
}
