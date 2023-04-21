package com.jgraph.gaeawt.java.awt;

import java.io.Serializable;
import org.apache.harmony.misc.HashCode;

public class Insets implements Cloneable, Serializable {

    private static final long serialVersionUID = -2272572637695466749L;

    public int top;

    public int left;

    public int bottom;

    public int right;

    public Insets(int top, int left, int bottom, int right) {
        setValues(top, left, bottom, right);
    }

    @Override
    public int hashCode() {
        int hashCode = HashCode.EMPTY_HASH_CODE;
        hashCode = HashCode.combine(hashCode, top);
        hashCode = HashCode.combine(hashCode, left);
        hashCode = HashCode.combine(hashCode, bottom);
        hashCode = HashCode.combine(hashCode, right);
        return hashCode;
    }

    @Override
    public Object clone() {
        return new Insets(top, left, bottom, right);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof Insets) {
            Insets i = (Insets) o;
            return ((i.left == left) && (i.bottom == bottom) && (i.right == right) && (i.top == top));
        }
        return false;
    }

    @Override
    public String toString() {
        return (getClass().getName() + "[left=" + left + ",top=" + top + ",right=" + right + ",bottom=" + bottom + "]");
    }

    public void set(int top, int left, int bottom, int right) {
        setValues(top, left, bottom, right);
    }

    private void setValues(int top, int left, int bottom, int right) {
        this.top = top;
        this.left = left;
        this.bottom = bottom;
        this.right = right;
    }
}
