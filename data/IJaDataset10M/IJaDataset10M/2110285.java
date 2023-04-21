package org.jfge.android.graphics;

import org.jfge.spi.graphics.Rectangle;
import android.graphics.Rect;

public class AndroidRectangle implements Rectangle {

    private Rect rectangle;

    public AndroidRectangle(Rect rectangle) {
        this.rectangle = rectangle;
    }

    public AndroidRectangle(int x, int y, int width, int height) {
        rectangle = new Rect(x, y, x + width, y + height);
    }

    @Override
    public int getRectHeight() {
        return this.rectangle.height();
    }

    @Override
    public int getRectWidth() {
        return this.rectangle.width();
    }

    @Override
    public int getRectX() {
        return this.rectangle.left;
    }

    @Override
    public int getRectY() {
        return this.rectangle.bottom;
    }

    @Override
    public Rectangle rectIntersection(Rectangle r) {
        if (r == null) return null;
        if (!(r instanceof AndroidRectangle)) return null;
        return null;
    }

    @Override
    public boolean rectIntersects(Rectangle r) {
        if (r == null) return false;
        if (!(r instanceof AndroidRectangle)) return false;
        return rectangle.intersect(((AndroidRectangle) r).getRect());
    }

    public Rect getRect() {
        return rectangle;
    }

    public String toString() {
        return this.rectangle.toString();
    }
}
