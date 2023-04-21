package us.wthr.jdem846.shapefile;

import us.wthr.jdem846.logging.Log;
import us.wthr.jdem846.logging.Logging;
import junit.framework.TestCase;

public class ShapeBaseTest extends TestCase {

    @SuppressWarnings("unused")
    private static Log log = Logging.getLog(ShapeBaseTest.class);

    @SuppressWarnings("unused")
    public void testShapeBaseInit() throws Exception {
        ShapeBase shapeBase = new ShapeBase("C:/srv/elevation/Shapefiles/BP14669/Foundation/Trans_RoadSegment.shp", "usgs-transportation-roads");
    }
}
