package org.tripcom.security;

import static org.tripcom.security.util.Checks.checkNotNull;

/**
 * An attribute assertion.
 * <p>
 * This class models an assertion issued by an authority (or provider). The
 * authority is identified by means of the SHA1 digest of the X509v3 certificate
 * used to sign the assertion.
 * <p>
 * The assertion consists in an attribute name (a string) and an optional value
 * (also a string). An empty value is used when the assertion simply plays a
 * role of marker. Note that for SAML assertions, although multiple values are
 * supported the specification, we handle this case with multiple
 * <tt>Assertion</tt>, each one for a single attribute-value pair issued by the
 * provider; the rationale is that the class <tt>Assertion</tt> represents a
 * minimum unity of information issued by an authority, which can be entirely
 * trusted or distrusted by the kernel according to a policy. Finally, note that
 * both the issuer certificate digest and the attribute name properties are
 * required and cannot be null.
 * </p>
 * 
 * @author Francesco Corcoglioniti &lt;francesco.corcoglioniti@cefriel.it&gt;
 */
public final class Assertion {

    /** The digest of the certificate of the authority issuing this assertion. */
    private String issuerCertificateDigest;

    /** The digest of the certificate of the subject of this assertion. */
    private String subjectCertificateDigest;

    /** The name of the attribute. */
    private String attributeName;

    /** An optional value associated to the attribute. */
    private String attributeValue;

    /**
     * Create an attribute assertion from the specified issuer certificate
     * digest and attribute name. The value is empty.
     * 
     * @param issuerCertificateDigest the digest of the issuer certificate (not
     *            null).
     * @param subjectCertificateDigest the digest of the subject certificate
     *            (not null).
     * @param attributeName the name of the attribute (not null).
     */
    public Assertion(String issuerCertificateDigest, String subjectCertificateDigest, String attributeName) {
        this(issuerCertificateDigest, subjectCertificateDigest, attributeName, null);
    }

    /**
     * Create an attribute assertion from the specified issuer certificate
     * digest, attribute name and value.
     * 
     * @param issuerCertificateDigest the digest of the issuer certificate (not
     *            null).
     * @param subjectCertificateDigest the digest of the subject certificate
     *            (not null).
     * @param attributeName the name of the attribute (not null).
     * @param attributeValue an optional attribute value.
     */
    public Assertion(String issuerCertificateDigest, String subjectCertificateDigest, String attributeName, String attributeValue) {
        checkNotNull(issuerCertificateDigest, "Null issuerCertificateDigest");
        checkNotNull(subjectCertificateDigest, "Null subjectCertificateDigest");
        checkNotNull(attributeName, "Null attribute name");
        this.issuerCertificateDigest = issuerCertificateDigest;
        this.subjectCertificateDigest = subjectCertificateDigest;
        this.attributeName = attributeName;
        this.attributeValue = attributeValue;
    }

    /**
     * Return the digest of the issuer certificate. This method returns the
     * digest of the certificate used by the issuer to sign this assertion.
     * 
     * @return the attribute provider certificate digest (not null).
     */
    public String getIssuerCertificateDigest() {
        return issuerCertificateDigest;
    }

    /**
     * Return the digest of the subject certificate. This method returns the
     * digest of the subject certificate included in the assertion to identify
     * the subject.
     * 
     * @return the subject certificate digest (not null).
     */
    public String getSubjectCertificateDigest() {
        return subjectCertificateDigest;
    }

    /**
     * Return the name of the attribute.
     * 
     * @return the name of the attribute (not null).
     */
    public String getAttributeName() {
        return attributeName;
    }

    /**
     * Return the optional value of the attribute. The result can be null, in
     * the case the attribute is not associated to a value.
     * 
     * @return the optional value of the attribute.
     */
    public String getAttributeValue() {
        return attributeValue;
    }

    /**
     * {@inheritDoc} Check whether two attribute assertions are equals. Two
     * assertions are equal if they both refers to the same attribute issued by
     * the same issuer with the same certificate, and the value is the same.
     * Note that comparisons are case-sensitive.
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object arg) {
        if ((arg == null) || !(arg instanceof Assertion)) {
            return false;
        }
        Assertion assertion = (Assertion) arg;
        return attributeName.equals(assertion.attributeName) && (issuerCertificateDigest.equals(assertion.issuerCertificateDigest)) && (subjectCertificateDigest.equals(assertion.subjectCertificateDigest)) && (((attributeValue == null) && (assertion.attributeValue == null)) || attributeValue.equals(assertion.attributeValue));
    }

    /**
     * {@inheritDoc} Returns an hash code for this attribute assertion. The
     * returned hash code depends on the digest of the issuer certificate, name
     * and value.
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return issuerCertificateDigest.hashCode() ^ subjectCertificateDigest.hashCode() ^ attributeName.hashCode() ^ attributeValue.hashCode();
    }

    /**
     * {@inheritDoc} Returns a short string representation of the attribute
     * assertion. The returned string contains the digest of the issuer
     * certificate, the attribute name and the value in the format "[digest]
     * name = value".
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "[" + issuerCertificateDigest + "] " + subjectCertificateDigest + " " + attributeName + ((attributeValue == null) ? "" : " = " + attributeValue.toString());
    }
}
