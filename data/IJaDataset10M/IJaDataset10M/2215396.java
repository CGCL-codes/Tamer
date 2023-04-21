package org.nakedobjects.plugins.dndviewer.viewer.drawing;

import org.nakedobjects.plugins.dndviewer.Canvas;

public class DrawingUtil {

    public static void drawHatching(final Canvas canvas, final int x, final int y, final int width, final int height, final Color foreground, final Color shadow) {
        final int bottom = y + height;
        for (int p = y; p < bottom; p += 4) {
            drawDots(canvas, x, p, width, foreground, shadow);
            if (p + 2 < bottom) {
                drawDots(canvas, x + 2, p + 2, width - 2, foreground, shadow);
            }
        }
    }

    private static void drawDots(final Canvas canvas, final int x, final int y, final int width, final Color foreground, final Color shadow) {
        final int x2 = x + width;
        for (int p = x; p < x2; p += 4) {
            canvas.drawLine(p, y, p, y, shadow);
            canvas.drawLine(p + 1, y + 1, p + 1, y + 1, foreground);
        }
    }
}
