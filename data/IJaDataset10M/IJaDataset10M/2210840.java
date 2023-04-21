package java.security.cert;

/**
 * The result of calling the {@link
 * CertPathBuilder#build(java.security.cert.CertPathParameters)} method
 * of PKIX {@link CertPathBuilder}s.
 *
 * @see CertPathBuilder
 * @see CertPathBuilderResult
 */
public class PKIXCertPathBuilderResult extends PKIXCertPathValidatorResult implements CertPathBuilderResult {

    /** The certificate path. */
    private CertPath certPath;

    /**
   * Creates a new PKIXCertPathBuilderResult.
   *
   * @param certPath         The certificate path.
   * @param trustAnchor      The trust anchor.
   * @param policyTree       The root node of the policy tree.
   * @param subjectPublicKey The public key.
   * @throws NullPointerException If <i>certPath</i>, <i>trustAnchor</i> or
   *         <i>subjectPublicKey</i> is null.
   */
    public PKIXCertPathBuilderResult(CertPath certPath, TrustAnchor trustAnchor, PolicyNode policyTree, java.security.PublicKey subjectPublicKey) {
        super(trustAnchor, policyTree, subjectPublicKey);
        if (certPath == null) throw new NullPointerException();
        this.certPath = certPath;
    }

    /**
   * Returns the certificate path that was built.
   *
   * @return The certificate path that was built.
   */
    public CertPath getCertPath() {
        return certPath;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer(super.toString());
        buf.insert(buf.length() - 2, "; CertPath=" + certPath);
        return buf.toString();
    }
}
