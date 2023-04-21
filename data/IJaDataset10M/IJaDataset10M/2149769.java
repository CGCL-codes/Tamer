package org.dishevelled.multimap.impl;

import org.dishevelled.multimap.AbstractTernaryKeyMapTest;
import org.dishevelled.multimap.TernaryKeyMap;

/**
 * Unit test for HashedTernaryKeyMap.
 *
 * @author  Michael Heuer
 * @version $Revision$ $Date$
 */
public final class HashedTernaryKeyMapTest extends AbstractTernaryKeyMapTest {

    /** {@inheritDoc} */
    protected <K1, K2, K3, V> TernaryKeyMap<K1, K2, K3, V> createTernaryKeyMap() {
        return new HashedTernaryKeyMap<K1, K2, K3, V>();
    }
}
