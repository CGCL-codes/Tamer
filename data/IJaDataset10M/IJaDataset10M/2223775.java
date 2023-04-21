package se.citerus.dddsample.tracking.core.domain.model.location;

import junit.framework.TestCase;
import java.util.TimeZone;

public class LocationTest extends TestCase {

    private static final TimeZone CET = TimeZone.getTimeZone("Europe/Amsterdam");

    public void testEquals() {
        assertTrue(new Location(new UnLocode("ATEST"), "test-name", CET, null).equals(new Location(new UnLocode("ATEST"), "test-name", CET, null)));
        assertFalse(new Location(new UnLocode("ATEST"), "test-name", CET, null).equals(new Location(new UnLocode("TESTB"), "test-name", CET, null)));
        Location location = new Location(new UnLocode("ATEST"), "test-name", CET, null);
        assertTrue(location.equals(location));
        assertFalse(location.equals(null));
        assertTrue(Location.NONE.equals(Location.NONE));
        try {
            new Location(null, null, null, null);
            fail("Should not allow any null constructor arguments");
        } catch (IllegalArgumentException expected) {
        }
    }
}
