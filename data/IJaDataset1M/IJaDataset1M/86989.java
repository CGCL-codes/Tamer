package org.plazmaforge.studio.reportdesigner.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;

public class GraphUtils {

    /**
     * Returns array of strings by text
     * @param gc
     * @param text
     * @param width
     * @return
     */
    public static String[] getTextLines(GC gc, String text, int width) {
        String wrapText = wrapText(gc, text, width);
        if (wrapText == null) {
            return null;
        }
        String[] lines = wrapText.split("\n");
        return lines;
    }

    public static String wrapText(GC gc, String text, int width) {
        Point textSize = getCachedStringExtent(gc, text);
        if (textSize.x > width) {
            int cutoffLength = width / gc.getFontMetrics().getAverageCharWidth();
            if (cutoffLength < 3) {
                return text;
            }
            StringBuffer wrappedText = new StringBuffer();
            String[] lines = text.split("\n");
            for (int i = 0; i < lines.length; i++) {
                int breakOffset = 0;
                while (breakOffset < lines[i].length()) {
                    String lPart = lines[i].substring(breakOffset, Math.min(breakOffset + cutoffLength, lines[i].length()));
                    Point lineSize = getCachedStringExtent(gc, lPart);
                    while ((lPart.length() > 0) && (lineSize.x >= width)) {
                        lPart = lPart.substring(0, Math.max(lPart.length() - 1, 0));
                        lineSize = getCachedStringExtent(gc, lPart);
                    }
                    wrappedText.append(lPart);
                    breakOffset += lPart.length();
                    wrappedText.append('\n');
                }
            }
            return wrappedText.substring(0, Math.max(wrappedText.length() - 1, 0));
        } else {
            return text;
        }
    }

    private static Point getCachedStringExtent(GC gc, String text) {
        return gc.textExtent(text);
    }

    public static void drawText(GC gc, FontMetrics fontMetrics, Font font, String text, int x, int y, int width, int height, int flags, int align) {
        if (gc == null || text == null) {
            return;
        }
        if (fontMetrics == null) {
            fontMetrics = gc.getFontMetrics();
        }
        if (font == null) {
            font = gc.getFont();
        }
        int descent = fontMetrics.getDescent();
        int lineHeight = font.getFontData()[0].getHeight();
        int lineInterval = descent * 2;
        Point textSize = gc.textExtent(text);
        if (width <= 0) {
            width = textSize.x;
        }
        if (height <= 0) {
            height = textSize.y;
        }
        String[] lines = getTextLines(gc, text, width);
        if (lines == null) {
            return;
        }
        int xOffset = 0;
        int yOffset = 0;
        for (int i = 0; i < lines.length; i++) {
            String str = lines[i];
            if (str == null) {
                str = "";
            }
            Point lineSize = gc.textExtent(str);
            xOffset = 0;
            if ((align & SWT.CENTER) != 0) {
                xOffset = Math.max(0, (width - lineSize.x) / 2);
            } else if ((align & SWT.RIGHT) != 0) {
                xOffset = Math.max(0, width - lineSize.x);
            }
            gc.drawText(str, x + xOffset, y + yOffset, flags);
            yOffset += (lineHeight + lineInterval);
        }
    }
}
