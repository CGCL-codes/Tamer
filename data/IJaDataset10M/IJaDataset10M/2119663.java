package jgnash.util;

import java.awt.*;
import java.util.Locale;

/**
 * EncodeDecode is used to encode/decode various objects using a String
 *
 * @author Craig Cavanaugh
 *
 * $Id: EncodeDecode.java 675 2008-06-17 01:36:01Z ccavanaugh $
 */
public class EncodeDecode {

    private EncodeDecode() {
    }

    public static String encodeDimension(Dimension d) {
        StringBuffer buf = new StringBuffer();
        buf.append(d.width);
        buf.append(',');
        buf.append(d.height);
        return buf.toString();
    }

    public static Dimension decodeDimension(String d) {
        if (d == null) {
            return null;
        }
        Dimension rect = null;
        String[] array = d.split(",");
        if (array.length == 2) {
            try {
                rect = new Dimension();
                rect.width = Integer.parseInt(array[0]);
                rect.height = Integer.parseInt(array[1]);
            } catch (NumberFormatException nfe) {
                rect = null;
            }
        }
        return rect;
    }

    public static String encodeRectangle(Rectangle bounds) {
        StringBuffer buf = new StringBuffer();
        buf.append(bounds.x);
        buf.append(',');
        buf.append(bounds.y);
        buf.append(',');
        buf.append(bounds.width);
        buf.append(',');
        buf.append(bounds.height);
        return buf.toString();
    }

    public static Rectangle decodeRectangle(String bounds) {
        if (bounds == null) {
            return null;
        }
        Rectangle rect = null;
        String[] array = bounds.split(",");
        if (array.length == 4) {
            try {
                rect = new Rectangle();
                rect.x = Integer.parseInt(array[0]);
                rect.y = Integer.parseInt(array[1]);
                rect.width = Integer.parseInt(array[2]);
                rect.height = Integer.parseInt(array[3]);
            } catch (NumberFormatException nfe) {
                rect = null;
            }
        }
        return rect;
    }

    public static String encodeLocale(Locale locale) {
        StringBuffer buf = new StringBuffer();
        buf.append(locale.getLanguage());
        if (!locale.getCountry().equals("")) {
            buf.append('.');
            buf.append(locale.getCountry());
            if (!locale.getVariant().equals("")) {
                buf.append('.');
                buf.append(locale.getVariant());
            }
        }
        return buf.toString();
    }

    public static Locale decodeLocale(String locale) {
        if (locale.equals("")) {
            return Locale.getDefault();
        } else if (locale.indexOf('.') == -1) {
            return new Locale(locale);
        } else {
            String[] array = locale.split("\\x2E");
            if (array.length == 3) {
                return new Locale(array[0], array[1], array[2]);
            } else if (array.length == 2) {
                return new Locale(array[0], array[1]);
            } else {
                return Locale.getDefault();
            }
        }
    }

    /** Encodes a boolean array as a string of 1's and 0's
	 * @param array a boolean array to encode as a String
	 * @return A string of 1's and 0's representing the boolean array
	 */
    public static String encodeBooleanArray(boolean[] array) {
        StringBuffer buf = new StringBuffer();
        if (array != null) {
            for (int i = 0; i < array.length; i++) {
                if (array[i]) {
                    buf.append('1');
                } else {
                    buf.append('0');
                }
            }
            return buf.toString();
        }
        return null;
    }

    /** Turns a string of "10101" into a boolean array
	 * @param array
	 * @return the boolean array, null if zero lengh or array was null
	 */
    public static boolean[] decodeBooleanArray(String array) {
        if (array != null) {
            int len = array.length();
            if (len > 0) {
                boolean b[] = new boolean[len];
                for (int i = 0; i < len; i++) {
                    if (array.charAt(i) == '1') {
                        b[i] = true;
                    }
                }
                return b;
            }
        }
        return null;
    }
}
