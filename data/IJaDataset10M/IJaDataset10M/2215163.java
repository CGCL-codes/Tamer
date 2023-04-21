package org.nakedobjects.nos.client.dnd;

import org.nakedobjects.nos.client.dnd.drawing.Bounds;
import org.nakedobjects.nos.client.dnd.drawing.Color;
import org.nakedobjects.nos.client.dnd.drawing.Image;
import org.nakedobjects.nos.client.dnd.drawing.Location;
import org.nakedobjects.nos.client.dnd.drawing.Shape;
import org.nakedobjects.nos.client.dnd.drawing.Text;

public class DummyCanvas implements Canvas {

    public DummyCanvas() {
        super();
    }

    public void clearBackground(final View view, final Color color) {
    }

    public Canvas createSubcanvas() {
        return null;
    }

    public Canvas createSubcanvas(final Bounds bounds) {
        return null;
    }

    public Canvas createSubcanvas(final int x, final int y, final int width, final int height) {
        return null;
    }

    public void draw3DRectangle(final int x, final int y, final int width, final int height, final Color color, final boolean raised) {
    }

    public void drawImage(final Image icon, final int x, final int y) {
    }

    public void drawImage(final Image icon, final int x, final int y, final int width, final int height) {
    }

    public void drawLine(final int x, final int y, final int x2, final int y2, final Color color) {
    }

    public void drawLine(final Location start, final int xExtent, final int yExtent, final Color color) {
    }

    public void drawOval(final int x, final int y, final int width, final int height, final Color color) {
    }

    public void drawRectangle(final int x, final int y, final int width, final int height, final Color color) {
    }

    public void drawRectangleAround(final View view, final Color color) {
    }

    public void drawRoundedRectangle(final int x, final int y, final int width, final int height, final int arcWidth, final int arcHeight, final Color color) {
    }

    public void drawShape(final Shape shape, final Color color) {
    }

    public void drawShape(final Shape shape, final int x, final int y, final Color color) {
    }

    public void drawSolidOval(final int x, final int y, final int width, final int height, final Color color) {
    }

    public void drawSolidRectangle(final int x, final int y, final int width, final int height, final Color color) {
    }

    public void drawSolidShape(final Shape shape, final Color color) {
    }

    public void drawSolidShape(final Shape shape, final int x, final int y, final Color color) {
    }

    public void drawText(final String text, final int x, final int y, final Color color, final Text style) {
    }

    public void drawText(final String text, final int x, final int y, final int maxWidth, final Color color, final Text style) {
    }

    public void offset(final int x, final int y) {
    }

    public boolean overlaps(final Bounds bounds) {
        return false;
    }

    public void drawDebugOutline(final Bounds bounds, final int baseline, final Color color) {
    }
}
