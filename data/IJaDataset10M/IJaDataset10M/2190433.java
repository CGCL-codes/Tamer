package org.jmlspecs.models;

public class JMLValueObjectPair<K extends JMLType, V> implements JMLType {

    /** The key of this pair.
     */
    public final K key;

    /** The value of this pair.
     */
    public final V value;

    /** Initialize the key and value of this pair.
     */
    public JMLValueObjectPair(K dv, V rv) throws NullPointerException {
        if (dv == null) {
            throw new NullPointerException("Attempt to create a" + " JMLValueObjectPair with null" + " key");
        }
        if (rv == null) {
            throw new NullPointerException("Attempt to create a" + " JMLValueObjectPair with null" + " value");
        }
        key = (K) dv.clone();
        value = rv;
    }

    public Object clone() {
        return new JMLValueObjectPair<K, V>(key, value);
    }

    public boolean keyEquals(K dv) {
        return key.equals(dv);
    }

    public boolean valueEquals(V rv) {
        return value == rv;
    }

    public boolean equals(Object obj) {
        if (obj != null && obj instanceof JMLValueObjectPair) {
            JMLValueObjectPair pair = (JMLValueObjectPair) obj;
            return pair.key.equals(key) && pair.value == value;
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
