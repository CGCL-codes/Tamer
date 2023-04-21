package websiteschema.element;

import com.webrenderer.swing.dom.IElement;

/**
 * IElement的矩形框大小和位置。
 * @author ray
 */
public class Rectangle {

    int height;

    int width;

    int left;

    int top;

    public Rectangle(int h, int w, int l, int t) {
        height = h;
        width = w;
        left = l;
        top = t;
    }

    public Rectangle(long h, long w, long l, long t) {
        this((int) h, (int) w, (int) l, (int) t);
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public String toString() {
        return "height: " + height + " width: " + width + " left: " + left + " top: " + top;
    }
}
