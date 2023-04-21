package morsen;

import java.lang.*;
import java.util.*;
import java.util.regex.*;
import eprog.*;

public class RegEx extends EprogIO {

    static String lZeichen = "-";

    static String kZeichen = ".";

    static String tZeichen = "!";

    static String ZEICHEN = "(" + lZeichen + "|" + kZeichen + "){0,6}";

    static String Morsen = tZeichen + ZEICHEN + "(" + tZeichen + ZEICHEN + ")*";

    public static boolean RegValidate(String input) {
        Pattern SDPattern = Pattern.compile(Morsen);
        Matcher m = SDPattern.matcher(input);
        boolean match = m.matches();
        return match;
    }

    public static String[] getZeichen(String input) {
        input = input.substring(1, input.length());
        String[] result = Pattern.compile(tZeichen).split(input);
        return result;
    }

    public static boolean compare(String pattern, String input) {
        Pattern SDPattern = Pattern.compile(pattern);
        Matcher m = SDPattern.matcher(input);
        boolean match = m.matches();
        return match;
    }

    public static boolean ValidateLength(String input, int length) {
        if (getZeichen(input).length > length) return false; else return true;
    }
}
