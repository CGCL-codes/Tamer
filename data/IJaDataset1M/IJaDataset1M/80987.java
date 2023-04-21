package uk.ac.ukoln.rrorife.gui;

import java.util.*;

/**
 * Set of utility methods for handling the arrays used in the RRoRIfE package.
 * 
 * @author Alex Ball
 * 
 */
public class ArrayFunctions {

    /**
	 * Adds a string to the beginning of an array of strings.
	 * 
	 * @param oldArray
	 *          The array of strings to which a string needs to be prepended.
	 * @param s
	 *          The string to be prepended to the array.
	 * @return A new array of strings, consisting of the input string followed by
	 *         the strings of the input array.
	 */
    public static String[] prependStringArray(String[] oldArray, String s) {
        String[] newArray = new String[oldArray.length + 1];
        newArray[0] = s;
        if (oldArray.length > 0) {
            int i = 0;
            for (String t : oldArray) {
                i++;
                newArray[i] = t;
            }
        }
        return newArray;
    }

    /**
	 * Transforms an array of file format RRoRIfE IDs to an alphabetically sorted
	 * array of file format names/versions.
	 * 
	 * @param oldArray
	 *          Array of file format RRoRIfE IDs (strings).
	 * @return Alphabetically sorted array of file format names/versions.
	 */
    public static String[] decodeFormats(String[] oldArray) {
        String[] newArray = new String[oldArray.length];
        if (oldArray.length > 0) {
            int i = 0;
            for (String s : oldArray) {
                newArray[i] = ParseFfChars.getFormat(s);
                i++;
            }
        }
        Arrays.sort(newArray);
        return newArray;
    }

    /**
	 * Transforms an array of file format RRoRIfE IDs into a map that translates
	 * (system generated) file format name/version strings back into RRoRIfE IDs.
	 * 
	 * @param formats
	 *          Array of file format RRoRIfE IDs (strings).
	 * @return Mapping of file format names/versions to RRoRIfE IDs.
	 */
    public static HashMap<String, String> formatMap(String[] formats) {
        HashMap<String, String> formatMap = new HashMap<String, String>();
        for (String format : formats) {
            formatMap.put(ParseFfChars.getFormat(format), format);
        }
        return formatMap;
    }

    /**
	 * Transforms an array of converter RRoRIfE IDs to an alphabetically sorted
	 * array of converter names/versions.
	 * 
	 * @param oldArray
	 *          Array of converter RRoRIfE IDs (strings).
	 * @return Alphabetically sorted array of converter names/versions.
	 */
    public static String[] decodeConvs(String[] oldArray) {
        String[] newArray = new String[oldArray.length];
        if (oldArray.length > 0) {
            for (int i = 0; i < newArray.length; i++) {
                newArray[i] = ParseConvIssues.getConverter(oldArray[i]);
            }
        }
        Arrays.sort(newArray);
        return newArray;
    }

    /**
	 * Transforms an array of converter RRoRIfE IDs into a map that translates
	 * (system generated) converter name/version strings back into RRoRIfE IDs.
	 * 
	 * @param formats
	 *          Array of converter RRoRIfE IDs (strings).
	 * @return Mapping of converter names/versions to RRoRIfE IDs.
	 */
    public static HashMap<String, String> convMap(String[] formats) {
        HashMap<String, String> formatMap = new HashMap<String, String>();
        for (String format : formats) {
            formatMap.put(ParseConvIssues.getConverter(format), format);
        }
        return formatMap;
    }
}
