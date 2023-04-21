package utilities;

import java.awt.Color;
import java.awt.Point;

/**
 * Parses a given String to an appropriate Object
 * @author Arvanitis Ioannis
 */
public class String2ObjectParser {

    private final String[] supportedClasses = { "java.awt.Color", "java.awt.Point" };

    /**
     * Gets the parsed object referring to string str respectively
     * @param str The string to be parsed
     * @return an object after parsing the string str
     */
    public Object getParsedObject(String str) {
        Object obj = null;
        if (!isSupported(str)) {
            return obj;
        } else {
            String class2parse = null;
            for (int i = 0; i < supportedClasses.length; i++) {
                if (str.startsWith(supportedClasses[i])) {
                    class2parse = supportedClasses[i];
                    break;
                }
            }
            str = str.substring(str.indexOf("["));
            if (class2parse.equals("java.awt.Color")) {
                String r = str.substring(str.lastIndexOf("r") + 2, str.indexOf(","));
                String g = str.substring(str.lastIndexOf("g") + 2, str.lastIndexOf(","));
                String b = str.substring(str.lastIndexOf("b") + 2, str.indexOf("]"));
                obj = new Color(Integer.parseInt(r), Integer.parseInt(g), Integer.parseInt(b));
            } else if (class2parse.equals("java.awt.Point")) {
                String x = str.substring(str.lastIndexOf("x") + 2, str.indexOf(","));
                String y = str.substring(str.lastIndexOf("y") + 2, str.lastIndexOf("]"));
                obj = new Point(Integer.parseInt(x), Integer.parseInt(y));
            }
            return obj;
        }
    }

    /**
     * Determines whether a specified string (class) is supported for parsing
     * @param str The given string (class) to parse
     * @return true if this string (class) is supported for parsing
     */
    private boolean isSupported(String str) {
        for (int i = 0; i < supportedClasses.length; i++) {
            if (str.startsWith(supportedClasses[i])) {
                return true;
            }
        }
        return false;
    }
}
