package net.sf.mset;

import java.io.Serializable;

/**
 * An upper or lower boundary of an interval. The boundary element may or may 
 * not be contained in the interval but every element in the base set from 
 * the opposite boundary to this one are. If the boundary element is null, 
 * that end of the interval is unbounded. When an end is unbounded, the 
 * boundary element (positive or negative infinity) is never included in the 
 * interval because it doesn't have a real value. However, every possible 
 * element approaching it is included.
 * <p>
 * The boundary is closed if the boundary element is included; otherwise the 
 * boundary is open. When an end is unbounded, it is considered closed even 
 * though the boundary element is excluded since every possible element 
 * approaching it is included.
 * 
 * @author Greg Ferguson
 *
 * @param <E>
 */
public class Boundary<V> implements Comparable<Boundary<V>>, Serializable {

    private static final long serialVersionUID = 1L;

    public enum Type {

        UPPER, LOWER
    }

    private final V value;

    private final boolean inclusive;

    private final Type type;

    private final TotallyOrderedSet<V> baseSet;

    Boundary(V value, boolean inclusive, Type type, TotallyOrderedSet<V> baseSet) {
        if (value == null && inclusive) {
            throw new IllegalArgumentException("An unbounded boundary cannot be included");
        }
        this.value = value;
        this.inclusive = inclusive;
        this.type = type;
        this.baseSet = baseSet;
    }

    public V getValue() {
        return value;
    }

    public boolean isInclusive() {
        return inclusive;
    }

    public Type getType() {
        return type;
    }

    public boolean after(V o) {
        if (value == null) return type == Type.UPPER;
        int c = compare(value, o);
        if (c == 0) {
            if (inclusive) return false;
            return type == Type.LOWER;
        }
        return c > 0;
    }

    public boolean before(V o) {
        if (value == null) return type == Type.LOWER;
        int c = compare(value, o);
        if (c == 0) {
            if (inclusive) return false;
            return type == Type.UPPER;
        }
        return c < 0;
    }

    public boolean meets(Boundary<V> o) {
        return type != o.type && value != null && o.value != null && compare(value, o.value) == 0 && inclusive != o.inclusive;
    }

    public int compareTo(Boundary<V> o) {
        if (value == null) {
            if (o.value == null && type == o.type) return 0;
            return type == Type.UPPER ? 1 : -1;
        } else if (o.value == null) {
            return o.type == Type.UPPER ? -1 : 1;
        }
        int c = compare(value, o.value);
        if (c == 0) {
            if (type == o.type) {
                if (inclusive == o.inclusive) return 0;
                if (inclusive) return type == Type.LOWER ? -1 : 1;
                return type == Type.LOWER ? 1 : -1;
            }
            if (inclusive && o.inclusive) return 0;
            return type == Type.LOWER ? 1 : -1;
        }
        return c;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + baseSet.hashCode();
        result = prime * result + (inclusive ? 1231 : 1237);
        result = prime * result + type.hashCode();
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final Boundary<?> other = (Boundary<?>) obj;
        if (!baseSet.equals(other.baseSet)) return false;
        if (type != other.type) return false;
        if (value == null) {
            if (other.value != null) return false;
        } else if (other.value == null) return false; else if (!value.equals(other.value)) return false; else if (inclusive != other.inclusive) return false;
        return true;
    }

    @Override
    public String toString() {
        if (type == Type.LOWER) {
            return (inclusive ? "[" : "(") + (value == null ? "-inf" : value);
        } else {
            return (value == null ? "inf" : value) + (inclusive ? "]" : ")");
        }
    }

    @SuppressWarnings(value = { "unchecked" })
    private int compare(V o1, V o2) {
        if (baseSet.comparator() != null) {
            return baseSet.comparator().compare(o1, o2);
        }
        Comparable<? super V> c1 = (Comparable<? super V>) o1;
        return c1.compareTo(o2);
    }
}
