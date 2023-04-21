package com.strategicgains.openef.util;

/**
 * @author Todd Fredrich
 * @since  Apr 6, 2004
 * @version $Revision: 1.8 $
 */
public abstract class Strings {

    public static final String EMPTY_STRING = "";

    /**
	 * Determines equality (case sensitive) of the two strings, allowing either or both to be null.
	 * 
	 * @param string1
	 * @param string2
	 * @return true if string1 and string2 have the same value, false otherwise.
	 */
    public static boolean areEqual(String string1, String string2) {
        if (string1 == null) {
            return (string2 == null);
        } else if (string2 == null) {
            return false;
        }
        return string1.equals(string2);
    }

    /**
	 * Determines equality (ignoring case) of the two strings, allowing either or both to be null.
	 * 
	 * @param string1
	 * @param string2
	 * @return true if string1 and string2 have the same value (ignoring case), false otherwise.
	 */
    public static boolean areEqualIgnoringCase(String string1, String string2) {
        if (string1 == null) {
            return (string2 == null);
        } else if (string2 == null) {
            return false;
        }
        return string1.equalsIgnoreCase(string2);
    }

    /**
	 * @param string
	 * @return
	 */
    public static String htmlEncode(String string) {
        return string;
    }

    /**
	 * Determines if the string is null or has a trimmed length of zero.
	 * 
	 * @param value A String to check for null or blank.
	 * @return true if the string is null or is blank.
	 */
    public static boolean isBlankOrNull(String string) {
        return (string == null || string.trim().length() == 0);
    }
}
