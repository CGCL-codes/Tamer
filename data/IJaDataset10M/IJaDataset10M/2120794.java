package org.shapelogic.polygon;

import junit.framework.TestCase;

/**   
 * 
 * @author Sami Badawi
 *
 */
public class PolygonEndPointAdjusterTest extends TestCase {

    public void testLBracketWithMissingCornerPoint() {
        CPointInt topPoint = new CPointInt(1, 1);
        CPointInt bottomPoint1 = new CPointInt(1, 27);
        CPointInt bottomPoint2 = new CPointInt(2, 28);
        CPointInt bottomPoint3 = new CPointInt(28, 28);
        MultiLinePolygon multiLinePolygon = new MultiLinePolygon();
        multiLinePolygon.startMultiLine();
        multiLinePolygon.addAfterEnd(topPoint);
        multiLinePolygon.addAfterEnd(bottomPoint1);
        multiLinePolygon.addAfterEnd(bottomPoint2);
        multiLinePolygon.addAfterEnd(bottomPoint3);
        multiLinePolygon.endMultiLine();
        multiLinePolygon.getValue();
        assertEquals(4, multiLinePolygon.getPoints().size());
        assertEquals(3, multiLinePolygon.getLines().size());
        PolygonEndPointAdjuster clusterAdjuster = new PolygonEndPointAdjuster(multiLinePolygon);
        MultiLinePolygon clusteredPolygon = (MultiLinePolygon) clusterAdjuster.getValue();
        assertNotSame(multiLinePolygon, clusteredPolygon);
        assertEquals(3, clusteredPolygon.getPoints().size());
        assertEquals(2, clusteredPolygon.getLines().size());
    }
}
