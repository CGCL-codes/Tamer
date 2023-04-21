package java.security;

/**
 * Represents the Service Provider Interface (SPI) for java.security.Policy
 * class.
 * 
 * If there is any class that wants to provide a Policy implementation, all
 * abstract methods in this SPI should be implemented.
 * 
 * The detailed implementations should offer a public constructor, in which a
 * Policy.Paramters implementation acts as an input parameter.If the
 * Policy.Paramters input cannot by understood by the constructor, an
 * IllegalArgumentException will be thrown.
 * 
 * @since 1.6
 */
public abstract class PolicySpi {

    public PolicySpi() {
    }

    /**
	 * Answers if the policy has granted a Permission to a
	 * ProtectionDomain.
	 * 
	 * @param domain -
	 *            the domain to check.
	 * @param permission -
	 *            check whether this permission is granted to the specified
	 *            domain.
	 * @return - true if the permission is granted to the domain.
	 * 
	 */
    protected abstract boolean engineImplies(ProtectionDomain domain, Permission permission);

    /**
	 * Refreshes/reloads the policy configuration. The behavior of this method
	 * depends on the implementation. For example, calling refresh on a
	 * file-based policy will cause the file to be re-read.
	 * 
	 * The default implementation of this method does nothing. This method
	 * should be overridden if a refresh operation is supported by the policy
	 * implementation.
	 * 
	 */
    protected void engineRefresh() {
    }

    /**
	 * Answers a PermissionCollection object containing the set of permissions
	 * granted to the specified CodeSource.
	 * 
	 * The default implementation of this method returns
	 * Policy.UNSUPPORTED_EMPTY_COLLECTION object. This method can be overridden
	 * if the policy implementation can return a set of permissions granted to a
	 * CodeSource.
	 * 
	 * @param codesource -
	 *            the CodeSource to which the returned PermissionCollection has
	 *            been granted.
	 * @return a set of permissions granted to the specified CodeSource. If this
	 *         operation is supported, the returned set of permissions must be a
	 *         new mutable instance and it must support heterogeneous Permission
	 *         types. If this operation is not supported,
	 *         Policy.UNSUPPORTED_EMPTY_COLLECTION is returned.
	 */
    protected PermissionCollection engineGetPermissions(CodeSource codesource) {
        return Policy.UNSUPPORTED_EMPTY_COLLECTION;
    }

    /**
	 * Answers a PermissionCollection object containing the set of permissions
	 * granted to the specified ProtectionDomain.
	 * 
	 * The default implementation of this method returns
	 * Policy.UNSUPPORTED_EMPTY_COLLECTION object. This method can be overridden
	 * if the policy implementation can return a set of permissions granted to a
	 * ProtectionDomain.
	 * 
	 * @param domain -
	 *            the ProtectionDomain to which the returned
	 *            PermissionCollection has been granted.
	 * @return a set of permissions granted to the specified ProtectionDomain.
	 *         If this operation is supported, the returned set of permissions
	 *         must be a new mutable instance and it must support heterogeneous
	 *         Permission types. If this operation is not supported,
	 *         Policy.UNSUPPORTED_EMPTY_COLLECTION is returned.
	 */
    protected PermissionCollection engineGetPermissions(ProtectionDomain domain) {
        return Policy.UNSUPPORTED_EMPTY_COLLECTION;
    }
}
