package org.mulgara.util;

import java.util.Map;

/**
 * A pair of values representing a first/second pair
 */
public class Pair<A, B> implements Map.Entry<A, B> {

    /** The first. */
    public final A _1;

    /** The second. */
    public final B _2;

    /**
   * Create a new first/value pair.
   * @param a The first part of the pair.
   * @param b The second part of the pair.
   */
    public Pair(A a, B b) {
        _1 = a;
        _2 = b;
    }

    /**
   * Gets the first
   */
    public A getFirst() {
        return _1;
    }

    /**
   * Synonym for getFirst
   */
    public A getKey() {
        return _1;
    }

    /**
   * Gets the value
   */
    public B getValue() {
        return _2;
    }

    /**
   * Synonym for getValue
   */
    public B getSecond() {
        return _2;
    }

    /**
   * Setting the value is not supported.
   */
    public B setValue(B b) {
        throw new UnsupportedOperationException("Unable to set an immutable object");
    }

    /**
   * Equality method. Based on the wrapped URI.
   */
    public boolean equals(Object o) {
        return (o instanceof Pair) && ((Pair<?, ?>) o)._1.equals(_1) && ((Pair<?, ?>) o)._2.equals(_2);
    }

    /**
   * The hashcode, based on the first and second.
   */
    public int hashCode() {
        return _1.hashCode() * 11 + _2.hashCode();
    }

    /**
   * Represents this node as a string.
   */
    public String toString() {
        return "(" + _1 + "," + _2 + ")";
    }
}
