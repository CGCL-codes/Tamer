package gene.core.util;

import gene.core.util.*;
import gene.core.Core;
import gene.core.GraphicDevice;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Polygon;

/**
 *
 * @author callagun
 */
public class VectorObject {

    private static final int TYPE_SQUARE = 0;

    private static final int TYPE_TRIANGLE = 1;

    private static final int TYPE_CIRCUNFERENCE = 2;

    protected int type = -1;

    private boolean isFull = true;

    protected short[] primitives;

    short[] color = { 0, 0, 0 };

    private VectorObject(int _type, short[] _primitives, int r_color, int g_color, int b_color) {
        type = _type;
        primitives = _primitives;
        if (r_color > 0 && r_color < 255 && b_color > 0 && b_color < 255 && g_color > 0 && g_color < 255) {
            color[0] = (short) r_color;
            color[1] = (short) g_color;
            color[2] = (short) b_color;
        } else {
            new Exception("Wrong color rage");
        }
    }

    public static VectorObject createSquare(int x, int y, int w, int h, int r_color, int g_color, int b_color) {
        short tmp[] = { (short) x, (short) y, (short) w, (short) h };
        return new VectorObject(TYPE_SQUARE, tmp, r_color, g_color, b_color);
    }

    public static VectorObject createTriange(int x1, int y1, int x2, int y2, int x3, int y3, int r_color, int g_color, int b_color) {
        short tmp[] = { (short) x1, (short) y1, (short) x2, (short) y2, (short) x3, (short) y3 };
        return new VectorObject(TYPE_TRIANGLE, tmp, r_color, g_color, b_color);
    }

    public static VectorObject createCircunference(int center_x, int center_y, int radius, int r_color, int g_color, int b_color) {
        short tmp[] = { (short) center_x, (short) center_y, (short) radius };
        return new VectorObject(TYPE_CIRCUNFERENCE, tmp, r_color, g_color, b_color);
    }

    public void draw() {
        Graphics g = Core.graphics;
        g.setColor(new Color(color[0], color[1], color[2]));
        switch(type) {
            case TYPE_SQUARE:
                {
                    if (isFull) {
                        g.fillRect(primitives[0], primitives[1], primitives[2], primitives[3]);
                    } else {
                        g.drawRect(primitives[0], primitives[1], primitives[2], primitives[3]);
                    }
                    break;
                }
            case TYPE_CIRCUNFERENCE:
                {
                    if (isFull) {
                        g.fillArc(primitives[0], primitives[1], primitives[2], primitives[2], 0, 360);
                    } else {
                        g.drawArc(primitives[0], primitives[1], primitives[2], primitives[3], 0, 360);
                    }
                    break;
                }
            case TYPE_TRIANGLE:
                {
                    if (isFull) {
                        int xp[] = { primitives[0], primitives[2], primitives[4] };
                        int yp[] = { primitives[1], primitives[3], primitives[5] };
                        g.fillPolygon(xp, yp, 3);
                    } else {
                        g.drawLine(primitives[0], primitives[1], primitives[2], primitives[3]);
                        g.drawLine(primitives[2], primitives[3], primitives[4], primitives[5]);
                        g.drawLine(primitives[4], primitives[5], primitives[0], primitives[1]);
                    }
                    break;
                }
        }
        g = null;
    }

    public boolean isFull() {
        return isFull;
    }

    public void setIsfull(boolean isFull) {
        this.isFull = isFull;
    }

    public void setX(int x) {
        primitives[0] = (short) x;
    }

    public void setY(int y) {
        primitives[1] = (short) y;
    }

    public int getX() {
        return primitives[0];
    }

    public int getY() {
        return primitives[1];
    }

    public int getH() {
        if (type != TYPE_SQUARE) {
            new Exception("Vector Object is not a square, variable type error");
        }
        return primitives[2];
    }

    public int getW() {
        if (type != TYPE_SQUARE) {
            new Exception("Vector Object is not a square,variable type error");
        }
        return primitives[3];
    }

    public int getRadius() {
        if (type != TYPE_CIRCUNFERENCE) {
            new Exception("Vector Object is not a circunference, variable type error");
        }
        return primitives[2];
    }

    public int getX1() {
        return primitives[0];
    }

    public int getY2() {
        return primitives[1];
    }

    public int getX3() {
        return primitives[0];
    }

    public int getY3() {
        return primitives[1];
    }

    public void move(int x, int y) {
        primitives[0] += x;
        primitives[1] += y;
    }

    public int getType() {
        return type;
    }

    public boolean collidsWith(VectorObject obj) {
        return false;
    }

    public boolean collidsWith(Image Img) {
        return collids(type, type, type, type, type);
    }

    private boolean collids(int x, int y, int w, int h, int type) {
        return false;
    }
}
