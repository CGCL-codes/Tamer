package org.ozoneDB.collections;

import java.util.SortedMap;

/**
 * See the overall description on {@link org.ozoneDB.collections.OzoneCollection}.
 * @author <a href="mailto:ozoneATmekenkampD0Tcom">Leo Mekenkamp (mind the anti-sp@m)</a>
 */
public interface OzoneSortedMap extends OzoneMap, SortedMap {

    /**
     * <p>Returns a <code>SortedMap</code> that contains the same entries as this
     * persistent one; it is (by nature of the client-server enviromnent) always
     * a 'deep' copy of this <code>OzoneSortedMap</code>. I.e. the contents of 
     * this <code>OzoneSortedMap</code> instance are always copied to the client
     * by use of serialization.</p>
     */
    public SortedMap getClientSortedMap();

    /**
     * <p>Basically nothing more than a typecasted <code>HeadMap</code> method.
     * Because subsets are also <code>OzoneSortedMap</code>s, this method is
     * provided to do away with the need for a typecast.</p>
     */
    public OzoneSortedMap ozoneHeadMap(Object toKey);

    /**
     * <p>Basically nothing more than a typecasted <code>SubMap</code> method.
     * Because subsets are also <code>OzoneSortedMap</code>s, this method is
     * provided to do away with the need for a typecast.</p>
     */
    public OzoneSortedMap ozoneSubMap(Object fromKey, Object toKey);

    /**
     * <p>Basically nothing more than a typecasted <code>TailMap</code> method.</p>
     * Because subsets are also <code>OzoneSortedMap</code>s, this method is
     * provided to do away with the need for a typecast.</p>
     */
    public OzoneSortedMap ozoneTailMap(Object toKey);
}
