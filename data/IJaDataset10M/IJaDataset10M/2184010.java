package org.yajul.text;

import java.text.ParsePosition;
import java.text.DecimalFormatSymbols;

/**
 * A decimal format that returns integers.
 * @author josh Apr 4, 2004 1:06:56 PM
 */
public class IntegerDecimalFormat extends java.text.DecimalFormat {

    public IntegerDecimalFormat() {
    }

    public IntegerDecimalFormat(String pattern) {
        super(pattern);
    }

    public IntegerDecimalFormat(String pattern, DecimalFormatSymbols symbols) {
        super(pattern, symbols);
    }

    public Number parse(String text, ParsePosition pos) {
        Number number = super.parse(text, pos);
        return new Integer(number.intValue());
    }
}
