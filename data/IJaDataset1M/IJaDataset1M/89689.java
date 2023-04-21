package es.gva.cit.catalog.utils;

import java.util.TreeMap;
import java.util.Vector;

/**
 * String Functions
 * 
 * 
 * @author Jorge Piera Llodra (piera_jor@gva.es)
 */
public class Strings {

    /**
 * Replace a part of a String
 * 
 * 
 * @return 
 * @param str String to find the pattern
 * @param pattern Pattern to find
 * @param replace String to replace
 */
    public static String replace(String str, String pattern, String replace) {
        int s = 0;
        int e = 0;
        StringBuffer result = new StringBuffer();
        while ((e = str.indexOf(pattern, s)) >= 0) {
            result.append(str.substring(s, e));
            result.append(replace);
            s = e + pattern.length();
        }
        result.append(str.substring(s));
        return result.toString();
    }

    /**
 * Find a pattern into one array
 * 
 * 
 * @return 
 * @param pattern 
 * @param array 
 */
    public static boolean find(String pattern, String[] array) {
        if (array != null) {
            for (int i = 0; i < array.length; i++) if (pattern.equals(array[i])) {
                return true;
            }
        }
        return false;
    }

    /**
 * This function joins two strings
 * 
 * 
 * @return 
 * @param s1 
 * @param s2 
 */
    public static String[] join(String[] s1, String[] s2) {
        if (s1 == null) {
            return s2;
        }
        if (s2 == null) {
            return s1;
        }
        String[] out = new String[s1.length + s2.length];
        for (int i = 0; i < s1.length; i++) out[i] = s1[i];
        for (int i = 0; i < s2.length; i++) out[s1.length + i] = s2[i];
        return out;
    }

    /**
 * returns a list (comma-separated) in one unique string
 * 
 * 
 * @return 
 * @param input 
 */
    public static String getComaSeparated(String[] input) {
        return getXSeparated(",", input);
    }

    /**
 * returns a list (blanck-separated) in one unique string
 * 
 * 
 * @return 
 * @param input 
 */
    public static String getBlankSeparated(String[] input) {
        return getXSeparated(" ", input);
    }

    /**
 * returns a list (X-Character-separated) in one unique string
 * 
 * 
 * @return 
 * @param X 
 * @param input 
 */
    private static String getXSeparated(String X, String[] input) {
        String output = "";
        if (input.length == 0) {
            return "";
        }
        output = input[0];
        for (int i = 1; i < input.length; i++) output = output + X + input[i];
        return output;
    }

    /**
 * This method creates an String of words (blanck separated)
 * enveloped by asteriscs: Example
 * "This is an example" returns
 * "*This* *is* *an* *example*"
 * 
 * 
 * @return String
 * @param array Input String
 */
    public static String addAsteriscsFromAnArray(String array) {
        String[] s = array.split(" ");
        String output = "";
        for (int i = 0; i < s.length; i++) {
            output = output + " *" + s[i] + "*";
        }
        return output;
    }

    /**
 * This function is used to try if a string is a number.
 * 
 * 
 * @return The numbers of the String
 * @param str Input String
 * @param charNumber Number of chars that must be tried
 */
    public static String tryIfIsNumber(String str, int charNumber) {
        char[] source = str.toCharArray();
        if (source.length < charNumber) charNumber = source.length;
        char[] resultado = new char[charNumber];
        int j = 0;
        for (int i = 0; i < resultado.length; i++) if (i < (source.length) && (source[i] >= '0') && (source[i] <= '9')) resultado[j++] = source[i];
        return new String(resultado, 0, j);
    }

    /**
     * This method remove all the string accents
     * @param str
     * Input String
     * @return
     * String withOut accents
     */
    public static String removeAccents(String str) {
        str = str.replace('�', 'a');
        str = str.replace('�', 'e');
        str = str.replace('�', 'i');
        str = str.replace('�', 'o');
        str = str.replace('�', 'u');
        str = str.replace('�', 'A');
        str = str.replace('�', 'E');
        str = str.replace('�', 'I');
        str = str.replace('�', 'O');
        str = str.replace('�', 'a');
        str = str.replace('�', 'e');
        str = str.replace('�', 'i');
        str = str.replace('�', 'o');
        str = str.replace('�', 'u');
        str = str.replace('�', 'A');
        str = str.replace('�', 'E');
        str = str.replace('�', 'I');
        str = str.replace('�', 'O');
        str = str.replace('�', 'U');
        str = str.replace('�', 'a');
        str = str.replace('�', 'e');
        str = str.replace('�', 'i');
        str = str.replace('�', 'o');
        str = str.replace('�', 'u');
        str = str.replace('�', 'A');
        str = str.replace('�', 'E');
        str = str.replace('�', 'I');
        str = str.replace('�', 'O');
        str = str.replace('�', 'U');
        return str;
    }

    /**
     * This method returns all the forms than can have a 
     * word to do an advanced search. A form could be
     * to change all the characters to lower case, other one
     * could be to change only the first character to
     * upper case, ...
     * @param str
     * Input string
     * @return
     * A vector of strings
     */
    public static Vector allWordForms(String str, boolean withAccents) {
        Vector v = new Vector();
        if (!withAccents) {
            str = str = Strings.removeAccents(str);
        }
        v.add(str);
        v.add(str.toLowerCase());
        v.add(str.toUpperCase());
        v.add(Strings.getUperCaseFirst(str));
        if (withAccents) {
            str = Strings.removeAccents(str);
            v.add(str.toLowerCase());
            v.add(str.toUpperCase());
            v.add(Strings.getUperCaseFirst(str));
        }
        return v;
    }

    /**
     * It returns an string with the firs chacarter in UperCase
     * @param str
     * Input str
     * @return
     */
    public static String getUperCaseFirst(String str) {
        if (str.length() == 0) {
            return str;
        }
        if (str.length() == 1) {
            return str.toUpperCase();
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1, str.length()).toLowerCase();
    }

    /**
     * Creates a TreeMap froma KVP
     * @param pairValues
     * KVP string
     * @return
     * TreeMap
     */
    public static TreeMap separateParams(String pairValues) {
        TreeMap map = new TreeMap();
        String[] params = pairValues.split("&");
        for (int i = 0; i < params.length; i++) {
            String[] nameValue = params[i].split("=");
            map.put(nameValue[0].toUpperCase(), nameValue[1]);
        }
        return map;
    }
}
