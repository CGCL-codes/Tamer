package com.android.unit_tests;

import junit.framework.Assert;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.test.AndroidTestCase;
import android.test.PerformanceTestCase;
import android.test.suitebuilder.annotation.Suppress;
import android.util.Log;

@Suppress
public class GraphicsPerformanceTests {

    private static final String TAG = "GfxPerf";

    public static String[] children() {
        return new String[] { DecodeBitmapTest.class.getName(), DrawBitmap7x7.class.getName(), DrawBitmap15x15.class.getName(), DrawBitmap31x31.class.getName(), DrawBitmap63x63.class.getName(), DrawBitmap127x127.class.getName(), DrawBitmap319x239.class.getName(), DrawBitmap319x479.class.getName(), DrawBitmap8x8.class.getName(), DrawBitmap16x16.class.getName(), DrawBitmap32x32.class.getName(), DrawBitmap64x64.class.getName(), DrawBitmap128x128.class.getName(), DrawBitmap320x240.class.getName(), DrawBitmap320x480.class.getName() };
    }

    /**
     * Base class for all graphics tests
     * 
     */
    public abstract static class GraphicsTestBase extends AndroidTestCase implements PerformanceTestCase {

        /** Target "screen" (bitmap) width and height */
        private static final int DEFAULT_ITERATIONS = 1;

        private static final int SCREEN_WIDTH = 320;

        private static final int SCREEN_HEIGHT = 480;

        /** Number of iterations to pass back to harness. Subclass should override */
        protected int mIterations = 1;

        /** Bitmap we allocate and draw to */
        protected Bitmap mDestBitmap;

        /** Canvas of drawing routines */
        protected Canvas mCanvas;

        /** Style and color information (uses defaults) */
        protected Paint mPaint;

        @Override
        public void setUp() throws Exception {
            super.setUp();
            mDestBitmap = Bitmap.createBitmap(SCREEN_WIDTH, SCREEN_HEIGHT, Bitmap.Config.RGB_565);
            mCanvas = new Canvas(mDestBitmap);
            mPaint = new Paint();
            mIterations = getIterations();
        }

        public int getIterations() {
            return DEFAULT_ITERATIONS;
        }

        public boolean isPerformanceOnly() {
            return true;
        }

        public int startPerformance(Intermediates intermediates) {
            intermediates.setInternalIterations(mIterations * 10);
            return 0;
        }
    }

    /**
     * Tests time to decode a number of sizes of images.
     */
    public static class DecodeBitmapTest extends GraphicsTestBase {

        /** Number of times to run this test */
        private static final int DECODE_ITERATIONS = 10;

        /** Used to access package bitmap images */
        private Resources mResources;

        @Override
        public void setUp() throws Exception {
            super.setUp();
            Context context = getContext();
            Assert.assertNotNull(context);
            mResources = context.getResources();
            Assert.assertNotNull(mResources);
        }

        @Override
        public int getIterations() {
            return DECODE_ITERATIONS;
        }

        public void testDecodeBitmap() {
            for (int i = 0; i < DECODE_ITERATIONS; i++) {
                BitmapFactory.decodeResource(mResources, R.drawable.test16x12);
                BitmapFactory.decodeResource(mResources, R.drawable.test32x24);
                BitmapFactory.decodeResource(mResources, R.drawable.test64x48);
                BitmapFactory.decodeResource(mResources, R.drawable.test128x96);
                BitmapFactory.decodeResource(mResources, R.drawable.test256x192);
                BitmapFactory.decodeResource(mResources, R.drawable.test320x240);
            }
        }
    }

    /**
     * Base class for bitmap drawing tests
     * 
     */
    public abstract static class DrawBitmapTest extends GraphicsTestBase {

        /** Number of times to run each draw test */
        private static final int ITERATIONS = 1000;

        /** Bitmap to draw. Allocated by subclass's createBitmap() function. */
        private Bitmap mBitmap;

        @Override
        public void setUp() throws Exception {
            super.setUp();
            mBitmap = createBitmap();
        }

        public int getIterations() {
            return ITERATIONS;
        }

        public abstract Bitmap createBitmap();

        public void drawBitmapEven() {
            for (int i = 0; i < ITERATIONS; i++) {
                mCanvas.drawBitmap(mBitmap, 0.0f, 0.0f, mPaint);
            }
        }

        public void drawBitmapOdd() {
            for (int i = 0; i < ITERATIONS; i++) {
                mCanvas.drawBitmap(mBitmap, 1.0f, 0.0f, mPaint);
            }
        }
    }

    /**
     * Test drawing of 7x7 image
     */
    public static class DrawBitmap7x7 extends DrawBitmapTest {

        public Bitmap createBitmap() {
            return Bitmap.createBitmap(7, 7, Bitmap.Config.RGB_565);
        }

        public void testDrawBitmapEven() {
            drawBitmapEven();
        }

        public void testDrawBitmapOdd() {
            drawBitmapOdd();
        }
    }

    /**
     * Test drawing of 15x15 image
     */
    public static class DrawBitmap15x15 extends DrawBitmapTest {

        public Bitmap createBitmap() {
            return Bitmap.createBitmap(15, 15, Bitmap.Config.RGB_565);
        }

