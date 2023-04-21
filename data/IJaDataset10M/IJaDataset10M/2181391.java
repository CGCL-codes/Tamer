package de.matthiasmann.twl.utils;

/**
 * A utility class to parse parameter lists in string form like the query
 * part of an Url or CSS styles.
 *
 * @author Matthias Mann
 */
public class ParameterStringParser {

    private final String str;

    private final char parameterSeparator;

    private final char keyValueSeparator;

    private boolean trim;

    private int pos;

    private String key;

    private String value;

    /**
     * Creates a new parser object.
     * 
     * @param str the String to parse
     * @param parameterSeparator the character which separates key-value pairs from each other
     * @param keyValueSeparator the character which separates key and value from each other
     */
    public ParameterStringParser(String str, char parameterSeparator, char keyValueSeparator) {
        if (str == null) {
            throw new NullPointerException("str");
        }
        if (parameterSeparator == keyValueSeparator) {
            throw new IllegalArgumentException("parameterSeperator == keyValueSeperator");
        }
        this.str = str;
        this.parameterSeparator = parameterSeparator;
        this.keyValueSeparator = keyValueSeparator;
    }

    public boolean isTrim() {
        return trim;
    }

    /**
     * Enables trimming of white spaces on key and values
     * @param trim true if white spaces should be trimmed
     * @see Character#isWhitespace(char)
     */
    public void setTrim(boolean trim) {
        this.trim = trim;
    }

    /**
     * Extract the next key-value pair
     * @return true if a pair was extracted false if the end of the string was reached.
     */
    public boolean next() {
        while (pos < str.length()) {
            int kvPairEnd = TextUtil.indexOf(str, parameterSeparator, pos);
            int keyEnd = TextUtil.indexOf(str, keyValueSeparator, pos);
            if (keyEnd < kvPairEnd) {
                key = substring(pos, keyEnd);
                value = substring(keyEnd + 1, kvPairEnd);
                pos = kvPairEnd + 1;
                return true;
            }
            pos = kvPairEnd + 1;
        }
        key = null;
        value = null;
        return false;
    }

    /**
     * Returns the current key
     * @return the current key
     * @see #next()
     */
    public String getKey() {
        if (key == null) {
            throw new IllegalStateException("no key-value pair available");
        }
        return key;
    }

    /**
     * Returns the current value
     * @return the current value
     * @see #next()
     */
    public String getValue() {
        if (value == null) {
            throw new IllegalStateException("no key-value pair available");
        }
        return value;
    }

    private String substring(int start, int end) {
        if (trim) {
            while (start < end && Character.isWhitespace(str.charAt(start))) {
                start++;
            }
            while (end > start && Character.isWhitespace(str.charAt(end - 1))) {
                end--;
            }
        }
        return str.substring(start, end);
    }
}
