package org.mobicents.media.server.impl.naming;

/**
 *
 * @author kulikov
 */
public class NumericRange implements NameToken {

    private int low, high;

    private int value;

    public NumericRange(String range) {
        range = range.substring(1, range.length() - 1);
        range = range.replaceAll("]", "");
        String tokens[] = range.split("\\.\\.");
        low = Integer.parseInt(tokens[0]);
        high = Integer.parseInt(tokens[1]);
        value = low - 1;
    }

    public boolean hasMore() {
        return value < high;
    }

    public String next() {
        if (value < high) value++;
        return Integer.toString(value);
    }
}
