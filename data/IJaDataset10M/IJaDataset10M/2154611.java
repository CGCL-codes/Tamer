package org.nakedobjects.viewer.skylark.basic;

import org.nakedobjects.utility.DebugString;
import org.nakedobjects.viewer.skylark.Canvas;
import org.nakedobjects.viewer.skylark.View;
import org.nakedobjects.viewer.skylark.drawing.Bounds;
import org.nakedobjects.viewer.skylark.drawing.Color;
import org.nakedobjects.viewer.skylark.drawing.Image;
import org.nakedobjects.viewer.skylark.drawing.Location;
import org.nakedobjects.viewer.skylark.drawing.Shape;
import org.nakedobjects.viewer.skylark.drawing.Text;

public class DebugCanvas implements Canvas {

    private DebugString buffer;

    private int level;

    public DebugCanvas(final DebugString buffer, final Bounds bounds) {
        this(buffer, 0);
    }

    private DebugCanvas(final DebugString buffer, final int level) {
        this.level = level;
        this.buffer = buffer;
    }

    public void clearBackground(final View view, final Color color) {
        indent();
        buffer.appendln("Clear background of " + view + " to " + color);
    }

    public Canvas createSubcanvas() {
        buffer.blankLine();
        indent();
        buffer.appendln("Create subcanvas for same area");
        return new DebugCanvas(buffer, level + 1);
    }

    public Canvas createSubcanvas(final Bounds bounds) {
        return createSubcanvas(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight());
    }

    public Canvas createSubcanvas(final int x, final int y, final int width, final int height) {
        buffer.blankLine();
        indent();
        buffer.appendln("Create subcanvas for area " + x + "," + y + " " + width + "x" + height);
        return new DebugCanvas(buffer, level + 1);
    }

    public void draw3DRectangle(final int x, final int y, final int width, final int height, final Color color, final boolean raised) {
        indent();
        buffer.appendln("Rectangle (3D) " + x + "," + y + " " + width + "x" + height);
    }

    public void drawIcon(final Image icon, final int x, final int y) {
        indent();
        buffer.appendln("Icon " + x + "," + y + " " + icon.getWidth() + "x" + icon.getHeight());
    }

    public void drawIcon(final Image icon, final int x, final int y, final int width, final int height) {
        indent();
        buffer.appendln("Icon " + x + "," + y + " " + width + "x" + height);
    }

    public void drawLine(final int x, final int y, final int x2, final int y2, final Color color) {
        indent();
        buffer.appendln("Line from " + x + "," + y + " to " + x2 + "," + y2 + " " + color);
    }

    public void drawLine(final Location start, final int xExtent, final int yExtent, final Color color) {
        indent();
        buffer.appendln("Line from " + start.getX() + "," + start.getY() + " to " + (start.getX() + xExtent) + "," + (start.getY() + yExtent) + " " + color);
    }

    public void drawOval(final int x, final int y, final int width, final int height, final Color color) {
        indent();
        buffer.appendln("Oval " + x + "," + y + " " + width + "x" + height + " " + color);
    }

    public void drawRectangle(final int x, final int y, final int width, final int height, final Color color) {
        indent();
        buffer.appendln("Rectangle " + x + "," + y + " " + width + "x" + height + " " + color);
    }

    public void drawRectangleAround(final View view, final Color color) {
        Bounds bounds = view.getBounds();
        indent();
        buffer.appendln("Rectangle 0,0 " + bounds.getWidth() + "x" + bounds.getHeight() + " " + color);
    }

    public void drawRoundedRectangle(final int x, final int y, final int width, final int height, final int arcWidth, final int arcHeight, final Color color) {
        indent();
        buffer.appendln("Rounded Rectangle " + x + "," + y + " " + (x + width) + "x" + (y + height) + " " + color);
    }

    public void drawShape(final Shape shape, final Color color) {
        indent();
        buffer.appendln("Shape " + shape + " " + color);
    }

    public void drawShape(final Shape shape, final int x, final int y, final Color color) {
        indent();
        buffer.appendln("Shape " + shape + " at " + x + "/" + y + " (left, top)" + " " + color);
    }

    public void drawSolidOval(final int x, final int y, final int width, final int height, final Color color) {
        indent();
        buffer.appendln("Oval (solid) " + x + "," + y + " " + width + "x" + height + " " + color);
    }

    public void drawSolidRectangle(final int x, final int y, final int width, final int height, final Color color) {
        indent();
        buffer.appendln("Rectangle (solid) " + x + "," + y + " " + width + "x" + height + " " + color);
    }

    public void drawSolidShape(final Shape shape, final Color color) {
        indent();
        buffer.appendln("Shape (solid) " + shape + " " + color);
    }

    public void drawSolidShape(final Shape shape, final int x, final int y, final Color color) {
        indent();
        buffer.appendln("Shape (solid)" + shape + " at " + x + "/" + y + " (left, top)" + " " + color);
    }

    public void drawText(final String text, final int x, final int y, final Color color, final Text style) {
        indent();
        buffer.appendln("Text " + x + "," + y + " \"" + text + "\" " + style + " " + color);
    }

    private void indent() {
        for (int i = 0; i < level; i++) {
            buffer.append("   ");
        }
    }

    public void offset(final int x, final int y) {
        indent();
        buffer.appendln("Offset by " + x + "/" + y + " (left, top)");
    }

    public boolean overlaps(final Bounds bounds) {
        return true;
    }

    public String toString() {
        return "Canvas";
    }

    public void drawDebugOutline(final Bounds bounds, final int baseline, final Color color) {
    }
}
