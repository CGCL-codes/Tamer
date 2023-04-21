package de.line2route.test;

import org.apache.xmlbeans.XmlException;
import de.line2route.Coordinate;
import net.opengis.kml.x22.CoordinatesType;
import junit.framework.TestCase;

public class CoordinateTest extends TestCase {

    public void testparse2Array() throws XmlException {
        CoordinatesType ct;
        ct = CoordinatesType.Factory.parse("<coordinates/>");
        Coordinate[] c = Coordinate.parse2Array(ct);
        assertTrue("Empty coordinates did not return empty array", c.length == 0);
        ct = CoordinatesType.Factory.parse("<coordinates>10,12,0</coordinates>");
        c = Coordinate.parse2Array(ct);
        assertEquals("Array length wrong", 1, c.length);
        assertEquals("Longitude", 10F, c[0].getLongitude());
        assertEquals("Latitude", 12F, c[0].getLatitude());
        assertEquals("Altitude", 0F, c[0].getAltitude());
        ct = CoordinatesType.Factory.parse("<coordinates>1.1,1.2,1.3 2.1,2.2,2.3</coordinates>");
        c = Coordinate.parse2Array(ct);
        assertEquals("Array length wrong", 2, c.length);
        assertEquals("Longitude", 1.1F, c[0].getLongitude());
        assertEquals("Latitude", 1.2F, c[0].getLatitude());
        assertEquals("Altitude", 1.3F, c[0].getAltitude());
        assertEquals("Longitude", 2.1F, c[1].getLongitude());
        assertEquals("Latitude", 2.2F, c[1].getLatitude());
        assertEquals("Altitude", 2.3F, c[1].getAltitude());
        ct = CoordinatesType.Factory.parse("<coordinates>Wurst 2.1,2.2,2.3</coordinates>");
        c = Coordinate.parse2Array(ct);
        assertEquals("Array length wrong", 1, c.length);
        assertEquals("Longitude", 2.1F, c[0].getLongitude());
        assertEquals("Latitude", 2.2F, c[0].getLatitude());
        assertEquals("Altitude", 2.3F, c[0].getAltitude());
        ct = CoordinatesType.Factory.parse("<coordinates>1.1,1.2,1.3 5.5,4.3 2.1,2.2,2.3</coordinates>");
        c = Coordinate.parse2Array(ct);
        assertEquals("Array length wrong", 2, c.length);
        assertEquals("Longitude", 1.1F, c[0].getLongitude());
        assertEquals("Latitude", 1.2F, c[0].getLatitude());
        assertEquals("Altitude", 1.3F, c[0].getAltitude());
        assertEquals("Longitude", 2.1F, c[1].getLongitude());
        assertEquals("Latitude", 2.2F, c[1].getLatitude());
        assertEquals("Altitude", 2.3F, c[1].getAltitude());
        ct = CoordinatesType.Factory.parse("<coordinates>1.1,1.2,1.3 2.1,2.2,2.3 Wurst</coordinates>");
        c = Coordinate.parse2Array(ct);
        assertEquals("Array length wrong", 2, c.length);
        assertEquals("Longitude", 1.1F, c[0].getLongitude());
        assertEquals("Latitude", 1.2F, c[0].getLatitude());
        assertEquals("Altitude", 1.3F, c[0].getAltitude());
        assertEquals("Longitude", 2.1F, c[1].getLongitude());
        assertEquals("Latitude", 2.2F, c[1].getLatitude());
        assertEquals("Altitude", 2.3F, c[1].getAltitude());
    }
}
