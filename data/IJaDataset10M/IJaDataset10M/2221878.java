package net.sf.planofattack.drawable;

import java.awt.Point;
import junit.framework.TestCase;
import test.util.AssertDrawableUtil;

public class RectangleTest extends TestCase {

    private static final int UPPER_LEFT = 0;

    private static final int UPPER_RIGHT = 1;

    private static final int LOWER_RIGHT = 2;

    private static final int LOWER_LEFT = 3;

    public void testGetHightlightPoints_FromZeroZero() {
        Rectangle rect = new Rectangle(0, 0, 100, 50, null);
        AssertDrawableUtil.assertHightlightPoints(rect, new Point[] { new Point(0, 0), new Point(100, 0), new Point(100, 50), new Point(0, 50), new Point(50, 25) });
    }

    public void testGetHightlightPoints_FromOffsetPoint() {
        Rectangle rect = new Rectangle(10, 10, 100, 50, null);
        AssertDrawableUtil.assertHightlightPoints(rect, new Point[] { new Point(10, 10), new Point(110, 10), new Point(110, 60), new Point(10, 60), new Point(60, 35) });
    }

    public void testUpdate_LowerRight_ToRight() {
        Rectangle rect = new Rectangle(10, 10, 100, 50, null);
        rect.clickOnAt(110, 60);
        rect.update(200, 60);
        AssertDrawableUtil.assertDrawableWidth(rect, 190);
        AssertDrawableUtil.assertDrawableHeight(rect, 50);
        AssertDrawableUtil.assertHightlightPoints(rect, new Point[] { new Point(10, 10), new Point(200, 10), new Point(200, 60), new Point(10, 60), new Point(105, 35) });
    }

    public void testUpdate_UpperRight() {
        Rectangle rect = new Rectangle(10, 10, 90, 50, null);
        rect.clickOnAt(100, 10);
        rect.update(90, 0);
        AssertDrawableUtil.assertDrawableWidth(rect, 80);
        AssertDrawableUtil.assertDrawableHeight(rect, 60);
        AssertDrawableUtil.assertHightlightPoints(rect, new Point[] { new Point(10, 0), new Point(90, 0), new Point(90, 60), new Point(10, 60), new Point(50, 30) });
    }

    public void testUpdate_UpperLeft() {
        Rectangle rect = new Rectangle(10, 10, 100, 50, null);
        rect.clickOnAt(10, 10);
        rect.update(0, 0);
        AssertDrawableUtil.assertDrawableWidth(rect, 110);
        AssertDrawableUtil.assertDrawableHeight(rect, 60);
        AssertDrawableUtil.assertHightlightPoints(rect, new Point[] { new Point(0, 0), new Point(110, 0), new Point(110, 60), new Point(0, 60), new Point(55, 30) });
    }

    public void testUpdate_LowerLeft() {
        Rectangle rect = new Rectangle(10, 10, 100, 50, null);
        rect.clickOnAt(10, 60);
        rect.update(0, 70);
        AssertDrawableUtil.assertDrawableWidth(rect, 110);
        AssertDrawableUtil.assertDrawableHeight(rect, 60);
        AssertDrawableUtil.assertHightlightPoints(rect, new Point[] { new Point(0, 10), new Point(110, 10), new Point(110, 70), new Point(0, 70), new Point(55, 40) });
    }

    public void testUpdate_LowerRight_GoesPastLeftSide() {
        Rectangle rect = new Rectangle(0, 0, 10, 5, null);
        Point point = getHighlightPoint(rect, LOWER_RIGHT);
        rect.clickOnAt(point.x, point.y);
        rect.update(-10, 5);
        AssertDrawableUtil.assertDrawableWidth(rect, 10);
        AssertDrawableUtil.assertDrawableHeight(rect, 5);
        AssertDrawableUtil.assertHightlightPoints(rect, new Point[] { new Point(-10, 0), new Point(0, 0), new Point(0, 5), new Point(-10, 5), new Point(-5, 2) });
    }

    public void testUpdate_LowerRight_GoesPastTop() {
        Rectangle rect = new Rectangle(0, 5, 10, 10, null);
        Point point = getHighlightPoint(rect, LOWER_RIGHT);
        rect.clickOnAt(point.x, point.y);
        rect.update(10, 0);
        AssertDrawableUtil.assertDrawableWidth(rect, 10);
        AssertDrawableUtil.assertDrawableHeight(rect, 5);
        AssertDrawableUtil.assertHightlightPoints(rect, new Point[] { new Point(0, 0), new Point(10, 0), new Point(10, 5), new Point(0, 5), new Point(5, 2) });
    }

