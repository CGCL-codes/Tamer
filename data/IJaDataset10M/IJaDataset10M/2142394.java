package com.rapidminer.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A heuristic number parser. That tries to analyze a given String in a more
 * flexible way than the traditional Java number parse:
 *  
 *  1. All "," are replaced by "."
 *  2. &#188;,&#189;,&#190; are replaced by 0.25, 0.5, 0.75 respectively
 *  3. Ranges ([number]-[number])are matched and the arithmetic average is used as result
 *  4. All Prefixes and suffices are ignored
 *  
 *  Example: "Weight: 8,5 - 10,5 g" would result in 9.5
 *    
 * @author Michael Wurst
 *
 */
public class NumberParser {

    /**
	 * Parse a number possibly surrounded by other information.
	 * @param s the string
	 * @return a double representation or NaN if it cannot be parsed
	 */
    public static double parse(String s) {
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
        }
        s = s.replaceAll(",", ".");
        s = s.replaceAll("¼", ".25");
        s = s.replaceAll("½", ".5");
        s = s.replaceAll("¾", ".75");
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
        }
        Pattern p2 = Pattern.compile("[^0-9]*([[0-9.]\\.]+)\\s?-\\s?([[0-9]\\.]+)(.*)");
        Matcher m2 = p2.matcher(s);
        if (m2.matches()) {
            try {
                return (Double.parseDouble(m2.group(1)) + Double.parseDouble(m2.group(2))) / 2;
            } catch (NumberFormatException e) {
            }
        }
        Pattern p1 = Pattern.compile("[^0-9]*([[0-9]\\.]+)(.*)");
        Matcher m1 = p1.matcher(s);
        if (m1.matches()) {
            try {
                return Double.parseDouble(m1.group(1));
            } catch (NumberFormatException e) {
            }
        }
        return Double.NaN;
    }

    /**
	 * This method parses the given string as double value.
	 * It first tries the normal parse method, then tests if it is the ?
	 * and would return NaN in this case.
	 * Otherwise a NumberFormatExceptin is thrown.
	 */
    public static double parseDouble(String s) throws NumberFormatException {
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
        }
        if (s.equals("?")) return Double.NaN;
        throw new NumberFormatException();
    }
}
