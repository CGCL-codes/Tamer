package android.graphics.cts;

import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;
import junit.framework.TestCase;

@TestTargetClass(LightingColorFilter.class)
public class LightingColorFilterTest extends TestCase {

    private static final int TOLERANCE = 2;

    @TestTargetNew(level = TestLevel.COMPLETE, method = "LightingColorFilter", args = { int.class, int.class })
    public void testLightingColorFilter() {
        Bitmap bitmap = Bitmap.createBitmap(1, 1, Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(Color.MAGENTA);
        paint.setColorFilter(new LightingColorFilter(Color.WHITE, Color.BLACK));
        canvas.drawPaint(paint);
        assertColor(Color.MAGENTA, bitmap.getPixel(0, 0));
        paint.setColor(Color.MAGENTA);
        paint.setColorFilter(new LightingColorFilter(Color.CYAN, Color.BLACK));
        canvas.drawPaint(paint);
        assertColor(Color.BLUE, bitmap.getPixel(0, 0));
        paint.setColor(Color.MAGENTA);
        paint.setColorFilter(new LightingColorFilter(Color.BLUE, Color.GREEN));
        canvas.drawPaint(paint);
        assertColor(Color.CYAN, bitmap.getPixel(0, 0));
        bitmap.eraseColor(Color.TRANSPARENT);
        paint.setColor(Color.MAGENTA);
        paint.setColorFilter(new LightingColorFilter(Color.TRANSPARENT, Color.argb(0, 0, 0xFF, 0)));
        canvas.drawPaint(paint);
        assertColor(Color.GREEN, bitmap.getPixel(0, 0));
        paint.setColor(Color.MAGENTA);
        paint.setColorFilter(new LightingColorFilter(Color.WHITE, Color.MAGENTA));
        canvas.drawPaint(paint);
        assertColor(Color.MAGENTA, bitmap.getPixel(0, 0));
        paint.setColor(Color.argb(255, 60, 20, 40));
        paint.setColorFilter(new LightingColorFilter(Color.rgb(0x80, 0xFF, 0x80), Color.rgb(0, 10, 10)));
        canvas.drawPaint(paint);
        assertColor(Color.argb(255, 30, 30, 30), bitmap.getPixel(0, 0));
        bitmap.eraseColor(Color.TRANSPARENT);
        paint.setColor(Color.argb(0x80, 60, 20, 40));
        paint.setColorFilter(new LightingColorFilter(Color.rgb(0x80, 0xFF, 0x80), Color.rgb(0, 10, 10)));
        canvas.drawPaint(paint);
        assertColor(Color.argb(0x80, 30, 30, 30), bitmap.getPixel(0, 0));
    }

    private void assertColor(int expected, int actual) {
        assertEquals(Color.alpha(expected), Color.alpha(actual), TOLERANCE);
        assertEquals(Color.red(expected), Color.red(actual), TOLERANCE);
        assertEquals(Color.green(expected), Color.green(actual), TOLERANCE);
        assertEquals(Color.blue(expected), Color.blue(actual), TOLERANCE);
    }
}
