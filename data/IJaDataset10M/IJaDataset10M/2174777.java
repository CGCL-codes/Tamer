package net.sf.dozer.util.mapping.vo;

import java.io.Serializable;

/**
 * @author garsombke.franz
 * @author sullins.ben
 * @author tierney.matt
 * 
 */
public class DoubleObject implements Serializable {

    private double value;

    public DoubleObject() {
    }

    public DoubleObject(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DoubleObject)) {
            return false;
        }
        final DoubleObject doubleObject = (DoubleObject) o;
        if (value != doubleObject.value) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        final long temp = value != +0.0d ? Double.doubleToLongBits(value) : 0l;
        return (int) (temp ^ (temp >>> 32));
    }

    public String toString() {
        return String.valueOf(value);
    }
}
