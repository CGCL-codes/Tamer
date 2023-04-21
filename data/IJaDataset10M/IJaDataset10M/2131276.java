package org.jboss.metadata.sip.spec;

import java.util.Collections;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.jboss.metadata.javaee.spec.EmptyMetaData;
import org.jboss.metadata.javaee.support.IdMetaDataImpl;
import org.jboss.metadata.web.spec.AuthConstraintMetaData;
import org.jboss.metadata.web.spec.TransportGuaranteeType;
import org.jboss.metadata.web.spec.UserDataConstraintMetaData;

/**
 * The sip app security-constraints
 * 
 * @author jean.deruelle@gmail.com
 * @version $Revision$
 */
@XmlType(name = "security-constraintType", propOrder = { "displayName", "resourceCollections", "proxyAuthentication", "authConstraint", "userDataConstraint" })
public class SipSecurityConstraintMetaData extends IdMetaDataImpl {

    private static final long serialVersionUID = 1;

    private String displayName;

    private SipResourceCollectionsMetaData resourceCollections;

    private EmptyMetaData proxyAuthentication;

    private AuthConstraintMetaData authConstraint;

    private UserDataConstraintMetaData userDataConstraint;

    public AuthConstraintMetaData getAuthConstraint() {
        return authConstraint;
    }

    public void setAuthConstraint(AuthConstraintMetaData authConstraint) {
        this.authConstraint = authConstraint;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public SipResourceCollectionsMetaData getResourceCollections() {
        return resourceCollections;
    }

    @XmlElement(name = "resource-collection")
    public void setResourceCollections(SipResourceCollectionsMetaData resourceCollections) {
        this.resourceCollections = resourceCollections;
    }

    public UserDataConstraintMetaData getUserDataConstraint() {
        return userDataConstraint;
    }

    public void setUserDataConstraint(UserDataConstraintMetaData userDataConstraint) {
        this.userDataConstraint = userDataConstraint;
    }

    public boolean isUnchecked() {
        return authConstraint == null;
    }

    public boolean isExcluded() {
        boolean isExcluded = authConstraint != null && authConstraint.getRoleNames() == null;
        return isExcluded;
    }

    public List<String> getRoleNames() {
        List<String> roleNames = Collections.emptyList();
        if (authConstraint != null && authConstraint.getRoleNames() != null) roleNames = authConstraint.getRoleNames();
        return roleNames;
    }

    public TransportGuaranteeType getTransportGuarantee() {
        TransportGuaranteeType type = TransportGuaranteeType.NONE;
        if (userDataConstraint != null) type = userDataConstraint.getTransportGuarantee();
        return type;
    }

    /**
	 * @param proxyAuthentication the proxyAuthentication to set
	 */
    public void setProxyAuthentication(EmptyMetaData proxyAuthentication) {
        this.proxyAuthentication = proxyAuthentication;
    }

    public EmptyMetaData getProxyAuthentication() {
        return proxyAuthentication;
    }
}
