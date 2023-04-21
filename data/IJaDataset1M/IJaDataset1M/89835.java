package uk.ac.lkl.common.util;

/**
 * Need a class like java.awt.Rectangle that is independent of AWT
 * 
 * @author Ken Kahn
 *
 */
public class Rectangle {

    public int x, y, width, height;

    public Rectangle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
}
