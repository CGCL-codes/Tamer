package nl.huub.van.amelsvoort.util;

import java.util.Vector;

/**
 * Vargs is a helper class to encapsulate printf arguments.
 * 
 * @author cwei
 */
public class Vargs {

    static final int SIZE = 5;

    Vector v;

    public Vargs() {
        this(SIZE);
    }

    public Vargs(int initialSize) {
        if (v != null) v.clear();
        v = new Vector(initialSize);
    }

    public Vargs add(boolean value) {
        v.add(new Boolean(value));
        return this;
    }

    public Vargs add(byte value) {
        v.add(new Byte(value));
        return this;
    }

    public Vargs add(char value) {
        v.add(new Character(value));
        return this;
    }

    public Vargs add(short value) {
        v.add(new Short(value));
        return this;
    }

    public Vargs add(int value) {
        v.add(new Integer(value));
        return this;
    }

    public Vargs add(long value) {
        v.add(new Long(value));
        return this;
    }

    public Vargs add(float value) {
        v.add(new Float(value));
        return this;
    }

    public Vargs add(double value) {
        v.add(new Double(value));
        return this;
    }

    public Vargs add(String value) {
        v.add(value);
        return this;
    }

    public Vargs add(Object value) {
        v.add(value);
        return this;
    }

    public Vargs clear() {
        v.clear();
        return this;
    }

    public Vector toVector() {
        return (Vector) v.clone();
    }

    public Object[] toArray() {
        return v.toArray();
    }

    public int size() {
        return v.size();
    }
}
