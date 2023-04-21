package com.abso.sunlight.explorer;

import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Control;

/**
 * SWT control's utilities.
 */
public class ControlUtils {

    /**
     * Returns the number of pixels corresponding to the height of the given
     * number of characters.
     * 
     * @param control   the SWT control.
     * @param chars   the number of chars.
     * @return the number of pixels.
     */
    public static int convertHeightInCharsToPixels(Control control, int chars) {
        GC gc = new GC(control);
        FontMetrics fontMetrics = gc.getFontMetrics();
        int pixels = fontMetrics.getHeight() * chars;
        gc.dispose();
        return pixels;
    }
}
