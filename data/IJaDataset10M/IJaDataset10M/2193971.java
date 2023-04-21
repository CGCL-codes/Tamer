package exformation.geom;

public class Rectangle extends java.awt.Rectangle {

    public Rectangle() {
        this(0, 0);
    }

    public Rectangle(float x, float y, float w, float h) {
        this((int) x, (int) y, (int) w, (int) h);
    }

    public Rectangle(int x, int y, int w, int h) {
        this(w, h);
        this.x = x;
        this.y = y;
    }

    public Rectangle(int w, int h) {
        width = w;
        height = h;
    }

    public void right(int val) {
        width = val - x;
    }

    public void bottom(int val) {
        height = val - y;
    }

    public float left() {
        return x;
    }

    public float right() {
        return x + width;
    }

    public float top() {
        return y;
    }

    public float bottom() {
        return y + height;
    }

    public boolean contains(Point p) {
        return contains(new java.awt.Point((int) p.x, (int) p.y));
    }

    public void setSize(int w, int h) {
        width = w;
        height = h;
    }

    public Point center() {
        return new Point((width / 2) + x, (height / 2) + x);
    }
}
