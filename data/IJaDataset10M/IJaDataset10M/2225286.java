package com.xiledsystems.AlternateJavaBridgelib.components.altbridge.util;

import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;

/**
 * Functionality related to {@link android.graphics.Color} and
 * {@link android.graphics.Paint}.
 *
 */
public class PaintUtil {

    private PaintUtil() {
    }

    /**
   * Changes the paint color to the specified value.
   *
   * @param paint the object to mutate with the new color
   * @param argb a 32-bit integer with eight bits for alpha, red, green, and blue,
   *        respectively
   */
    public static void changePaint(Paint paint, int argb) {
        paint.setColor(argb & 0x00FFFFFF);
        paint.setAlpha((argb >> 24) & 0xFF);
        paint.setXfermode(null);
    }

    /**
   * Changes the paint color to transparent
   *
   * @param paint the object to mutate with the new color
   */
    public static void changePaintTransparent(Paint paint) {
        paint.setAlpha(0x00);
        paint.setXfermode(new PorterDuffXfermode(android.graphics.PorterDuff.Mode.CLEAR));
    }
}
