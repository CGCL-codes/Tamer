package globalwars;

import java.awt.Dimension;
import java.awt.Image;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import xjava.xmath;

/**
 * This class stores an vector in R² (floats)
 */
public class Vector2d {

    public float x;

    public float y;

    private static float epsilon = Float.MIN_VALUE;

    public Vector2d() {
    }

    public Vector2d(int x, int y) {
        this.x = (float) x;
        this.y = (float) y;
    }

    public Vector2d(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2d(double x, double y) {
        this.x = (float) x;
        this.y = (float) y;
    }

    public Vector2d(Image img) {
        this.x = (float) img.getWidth(null);
        this.y = (float) img.getHeight(null);
    }

    public Vector2d(Vector2d v) {
        this.x = v.x;
        this.y = v.y;
    }

    public Vector2d(Dimension v) {
        this.x = v.width;
        this.y = v.height;
    }

    public Vector2d(String v) {
        v = v.replace("(", "");
        v = v.replace(")", "");
        String[] split = v.split(";");
        x = Float.parseFloat(split[0]);
        y = Float.parseFloat(split[1]);
    }

    public static Vector2d parseResolution(String r) throws Exception {
        Pattern p = Pattern.compile("([0-9]+)[x\\*]([0-9]+)");
        Matcher m = p.matcher(r);
        if (m.matches()) {
            int x = Integer.parseInt(m.group(1));
            int y = Integer.parseInt(m.group(2));
            return new Vector2d(x, y);
        } else {
            throw new Exception("No match");
        }
    }

    public Dimension getDimension() {
        return new Dimension(intX(), intY());
    }

    public Vector2d add(Vector2d v) {
        return new Vector2d(x + v.x, y + v.y);
    }

    public Vector2d subtract(Vector2d v) {
        return new Vector2d(x - v.x, y - v.y);
    }

    public Vector2d divide(Vector2d v) {
        return new Vector2d(x / v.x, y / v.y);
    }

    public Vector2d multiply(Vector2d v) {
        return new Vector2d(x * v.x, y * v.y);
    }

    public Vector2d divide(float d) {
        return new Vector2d(x / d, y / d);
    }

    public Vector2d multiply(float d) {
        return new Vector2d(x * d, y * d);
    }

    public PolarVector getPolar() {
        float len = (float) Math.abs(xmath.pyt_c(x, y));
        float ang = (float) Math.atan2(y, x);
        return new PolarVector(len, ang);
    }

    public static Vector2d ZeroVector() {
        return new Vector2d(0f, 0f);
    }

    public boolean zero() {
        if (x == 0 && y == 0) {
            return true;
        } else {
            return false;
        }
    }

    public void floor(float limit) {
        if (x <= limit && x >= -limit) {
            x = 0;
        }
        if (y <= limit && y >= -limit) {
            y = 0;
        }
    }

    public float average() {
        return (x + y) / 2f;
    }

    public Vector2d normalize() {
        float norm = (float) xmath.pyt_c(x, y);
        return new Vector2d(x / norm, y / norm);
    }

    /**
     * Returns the norm of the vector
     * @return
     */
    public float norm() {
        return (float) xmath.pyt_c(x, y);
    }

    /**
     * Returns a vector containing the values of the vector, mulitplied with -1
     * @return a vector containing the values of the vector, mulitplied with -1
     */
    public Vector2d invert() {
        return new Vector2d(-1f * x, -1f * y);
    }

    /**
     * Returns this vector, but with the coordinates flipped
     * @return this vector, but with the coordinates flipped
     */
    public Vector2d flip() {
        return new Vector2d(y, x);
    }

    /**
     * Compares two vectors, result as an int: (u is this, and v is parameter)
     *                  v1  v2 
     * u.x > v.x    1 0 0 0
     * u.y > v.y    0 1 0 0
     * u.x = v.x    0 0 1 0
     * u.y = v.y    0 0 0 1
     *
     * The result is then added binary, meaning for example 1100 (3) means u > y (both), but 0100 (2) means ux < vx, but uy>vy
     * 0011 (12) means u==v
     * 
     * @param v Vector to compare with
     * @return the result
     */
    public int compare(Vector2d v) {
        byte v1 = 0, v2 = 0, v3 = 0, v4 = 0;
        if (this.x > v.x) {
            v1 = 1;
        } else if (floatCompare(x, v.x)) {
            v3 = 1;
        }
        if (this.y > v.y) {
            v2 = 1;
        } else if (floatCompare(y, v.y)) {
            v4 = 1;
        }
        return 1 * v1 + 2 * v2 + 4 * v3 + 8 * v4;
    }

    public boolean floatCompare(float a, float b) {
        return (Math.abs(a - b) < epsilon);
    }

    public boolean equals(Vector2d v) {
        if (floatCompare(x, v.x) && floatCompare(y, v.y)) {
            return true;
        } else {
            return false;
        }
    }

    public int intX() {
        return (int) Math.floor(x);
    }

    public int intY() {
        return (int) Math.floor(y);
    }

    public Vector2d floor() {
        float nx = (float) Math.floor(x);
        float ny = (float) Math.floor(y);
        return new Vector2d(nx, ny);
    }

    public Vector2d ceil() {
        float nx = (float) Math.ceil(x);
        float ny = (float) Math.ceil(y);
        return new Vector2d(nx, ny);
    }

    public String toString() {
        return "(" + x + ";" + y + ")";
    }

    public Vector2d round() {
        float nx = (float) Math.round(x);
        float ny = (float) Math.round(y);
        return new Vector2d(nx, ny);
    }

    public Vector2d add(float a) {
        return new Vector2d(this.x + a, this.y + a);
    }

    public Vector2d subtract(float a) {
        return new Vector2d(this.x - a, this.y - a);
    }

    public static boolean inVector(ArrayList<Vector2d> used, Vector2d slot) {
        Iterator<Vector2d> it = used.iterator();
        while (it.hasNext()) {
            if (it.next().equals(slot)) {
                return true;
            }
        }
        return false;
    }

    public String intString() {
        return "(" + intX() + ";" + intY() + ")";
    }
}
