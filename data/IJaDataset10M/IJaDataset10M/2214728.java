package xades4j.properties;

import java.security.cert.X509Certificate;
import java.util.Collection;

/**
 * The {@code CompleteCertificateRefs} property which contains the sequence of
 * references to the full set of CA certificates that have been used to validate
 * the electronic signature up to (but not including) the signer's certificate.
 * <p>
 * This is an optional unsigned property that qualifies the signature. There is
 * at most one occurence of this property in the signature.
 * <p>
 * This property is not used directly when producing a signature. Instead, it is
 * enforced by the {@link xades4j.production.XadesSigner} producing a XAdES-C.
 * @author Luís
 */
public final class CompleteCertificateRefsProperty extends UnsignedSignatureProperty {

    public static final String PROP_NAME = "CompleteCertificateRefs";

    private final Collection<X509Certificate> certificates;

    /**
     * Creates an instance of the property that will result in certificate references
     * for {@code certificates}.
     * @param certificates the set of certificates that will have references on the property
     * @throws NullPointerException if {@code certificates} is {@code null}
     */
    public CompleteCertificateRefsProperty(Collection<X509Certificate> certificates) {
        if (null == certificates) throw new NullPointerException();
        this.certificates = certificates;
    }

    public Collection<X509Certificate> getCertificates() {
        return certificates;
    }

    @Override
    public String getName() {
        return PROP_NAME;
    }
}
