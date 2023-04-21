package be.fedict.trust.constraints;

import java.security.cert.X509Certificate;
import javax.security.auth.x500.X500Principal;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import be.fedict.trust.CertificateConstraint;

/**
 * Distinguished Name Certificate Constraint implemenation.
 * 
 * @author Frank Cornelis
 * 
 */
public class DistinguishedNameCertificateConstraint implements CertificateConstraint {

    private static final Log LOG = LogFactory.getLog(DistinguishedNameCertificateConstraint.class);

    private final X500Principal acceptedSubject;

    public DistinguishedNameCertificateConstraint(String acceptedSubjectName) {
        this.acceptedSubject = new X500Principal(acceptedSubjectName);
    }

    public boolean check(X509Certificate certificate) {
        X500Principal certificateSubject = certificate.getSubjectX500Principal();
        LOG.debug("accepted subject: " + this.acceptedSubject);
        return this.acceptedSubject.equals(certificateSubject);
    }
}
