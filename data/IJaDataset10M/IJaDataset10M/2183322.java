package net.sf.javarisk.tools;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.awt.Dimension;
import java.awt.Point;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import net.sf.javarisk.TestBase;
import org.junit.Test;

/** JUnit Test for {@link HexGridTools}. */
public final class HexGridToolsTest extends TestBase {

    /**
     * Tests {@link HexGridTools}'s private constructor.<br/>
     * <i>Yes, I do very much to get a green bar for code coverage :)</i>
     * 
     * @throws Exception
     *             if something fails
     */
    @Test
    public void testHexGridTools() throws Exception {
        Constructor<HexGridTools> c = HexGridTools.class.getDeclaredConstructor();
        c.setAccessible(true);
        c.newInstance();
    }

    /**
     * Used to test {@link HexGridTools#getCenter(Dimension)}.
     * 
     * @param expectedPosition
     *            the expected result
     * @param width
     *            the dimension's width
     * @param height
     *            the dimension's height
     */
    private static void testGetCenter(Point expectedPosition, int width, int height) {
        assertEquals("Wrong center calculated for [" + width + "," + height + "]!", expectedPosition, HexGridTools.getCenter(new Dimension(width, height)));
    }

    /**
     * Used to test {@link HexGridTools#getCenter(Dimension)}.
     * 
     * @param expectedX
     *            the expected result's x value
     * @param expectedY
     *            the expected result's y value
     * @since 0.1
     */
    private static void testGetCenter(int expectedX, int expectedY) {
        Point expectedPosition = new Point(expectedX, expectedY);
        testGetCenter(expectedPosition, expectedX * 2, expectedY * 2);
        testGetCenter(expectedPosition, expectedX * 2, expectedY * 2 + 1);
        testGetCenter(expectedPosition, expectedX * 2 + 1, expectedY * 2);
        testGetCenter(expectedPosition, expectedX * 2 + 1, expectedY * 2 + 1);
    }

    /** Tests {@link HexGridTools#getCenter(Dimension)}. */
    @Test
    public void testGetCenter() {
        try {
            HexGridTools.getCenter(null);
            fail("A Dimension must be required!");
        } catch (RuntimeException e) {
        }
        for (int x = 0; x++ < 16; ) {
            for (int y = 0; y++ < 16; ) {
                testGetCenter(x, y);
            }
        }
    }

    /**
     * Used to test {@link HexGridTools#getFieldHeight(int)}.
     * 
     * @param expectedHeight
     *            the expected result
     * @param width
     *            the field width
     * @since 0.1
     */
    private static void testGetFieldHeight(int expectedHeight, int width) {
        assertEquals("Wrong height calculated for width [" + width + "]!", expectedHeight, HexGridTools.getFieldHeight(width));
    }

    /** Tests {@link HexGridTools#getFieldHeight(int)}. */
    @Test
    public void testGetFieldHeight() {
        testGetFieldHeight(170, 196);
        testGetFieldHeight(171, 197);
        testGetFieldHeight(171, 198);
        testGetFieldHeight(172, 199);
        testGetFieldHeight(173, 200);
        testGetFieldHeight(174, 201);
        testGetFieldHeight(175, 202);
        testGetFieldHeight(176, 203);
        testGetFieldHeight(177, 204);
        testGetFieldHeight(178, 205);
        testGetFieldHeight(178, 206);
        testGetFieldHeight(179, 207);
        testGetFieldHeight(80, 92);
        testGetFieldHeight(81, 93);
        testGetFieldHeight(81, 94);
        testGetFieldHeight(82, 95);
        testGetFieldHeight(83, 96);
        testGetFieldHeight(84, 97);
        testGetFieldHeight(85, 98);
        testGetFieldHeight(86, 99);
        testGetFieldHeight(87, 100);
        testGetFieldHeight(87, 101);
        testGetFieldHeight(88, 102);
    }

    /**
     * Used to test {@link HexGridTools#getFieldSize(int, float)}.
     * 
     * @param expectedWidth
     *            the expected result's width
     * @param expectedHeight
     *            the expected result's height
     * @param width
     *            the field width
     * @param zoom
     *            the zoom factor
     * @since 0.1
     */
    private static void testGetFieldSize(int expectedWidth, int expectedHeight, int width, float zoom) {
        assertEquals("Wrong size calculated for width [" + width + "] & zoom [" + zoom + "]!", new Dimension(expectedWidth, expectedHeight), HexGridTools.getFieldSize(width, zoom));
    }

