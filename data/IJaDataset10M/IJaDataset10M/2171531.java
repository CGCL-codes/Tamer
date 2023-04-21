package org.eclipse.osgi.service.resolver;

/**
 * VersionConstraints represent the relationship between two bundles (in the 
 * case of bundle requires) or a bundle and a package (in the case of import/export).
 * <p>
 * Clients may implement this interface.
 * </p>
 * @since 3.1
 */
public interface VersionConstraint extends Cloneable {

    /**
	 * Returns this constraint's name.
	 * 
	 * @return this constraint's name
	 */
    public String getName();

    /**
	 * Returns the version range for this constraint.
	 * @return the version range for this constraint, or <code>null</code>
	 */
    public VersionRange getVersionRange();

    /**
	 * Returns the bundle that declares this constraint.
	 * 
	 * @return a bundle description
	 */
    public BundleDescription getBundle();

    /**
	 * Returns whether this constraint is resolved. A resolved constraint 
	 * is guaranteed to have its supplier defined. 
	 * 
	 * @return <code>true</code> if this bundle is resolved, <code>false</code> 
	 * otherwise
	 */
    public boolean isResolved();

    /**
	 * Returns whether this constraint could be satisfied by the given supplier.
	 * This will depend on the suppliers different attributes including its name,
	 * versions and other arbitrary attributes
	 * 
	 * @param supplier a supplier to be tested against this constraint (may be 
	 * <code>null</code>)
	 * @return <code>true</code> if this constraint could be resolved using the supplier, 
	 * <code>false</code> otherwise 
	 */
    public boolean isSatisfiedBy(BaseDescription supplier);

    /**
	 * Returns the supplier that satisfies this constraint, if it is resolved.
	 *  
	 * @return a supplier, or <code>null</code> 
	 * @see #isResolved()
	 */
    public BaseDescription getSupplier();
}
