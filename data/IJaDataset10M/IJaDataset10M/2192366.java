package com.rbnb.compat;

/** Utility class for compatibility with J2ME. Usually fills in for classes and methods that aren't
 * existing in J2ME but are in J2SE
 */
public class Utilities {

    /** Returns true if the character is a whitespace
	 */
    public static final boolean isWhitespace(char c) {
        if (c == ' ' || c == ' ' || c == ' ' || c == '	' || c == '' || c == '' || c == '' || c == '' || c == '' || c == '' || c == ' ' || c == '\t' || c == '\n' || c == '\r') {
            return true;
        } else {
            return false;
        }
    }

    /** Returns a date formatted.
	 */
    public static String dateFormat(java.util.Date d) {
        return d.toString();
    }

    /** Returns a Date object from the String
	 */
    public static java.util.Date SimpleDateFormat(String str) {
        return SimpleDateFormat(null, str);
    }

    /** Returns a Date object from the String, the first str does nothing, but
	 * is initially intended as a format string
	 */
    public static java.util.Date SimpleDateFormat(String str, String d) {
        StringTokenizer t = new StringTokenizer(d, " :");
        String month = t.nextToken();
        int day = Integer.parseInt(t.nextToken());
        int year = Integer.parseInt(t.nextToken());
        String temp = null;
        int hour = 0, min = 0, sec = 0;
        if (t.hasMoreTokens()) {
            hour = Integer.parseInt(t.nextToken());
            min = Integer.parseInt(t.nextToken());
            sec = Integer.parseInt(t.nextToken());
        }
        int mon;
        if (month.equals("Jan")) mon = 1; else if (month.equals("Feb")) mon = 2; else if (month.equals("Mar")) mon = 3; else if (month.equals("Apr")) mon = 4; else if (month.equals("May")) mon = 5; else if (month.equals("Jun")) mon = 6; else if (month.equals("Jul")) mon = 7; else if (month.equals("Aug")) mon = 8; else if (month.equals("Sep")) mon = 9; else if (month.equals("Oct")) mon = 10; else if (month.equals("Nov")) mon = 11; else mon = 12;
        long yeardiff = year - 1970;
        long total = 0;
        total += yeardiff * 365 * 24 * 60 * 60 * 1000;
        int leaps = (int) (yeardiff + 2) / 4;
        total += leaps * 24 * 60 * 60 * 1000;
        int nowleap;
        if ((yeardiff + 2) % 4 == 0) nowleap = 1; else nowleap = 0;
        long[] daysdiff = { 0, 31, 59 + nowleap, 90 + nowleap, 120 + nowleap, 151 + nowleap, 181 + nowleap, 212 + nowleap, 243 + nowleap, 273 + nowleap, 304 + nowleap, 334 + nowleap };
        total += daysdiff[mon - 1] * 24 * 60 * 60 * 1000;
        total += (day - 1) * 24 * 60 * 60 * 1000;
        total += hour * 60 * 60 * 1000;
        total += sec * 1000;
        total += min * 60 * 1000;
        total += 8 * 60 * 60 * 1000;
        return new java.util.Date(total);
    }

    /** Returns if the Thread is interrupted. No such thing in J2ME, so we just return false for now
	 */
    public static boolean interrupted(Thread t) {
        return false;
    }

    /** Returns the length of the array, assuming it is an array object
	 */
    public static int getArrayLength(Object arrayI) {
        int elements;
        if (arrayI instanceof boolean[]) {
            elements = ((boolean[]) arrayI).length;
        } else if (arrayI instanceof byte[]) {
            elements = ((byte[]) arrayI).length;
        } else if (arrayI instanceof double[]) {
            elements = ((double[]) arrayI).length;
        } else if (arrayI instanceof float[]) {
            elements = ((float[]) arrayI).length;
        } else if (arrayI instanceof int[]) {
            elements = ((int[]) arrayI).length;
        } else if (arrayI instanceof long[]) {
            elements = ((long[]) arrayI).length;
        } else if (arrayI instanceof short[]) {
            elements = ((short[]) arrayI).length;
        } else if (arrayI instanceof String[]) {
            elements = ((String[]) arrayI).length;
        } else elements = ((Object[]) arrayI).length;
        return elements;
    }

    /** Clones the vector. Clones every item within the vector as well.
	 */
    public static java.util.Vector cloneVector(java.util.Vector v) {
        java.util.Vector ret;
        if (v instanceof com.rbnb.utility.SortedVector) {
            return (java.util.Vector) ((com.rbnb.utility.SortedVector) v).clone();
        } else {
            ret = new java.util.Vector();
        }
        for (int i = 0; i < v.size(); i++) {
            try {
                ret.addElement(((com.rbnb.compat.Cloneable) v.elementAt(i)).clone());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return v;
    }

    /** Returns the last index of the given string from the first string.
	 */
    public static int lastIndexOf(String base, String str) {
        int result = 0, temp = 0;
        result = temp = base.indexOf(str);
        while (temp != -1) {
            result = temp;
            temp = base.indexOf(str, result + 1);
        }
        return result;
    }

    /** Returns true if the object is a number object.
	 */
    public static boolean isNumber(Object o) {
        return ((o instanceof Byte) || (o instanceof Double) || (o instanceof Float) || (o instanceof Integer) || (o instanceof Long) || (o instanceof Short));
    }

    /** Rounds the double to the nearest integer.
	 */
    public static double round(double a) {
        if ((a - (int) a) * 10 >= 5) return Math.ceil(a); else return Math.floor(a);
    }

    /** Returns the power of a^b. This version assumes b is an integer
	 */
    public static double pow(double a, double b) {
        double ret = 1;
        for (int i = 0; i < b; i++) ret *= a;
        return ret;
    }

    /** Returns the object at the given index
	 */
    public static Object arrayGet(Object arrayI, int index) throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
        if (arrayI instanceof boolean[]) {
            return new Boolean(((boolean[]) arrayI)[index]);
        } else if (arrayI instanceof byte[]) {
            return new Byte(((byte[]) arrayI)[index]);
        } else if (arrayI instanceof double[]) {
            return new Double(((double[]) arrayI)[index]);
        } else if (arrayI instanceof float[]) {
            return new Float(((float[]) arrayI)[index]);
        } else if (arrayI instanceof int[]) {
            return new Integer(((int[]) arrayI)[index]);
        } else if (arrayI instanceof long[]) {
            return new Long(((long[]) arrayI)[index]);
        } else if (arrayI instanceof short[]) {
            return new Short(((short[]) arrayI)[index]);
        } else if (arrayI instanceof String[]) {
            return ((String[]) arrayI)[index];
        } else if (arrayI instanceof Object[]) {
            return ((Object[]) arrayI)[index];
        } else throw new IllegalArgumentException("Not an array argument");
    }
}