    /** Tests {@link HexGridTools#getFieldSize(int, float)}. */
    @Test
    public void testGetFieldSize() {
        testGetFieldSize(200, 173, 200, 1f);
        testGetFieldSize(63, 55, 190, 1f / 3);
        testGetFieldSize(64, 55, 191, 1f / 3);
        testGetFieldSize(64, 55, 192, 1f / 3);
        testGetFieldSize(64, 55, 193, 1f / 3);
        testGetFieldSize(65, 56, 194, 1f / 3);
        testGetFieldSize(65, 56, 195, 1f / 3);
        testGetFieldSize(65, 56, 196, 1f / 3);
        testGetFieldSize(66, 57, 197, 1f / 3);
        testGetFieldSize(66, 57, 198, 1f / 3);
        testGetFieldSize(66, 57, 199, 1f / 3);
        testGetFieldSize(67, 58, 200, 1f / 3);
        testGetFieldSize(67, 58, 201, 1f / 3);
        testGetFieldSize(67, 58, 202, 1f / 3);
        testGetFieldSize(68, 59, 203, 1f / 3);
        testGetFieldSize(68, 59, 204, 1f / 3);
        testGetFieldSize(68, 59, 205, 1f / 3);
        testGetFieldSize(69, 60, 206, 1f / 3);
        testGetFieldSize(69, 60, 207, 1f / 3);
        testGetFieldSize(69, 60, 208, 1f / 3);
        testGetFieldSize(70, 61, 209, 1f / 3);
        testGetFieldSize(70, 61, 210, 1f / 3);
        testGetFieldSize(70, 61, 211, 1f / 3);
        testGetFieldSize(71, 61, 212, 1f / 3);
        testGetFieldSize(317, 275, 190, 1 + 2f / 3);
        testGetFieldSize(318, 275, 191, 1 + 2f / 3);
        testGetFieldSize(320, 277, 192, 1 + 2f / 3);
        testGetFieldSize(322, 279, 193, 1 + 2f / 3);
        testGetFieldSize(323, 280, 194, 1 + 2f / 3);
        testGetFieldSize(325, 281, 195, 1 + 2f / 3);
        testGetFieldSize(327, 283, 196, 1 + 2f / 3);
        testGetFieldSize(328, 284, 197, 1 + 2f / 3);
        testGetFieldSize(330, 286, 198, 1 + 2f / 3);
        testGetFieldSize(332, 288, 199, 1 + 2f / 3);
        testGetFieldSize(333, 288, 200, 1 + 2f / 3);
        testGetFieldSize(335, 290, 201, 1 + 2f / 3);
        testGetFieldSize(337, 292, 202, 1 + 2f / 3);
        testGetFieldSize(338, 293, 203, 1 + 2f / 3);
        testGetFieldSize(340, 294, 204, 1 + 2f / 3);
        testGetFieldSize(342, 296, 205, 1 + 2f / 3);
        testGetFieldSize(343, 297, 206, 1 + 2f / 3);
        testGetFieldSize(345, 299, 207, 1 + 2f / 3);
        testGetFieldSize(347, 301, 208, 1 + 2f / 3);
        testGetFieldSize(348, 301, 209, 1 + 2f / 3);
        testGetFieldSize(350, 303, 210, 1 + 2f / 3);
    }

    /**
     * Checks if {@link HexGridTools#getGridPosition(Dimension, Dimension, Point, Point)} returns the expected field.
     * 
     * @param center
     *            the centered field's grid position
     * @param x
     *            the x (pixel) position
     * @param y
     *            the y (pixel) position
     * @param expectedField
     *            the expected field's grid position
     */
    private static void assertGetGridPositionReturnsExpectedField(Point center, int x, int y, Point expectedField) {
        Point field = HexGridTools.getGridPosition(new Dimension(1024, 768), new Dimension(200, 173), center, new Point(x, y));
        assertEquals("Incorrect grid position calculated!", expectedField, field);
    }

