package jaxlib.col.restricted;

import java.io.Serializable;
import jaxlib.util.AccessType;
import jaxlib.util.AccessTypeSet;
import jaxlib.col.XSet;

/**
 * Provides a restricted view of a <tt>XSet</tt> for denying access to modifying operations like <tt>add</tt> and <tt>remove</tt>.
 * The most common usage is to create an unmodifiable view of a <tt>XSet</tt>.
 * <p>
 * Only modifying operations can be restricted - query operations like <tt>contains</tt> or <tt>iterator().next()</tt> can not.
 * </p>
 *
 * @see #unmodifiable(XSet)
 * @see #allow(XSet, AccessTypeSet)
 * @see #deny(XSet, AccessTypeSet)
 * @see java.util.Collections#unmodifiableSet(java.util.Set)
 *
 * @author  <a href="mailto:joerg.wassmer@web.de">J�rg Wa�mer</a>
 * @since   JaXLib 1.0
 * @version $Id: RestrictedSet.java 1044 2004-04-06 16:37:29Z joerg_wassmer $
 */
public class RestrictedSet<E> extends RestrictedCollection<E> implements XSet<E>, Serializable {

    /**
   * @since JaXLib 1.0
   */
    private static final long serialVersionUID = 1L;

    /**
   * Returns an unmodifiable view of the specified set.
   * <p>
   * All modifying operations of the returned set, like <tt>add</tt> and <tt>remove</tt>, will throw an 
   * <tt>UnsupportedOperationException</tt>.
   * </p>
   *
   * @return An unmodifiable view of the specified set.
   *
   * @param delegate the collection to restrict access for.
   *
   * @throws NullPointerException if specified collection is <tt>null</tt>.
   *
   * @see XSet#accessTypes()
   * @see java.lang.UnsupportedOperationException
   * @see java.util.Collections#unmodifiableSet(java.util.Set)
   *
   * @since JaXLib 1.0
   */
    public static <E> RestrictedSet<E> unmodifiable(XSet<? extends E> delegate) {
        return allow(delegate, AccessTypeSet.READ_ONLY);
    }

    /**
   * Returns a view of the specified set which allows only specified types of operations.
   * <p>
   * All modifying operations of the returned set, which are not included in specified set of accesstypes, will throw an 
   * <tt>UnsupportedOperationException</tt>.
   * </p>
   *
   * @return A view of the specified set which's operations are limited to specified set of accesstypes.
   *
   * @param delegate      the set to restrict access for.
   * @param allowedAccess the types of operations to allow the specified set to be accessed through.
   *
   * @throws NullPointerException if the specified set or the set of accesstypes is <tt>null</tt>.
   *
   * @see XSet#accessTypes()
   * @see #unmodifiable(XSet)
   * @see #deny(XSet, AccessTypeSet)
   * @see java.lang.UnsupportedOperationException
   *
   * @since JaXLib 1.0
   */
    public static <E> RestrictedSet<E> allow(XSet<? extends E> delegate, AccessTypeSet allowedAccess) {
        return deny(delegate, allowedAccess.denied());
    }

    /**
   * Returns a view of the specified set which denies the specified types of operations.
   * <p>
   * All modifying operations of the returned set, which are included in specified set of accesstypes, will throw an 
   * <tt>UnsupportedOperationException</tt>.
   * </p>
   *
   * @return A view of the specified set which's operations are limited to those not contained in specified set of accesstypes.
   *
   * @param delegate      the set to restrict access for.
   * @param deniedAccess  the types of operations of the set to deny.
   *
   * @throws NullPointerException if the specified set or the set of accesstypes is <tt>null</tt>.
   *
   * @see XSet#accessTypes()
   * @see #unmodifiable(XSet)
   * @see #allow(XSet, AccessTypeSet)
   *
   * @since JaXLib 1.0
   */
    public static <E> RestrictedSet<E> deny(XSet<? extends E> delegate, AccessTypeSet deniedAccess) {
        if ((delegate instanceof RestrictedSet) && delegate.accessTypes().deniesAll(deniedAccess.except(AccessType.READ))) return (RestrictedSet<E>) delegate; else return new RestrictedSet(delegate, deniedAccess.denied().and(AccessType.READ));
    }

    /**
   * @serial
   * @since JaXLib 1.0
   */
    final XSet<E> delegate;

    /**
   * Constructs a new <tt>RestrictedSet</tt>.
   *
   * @param delegate      the <tt>XSet</tt> to restrict access to.
   * @param allowedAccess the set of operations to allow.
   *
   * @throws NullPointerException if the specified <tt>XSet</tt> or the set of accesstypes is <tt>null</tt>.
   * 
   * @see #unmodifiable(XSet)
   * @see #allow(XSet, AccessTypeSet)
   * @see #deny(XSet, AccessTypeSet)
   *
   * @since JaXLib 1.0
   */
    public RestrictedSet(XSet<E> delegate, AccessTypeSet allowedAccess) {
        super(delegate, allowedAccess);
        this.delegate = delegate;
    }
}
