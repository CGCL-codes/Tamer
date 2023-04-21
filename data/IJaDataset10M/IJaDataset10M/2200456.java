package jdsl.core.api;

/** A locator is a coat-check, of sorts, for a (key,element) pair inside
 * a KeyBasedContainer.  If you give the container a pair to hold, the
 * container gives you back a locator, and you can later ask the
 * container to do useful things with the locator.  For example, one
 * thing you can do is have
 * the container stop storing the pair, by passing the locator to the
 * container's remove(.) method.
 *
 * What Locator adds to Accessor is the association between the
 * element and a key (contrast Position, which adds the association
 * between the element and a topological "place" in the container).
 * No guarantee is made by KeyBasedContainer about the way in
 * which the locators and the associated pairs are stored.
 *
 * <p>A different locator is associated with each reference to a pair
 * that you ask the container to store.  That is, if you put the same
 * pair into a container twice, or the same key or element, the pairs' locators
 * will not be the same.  However, if the container moves a pair
 * around, the locator follows the pair.  That is, a locator is
 * associated with one stored reference to a pair, no matter where
 * that reference is stored in the container.</p>
 * 
 * The implementation of locators is not specified, beyond this
 * interface.  However, the point of locators is to allow faster
 * access to arbitrary elements stored in the container than would be
 * possible by simply searching the container for them, so they should
 * not be implemented with a simple linear search.  They might be references
 * to node objects or indices into arrays.<p>
 *
 * @see KeyBasedContainer
 * @author Mark Handy (mdh)
 * @author Andrew Schwerin (schwerin) 
 *
 * @version JDSL 2.1.1 
 */
public interface Locator extends Accessor {

    /** 
     * @return the key associated with the locator
     * @exception InvalidAccessorException if the locator has been
     * removed from its container
     */
    public Object key() throws InvalidAccessorException;
}
