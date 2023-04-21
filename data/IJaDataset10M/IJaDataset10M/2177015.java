package org.dishevelled.multimap.impl;

import junit.framework.TestCase;
import org.dishevelled.multimap.TernaryKeyMap;
import static org.dishevelled.multimap.impl.TernaryKeyMaps.*;

/**
 * Unit test for TernaryKeyMaps.
 *
 * @author  Michael Heuer
 * @version $Revision$ $Date$
 */
public final class TernaryKeyMapsTest extends TestCase {

    public void testCreateTernaryKeyMap() {
        TernaryKeyMap<Float, Object, String, Integer> ternaryKeyMap = createTernaryKeyMap();
        assertNotNull(ternaryKeyMap);
    }

    public void testCreateTernaryKeyMapInitialCapacity() {
        TernaryKeyMap<Float, Object, String, Integer> ternaryKeyMap = createTernaryKeyMap(32);
        assertNotNull(ternaryKeyMap);
    }

    public void testCreateTernaryKeyMapInitialCapacityLoadFactorThreshold() {
        TernaryKeyMap<Float, Object, String, Integer> ternaryKeyMap = createTernaryKeyMap(32, 0.5f, 16);
        assertNotNull(ternaryKeyMap);
    }
}
