package net.sf.pachamama.domain.common.location;

import junit.framework.TestCase;

public class LocationTest extends TestCase {

    public void testTrivialDistanceCalculation() {
        Location a = new Location(1L, 1L, 0L);
        Location b = new Location(2L, 2L, 0L);
        assertTrue(a.getDistanceTo(b) == Math.sqrt(2));
    }
}
