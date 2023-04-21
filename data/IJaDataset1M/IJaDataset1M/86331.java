package gpsmate.tests;

import static org.junit.Assert.*;
import gpsmate.geodata.GeoTool;
import gpsmate.geodata.Point;
import gpsmate.geodata.UtmPoint;
import org.junit.Test;

/**
 * GeoToolTest
 *
 * @author longdistancewalker
 *
 */
public class GeoToolTest {

    /**
   * Test method for {@link gpsmate.geodata.GeoTool#convertLatLonToUtm(gpsmate.geodata.Point)}.
   */
    @Test
    public void testConvertLatLonToUtm() {
        Point p = new Point(47.09, 15.47, 0.0);
        UtmPoint utm = GeoTool.convertLatLonToUtm(p);
        assertTrue(utm.getEasting() - 535671.9663269761 < 0.00001);
        assertTrue(utm.getNorthing() - 5215272.7416988155 < 0.00001);
    }
}