    /**
     * Performs various tests for the specified centered field.
     * 
     * @param center
     *            the centered field's grid position
     */
    private static void testGetGridPositionAtGridCoordinates(Point center) {
        HexGridWalker walker = new HexGridWalker(center);
        assertGetGridPositionReturnsExpectedField(center, 512, 384, center);
        assertGetGridPositionReturnsExpectedField(center, 512 - 100 - 102, 384 - 1, new HexGridWalker(walker.leftLowerNeighbor()).leftUpperNeighbor());
        assertGetGridPositionReturnsExpectedField(center, 512 - 100 - 102, 384, new HexGridWalker(walker.leftLowerNeighbor()).leftUpperNeighbor());
        assertGetGridPositionReturnsExpectedField(center, 512 - 100 - 102, 384 + 1, new HexGridWalker(walker.leftLowerNeighbor()).leftUpperNeighbor());
        assertGetGridPositionReturnsExpectedField(center, 512 - 100 - 98, 384 + 2, walker.leftLowerNeighbor());
        assertGetGridPositionReturnsExpectedField(center, 512 - 100 - 98, 384 - 2, walker.leftUpperNeighbor());
        assertGetGridPositionReturnsExpectedField(center, 512 - 102, 384 + 2, walker.leftLowerNeighbor());
        assertGetGridPositionReturnsExpectedField(center, 512 - 102, 384 - 2, walker.leftUpperNeighbor());
        assertGetGridPositionReturnsExpectedField(center, 512 - 98, 384, center);
        assertGetGridPositionReturnsExpectedField(center, 512 - 50 - 5, 384 - 5, center);
        assertGetGridPositionReturnsExpectedField(center, 512 - 100 + 5, 384 - 86 + 5, walker.leftUpperNeighbor());
        assertGetGridPositionReturnsExpectedField(center, 512 - 50 - 5, 384 + 5, center);
        assertGetGridPositionReturnsExpectedField(center, 512 - 100 + 5, 384 + 86 - 5, walker.leftLowerNeighbor());
        assertGetGridPositionReturnsExpectedField(center, 512 + 98, 384, center);
        assertGetGridPositionReturnsExpectedField(center, 512 + 102, 384 - 2, walker.rightUpperNeighbor());
        assertGetGridPositionReturnsExpectedField(center, 512 + 102, 384 + 2, walker.rightLowerNeighbor());
        assertGetGridPositionReturnsExpectedField(center, 512 + 100 + 98, 384 - 2, walker.rightUpperNeighbor());
        assertGetGridPositionReturnsExpectedField(center, 512 + 100 + 98, 384 + 2, walker.rightLowerNeighbor());
        assertGetGridPositionReturnsExpectedField(center, 512 + 100 + 102, 384 - 1, new HexGridWalker(walker.rightUpperNeighbor()).rightLowerNeighbor());
        assertGetGridPositionReturnsExpectedField(center, 512 + 100 + 102, 384, new HexGridWalker(walker.rightUpperNeighbor()).rightLowerNeighbor());
        assertGetGridPositionReturnsExpectedField(center, 512 + 100 + 102, 384 + 1, new HexGridWalker(walker.rightUpperNeighbor()).rightLowerNeighbor());
        assertGetGridPositionReturnsExpectedField(center, 512 + 50 + 5, 384 - 5, center);
        assertGetGridPositionReturnsExpectedField(center, 512 + 100 - 5, 384 - 86 + 5, walker.rightUpperNeighbor());
        assertGetGridPositionReturnsExpectedField(center, 512 + 50 + 5, 384 + 5, center);
        assertGetGridPositionReturnsExpectedField(center, 512 + 100 - 5, 384 + 86 - 5, walker.rightLowerNeighbor());
        assertGetGridPositionReturnsExpectedField(center, 512, 384 - 173 - 88, new HexGridWalker(walker.upperNeighbor()).upperNeighbor());
        assertGetGridPositionReturnsExpectedField(center, 512, 384 - 173 - 84, walker.upperNeighbor());
        assertGetGridPositionReturnsExpectedField(center, 512, 384 - 88, walker.upperNeighbor());
        assertGetGridPositionReturnsExpectedField(center, 512, 384 - 84, center);
        assertGetGridPositionReturnsExpectedField(center, 512 - 100 + 5, 384 - 86 - 5, walker.leftUpperNeighbor());
        assertGetGridPositionReturnsExpectedField(center, 512 - 50 - 5, 384 - 173 + 5, walker.upperNeighbor());
        assertGetGridPositionReturnsExpectedField(center, 512 + 100 - 5, 384 - 86 - 5, walker.rightUpperNeighbor());
        assertGetGridPositionReturnsExpectedField(center, 512 + 50 + 5, 384 - 173 + 5, walker.upperNeighbor());
        assertGetGridPositionReturnsExpectedField(center, 512, 384 + 84, center);
        assertGetGridPositionReturnsExpectedField(center, 512, 384 + 88, walker.lowerNeighbor());
        assertGetGridPositionReturnsExpectedField(center, 512, 384 + 173 + 84, walker.lowerNeighbor());
        assertGetGridPositionReturnsExpectedField(center, 512, 384 + 173 + 88, new HexGridWalker(walker.lowerNeighbor()).lowerNeighbor());
        assertGetGridPositionReturnsExpectedField(center, 512 - 100 + 5, 384 + 86 + 5, walker.leftLowerNeighbor());
        assertGetGridPositionReturnsExpectedField(center, 512 - 50 - 5, 384 + 173 - 5, walker.lowerNeighbor());
        assertGetGridPositionReturnsExpectedField(center, 512 + 100 - 5, 384 + 86 + 5, walker.rightLowerNeighbor());
        assertGetGridPositionReturnsExpectedField(center, 512 + 50 + 5, 384 + 173 - 5, walker.lowerNeighbor());
    }

