package com.google.zxing.client.result;

import com.google.zxing.Result;

/**
 * <p>Abstract class representing the result of decoding a barcode, as more than
 * a String -- as some type of structured data. This might be a subclass which represents
 * a URL, or an e-mail address. {@link ResultParser#parseResult(Result)} will turn a raw
 * decoded string into the most appropriate type of structured representation.</p>
 *
 * <p>Thanks to Jeff Griffin for proposing rewrite of these classes that relies less
 * on exception-based mechanisms during parsing.</p>
 *
 * @author Sean Owen
 */
public abstract class ParsedResult {

    private final ParsedResultType type;

    protected ParsedResult(ParsedResultType type) {
        this.type = type;
    }

    public ParsedResultType getType() {
        return type;
    }

    public abstract String getDisplayResult();

    public String toString() {
        return getDisplayResult();
    }

    public static void maybeAppend(String value, StringBuffer result) {
        if (value != null && value.length() > 0) {
            if (result.length() > 0) {
                result.append('\n');
            }
            result.append(value);
        }
    }

    public static void maybeAppend(String[] value, StringBuffer result) {
        if (value != null) {
            for (int i = 0; i < value.length; i++) {
                if (value[i] != null && value[i].length() > 0) {
                    if (result.length() > 0) {
                        result.append('\n');
                    }
                    result.append(value[i]);
                }
            }
        }
    }
}