        public void testDrawBitmapEven() {
            drawBitmapEven();
        }

        public void testDrawBitmapOdd() {
            drawBitmapOdd();
        }
    }

    /**
     * Test drawing of 31x31 image
     */
    public static class DrawBitmap31x31 extends DrawBitmapTest {

        public Bitmap createBitmap() {
            return Bitmap.createBitmap(31, 31, Bitmap.Config.RGB_565);
        }

        public void testDrawBitmapEven() {
            drawBitmapEven();
        }

        public void testDrawBitmapOdd() {
            drawBitmapOdd();
        }
    }

    /**
     * Test drawing of 63x63 image
     */
    public static class DrawBitmap63x63 extends DrawBitmapTest {

        public Bitmap createBitmap() {
            return Bitmap.createBitmap(63, 63, Bitmap.Config.RGB_565);
        }

        public void testDrawBitmapEven() {
            drawBitmapEven();
        }

        public void testDrawBitmapOdd() {
            drawBitmapOdd();
        }
    }

    /**
     * Test drawing of 127x127 image
     */
    public static class DrawBitmap127x127 extends DrawBitmapTest {

        public Bitmap createBitmap() {
            return Bitmap.createBitmap(127, 127, Bitmap.Config.RGB_565);
        }

        public void testDrawBitmapEven() {
            drawBitmapEven();
        }

        public void testDrawBitmapOdd() {
            drawBitmapOdd();
        }
    }

    /**
     * Test drawing of 319x239 image
     */
    public static class DrawBitmap319x239 extends DrawBitmapTest {

        public Bitmap createBitmap() {
            return Bitmap.createBitmap(319, 239, Bitmap.Config.RGB_565);
        }

        public void testDrawBitmapEven() {
            drawBitmapEven();
        }

        public void testDrawBitmapOdd() {
            drawBitmapOdd();
        }
    }

    /**
     * Test drawing of 319x479 image
     */
    public static class DrawBitmap319x479 extends DrawBitmapTest {

        public Bitmap createBitmap() {
            return Bitmap.createBitmap(319, 479, Bitmap.Config.RGB_565);
        }

        public void testDrawBitmapEven() {
            drawBitmapEven();
        }

        public void testDrawBitmapOdd() {
            drawBitmapOdd();
        }
    }

    /**
     * Test drawing of 8x8 image
     */
    public static class DrawBitmap8x8 extends DrawBitmapTest {

        public Bitmap createBitmap() {
            return Bitmap.createBitmap(8, 8, Bitmap.Config.RGB_565);
        }

        public void testDrawBitmapEven() {
            drawBitmapEven();
        }

        public void testDrawBitmapOdd() {
            drawBitmapOdd();
        }
    }

    /**
     * Test drawing of 16x16 image
     */
    public static class DrawBitmap16x16 extends DrawBitmapTest {

        public Bitmap createBitmap() {
            return Bitmap.createBitmap(16, 16, Bitmap.Config.RGB_565);
        }

        public void testDrawBitmapEven() {
            drawBitmapEven();
        }

        public void testDrawBitmapOdd() {
            drawBitmapOdd();
        }
    }

    /**
     * Test drawing of 32x32 image
     */
    public static class DrawBitmap32x32 extends DrawBitmapTest {

        public Bitmap createBitmap() {
            return Bitmap.createBitmap(32, 32, Bitmap.Config.RGB_565);
        }

        public void testDrawBitmapEven() {
            drawBitmapEven();
        }

        public void testDrawBitmapOdd() {
            drawBitmapOdd();
        }
    }

    /**
     * Test drawing of 64x64 image
     */
    public static class DrawBitmap64x64 extends DrawBitmapTest {

        public Bitmap createBitmap() {
            return Bitmap.createBitmap(64, 64, Bitmap.Config.RGB_565);
        }

        public void testDrawBitmapEven() {
            drawBitmapEven();
        }

        public void testDrawBitmapOdd() {
            drawBitmapOdd();
        }
    }

    /**
     * Test drawing of 128x128 image
     */
    public static class DrawBitmap128x128 extends DrawBitmapTest {

        public Bitmap createBitmap() {
            return Bitmap.createBitmap(128, 128, Bitmap.Config.RGB_565);
        }

        public void testDrawBitmapEven() {
            drawBitmapEven();
        }

        public void testDrawBitmapOdd() {
            drawBitmapOdd();
        }
    }

    /**
     * Test drawing of 320x240 image
     */
    public static class DrawBitmap320x240 extends DrawBitmapTest {

        public Bitmap createBitmap() {
            return Bitmap.createBitmap(320, 240, Bitmap.Config.RGB_565);
        }

        public void testDrawBitmapEven() {
            drawBitmapEven();
        }

        public void testDrawBitmapOdd() {
            drawBitmapOdd();
        }
    }

    /**
     * Test drawing of 320x480 image
     */
    public static class DrawBitmap320x480 extends DrawBitmapTest {

        public Bitmap createBitmap() {
            return Bitmap.createBitmap(320, 480, Bitmap.Config.RGB_565);
        }

        public void testDrawBitmapEven() {
            drawBitmapEven();
        }

        public void testDrawBitmapOdd() {
            drawBitmapOdd();
        }
    }
}
