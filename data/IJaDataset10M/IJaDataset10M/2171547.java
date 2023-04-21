package net.sourceforge.jaulp.layout;

import java.awt.Point;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.sourceforge.jaulp.BaseTestCase;

/**
 * Test class for the class ScreenSizeUtils.
 * 
 * @version 1.0
 * @author Asterios Raptis
 */
public class ScreenSizeUtilsTest extends BaseTestCase {

    /**
     * Sets the up.
     *
     * @throws Exception the exception
     * {@inheritDoc}
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * Tear down.
     *
     * @throws Exception the exception
     * {@inheritDoc}
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test method for.
     *
     * {@link net.sourceforge.jaulp.layout.ScreenSizeUtils#computeDialogPositions(int, int)}
     * .
     */
    @SuppressWarnings("unchecked")
    public void testComputeDialogPositions() {
        final List expected = new ArrayList();
        final int dialogHeight = 200;
        final int dialogWidth = 250;
        final int windowBesides = ScreenSizeUtils.getScreenWidth() / dialogWidth;
        final int windowBelow = ScreenSizeUtils.getScreenHeight() / dialogHeight;
        final int listSize = windowBesides * windowBelow;
        final List dialogPositions = ScreenSizeUtils.computeDialogPositions(dialogWidth, dialogHeight);
        this.result = listSize == dialogPositions.size();
        assertTrue("", this.result);
        int dotWidth = 0;
        int dotHeight = 0;
        for (int y = 0; y < windowBelow; y++) {
            dotWidth = 0;
            for (int x = 0; x < windowBesides; x++) {
                final Point p = new Point(dotWidth, dotHeight);
                expected.add(p);
                dotWidth = dotWidth + dialogWidth;
            }
            dotHeight = dotHeight + dialogHeight;
        }
        for (final Iterator iterator = dialogPositions.iterator(); iterator.hasNext(); ) {
            final Point point = (Point) iterator.next();
            this.result = expected.contains(point);
            assertTrue("", this.result);
        }
    }

    /**
     * Test method for.
     *
     * {@link net.sourceforge.jaulp.layout.ScreenSizeUtils#getPoint()}.
     */
    public void testGetPoint() {
        final Point screenpoint = ScreenSizeUtils.getPoint();
        this.result = screenpoint.x == ScreenSizeUtils.getScreenWidth();
        assertTrue("", this.result);
        this.result = screenpoint.y == ScreenSizeUtils.getScreenHeight();
        assertTrue("", this.result);
    }

    /**
     * Test method for.
     *
     * {@link net.sourceforge.jaulp.layout.ScreenSizeUtils#getScreenHeight()}.
     */
    public void testGetScreenHeight() {
        final int expected = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        final int compare = ScreenSizeUtils.getScreenHeight();
        this.result = expected == compare;
        assertTrue("", this.result);
    }

    /**
     * Test method for.
     *
     * {@link net.sourceforge.jaulp.layout.ScreenSizeUtils#getScreenWidth()}.
     */
    public void testGetScreenWidth() {
        final int expected = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        final int compare = ScreenSizeUtils.getScreenWidth();
        this.result = expected == compare;
        assertTrue("", this.result);
    }
}
