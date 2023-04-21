package eu.pouvesle.nicolas.dpp.util;

import java.util.regex.Pattern;

/**
 * <p align=justify> Provide static function for common dpp test.
 */
public class Test {

    /**
	 * Test if the input string is not null or if it is equal to the strin "null".
	 * 
	 * @param s String to test.
	 * @return  True if the string is not null or equal to "null".
	 */
    public static boolean isNoNull(String s) {
        if (s != null) return (!s.equalsIgnoreCase("null")); else return false;
    }

    /**
	 * Test if the input string is null or if it is equal to the strin "null".
	 * 
	 * @param s String to test.
	 * @return  True if the string is null or equal to "null".
	 */
    public static boolean isNull(String s) {
        if (s != null) return (s.equalsIgnoreCase("null")); else return true;
    }

    public static boolean isInt(String string) {
        Pattern intPattern = Pattern.compile("\\d+");
        return intPattern.matcher(string).matches();
    }

    public static boolean isNumeric(String string) {
        Pattern intPattern = Pattern.compile("\\d+");
        return intPattern.matcher(string).matches();
    }
}
