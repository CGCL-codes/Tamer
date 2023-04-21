package de.beas.explicanto.client.rcp.imageeditor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

public final class DrawingUtils {

    private DrawingUtils() {
    }

    public static void drawSelectionRect(Display d, GC g, Point start, Point stop) {
        if (start == null || stop == null) return;
        Color oldColor = g.getForeground();
        g.setForeground(new Color(d, 255, 50, 50));
        g.setLineStyle(SWT.LINE_DASH);
        g.drawRectangle(rect(start, stop));
        g.setLineStyle(SWT.LINE_SOLID);
        g.setForeground(oldColor);
    }

    public static Rectangle rect(Point start, Point stop) {
        int x, y, width, height;
        x = Math.min(start.x, stop.x);
        width = Math.abs(start.x - stop.x);
        y = Math.min(start.y, stop.y);
        height = Math.abs(start.y - stop.y);
        return new Rectangle(x, y, width, height);
    }

    public static double distance(Point a, Point b) {
        return Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2));
    }
}