    /**
     * For testing {@link HexGridTools#getGridPosition(Dimension, Dimension, Point, Point)}.
     * 
     * @param xPos
     *            x position
     * @param yPos
     *            y position
     */
    private static void testGetGridPosition(int xPos, int yPos) {
        Point center = new Point(xPos * 2, yPos * 2);
        testGetGridPositionAtGridCoordinates(center);
        center.translate(0, 1);
        testGetGridPositionAtGridCoordinates(center);
        center.translate(1, -1);
        testGetGridPositionAtGridCoordinates(center);
        center.translate(0, 1);
        testGetGridPositionAtGridCoordinates(center);
    }

    /** Tests {@link HexGridTools#getGridPosition(Dimension, Dimension, Point, Point)}. */
    @Test
    public void testGetGridPosition() {
        for (int x = -4; x++ < 4; ) for (int y = -4; y++ < 4; ) testGetGridPosition(x, y);
    }

    /**
     * For testing {@link HexGridTools#getVisibleFields(Dimension, Dimension, Point)}.
     * 
     * @param xPos
     *            x position
     * @param yPos
     *            y position
     */
    private static void testGetVisibleFields(int xPos, int yPos) {
        Dimension window = new Dimension(20, 20);
        int x = xPos * 2;
        int y = yPos * 2;
        Dimension fieldSize = new Dimension(100, 86);
        Point center = new Point(x, y);
        ArrayList<Point> cont = new ArrayList<Point>();
        cont.add(center);
        List<Point> aL = HexGridTools.getVisibleFields(window, fieldSize, center);
        assertTrue(aL.containsAll(cont));
        window = new Dimension(20, 100);
        cont.add(new Point(x, y - 1));
        cont.add(new Point(x, y + 1));
        aL = HexGridTools.getVisibleFields(window, fieldSize, center);
        assertTrue(aL.containsAll(cont));
        window = new Dimension(75, 30);
        cont = new ArrayList<Point>();
        cont.add(center);
        cont.add(new Point(x - 1, y - 1));
        cont.add(new Point(x - 1, y));
        cont.add(new Point(x + 1, y - 1));
        cont.add(new Point(x + 1, y));
        aL = HexGridTools.getVisibleFields(window, fieldSize, center);
        assertTrue(aL.containsAll(cont));
        window = new Dimension(75, 100);
        cont.add(new Point(x, y - 1));
        cont.add(new Point(x, y + 1));
        aL = HexGridTools.getVisibleFields(window, fieldSize, center);
        assertTrue(aL.containsAll(cont));
        window = new Dimension(75, 175);
        cont.add(new Point(x - 1, y - 2));
        cont.add(new Point(x - 1, y + 1));
        cont.add(new Point(x + 1, y - 2));
        cont.add(new Point(x + 1, y + 1));
        aL = HexGridTools.getVisibleFields(window, fieldSize, center);
        assertTrue(aL.containsAll(cont));
        x++;
        y++;
        center = new Point(x, y);
        cont = new ArrayList<Point>();
        cont.add(center);
        aL = HexGridTools.getVisibleFields(window, fieldSize, center);
        assertTrue(aL.containsAll(cont));
        window = new Dimension(20, 100);
        cont.add(new Point(x, y - 1));
        cont.add(new Point(x, y + 1));
        aL = HexGridTools.getVisibleFields(window, fieldSize, center);
        assertTrue(aL.containsAll(cont));
        window = new Dimension(75, 30);
        cont = new ArrayList<Point>();
        cont.add(center);
        cont.add(new Point(x - 1, y));
        cont.add(new Point(x - 1, y + 1));
        cont.add(new Point(x + 1, y));
        cont.add(new Point(x + 1, y + 1));
        aL = HexGridTools.getVisibleFields(window, fieldSize, center);
        assertTrue(aL.containsAll(cont));
        window = new Dimension(75, 100);
        cont.add(new Point(x, y - 1));
        cont.add(new Point(x, y + 1));
        aL = HexGridTools.getVisibleFields(window, fieldSize, center);
        assertTrue(aL.containsAll(cont));
        window = new Dimension(75, 175);
        cont.add(new Point(x - 1, y - 1));
        cont.add(new Point(x - 1, y + 2));
        cont.add(new Point(x + 1, y - 1));
        cont.add(new Point(x + 1, y + 2));
        aL = HexGridTools.getVisibleFields(window, fieldSize, center);
        assertTrue(aL.containsAll(cont));
    }

