package nl.desiree.resources;

import nl.desiree.resources.GpsResource;
import android.test.AndroidTestCase;

public class GpsResourceTest extends AndroidTestCase {

    GpsResource gps;

    @Override
    public void setUp() {
        gps = new GpsResource(getContext());
    }

    public void testAvailable() {
        assertFalse(gps.isAvailable());
    }
}
