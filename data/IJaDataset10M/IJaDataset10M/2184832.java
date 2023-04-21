package org.ozoneDB.collections;

import java.util.WeakHashMap;

/**
 * See the overall description on {@link org.ozoneDB.collections.OzoneCollection}.
 * @author <a href="mailto:ozoneATmekenkampD0Tcom">Leo Mekenkamp (mind the anti-sp@m)</a>
 */
public interface OzoneWeakHashMap extends OzoneMap {

    /**
     * <p>Returns a <code>WeakHashMap</code> that contains the same entries as this
     * persistent one; it is (by nature of the client-server enviromnent) always
     * a 'deep' copy of this <code>OzoneWeakHashMap</code>. I.e. the contents of 
     * this <code>OzoneWeakHashMap</code> instance are always copied to the client
     * by use of serialization.</p>
     */
    public WeakHashMap getClientWeakHashMap();
}
