package org.personalsmartspace.spm.access.api.platform;

import java.io.Serializable;
import java.util.Iterator;
import org.personalsmartspace.sre.api.pss3p.IDigitalPersonalIdentifier;

/**
 * The <code>IAccessControlDecision</code> interface is used to assign granted
 * {@link Permission} objects to a particular requestor. More specifically, when the
 * <code>add</code> method is called to add a <code>Permission</code>, the
 * <code>Permission</code> is stored in a collection. The <code>remove</code>
 * method can be called to remove the given <code>Permission</code> from this
 * collection. The <code>implies</code> method is used to check if this object's
 * collection of permissions implies the permissions expressed in the specified
 * <code>Permission</code> object.
 * 
 * @author <a href="mailto:nliampotis@users.sourceforge.net">Nicolas
 *         Liampotis</a> (ICCS)
 * @see Permission
 * @since 0.1.0
 */
public interface IAccessControlDecision extends Serializable {

    /**
     * Returns the DPI of the requestor this <code>IAccessControlDecision</code>
     * applies to.
     * 
     * @return the requestor this access control decision applies to.
     */
    public IDigitalPersonalIdentifier getRequestor();

    /**
     * Returns an iterator over the permissions contained in this
     * <code>IAccessControlDecision</code>.
     * <p>
     * The returned iterator provides a snapshot of the state of the permission
     * collection when the iterator was constructed. No synchronization is
     * needed while traversing the iterator. The iterator does <em>NOT</em>
     * support the <code>remove</code> method.
     * 
     * @return an iterator over the permissions in this
     *         <code>IAccessControlDecision</code>.
     * @since 0.5.0
     */
    public Iterator<Permission> permissions();

    /**
     * Adds a <code>Permission</code> object to the current collection of
     * permission objects associated with this
     * <code>IAccessControlDecision</code>.
     * <p>
     * The method returns <code>true</code> if the collection of permissions
     * changed as a result of the call, <code>false</code> if this collection
     * already contains the specified <code>Permission</code>.
     * 
     * @param permission
     *            the <code>Permission</code> object to add.
     */
    public boolean add(Permission permission);

    /**
     * Removes the specified <code>Permission</code> object from the current
     * collection of permission objects associated with this
     * <code>IAccessControlDecision</code>.
     * <p>
     * The method returns <code>true</code> if the collection of permissions
     * contained the specified <code>Permission</code>.
     * 
     * @param permission
     *            the <code>Permission</code> object to remove.
     * @since 0.3.0
     */
    public boolean remove(Permission permission);

    /**
     * Checks if this object's collection of permissions implies the permissions
     * expressed in the specified <code>Permission</code> object.
     * 
     * @param permission
     *            the <code>Permission</code> object to check.
     * 
     * @return <code>true</code> if the permissions contained in the collection
     *         imply the specified <code>Permission</code> object;
     *         <code>false</code> otherwise.
     */
    public boolean implies(Permission permission);

    /**
     * Returns <code>true</code> if this <code>IAccessControlDecision</code>
     * contains the specified <code>Permission</code>.
     * 
     * @param permission
     *            the <code>Permission</code> to be checked for containment in
     *            this <code>IAccessControlDecision</code>.
     * @return <code>true</code> if this <code>IAccessControlDecision</code>
     *         contains the specified <code>Permission</code>;
     *         <code>false</code> otherwise.
     * @since 0.3.0
     */
    public boolean contains(Permission permission);

    /**
     * Removes all of the <code>Permission</code> objects associated with this
     * <code>AccessControlDecision</code>.
     * 
     * @since 0.3.0
     */
    public void clear();

    /**
     * Returns a <code>String</code> providing information about all the
     * permissions contained in this object's collection of permissions. The
     * format is:
     * 
     * <pre>
     * requestor.toString() (
     *   // enumerate all the Permission
     *   // objects and call toString() on them,
     *   // one per line..
     * )
     * </pre>
     * 
     * @return information about this <code>AccessControlDecision</code> object.
     * @see Permission#toString()
     */
    @Override
    public String toString();
}