    /** Tests {@link HexGridTools#getVisibleFields(Dimension, Dimension, Point)}. */
    @Test
    public void testGetVisibleFields() {
        for (int x = -3; x < 4; x++) for (int y = -3; y < 4; y++) testGetVisibleFields(x, y);
    }

    /**
     * For testing {@link HexGridTools#getPixelPosition(Dimension, Dimension, Point, Point)}.
     * 
     * @param xPos
     *            x position
     * @param yPos
     *            y position
     */
    private static void testGetPixelPosition(int xPos, int yPos) {
        Dimension window = new Dimension(600, 400);
        Point cPix = HexGridTools.getCenter(window);
        Dimension fieldSize = new Dimension(100, 86);
        int x = xPos * 2;
        int y = yPos * 2;
        Point center = new Point(x, y);
        Point test = new Point(x, y + 1);
        cPix.y += 86;
        Point res = HexGridTools.getPixelPosition(window, fieldSize, center, test);
        assertTrue(res.equals(cPix));
        test = new Point(x, y - 1);
        cPix = HexGridTools.getCenter(window);
        cPix.y -= 86;
        res = HexGridTools.getPixelPosition(window, fieldSize, center, test);
        assertTrue(res.equals(cPix));
        test = new Point(x, y - 2);
        cPix = HexGridTools.getCenter(window);
        cPix.y -= 86 * 2;
        res = HexGridTools.getPixelPosition(window, fieldSize, center, test);
        assertTrue(res.equals(cPix));
        test = new Point(x - 1, y);
        cPix = HexGridTools.getCenter(window);
        cPix.x -= 75;
        cPix.y += 43;
        res = HexGridTools.getPixelPosition(window, fieldSize, center, test);
        assertTrue(res.equals(cPix));
        test = new Point(x - 3, y);
        cPix = HexGridTools.getCenter(window);
        cPix.x -= 75 * 3;
        cPix.y += 43;
        res = HexGridTools.getPixelPosition(window, fieldSize, center, test);
        assertTrue(res.equals(cPix));
        test = new Point(x + 3, y);
        cPix = HexGridTools.getCenter(window);
        cPix.x += 75 * 3;
        cPix.y += 43;
        res = HexGridTools.getPixelPosition(window, fieldSize, center, test);
        assertTrue(res.equals(cPix));
        test = new Point(x - 2, y);
        cPix = HexGridTools.getCenter(window);
        cPix.x -= 75 * 2;
        res = HexGridTools.getPixelPosition(window, fieldSize, center, test);
        assertTrue(res.equals(cPix));
        test = new Point(x + 1, y + 2);
        cPix = HexGridTools.getCenter(window);
        cPix.x += 75;
        cPix.y += 43;
        cPix.y += 86 * 2;
        res = HexGridTools.getPixelPosition(window, fieldSize, center, test);
        assertTrue(res.equals(cPix));
        test = new Point(x - 1, y - 1);
        cPix = HexGridTools.getCenter(window);
        cPix.x -= 75;
        cPix.y += 43;
        cPix.y -= 86;
        res = HexGridTools.getPixelPosition(window, fieldSize, center, test);
        assertTrue(res.equals(cPix));
        test = new Point(x + 5, y + 3);
        cPix = HexGridTools.getCenter(window);
        cPix.x += 75 * 5;
        cPix.y += 43;
        cPix.y += 86 * 3;
        res = HexGridTools.getPixelPosition(window, fieldSize, center, test);
        assertTrue(res.equals(cPix));
        x += 1;
        y += 1;
        center = new Point(x, y);
        test = new Point(x, y + 1);
        cPix = HexGridTools.getCenter(window);
        cPix.y += 86;
        res = HexGridTools.getPixelPosition(window, fieldSize, center, test);
        assertTrue(res.equals(cPix));
        test = new Point(x, y - 1);
        cPix = HexGridTools.getCenter(window);
        cPix.y -= 86;
        res = HexGridTools.getPixelPosition(window, fieldSize, center, test);
        assertTrue(res.equals(cPix));
        test = new Point(x, y - 2);
        cPix = HexGridTools.getCenter(window);
        cPix.y -= 86 * 2;
        res = HexGridTools.getPixelPosition(window, fieldSize, center, test);
        assertTrue(res.equals(cPix));
        test = new Point(x - 1, y);
        cPix = HexGridTools.getCenter(window);
        cPix.x -= 75;
        cPix.y -= 43;
        res = HexGridTools.getPixelPosition(window, fieldSize, center, test);
        assertTrue(res.equals(cPix));
        test = new Point(x - 3, y);
        cPix = HexGridTools.getCenter(window);
        cPix.x -= 75 * 3;
        cPix.y -= 43;
        res = HexGridTools.getPixelPosition(window, fieldSize, center, test);
        assertTrue(res.equals(cPix));
        test = new Point(x + 3, y);
        cPix = HexGridTools.getCenter(window);
        cPix.x += 75 * 3;
        cPix.y -= 43;
        res = HexGridTools.getPixelPosition(window, fieldSize, center, test);
        assertTrue(res.equals(cPix));
        test = new Point(x - 2, y);
        cPix = HexGridTools.getCenter(window);
        cPix.x -= 75 * 2;
        res = HexGridTools.getPixelPosition(window, fieldSize, center, test);
        assertTrue(res.equals(cPix));
        test = new Point(x + 1, y + 2);
        cPix = HexGridTools.getCenter(window);
        cPix.x += 75;
        cPix.y -= 43;
        cPix.y += 86 * 2;
        res = HexGridTools.getPixelPosition(window, fieldSize, center, test);
        assertTrue(res.equals(cPix));
        test = new Point(x - 1, y - 1);
        cPix = HexGridTools.getCenter(window);
        cPix.x -= 75;
        cPix.y -= 43;
        cPix.y -= 86;
        res = HexGridTools.getPixelPosition(window, fieldSize, center, test);
        assertTrue(res.equals(cPix));
        test = new Point(x + 5, y + 3);
        cPix = HexGridTools.getCenter(window);
        cPix.x += 75 * 5;
        cPix.y -= 43;
        cPix.y += 86 * 3;
        res = HexGridTools.getPixelPosition(window, fieldSize, center, test);
        assertTrue(res.equals(cPix));
    }

    /** Tests {@link HexGridTools#getPixelPosition(Dimension, Dimension, Point, Point)}. */
    @Test
    public void testGetPixelPosition() {
        for (int x = -3; x < 4; x++) for (int y = -3; y < 4; y++) testGetPixelPosition(x, y);
    }
}
