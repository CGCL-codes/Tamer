package android.graphics.cts;

import android.graphics.Color;
import android.test.AndroidTestCase;
import dalvik.annotation.TestTargets;
import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestTargetClass;

@TestTargetClass(Color.class)
public class ColorTest extends AndroidTestCase {

    @TestTargets({ @TestTargetNew(level = TestLevel.COMPLETE, method = "alpha", args = { int.class }), @TestTargetNew(level = TestLevel.COMPLETE, method = "Color", args = {  }) })
    public void testAlpha() {
        assertEquals(0xff, Color.alpha(Color.RED));
        assertEquals(0xff, Color.alpha(Color.YELLOW));
        new Color();
    }

    @TestTargetNew(level = TestLevel.COMPLETE, method = "argb", args = { int.class, int.class, int.class, int.class })
    public void testArgb() {
        assertEquals(Color.RED, Color.argb(0xff, 0xff, 0x00, 0x00));
        assertEquals(Color.YELLOW, Color.argb(0xff, 0xff, 0xff, 0x00));
    }

    @TestTargetNew(level = TestLevel.COMPLETE, method = "blue", args = { int.class })
    public void testBlue() {
        assertEquals(0x00, Color.blue(Color.RED));
        assertEquals(0x00, Color.blue(Color.YELLOW));
    }

    @TestTargetNew(level = TestLevel.COMPLETE, method = "green", args = { int.class })
    public void testGreen() {
        assertEquals(0x00, Color.green(Color.RED));
        assertEquals(0xff, Color.green(Color.GREEN));
    }

    @TestTargets({ @TestTargetNew(level = TestLevel.COMPLETE, method = "HSVToColor", args = { float[].class }), @TestTargetNew(level = TestLevel.COMPLETE, method = "colorToHSV", args = { int.class, float[].class }) })
    public void testHSVToColor1() {
        try {
            float[] hsv = new float[2];
            Color.HSVToColor(hsv);
            fail("shouldn't come to here");
        } catch (RuntimeException e) {
        }
        float[] hsv = new float[3];
        Color.colorToHSV(Color.RED, hsv);
        assertEquals(Color.RED, Color.HSVToColor(hsv));
    }

    @TestTargets({ @TestTargetNew(level = TestLevel.COMPLETE, method = "HSVToColor", args = { int.class, float[].class }), @TestTargetNew(level = TestLevel.COMPLETE, method = "colorToHSV", args = { int.class, float[].class }) })
    public void testHSVToColor2() {
        try {
            float[] hsv = new float[2];
            Color.HSVToColor(hsv);
            fail("shouldn't come to here");
        } catch (RuntimeException e) {
        }
        float[] hsv = new float[3];
        Color.colorToHSV(Color.RED, hsv);
        assertEquals(Color.RED, Color.HSVToColor(0xff, hsv));
    }

    @TestTargetNew(level = TestLevel.COMPLETE, method = "parseColor", args = { java.lang.String.class })
    public void testParseColor() {
        try {
            Color.parseColor("#ff00ff0");
            fail("should come to here");
        } catch (IllegalArgumentException e) {
        }
        assertEquals(Color.RED, Color.parseColor("#ff0000"));
        assertEquals(Color.RED, Color.parseColor("#ffff0000"));
        try {
            Color.parseColor("hello");
            fail("should come to here");
        } catch (IllegalArgumentException e) {
        }
        assertEquals(Color.BLACK, Color.parseColor("black"));
        assertEquals(Color.DKGRAY, Color.parseColor("darkgray"));
        assertEquals(Color.GRAY, Color.parseColor("gray"));
        assertEquals(Color.LTGRAY, Color.parseColor("lightgray"));
        assertEquals(Color.WHITE, Color.parseColor("white"));
        assertEquals(Color.RED, Color.parseColor("red"));
        assertEquals(Color.GREEN, Color.parseColor("green"));
        assertEquals(Color.BLUE, Color.parseColor("blue"));
        assertEquals(Color.YELLOW, Color.parseColor("yellow"));
        assertEquals(Color.CYAN, Color.parseColor("cyan"));
        assertEquals(Color.MAGENTA, Color.parseColor("magenta"));
    }

    @TestTargetNew(level = TestLevel.COMPLETE, method = "red", args = { int.class })
    public void testRed() {
        assertEquals(0xff, Color.red(Color.RED));
        assertEquals(0xff, Color.red(Color.YELLOW));
    }

    @TestTargetNew(level = TestLevel.COMPLETE, method = "rgb", args = { int.class, int.class, int.class })
    public void testRgb() {
        assertEquals(Color.RED, Color.rgb(0xff, 0x00, 0x00));
        assertEquals(Color.YELLOW, Color.rgb(0xff, 0xff, 0x00));
    }

    @TestTargetNew(level = TestLevel.COMPLETE, method = "RGBToHSV", args = { int.class, int.class, int.class, float[].class })
    public void testRGBToHSV() {
        try {
            float[] hsv = new float[2];
            Color.RGBToHSV(0xff, 0x00, 0x00, hsv);
            fail("shouldn't come to here");
        } catch (RuntimeException e) {
        }
        float[] hsv = new float[3];
        Color.RGBToHSV(0xff, 0x00, 0x00, hsv);
        assertEquals(Color.RED, Color.HSVToColor(hsv));
    }
}
