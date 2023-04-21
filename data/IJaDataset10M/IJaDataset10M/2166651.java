package org.matsim.world;

import org.matsim.api.basic.v01.Coord;
import org.matsim.core.basic.v01.IdImpl;
import org.matsim.core.utils.geometry.CoordImpl;
import org.matsim.core.utils.geometry.CoordUtils;
import org.matsim.testcases.MatsimTestCase;
import org.matsim.world.World;
import org.matsim.world.WorldUtils;
import org.matsim.world.Zone;
import org.matsim.world.ZoneLayer;

public class WorldUtilsTest extends MatsimTestCase {

    /**
	 * Tests org.matsim.util.WorldUtils.distancePointLinesegment()
	 *
	 * @author mrieser
	 */
    public void testDistancePointLinesegment() {
        CoordImpl p1 = new CoordImpl(10, 20);
        CoordImpl p2 = new CoordImpl(10, 30);
        CoordImpl p3 = new CoordImpl(10, 40);
        CoordImpl p4 = new CoordImpl(20, 30);
        assertEquals(10.0, WorldUtils.distancePointLinesegment(p1, p3, p4), 1e-10);
        assertEquals("special case where lineFrom == lineTo", 10.0, WorldUtils.distancePointLinesegment(p1, p1, p2), 1e-10);
        assertEquals("special case where point before lineFrom", 10.0, WorldUtils.distancePointLinesegment(p2, p3, p1), 1e-10);
        assertEquals("special case where point after lineTo", 10.0, WorldUtils.distancePointLinesegment(p1, p2, p3), 1e-10);
        assertEquals("special case where point on line segment", 0.0, WorldUtils.distancePointLinesegment(p1, p3, p2), 1e-10);
    }

    /**
	 * Tests that the returned random coordinates are indeed somehow random
	 * within the area when the zones have a bounding box, and that the
	 * coordinates cover the whole area more or less equally.
	 *
	 * @author mrieser
	 */
    public void testGetRandomCoordInZoneBoundingBox() {
        final double minX = 0.0;
        final double minY = 0.0;
        final double maxX = 9.0;
        final double maxY = 18.0;
        final int[] areaCounters = new int[9];
        final World world = new World();
        ZoneLayer layer = (ZoneLayer) world.createLayer(new IdImpl("zones"), "zones for test");
        Zone zone = layer.createZone("1", "4.5", "9", "0", "0", "9", "18", null, "center zone");
        layer.createZone("2", "30", "15", "9", "0", "51", "30", null, "another zone");
        for (int i = 0; i < 900; i++) {
            Coord c = WorldUtils.getRandomCoordInZone(zone, layer);
            assertTrue("Coordinate is out of bounds: x = " + c.getX(), c.getX() >= minX);
            assertTrue("Coordinate is out of bounds: x = " + c.getX(), c.getX() <= maxX);
            assertTrue("Coordinate is out of bounds: y = " + c.getY(), c.getY() >= minY);
            assertTrue("Coordinate is out of bounds: y = " + c.getY(), c.getY() <= maxY);
            int areaIndex = ((int) c.getX()) / ((int) ((maxX - minX) / 3.0)) * 3 + ((int) c.getY()) / ((int) ((maxY - minY) / 3.0));
            areaCounters[areaIndex]++;
        }
        for (int i = 0; i < areaCounters.length; i++) {
            int count = areaCounters[i];
            assertTrue("random coordinates seem not to be equally distributed, as area " + i + " has only " + count + " points in it.", count > 90);
        }
    }

    /**
	 * Tests that the returned random coordinates are indeed somehow random
	 * within the area when the zones have <em>no</em> bounding box, and that
	 * the coordinates cover the center area stronger than the outer areas.
	 *
	 * @author mrieser
	 */
    public void testGetRandomCoordInZoneCenterOnly() {
        double minX = Double.POSITIVE_INFINITY;
        double minY = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY;
        double maxY = Double.NEGATIVE_INFINITY;
        final int[] areaCounters = new int[11];
        final World world = new World();
        ZoneLayer layer = (ZoneLayer) world.createLayer(new IdImpl("zones"), "zones for test");
        Zone zone = layer.createZone("1", "4.5", "9", null, null, null, null, null, "center zone");
        Zone zone2 = layer.createZone("2", "30", "15", "9", null, null, null, null, "another zone");
        Coord center = zone.getCoord();
        final double distance = CoordUtils.calcDistance(center, zone2.getCoord());
        for (int i = 0; i < 700; i++) {
            Coord c = WorldUtils.getRandomCoordInZone(zone, layer);
            if (c.getX() < minX) {
                minX = c.getX();
            }
            if (c.getY() < minY) {
                minY = c.getY();
            }
            if (c.getX() > maxX) {
                maxX = c.getX();
            }
            if (c.getY() > maxY) {
                maxY = c.getY();
            }
            int areaIndex = (int) (CoordUtils.calcDistance(c, center) / distance * 10);
            areaCounters[areaIndex]++;
        }
        assertTrue("random coordinates are not spread enough. minX = " + minX, minX < (4.5 - distance / 2.0));
        assertTrue("random coordinates are not spread enough. maxX = " + maxX, maxX > (4.5 + distance / 2.0));
        assertTrue("random coordinates are not spread enough. minY = " + minY, minY < (9.0 - distance / 2.0));
        assertTrue("random coordinates are not spread enough. maxY = " + maxY, maxY > (9.0 + distance / 2.0));
        for (int i = 0; i < 7; i++) {
            int count = areaCounters[i];
            assertTrue("random coordinates seem not to be equally distributed, as area " + i + " has only " + count + " points in it.", count > 88);
        }
        for (int i = 8; i < areaCounters.length; i++) {
            assertTrue("there should not be any point in area " + i, areaCounters[i] == 0);
        }
    }
}
