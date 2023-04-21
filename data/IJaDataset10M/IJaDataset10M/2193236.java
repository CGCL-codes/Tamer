package axs.jdbc.utils;

/**
 * 
 * This class is used to provide String utilities for the WsvJdbc driver.
 * 
 * @author 	Daniele Pes  
 */
public class StringUtilities {

    public static boolean isInStringVector(String[] strVect, String str) {
        boolean result = false;
        if (strVect == null) return result;
        for (int i = 0; i < strVect.length; i++) {
            if (strVect[i].equals(str)) {
                result = true;
                break;
            }
        }
        return result;
    }

    public static boolean isInString(String strToBeSearchedIn, String strToBeSearchedFor) {
        boolean result = false;
        if (strToBeSearchedIn == null) return result;
        int pos = strToBeSearchedIn.indexOf(strToBeSearchedFor);
        if (pos >= 0) result = true;
        return result;
    }

    public static boolean isNumeric(String s) {
        boolean result = false;
        try {
            Double.parseDouble(s);
            result = true;
        } catch (Exception e) {
            ;
        }
        return result;
    }

    public static boolean isInteger(String s) {
        boolean result = false;
        try {
            Integer.parseInt(s);
            result = true;
        } catch (Exception e) {
            ;
        }
        return result;
    }

    public static String getUpperCaseWithoutBlanks(String str) {
        if (str != null) return str.toUpperCase().replaceAll(" ", ""); else return null;
    }

    public static String getNewStringReplyingBaseString(String baseStr, int howManyTimes) {
        String result;
        result = null;
        if (baseStr != null && howManyTimes > 0) {
            result = "";
            for (int i = 0; i < howManyTimes; i++) {
                result += baseStr;
            }
        }
        return result;
    }

    public static String getClassName(Class c) {
        return StringUtilities.getClassName(c.getName());
    }

    /**
	 * Get the class name given the fully qualified class name.
	 */
    public static String getClassName(String fqnName) {
        int pos = 0;
        pos = fqnName.lastIndexOf('.');
        if (pos == -1) return fqnName;
        String name = fqnName.substring(pos + 1);
        return name;
    }

    public static boolean isEmpty(String s) {
        return (s == null || s.equals(""));
    }

    public static boolean isEmptyIgnoreBlanks(String s) {
        return (s == null || s.replaceAll(" ", "").equals(""));
    }
}
