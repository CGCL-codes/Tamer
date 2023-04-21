package org.shapelogic.imageprocessing;

import java.util.Collection;
import java.util.Set;
import org.shapelogic.imageutil.SLImage;
import org.shapelogic.polygon.CLine;
import org.shapelogic.polygon.CPointInt;
import org.shapelogic.polygon.GeometricShape2D;
import org.shapelogic.polygon.IPoint2D;
import org.shapelogic.polygon.MultiLinePolygon;
import org.shapelogic.polygon.PolygonEndPointAdjuster;
import org.shapelogic.util.LineType;
import org.shapelogic.util.PointType;
import static org.shapelogic.imageutil.ImageUtil.runPluginFilterOnImage;

/** Test MaxDistanceVectorizer.
 * 
 * @author Sami Badawi
 *
 */
public class MaxDistanceVectorizerTest extends AbstractImageProcessingTests {

    MaxDistanceVectorizer maxDistanceVectorizer = new MaxDistanceVectorizer();

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        _dirURL = "./src/test/resources/images/smallThinShapes";
        _fileFormat = ".gif";
    }

    public void testShortVertical() {
        String fileName = "vertical";
        SLImage bp = runPluginFilterOnImage(filePath(fileName), maxDistanceVectorizer);
        assertEquals(20, bp.getWidth());
        int pixel = bp.get(0, 0);
        assertEquals(PixelType.BACKGROUND_POINT.color, pixel);
        Collection<IPoint2D> points = maxDistanceVectorizer.getPoints();
        assertEquals(2, points.size());
        Collection<CLine> lines = maxDistanceVectorizer.getPolygon().getLines();
        assertEquals(1, lines.size());
        MultiLinePolygon polygon = ((MultiLinePolygon) maxDistanceVectorizer.getPolygon());
        assertEquals(0, polygon.getMultiLines().size());
        CLine line = lines.iterator().next();
        polygon.getAnnotatedShape().getMap();
        Set<Object> annotations = polygon.getAnnotatedShape().getAnnotationForShapes(line);
        assertEquals(1, annotations.size());
        assertTrue(annotations.contains(LineType.STRAIGHT));
    }

    public void testShortVerticalArch() {
        String fileName = "verticalArch";
        SLImage bp = runPluginFilterOnImage(filePath(fileName), maxDistanceVectorizer);
        assertEquals(30, bp.getWidth());
        int pixel = bp.get(0, 0);
        assertEquals(PixelType.BACKGROUND_POINT.color, pixel);
        Collection<IPoint2D> points = maxDistanceVectorizer.getPoints();
        assertEquals(2, points.size());
        Collection<CLine> lines = maxDistanceVectorizer.getPolygon().getLines();
        assertEquals(1, lines.size());
        MultiLinePolygon polygon = ((MultiLinePolygon) maxDistanceVectorizer.getPolygon());
        assertEquals(0, polygon.getMultiLines().size());
        CLine line = lines.iterator().next();
        polygon.getAnnotatedShape().getMap();
        Set<Object> annotations = polygon.getAnnotatedShape().getAnnotationForShapes(line);
        assertEquals(1, annotations.size());
        System.out.println(annotations);
        assertTrue(annotations.contains(LineType.CURVE_ARCH));
    }

    public void testShortVerticalAndHorizontal() {
        String fileName = "verticalAndHorizontal";
        SLImage bp = runPluginFilterOnImage(filePath(fileName), maxDistanceVectorizer);
        assertEquals(20, bp.getWidth());
        int pixel = bp.get(0, 0);
        assertEquals(PixelType.BACKGROUND_POINT.color, pixel);
        Collection<IPoint2D> points = maxDistanceVectorizer.getPoints();
        CPointInt topPoint = new CPointInt(1, 1);
        CPointInt bottomPoint1 = new CPointInt(1, 17);
        CPointInt bottomPoint2 = new CPointInt(15, 17);
        assertTrue(points.contains(topPoint));
        assertTrue(points.contains(bottomPoint1));
        assertTrue(points.contains(bottomPoint2));
        printPoints(maxDistanceVectorizer.getPolygon());
        assertEquals(3, points.size());
        Collection<CLine> lines = maxDistanceVectorizer.getPolygon().getLines();
        assertEquals(2, lines.size());
        assertEquals(3, maxDistanceVectorizer.getPolygon().getEndPointsClusters().size());
        MultiLinePolygon polygon = ((MultiLinePolygon) maxDistanceVectorizer.getPolygon());
        assertEquals(1, polygon.getMultiLines().size());
        assertFalse(polygon.getMultiLines().get(0).isClosed());
        assertNull(polygon.getMultiLines().get(0).isClosedLineClockWise());
        polygon.getAnnotatedShape().getMap();
        Set<Object> annotations = polygon.getAnnotatedShape().getAnnotationForShapes(bottomPoint1);
        System.out.println(annotations);
        assertEquals(1, annotations.size());
        assertTrue(annotations.contains(PointType.HARD_CORNER));
    }

    public void testShortRotatedTThin() {
        String fileName = "rotatedT";
        SLImage bp = runPluginFilterOnImage(filePath(fileName), maxDistanceVectorizer);
        assertEquals(20, bp.getWidth());
        int pixel = bp.get(0, 0);
        assertEquals(PixelType.BACKGROUND_POINT.color, pixel);
        printPolygon(maxDistanceVectorizer.getPolygon());
        assertEquals(2, maxDistanceVectorizer.countRegionCrossingsAroundPoint(maxDistanceVectorizer.pointToPixelIndex(1, 1)));
        assertEquals(4, maxDistanceVectorizer.countRegionCrossingsAroundPoint(maxDistanceVectorizer.pointToPixelIndex(1, 2)));
        assertEquals(4, maxDistanceVectorizer.countRegionCrossingsAroundPoint(maxDistanceVectorizer.pointToPixelIndex(1, 7)));
        assertEquals(6, maxDistanceVectorizer.countRegionCrossingsAroundPoint(maxDistanceVectorizer.pointToPixelIndex(1, 8)));
        Collection<IPoint2D> points = maxDistanceVectorizer.getPoints();
        assertEquals(4, points.size());
        CPointInt topPoint = new CPointInt(1, 1);
        CPointInt middlePoint1 = new CPointInt(1, 8);
        CPointInt middlePoint2 = new CPointInt(16, 8);
        CPointInt bottomPoint = new CPointInt(1, 17);
        assertTrue(points.contains(topPoint));
        assertTrue(points.contains(middlePoint1));
        assertTrue(points.contains(middlePoint2));
        assertTrue(points.contains(bottomPoint));
        Collection<CLine> lines = maxDistanceVectorizer.getPolygon().getLines();
        assertEquals(3, lines.size());
    }

    public void testThinProblematicL() {
        String fileName = "problematicL";
        SLImage bp = runPluginFilterOnImage(filePath(fileName), maxDistanceVectorizer);
        assertEquals(20, bp.getWidth());
        int pixel = bp.get(0, 0);
        assertEquals(PixelType.BACKGROUND_POINT.color, pixel);
        Collection<IPoint2D> points = maxDistanceVectorizer.getPoints();
        assertEquals(3, points.size());
        Collection<CLine> lines = maxDistanceVectorizer.getPolygon().getLines();
        assertEquals(2, lines.size());
        assertEquals("L", maxDistanceVectorizer.getMatchingOH());
        MultiLinePolygon polygon = ((MultiLinePolygon) maxDistanceVectorizer.getPolygon());
        assertEquals(1, polygon.getMultiLines().size());
        assertFalse(polygon.getMultiLines().get(0).isClosed());
        polygon.getAnnotatedShape().getMap();
        Set<GeometricShape2D> shapes = polygon.getAnnotatedShape().getShapesForAnnotation(PointType.HARD_CORNER);
        System.out.println(shapes);
        assertEquals(1, shapes.size());
    }

    public void testThinDiagonal() {
        String fileName = "diagonal";
        SLImage bp = runPluginFilterOnImage(filePath(fileName), maxDistanceVectorizer);
        int pixel = bp.get(0, 0);
        assertEquals(PixelType.BACKGROUND_POINT.color, pixel);
        Collection<IPoint2D> points = maxDistanceVectorizer.getPoints();
        assertNotNull(points);
        Collection<CLine> lines = maxDistanceVectorizer.getPolygon().getLines();
        assertEquals(1, lines.size());
        MultiLinePolygon polygon = ((MultiLinePolygon) maxDistanceVectorizer.getPolygon());
        assertEquals(0, polygon.getMultiLines().size());
        CLine line = lines.iterator().next();
        polygon.getAnnotatedShape().getMap();
        Set<Object> annotations = polygon.getAnnotatedShape().getAnnotationForShapes(line);
        assertEquals(1, annotations.size());
        System.out.println(annotations);
        assertTrue(annotations.contains(LineType.STRAIGHT));
    }

    public void testSmallThinTriangle() {
        String fileName = "triangle";
        SLImage bp = runPluginFilterOnImage(filePath(fileName), maxDistanceVectorizer);
        int pixel = bp.get(0, 0);
        assertEquals(PixelType.BACKGROUND_POINT.color, pixel);
        Collection<IPoint2D> points = maxDistanceVectorizer.getPoints();
        assertNotNull(points);
        printPoints(maxDistanceVectorizer.getPolygon());
        CPointInt topPoint2 = new CPointInt(2, 2);
        CPointInt bottomPoint2 = new CPointInt(2, 28);
        CPointInt bottomPoint4 = new CPointInt(27, 27);
        int crossingsForbottomPoint2 = maxDistanceVectorizer.countRegionCrossingsAroundPoint(maxDistanceVectorizer.pointToPixelIndex(bottomPoint2.x, bottomPoint2.y));
        assertEquals(4, crossingsForbottomPoint2);
        assertTrue(points.contains(topPoint2));
        assertTrue(points.contains(bottomPoint2));
        assertTrue(points.contains(bottomPoint4));
        assertEquals(3, points.size());
        Collection<CLine> lines = maxDistanceVectorizer.getPolygon().getLines();
        assertEquals(3, lines.size());
        assertEquals(3, maxDistanceVectorizer.getPolygon().getEndPointsClusters().size());
        MultiLinePolygon polygon = ((MultiLinePolygon) maxDistanceVectorizer.getPolygon());
        assertEquals(1, polygon.getMultiLines().size());
        assertTrue(polygon.getMultiLines().get(0).isClosed());
        PolygonEndPointAdjuster clusterAdjuster = new PolygonEndPointAdjuster(polygon);
        MultiLinePolygon clusteredPolygon = (MultiLinePolygon) clusterAdjuster.getValue();
        assertEquals(3, clusteredPolygon.getPoints().size());
        assertEquals(3, clusteredPolygon.getLines().size());
        CPointInt adjustedTopPoint = new CPointInt(2, 2);
        CPointInt adjustedBottomPoint1 = new CPointInt(2, 28);
        CPointInt adjustedBottomPoint2 = new CPointInt(27, 27);
        Collection<IPoint2D> adjustedPoints = clusteredPolygon.getPoints();
        assertTrue(adjustedPoints.contains(adjustedTopPoint));
        assertTrue(adjustedPoints.contains(adjustedBottomPoint1));
        assertTrue(adjustedPoints.contains(adjustedBottomPoint2));
        assertTrue(polygon.getMultiLines().get(0).isClosed());
        assertNotNull(polygon.getMultiLines().get(0).isClosedLineClockWise());
        assertTrue(polygon.getMultiLines().get(0).isClosedLineClockWise());
        CLine line = lines.iterator().next();
        polygon.getAnnotatedShape().getMap();
        Set<Object> annotations = polygon.getAnnotatedShape().getAnnotationForShapes(line);
        System.out.println(annotations);
        assertEquals(1, annotations.size());
        assertTrue(annotations.contains(LineType.STRAIGHT));
        polygon.getAnnotatedShape().getMap();
        Set<GeometricShape2D> shapes = polygon.getAnnotatedShape().getShapesForAnnotation(PointType.HARD_CORNER);
        System.out.println(shapes);
        assertEquals(3, shapes.size());
    }

    public void testThinLBracket() {
        String fileName = "LBracket";
        SLImage bp = runPluginFilterOnImage(filePath(fileName), maxDistanceVectorizer);
        int pixel = bp.get(0, 0);
        assertEquals(PixelType.BACKGROUND_POINT.color, pixel);
        Set<IPoint2D> points = (Set<IPoint2D>) maxDistanceVectorizer.getPoints();
        assertNotNull(points);
        printPoints(maxDistanceVectorizer.getPolygon());
        CPointInt topPoint = new CPointInt(1, 1);
        CPointInt bottomPoint1 = new CPointInt(1, 27);
        CPointInt bottomPoint3 = new CPointInt(28, 28);
        assertTrue(points.contains(topPoint));
        assertTrue(points.contains(bottomPoint1));
        assertTrue(points.contains(bottomPoint3));
        assertEquals(3, points.size());
        Collection<CLine> lines = maxDistanceVectorizer.getPolygon().getLines();
        assertEquals(2, lines.size());
        MultiLinePolygon polygon = ((MultiLinePolygon) maxDistanceVectorizer.getPolygon());
        assertEquals(1, polygon.getMultiLines().size());
        PolygonEndPointAdjuster clusterAdjuster = new PolygonEndPointAdjuster(polygon);
        MultiLinePolygon clusteredPolygon = (MultiLinePolygon) clusterAdjuster.getValue();
        assertEquals(3, clusteredPolygon.getPoints().size());
        assertEquals(2, clusteredPolygon.getLines().size());
    }

    public void testElongatedX() {
        String fileName = "elongatedX";
        SLImage bp = runPluginFilterOnImage(filePath(fileName), maxDistanceVectorizer);
        int pixel = bp.get(0, 0);
        assertEquals(PixelType.BACKGROUND_POINT.color, pixel);
        Set<IPoint2D> points = (Set<IPoint2D>) maxDistanceVectorizer.getPoints();
        assertNotNull(points);
        printPolygon(maxDistanceVectorizer.getPolygon());
        CPointInt topPoint1 = new CPointInt(1, 1);
        CPointInt bottomPoint1 = new CPointInt(1, 8);
        CPointInt topPoint2 = new CPointInt(7, 1);
        CPointInt bottomPoint2 = new CPointInt(7, 8);
        CPointInt midtTopPoint = new CPointInt(4, 4);
        CPointInt midtBottomPoint = new CPointInt(4, 5);
        assertTrue(points.contains(topPoint1));
        assertTrue(points.contains(midtTopPoint));
        assertTrue(points.contains(bottomPoint1));
        assertTrue(points.contains(topPoint2));
        assertTrue(points.contains(bottomPoint2));
        assertTrue(points.contains(midtBottomPoint));
        assertEquals(6, points.size());
        Collection<CLine> lines = maxDistanceVectorizer.getPolygon().getLines();
        assertEquals(5, lines.size());
    }

    public void testThinPlus() {
        String fileName = "plus";
        SLImage bp = runPluginFilterOnImage(filePath(fileName), maxDistanceVectorizer);
        int pixel = bp.get(0, 0);
        assertEquals(PixelType.BACKGROUND_POINT.color, pixel);
        Set<IPoint2D> points = (Set<IPoint2D>) maxDistanceVectorizer.getPoints();
        assertNotNull(points);
        printPoints(maxDistanceVectorizer.getPolygon());
        CPointInt topPoint = new CPointInt(10, 1);
        CPointInt middlePoint = new CPointInt(10, 10);
        CPointInt leftPoint = new CPointInt(1, 10);
        CPointInt rightPoint = new CPointInt(18, 10);
        CPointInt bottomPoint = new CPointInt(10, 18);
        assertTrue(points.contains(topPoint));
        assertTrue(points.contains(bottomPoint));
        assertTrue(points.contains(middlePoint));
        assertTrue(points.contains(leftPoint));
        assertTrue(points.contains(rightPoint));
        assertEquals(5, points.size());
        Collection<CLine> lines = maxDistanceVectorizer.getPolygon().getLines();
    }

    /** This is an test for new images with problems, should only be used when there are problems*/
    public void testBigCircle() {
        String fileName = "bigCircle";
        SLImage bp = runPluginFilterOnImage(filePath(fileName), maxDistanceVectorizer);
        int pixel = bp.get(0, 0);
        assertEquals(PixelType.BACKGROUND_POINT.color, pixel);
        Collection<IPoint2D> points = maxDistanceVectorizer.getPoints();
        assertNotNull(points);
        MultiLinePolygon polygon = ((MultiLinePolygon) maxDistanceVectorizer.getPolygon());
        assertEquals(1, polygon.getMultiLines().size());
        assertTrue(polygon.getMultiLines().get(0).isClosed());
        assertTrue(polygon.getMultiLines().get(0).isClosed());
        assertNotNull(polygon.getMultiLines().get(0).isClosedLineClockWise());
        assertTrue(polygon.getMultiLines().get(0).isClosedLineClockWise());
        Collection<CLine> lines = maxDistanceVectorizer.getPolygon().getLines();
        for (CLine line : lines) {
            polygon.getAnnotatedShape().getMap();
            Set<Object> annotations = polygon.getAnnotatedShape().getAnnotationForShapes(line);
            assertTrue(annotations.contains(LineType.CURVE_ARCH));
        }
        polygon.getAnnotatedShape().getMap();
        Set<GeometricShape2D> shapes = polygon.getAnnotatedShape().getShapesForAnnotation(PointType.HARD_CORNER);
        assertEmptyCollection(shapes);
    }

    /** This is an test for new images with problems, should only be used when there are problems*/
    public void testFailImage() {
        boolean testFailedIamge = false;
        if (!testFailedIamge) return;
        String fileName = "fail";
        SLImage bp = runPluginFilterOnImage(filePath(fileName), maxDistanceVectorizer);
        int pixel = bp.get(0, 0);
        assertEquals(PixelType.BACKGROUND_POINT.color, pixel);
        Collection<IPoint2D> points = maxDistanceVectorizer.getPoints();
        assertNotNull(points);
        assertEquals("N", maxDistanceVectorizer.getMatchingOH());
    }
}
