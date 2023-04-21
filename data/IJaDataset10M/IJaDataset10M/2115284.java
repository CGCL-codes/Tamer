package org.jmlspecs.unfinished;

public class JMLEqualsEqualsPair implements JMLType {

    /** The key of this pair.
     */
    public final Object key;

    /** The value of this pair.
     */
    public final Object value;

    /** Initialize the key and value of this pair.
     */
    public JMLEqualsEqualsPair(Object dv, Object rv) throws NullPointerException {
        if (dv == null) {
            throw new NullPointerException("Attempt to create a" + " JMLEqualsEqualsPair with null" + " key");
        }
        if (rv == null) {
            throw new NullPointerException("Attempt to create a" + " JMLEqualsEqualsPair with null" + " value");
        }
        key = dv;
        value = rv;
    }

    public Object clone() {
        return new JMLEqualsEqualsPair(key, value);
    }

    public boolean keyEquals(Object dv) {
        return key.equals(dv);
    }

    public boolean valueEquals(Object rv) {
        return value.equals(rv);
    }

    public boolean equals(Object obj) {
        if (obj != null && obj instanceof JMLEqualsEqualsPair) {
            JMLEqualsEqualsPair pair = (JMLEqualsEqualsPair) obj;
            return pair.key.equals(key) && pair.value.equals(value);
        } else {
            return false;
        }
    }

    /** Return a hash code for this object.
     */
    public int hashCode() {
        return key.hashCode() + value.hashCode();
    }

    public String toString() {
        return (new String("(") + key + new String(", ") + value + new String(")"));
    }
}

;
