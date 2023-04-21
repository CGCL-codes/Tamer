package org.nakedobjects.plugins.dnd;

import org.nakedobjects.plugins.dnd.viewer.drawing.Bounds;
import org.nakedobjects.plugins.dnd.viewer.drawing.Color;
import org.nakedobjects.plugins.dnd.viewer.drawing.Image;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;
import org.nakedobjects.plugins.dnd.viewer.drawing.Shape;
import org.nakedobjects.plugins.dnd.viewer.drawing.Text;

public interface Canvas {

    void clearBackground(View view, Color color);

    Canvas createSubcanvas();

    Canvas createSubcanvas(Bounds bounds);

    Canvas createSubcanvas(int x, int y, int width, int height);

    void draw3DRectangle(int x, int y, int width, int height, Color color, boolean raised);

    void drawDebugOutline(Bounds bounds, int baseline, Color color);

    void drawImage(Image image, int x, int y);

    void drawImage(Image image, int x, int y, int width, int height);

    void drawLine(int x, int y, int x2, int y2, Color color);

    void drawLine(Location start, int xExtent, int yExtent, Color color);

    void drawOval(int x, int y, int width, int height, Color color);

    void drawRectangle(int x, int y, int width, int height, Color color);

    void drawRectangleAround(View view, Color color);

    void drawRoundedRectangle(int x, int y, int width, int height, int arcWidth, int arcHeight, Color color);

    void drawShape(Shape shape, Color color);

    void drawShape(Shape shape, int x, int y, Color color);

    void drawSolidOval(int x, int y, int width, int height, Color color);

    void drawSolidRectangle(int x, int y, int width, int height, Color color);

    void drawSolidShape(Shape shape, Color color);

    void drawSolidShape(Shape shape, int x, int y, Color color);

    void drawText(String text, int x, int y, Color color, Text style);

    void drawText(String text, int x, int y, int maxWidth, Color color, Text style);

    void offset(int x, int y);

    boolean overlaps(Bounds bounds);
}
