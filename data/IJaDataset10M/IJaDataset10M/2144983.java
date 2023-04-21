package org.jaitools.imageutils.iterator;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;
import java.util.Random;
import javax.media.jai.PlanarImage;
import org.jaitools.imageutils.TestBase;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for SimpleIterator.
 * 
 * @author michael
 */
public class SimpleIteratorTest extends TestBase {

    private static final int NUM_RESET_TESTS = 10;

    private static final int WIDTH = 17;

    private static final int HEIGHT = 19;

    private static final int NUM_BANDS = 3;

    private static final int OUTSIDE = -99;

    private SimpleIterator iter;

    private PlanarImage image;

    @Test(expected = IllegalArgumentException.class)
    public void nullImageArg() {
        iter = new SimpleIterator(null, new Rectangle(0, 0, WIDTH, HEIGHT), OUTSIDE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullOrderArg() {
        image = createSequentialImage(WIDTH, HEIGHT, 1);
        iter = new SimpleIterator(image, image.getBounds(), OUTSIDE, null);
    }

    @Test
    public void nullToGetSample() {
        image = createSequentialImage(WIDTH, HEIGHT, 1);
        iter = new SimpleIterator(image, null, OUTSIDE);
        boolean gotEx = false;
        try {
            iter.getSample(null);
        } catch (IllegalArgumentException ex) {
            gotEx = true;
        }
        assertTrue(gotEx);
    }

    @Test
    public void nullToSetPosPoint() {
        image = createSequentialImage(WIDTH, HEIGHT, 1);
        iter = new SimpleIterator(image, null, OUTSIDE);
        boolean gotEx = false;
        try {
            iter.setPos(null);
        } catch (IllegalArgumentException ex) {
            gotEx = true;
        }
        assertTrue(gotEx);
    }

    @Test
    public void getSampleOutsideBoundsReturnsNull() {
        image = createSequentialImage(WIDTH, HEIGHT, 1);
        iter = new SimpleIterator(image, null, OUTSIDE);
        assertNull(iter.getSample(-1, -1));
    }

    @Test
    public void setPosOutsideBoundsReturnsFalse() {
        image = createSequentialImage(WIDTH, HEIGHT, 1);
        iter = new SimpleIterator(image, null, OUTSIDE);
        assertFalse(iter.setPos(-1, -1));
    }

    @Test
    public void getImage() {
        image = createSequentialImage(WIDTH, HEIGHT, NUM_BANDS);
        iter = new SimpleIterator(image, null, OUTSIDE);
        assertTrue(image == iter.getImage());
    }

    @Test
    public void getPosWithDestinationArg() throws Exception {
        final Point origin = new Point(-7, 11);
        image = createSequentialImage(origin.x, origin.y, WIDTH, HEIGHT, NUM_BANDS, 0);
        iter = new SimpleIterator(image, null, OUTSIDE, SimpleIterator.Order.IMAGE_X_Y);
        Point pos = new Point();
        Point posRtn = null;
        posRtn = iter.getPos(pos);
        assertEquals(origin, pos);
        assertTrue(posRtn == pos);
    }

    @Test
    public void getPosWithNullDestinationArg() throws Exception {
        final Point origin = new Point(-7, 11);
        image = createSequentialImage(origin.x, origin.y, WIDTH, HEIGHT, NUM_BANDS, 0);
        iter = new SimpleIterator(image, null, OUTSIDE, SimpleIterator.Order.IMAGE_X_Y);
        Point pos = iter.getPos(null);
        assertEquals(origin, pos);
    }

    @Test
    public void getStartPos() {
        final Point origin = new Point(-7, 11);
        image = createSequentialImage(origin.x, origin.y, WIDTH, HEIGHT, NUM_BANDS, 0);
        iter = new SimpleIterator(image, null, OUTSIDE);
        assertEquals(origin, iter.getStartPos());
    }

    @Test
    public void getEndPos() {
        final Point origin = new Point(-7, 11);
        final Point endPos = new Point(origin.x + WIDTH - 1, origin.y + HEIGHT - 1);
        image = createSequentialImage(origin.x, origin.y, WIDTH, HEIGHT, NUM_BANDS, 0);
        iter = new SimpleIterator(image, null, OUTSIDE);
        assertEquals(endPos, iter.getEndPos());
    }

    @Test
    public void isWithinImage() {
        image = createSequentialImage(WIDTH, HEIGHT, 1);
        final Rectangle imageBounds = image.getBounds();
        final Rectangle iterBounds = createAdjustedBounds(imageBounds, 5);
        iter = new SimpleIterator(image, iterBounds, OUTSIDE);
        do {
            assertEquals(imageBounds.contains(iter.getPos()), iter.isWithinImage());
        } while (iter.next());
    }

    @Test
    public void nullBoundsMeansImageBounds() {
        image = createSequentialImage(WIDTH, HEIGHT, NUM_BANDS);
        iter = new SimpleIterator(image, null, null);
        Rectangle imageBounds = image.getBounds();
        assertEquals(imageBounds, iter.getBounds());
    }

    @Test
    public void checkSubBoundsWhenUsingDefaultImageBounds() throws Exception {
        final int minX = -3;
        final int minY = 3;
        final int tileWidth = 7;
        final int tileHeight = 11;
        assertSubBounds(minX, minY, tileWidth, tileHeight, null);
    }

    @Test
    public void checkSubBoundsWhenIterBoundsPartlyOverlapImageBounds() throws Exception {
        final int minX = -3;
        final int minY = 3;
        final int tileWidth = 7;
        final int tileHeight = 11;
        Rectangle iterBounds = new Rectangle(-10, 0, WIDTH, HEIGHT);
        assertSubBounds(minX, minY, tileWidth, tileHeight, iterBounds);
    }

    @Test
    public void checkSubBoundsWhenIterBoundsLieWithinImageBounds() throws Exception {
        final int minX = -3;
        final int minY = 3;
        final int tileWidth = 7;
        final int tileHeight = 11;
        Rectangle iterBounds = new Rectangle(0, 6, WIDTH / 2, HEIGHT / 2);
        assertSubBounds(minX, minY, tileWidth, tileHeight, iterBounds);
    }

    /**
     * Tests that:
     * <ul>
     * <li>all sub-bounds are within iterator bounds</li>
     * <li>no overlap between sub-bounds</li>
     * <li>union of sub-bounds equals iterator bounds with no gaps between sub-bounds</li>
     * </ul>
     */
    private void assertSubBounds(int minX, int minY, int tileWidth, int tileHeight, Rectangle iterBounds) {
        image = createSequentialTiledImage(minX, minY, WIDTH, HEIGHT, tileWidth, tileHeight, 1, 0);
        if (iterBounds == null) {
            iterBounds = image.getBounds();
        }
        iter = new SimpleIterator(image, iterBounds, null, SimpleIterator.Order.TILE_X_Y);
        List<Rectangle> subBounds = iter.getSubBounds();
        for (Rectangle r : subBounds) {
            assertTrue(iterBounds.contains(r));
        }
        for (int i = 0; i < subBounds.size() - 1; i++) {
            Rectangle ri = subBounds.get(i);
            for (int j = i + 1; j < subBounds.size(); j++) {
                Rectangle rj = subBounds.get(j);
                if (ri.intersects(rj)) {
                    String msg = String.format("Overlap between subBound %d [x=%d y=%d w=%d h=%d] " + "and subBound %d [x=%d y=%d w=%d h=%d]", i, ri.x, ri.y, ri.width, ri.height, j, rj.x, rj.y, rj.width, rj.height);
                    fail(msg);
                }
            }
        }
        boolean[][] array = new boolean[iterBounds.height][iterBounds.width];
        for (Rectangle r : subBounds) {
            for (int y = r.y; y < r.y + r.height; y++) {
                int yy = y - iterBounds.y;
                for (int x = r.x; x < r.x + r.width; x++) {
                    int xx = x - iterBounds.x;
                    array[yy][xx] = true;
                }
            }
        }
        for (int yy = 0; yy < iterBounds.height; yy++) {
            for (int xx = 0; xx < iterBounds.width; xx++) {
                if (!array[yy][xx]) {
                    fail(String.format("gap between sub-bounds at image position: %d %d", xx + iterBounds.x, yy + iterBounds.y));
                }
            }
        }
    }

    @Test
    public void iterateOverImage() {
        image = createSequentialImage(WIDTH, HEIGHT, NUM_BANDS);
        iter = new SimpleIterator(image, null, null);
        assertSamples();
    }

    @Test
    public void iterateByTile() {
        image = createSequentialTiledImage(0, 0, WIDTH, HEIGHT, WIDTH / 2, HEIGHT / 2, NUM_BANDS, 0);
        iter = new SimpleIterator(image, null, null, SimpleIterator.Order.TILE_X_Y);
        assertSamples();
    }

    @Test
    public void boundsWhollyOutsideImage() {
        image = createSequentialImage(WIDTH, HEIGHT, NUM_BANDS);
        Rectangle iterBounds = new Rectangle(-WIDTH, -HEIGHT, WIDTH, HEIGHT);
        iter = new SimpleIterator(image, iterBounds, OUTSIDE);
        assertSamples();
    }

    @Test
    public void iterBoundsBeyondImageBounds() {
        image = createSequentialImage(WIDTH, HEIGHT, NUM_BANDS);
        Rectangle iterBounds = createAdjustedBounds(image.getBounds(), 5);
        iter = new SimpleIterator(image, iterBounds, OUTSIDE);
        assertSamples();
    }

    @Test
    public void iterBoundsBeyondImageBounds_ByTile() {
        image = createSequentialTiledImage(0, 0, WIDTH, HEIGHT, WIDTH / 2, HEIGHT / 2, NUM_BANDS, 0);
        Rectangle iterBounds = createAdjustedBounds(image.getBounds(), 5);
        iter = new SimpleIterator(image, iterBounds, OUTSIDE, SimpleIterator.Order.TILE_X_Y);
        assertSamples();
    }

    @Test
    public void imageWithNonZeroOrigin() {
        image = createSequentialImage(-7, 11, WIDTH, HEIGHT, NUM_BANDS, 0);
        iter = new SimpleIterator(image, null, OUTSIDE);
        assertSamples();
    }

    @Test
    public void tiledImageWithNonZeroOrigin() {
        image = createSequentialTiledImage(-7, 11, WIDTH, HEIGHT, WIDTH / 2, HEIGHT / 2, NUM_BANDS, 0);
        iter = new SimpleIterator(image, null, OUTSIDE, SimpleIterator.Order.TILE_X_Y);
        assertSamples();
    }

    @Test
    public void iterBoundsOutsideImageWithNonZeroOrigin() {
        image = createSequentialImage(-7, 11, WIDTH, HEIGHT, NUM_BANDS, 0);
        final int MARGIN = 5;
        Rectangle imageBounds = image.getBounds();
        Rectangle iterBounds = new Rectangle(imageBounds.x - MARGIN, imageBounds.y - MARGIN, imageBounds.width + 2 * MARGIN, imageBounds.height + 2 * MARGIN);
        iter = new SimpleIterator(image, iterBounds, OUTSIDE);
        assertSamples();
    }

    @Test
    public void iterBoundsOutsideImageWithNonZeroOrigin_ByTile() {
        image = createSequentialTiledImage(-7, 11, WIDTH, HEIGHT, WIDTH / 2, HEIGHT / 2, NUM_BANDS, 0);
        final int MARGIN = 5;
        Rectangle imageBounds = image.getBounds();
        Rectangle iterBounds = new Rectangle(imageBounds.x - MARGIN, imageBounds.y - MARGIN, imageBounds.width + 2 * MARGIN, imageBounds.height + 2 * MARGIN);
        iter = new SimpleIterator(image, iterBounds, OUTSIDE, SimpleIterator.Order.TILE_X_Y);
        assertSamples();
    }

    @Test
    public void iterBoundsShortAndWide() {
        image = createSequentialImage(WIDTH, HEIGHT, NUM_BANDS);
        Rectangle imageBounds = image.getBounds();
        Rectangle iterBounds = new Rectangle(imageBounds.x - 10, imageBounds.y + imageBounds.height / 4, imageBounds.width + 20, imageBounds.height / 2);
        iter = new SimpleIterator(image, iterBounds, OUTSIDE);
        assertSamples();
    }

    @Test
    public void iterBoundsShortAndWide_ByTile() {
        image = createSequentialTiledImage(0, 0, WIDTH, HEIGHT, WIDTH / 2, HEIGHT / 2, NUM_BANDS, 0);
        Rectangle imageBounds = image.getBounds();
        Rectangle iterBounds = new Rectangle(imageBounds.x - 10, imageBounds.y + imageBounds.height / 4, imageBounds.width + 20, imageBounds.height / 2);
        iter = new SimpleIterator(image, iterBounds, OUTSIDE, SimpleIterator.Order.TILE_X_Y);
        assertSamples();
    }

    @Test
    public void iterBoundsTallAndThin() {
        image = createSequentialImage(WIDTH, HEIGHT, NUM_BANDS);
        Rectangle imageBounds = image.getBounds();
        Rectangle iterBounds = new Rectangle(imageBounds.x + imageBounds.width / 4, imageBounds.y - 10, imageBounds.width / 2, imageBounds.height + 20);
        iter = new SimpleIterator(image, iterBounds, OUTSIDE);
        assertSamples();
    }

    @Test
    public void iterBoundsTallAndThin_ByTile() {
        image = createSequentialTiledImage(0, 0, WIDTH, HEIGHT, WIDTH / 2, HEIGHT / 2, NUM_BANDS, 0);
        Rectangle imageBounds = image.getBounds();
        Rectangle iterBounds = new Rectangle(imageBounds.x + imageBounds.width / 4, imageBounds.y - 10, imageBounds.width / 2, imageBounds.height + 20);
        iter = new SimpleIterator(image, iterBounds, OUTSIDE, SimpleIterator.Order.TILE_X_Y);
        assertSamples();
    }

    @Test
    public void resetIteratorWithInnerBounds() {
        image = createSequentialImage(WIDTH, HEIGHT, NUM_BANDS);
        Rectangle iterBounds = createAdjustedBounds(image.getBounds(), -1);
        iter = new SimpleIterator(image, iterBounds, OUTSIDE);
        doResetTest();
    }

    @Test
    public void resetIteratorWithInnerBounds_ByTile() {
        image = createSequentialTiledImage(0, 0, WIDTH, HEIGHT, WIDTH / 2, HEIGHT / 2, NUM_BANDS, 0);
        Rectangle iterBounds = createAdjustedBounds(image.getBounds(), -1);
        iter = new SimpleIterator(image, iterBounds, OUTSIDE, SimpleIterator.Order.TILE_X_Y);
        doResetTest();
    }

    @Test
    public void resetIteratorWithOuterBounds() {
        image = createSequentialImage(WIDTH, HEIGHT, NUM_BANDS);
        Rectangle iterBounds = createAdjustedBounds(image.getBounds(), 1);
        iter = new SimpleIterator(image, iterBounds, OUTSIDE);
        doResetTest();
    }

    @Test
    public void resetIteratorWithOuterBounds_ByTile() {
        image = createSequentialTiledImage(0, 0, WIDTH, HEIGHT, WIDTH / 2, HEIGHT / 2, NUM_BANDS, 0);
        Rectangle iterBounds = createAdjustedBounds(image.getBounds(), 1);
        iter = new SimpleIterator(image, iterBounds, OUTSIDE, SimpleIterator.Order.TILE_X_Y);
        doResetTest();
    }

    private void doResetTest() {
        moveToEnd(iter);
        iter.reset();
        assertSamples();
        Rectangle iterBounds = iter.getBounds();
        final int N = iterBounds.width * iterBounds.height;
        Random rand = new Random();
        for (int i = 0; i < NUM_RESET_TESTS; i++) {
            int n = rand.nextInt(N) + 1;
            while (n > 0) {
                iter.next();
                n--;
            }
            iter.reset();
            assertSamples();
        }
    }

    @Test
    public void setPosBeforeNext() {
        image = createSequentialImage(WIDTH, HEIGHT, NUM_BANDS);
        iter = new SimpleIterator(image, null, OUTSIDE);
        assertTrue(iter.setPos(WIDTH / 2, HEIGHT / 2));
        assertSamples();
    }

    @Test
    public void setPosAfterNext() {
        image = createSequentialImage(WIDTH, HEIGHT, NUM_BANDS);
        iter = new SimpleIterator(image, null, OUTSIDE);
        moveToEnd(iter);
        assertTrue(iter.setPos(WIDTH / 2, HEIGHT / 2));
        assertSamples();
    }

    @Test
    public void setPosOutsideBounds() {
        image = createSequentialImage(WIDTH, HEIGHT, NUM_BANDS);
        iter = new SimpleIterator(image, null, OUTSIDE);
        Rectangle bounds = iter.getBounds();
        Point origin = new Point(bounds.x, bounds.y);
        assertFalse(iter.setPos(bounds.x - 1, bounds.y));
        assertEquals(origin, iter.getPos());
        assertFalse(iter.setPos(bounds.x, bounds.y - 1));
        assertEquals(origin, iter.getPos());
        assertFalse(iter.setPos(bounds.x + bounds.width, bounds.y));
        assertEquals(origin, iter.getPos());
        assertFalse(iter.setPos(bounds.x, bounds.y + bounds.height));
        assertEquals(origin, iter.getPos());
    }

    @Test
    public void getSampleForPosition() {
        image = createSequentialImage(WIDTH, HEIGHT, NUM_BANDS);
        Rectangle imageBounds = image.getBounds();
        Rectangle iterBounds = createAdjustedBounds(imageBounds, 5);
        iter = new SimpleIterator(image, iterBounds, OUTSIDE);
        Point pos = new Point();
        for (int x = iterBounds.x, ix = 0; ix < iterBounds.width; x++, ix++) {
            for (int y = iterBounds.y, iy = 0; iy < iterBounds.height; y++, iy++) {
                boolean inside = imageBounds.contains(x, y);
                pos.setLocation(x, y);
                Number sample = iter.getSample(pos);
                assertSample(x, y, 0, sample);
                for (int band = 0; band < NUM_BANDS; band++) {
                    sample = iter.getSample(pos, band);
                    assertSample(x, y, band, sample);
                }
            }
        }
    }

    @Test
    public void doneClearsSourceImageRef() {
        image = createSequentialImage(WIDTH, WIDTH, 1);
        iter = new SimpleIterator(image, null, null);
        assertTrue(image == iter.getImage());
        iter.done();
        assertNull(iter.getImage());
    }

    @Test(expected = IllegalStateException.class)
    public void usingAfterDoneThrowsException() {
        image = createSequentialImage(WIDTH, WIDTH, 1);
        iter = new SimpleIterator(image, null, null);
        assertNotNull(iter.getSample());
        iter.done();
        iter.getSample();
    }

    private void assertSamples() {
        Point pos = new Point();
        do {
            iter.getPos(pos);
            for (int band = 0; band < NUM_BANDS; band++) {
                Number sample = iter.getSample();
                assertSample(pos.x, pos.y, band, sample);
            }
        } while (iter.next());
    }

    private void assertSample(int x, int y, int band, Number sample) {
        final Rectangle imageBounds = image.getBounds();
        final int N = imageBounds.width * imageBounds.height;
        boolean inside = imageBounds.contains(x, y);
        int expected;
        if (inside) {
            expected = (y - imageBounds.y) * WIDTH + (x - imageBounds.x) + band * N;
        } else {
            expected = OUTSIDE;
        }
        assertEquals(expected, iter.getSample(band).intValue());
    }
}