    public void testUpdate_LowerLeft_GoesPastRightSide() {
        Rectangle rect = new Rectangle(-10, 0, 10, 5, null);
        Point point = getHighlightPoint(rect, LOWER_LEFT);
        rect.clickOnAt(point.x, point.y);
        rect.update(10, 5);
        AssertDrawableUtil.assertDrawableWidth(rect, 10);
        AssertDrawableUtil.assertDrawableHeight(rect, 5);
        AssertDrawableUtil.assertHightlightPoints(rect, new Point[] { new Point(0, 0), new Point(10, 0), new Point(10, 5), new Point(0, 5), new Point(5, 2) });
    }

    public void testUpdate_LowerLeft_GoesPastTop() {
        Rectangle rect = new Rectangle(0, 5, 10, 10, null);
        Point point = getHighlightPoint(rect, LOWER_LEFT);
        rect.clickOnAt(point.x, point.y);
        rect.update(0, 0);
        AssertDrawableUtil.assertDrawableWidth(rect, 10);
        AssertDrawableUtil.assertDrawableHeight(rect, 5);
        AssertDrawableUtil.assertHightlightPoints(rect, new Point[] { new Point(0, 0), new Point(10, 0), new Point(10, 5), new Point(0, 5), new Point(5, 2) });
    }

    public void testUpdate_UpperLeft_GoesPastRightSide() {
        Rectangle rect = new Rectangle(0, 0, 10, 5, null);
        Point point = getHighlightPoint(rect, UPPER_LEFT);
        rect.clickOnAt(point.x, point.y);
        rect.update(15, 0);
        AssertDrawableUtil.assertDrawableWidth(rect, 5);
        AssertDrawableUtil.assertDrawableHeight(rect, 5);
        AssertDrawableUtil.assertHightlightPoints(rect, new Point[] { new Point(10, 0), new Point(15, 0), new Point(15, 5), new Point(10, 5), new Point(12, 2) });
    }

    public void testUpdate_UpperLeft_GoesPastBottom() {
        Rectangle rect = new Rectangle(0, 0, 10, 5, null);
        Point point = getHighlightPoint(rect, UPPER_LEFT);
        rect.clickOnAt(point.x, point.y);
        rect.update(0, 15);
        AssertDrawableUtil.assertDrawableWidth(rect, 10);
        AssertDrawableUtil.assertDrawableHeight(rect, 10);
        AssertDrawableUtil.assertHightlightPoints(rect, new Point[] { new Point(0, 5), new Point(10, 5), new Point(10, 15), new Point(0, 15), new Point(5, 10) });
    }

    public void testUpdate_UpperRight_GoesPastBottom() {
        Rectangle rect = new Rectangle(0, 0, 10, 5, null);
        Point point = getHighlightPoint(rect, UPPER_RIGHT);
        rect.clickOnAt(point.x, point.y);
        rect.update(10, 15);
        AssertDrawableUtil.assertDrawableWidth(rect, 10);
        AssertDrawableUtil.assertDrawableHeight(rect, 10);
        AssertDrawableUtil.assertHightlightPoints(rect, new Point[] { new Point(0, 5), new Point(10, 5), new Point(10, 15), new Point(0, 15), new Point(5, 10) });
    }

    public void testUpdate_UpperRight_GoesPastLeftSide() {
        Rectangle rect = new Rectangle(0, 0, 10, 5, null);
        Point point = getHighlightPoint(rect, UPPER_RIGHT);
        rect.clickOnAt(point.x, point.y);
        rect.update(-10, 0);
        AssertDrawableUtil.assertDrawableWidth(rect, 10);
        AssertDrawableUtil.assertDrawableHeight(rect, 5);
        AssertDrawableUtil.assertHightlightPoints(rect, new Point[] { new Point(-10, 0), new Point(0, 0), new Point(0, 5), new Point(-10, 5), new Point(-5, 2) });
    }

    private Point getHighlightPoint(Rectangle rect, int point) {
        return rect.getHightlightPoints().get(point);
    }

    public void testUpdate_Move() {
        Rectangle rect = new Rectangle(0, 0, 100, 50, null);
        rect.clickOnAt(50, 25);
        rect.update(60, 35);
        AssertDrawableUtil.assertDrawableWidth(rect, 100);
        AssertDrawableUtil.assertDrawableHeight(rect, 50);
        AssertDrawableUtil.assertHightlightPoints(rect, new Point[] { new Point(10, 10), new Point(110, 10), new Point(110, 60), new Point(10, 60), new Point(60, 35) });
    }
}
