package org.homeunix.thecave.moss.util;

import java.awt.FontMetrics;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

/**
 * A combination of many Formatting functions.
 * 
 * @author wyatt
 *
 */
public class Formatter {

    private static StringLengthFormat lengthFormat = new StringLengthFormat();

    public static NumberFormat getDecimalFormat() {
        return getDecimalFormat(2);
    }

    public static NumberFormat getDecimalFormat(int decimalPlaces) {
        NumberFormat f = DecimalFormat.getInstance();
        f.setMaximumFractionDigits(decimalPlaces);
        f.setMinimumFractionDigits(decimalPlaces);
        return f;
    }

    public static DateFormat getDateFormat() {
        return SimpleDateFormat.getInstance();
    }

    public static DateFormat getDateFormat(String format) {
        return new SimpleDateFormat(format);
    }

    public static StringLengthFormat getStringLengthFormat(int length) {
        lengthFormat.setLength(length);
        return lengthFormat;
    }

    /**
	 * Returns a string which will be at most maxPixelWidth pixels long as displayed by
	 * the given FontMetrics object.  If the string is any longer than that, it will
	 * be cut off, and show '...' at the end.
	 * @param value
	 * @param maxPixelWidth
	 * @param fm
	 * @return
	 */
    public static String getStringToLength(String value, int maxPixelWidth, FontMetrics fm) {
        if (fm == null) return value;
        int width;
        for (width = 5; width <= value.length() && fm.stringWidth(value.substring(0, width)) < maxPixelWidth; width++) ;
        return getStringLengthFormat(width).format(value);
    }

    /**
	 * A formatter-style class which cuts strings off after a given length.
	 * @author wyatt
	 *
	 */
    public static class StringLengthFormat {

        private int length;

        public String format(Object o) {
            StringBuffer sb = new StringBuffer();
            sb.append(o.toString());
            if (length < sb.toString().length()) {
                sb.delete(length, sb.length());
                sb.append("...");
            }
            return sb.toString();
        }

        public int getLength() {
            return length;
        }

        public void setLength(int length) {
            this.length = length;
        }
    }
}
