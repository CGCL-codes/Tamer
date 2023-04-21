package org.ozoneDB.collections;

/**
 * <p>Defines all methods that a <code>FullTreeMapImpl</code> needs to implement;
 * these also include the methods that need to be called by iterators, submaps
 * etc. (in non-ozone implementations these are normally inner classes and have
 * direct acces to protected and package parts).</p>
 * <p>You are encouraged NOT to use this interface, but rather just use {@link
 * OzoneTreeMap}, which does not contain the 'internal' methods, or even
 * {@link java.util.SortedMap}, which does not have any ozone dependency at all</p>
 *
 * @author <a href="mailto:ozoneATmekenkampD0Tcom">Leo Mekenkamp (mind the anti-sp@m)</a>
 */
public interface FullTreeMap extends BaseTreeMap {
}
