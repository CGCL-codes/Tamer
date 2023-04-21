package infinitewisdom.model.util;

import static java.lang.Math.*;
import java.io.Serializable;
import java.util.Locale;

public class Vec2f implements Serializable, Cloneable {

    public double x;

    public double y;

    public Vec2f() {
    }

    public Vec2f(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vec2f(Vec2i o, double quant) {
        x = o.x * quant;
        y = o.y * quant;
    }

    public void add(Vec2f o) {
        x = x + o.x;
        y = y + o.y;
    }

    /** Multiply with scalar. */
    public void muls(double s) {
        x = x * s;
        y = y * s;
    }

    /** Multiply with transposed vector. (i.e. elemntwise muliplication)*/
    public void mulvt(Vec2f o) {
        x = x * o.x;
        y = y * o.y;
    }

    /** Dot product. */
    public double dot(Vec2f o) {
        return x * o.x + y * o.y;
    }

    public void sub(Vec2f o) {
        x = x - o.x;
        y = y - o.y;
    }

    public void sub(Vec2i o, double quant) {
        sub(new Vec2f(o.x * quant, o.y * quant));
    }

    public double dist(Vec2f o) {
        return sqrt(pow((this.x - o.x), 2.0) + pow((this.y - o.y), 2.0));
    }

    public double dist(Vec2i o, double quant) {
        return dist(new Vec2f(o.x * quant, o.y * quant));
    }

    public double length() {
        return sqrt(pow((this.x), 2.0) + pow((this.y), 2.0));
    }

    public void normalize() {
        muls(1.0 / length());
    }

    @Override
    public Vec2f clone() {
        try {
            return (Vec2f) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String toString() {
        return String.format(Locale.ENGLISH, "%.2f, %.2f", x, y);
    }

    public void set(Vec2f o) {
        x = o.x;
        y = o.y;
    }
}
