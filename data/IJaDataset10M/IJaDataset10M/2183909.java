package com.tekimpact.tuples4j;

/**
 *
 * @author Paulo - Tekimpact
 */
public class KeyValuePair<A, B> extends Pair<A, B> {

    public KeyValuePair(A first, B second) {
        super(first, second);
    }

    public String toString() {
        if (getSecond() == null) {
            return "";
        }
        return getSecond().toString();
    }
}
