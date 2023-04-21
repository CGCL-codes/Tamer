package org.openuss.security.ldap;

/**
 * @see org.openuss.security.ldap.AttributeMapping
 */
public class AttributeMappingImpl extends org.openuss.security.ldap.AttributeMappingBase implements org.openuss.security.ldap.AttributeMapping {

    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = -2092439924857081239L;

    public void addAuthenticationDomain(org.openuss.security.ldap.AuthenticationDomain authDomain) {
        if (authDomain != null) {
            getAuthenticationDomains().add(authDomain);
        }
    }

    public void removeAuthenticationDomain(org.openuss.security.ldap.AuthenticationDomain authDomain) {
        if (authDomain != null) {
            getAuthenticationDomains().remove(authDomain);
        }
    }
}
