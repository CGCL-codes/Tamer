package org.gvsig.topology;

import java.awt.geom.Point2D;
import java.util.List;
import org.gvsig.fmap.core.FGeometryUtil;
import org.gvsig.fmap.core.ShapePointExtractor;
import com.iver.cit.gvsig.fmap.core.GeneralPathX;
import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.iver.cit.gvsig.fmap.core.ShapeFactory;
import junit.framework.TestCase;

public class FGeometryUtilTest extends TestCase {

    public void testInsertVertex() {
        GeneralPathX gpx = new GeneralPathX();
        gpx.moveTo(0d, 0d);
        gpx.lineTo(100d, 0d);
        gpx.lineTo(300d, 150d);
        IGeometry geometry = ShapeFactory.createPolyline2D(gpx);
        Point2D newVertex = new Point2D.Double(50d, 0.02d);
        IGeometry newGeometry = FGeometryUtil.insertVertex(geometry, newVertex, 0.1);
        List<Point2D[]> pointList = ShapePointExtractor.extractPoints(newGeometry);
        Point2D[] points = pointList.get(0);
        assertTrue(points.length == 4);
    }
}
