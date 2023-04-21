package de.jmda.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;

public abstract class StringUtil {

    /**
	 * Removes the characters in <code>charsToTrim</code> from both sides of
	 * <code>input</code>.
	 *
	 * @param input string to be trimmed
	 * @param charsToTrim characters to be removed
	 *
	 * @return trimmed input string
	 */
    public static String allTrimChars(String input, String charsToTrim) {
        return lrTrimChars(input, charsToTrim);
    }

    /**
	 * @see #allTrimChars(String, String)
	 * @param input
	 * @param charsToTrim
	 * @return trimmed input string
	 */
    public static StringBuffer allTrimChars(StringBuffer input, String charsToTrim) {
        return lrTrimChars(input, charsToTrim);
    }

    /**
	 * Removes the characters in <code>charsToTrim</code> from both sides of
	 * <code>input</code>.
	 *
	 * @param input string to be trimmed
	 * @param charsToTrim characters to be removed
	 *
	 * @return trimmed input string
	 */
    public static String lrTrimChars(String input, String charsToTrim) {
        return rTrimChars(lTrimChars(input, charsToTrim), charsToTrim);
    }

    /**
	 * @see #lrTrimChars(String, String)
	 * @param input
	 * @param charsToTrim
	 * @return trimmed input string
	 */
    public static StringBuffer lrTrimChars(StringBuffer input, String charsToTrim) {
        return rTrimChars(lTrimChars(input, charsToTrim), charsToTrim);
    }

    /**
	 * Removes the characters in <code>charsToTrim</code> from left side of
	 * <code>input</code>.
	 *
	 * @param input string to be trimmed
	 * @param charsToTrim characters to be removed
	 *
	 * @return trimmed <code>input</code> string, <code>null</code> if <code>input
	 *         </code> was <code>null</code>
	 */
    public static String lTrimChars(String input, String charsToTrim) {
        if (input == null) {
            return null;
        }
        StringBuffer result = new StringBuffer(input);
        if (!StringUtils.isEmpty(input) && !StringUtils.isEmpty(charsToTrim)) {
            result = trimChars(new StringBuffer(input), charsToTrim, true);
        }
        return result.toString();
    }

    /**
	 * @see #lTrimChars(String, String)
	 * @param input
	 * @param charsToTrim
	 * @return trimmed input string
	 */
    public static StringBuffer lTrimChars(StringBuffer input, String charsToTrim) {
        if (!isEmpty(input) && !StringUtils.isEmpty(charsToTrim)) {
            return trimChars(input, charsToTrim, true);
        }
        return input;
    }

    /**
	 * Removes the characters in <code>charsToTrim</code> from right side of
	 * <code>input</code>.
	 *
	 * @param input string to be trimmed
	 * @param charsToTrim characters to be removed
	 *
	 * @return trimmed <code>input</code> string, <code>null</code> if <code>input
	 *         </code> was <code>null</code>
	 */
    public static String rTrimChars(String input, String charsToTrim) {
        if (input == null) {
            return null;
        }
        StringBuffer result = new StringBuffer(input);
        if (!StringUtils.isEmpty(input) && !StringUtils.isEmpty(charsToTrim)) {
            result = trimChars(new StringBuffer(input), charsToTrim, false);
        }
        return result.toString();
    }

    /**
	 * @param input string buffer to be trimmed
	 * @param charsToTrim characters to be removed
	 * @return trimmed input string buffer
	 */
    public static StringBuffer rTrimChars(StringBuffer input, String charsToTrim) {
        if (!isEmpty(input) && !StringUtils.isEmpty(charsToTrim)) {
            return trimChars(input, charsToTrim, false);
        }
        return input;
    }

    /**
	 * Recursive implementation!
	 * <p>
	 * Removes the characters in <code>charsToTrim</code> from front or back of
	 * <code>input</code>.
	 *
	 * @param input
	 * @param charsToTrim
	 * @param fromFront
	 * @return
	 */
    private static StringBuffer trimChars(StringBuffer input, String charsToTrim, boolean fromFront) {
        if (input.length() == 0) {
            return new StringBuffer();
        }
        char[] charsToTrimAsArray = charsToTrim.toCharArray();
        for (char c : charsToTrimAsArray) {
            if (fromFront) {
                if (input.charAt(0) == c) {
                    input.deleteCharAt(0);
                    return trimChars(input, charsToTrim, fromFront);
                }
            } else {
                int lastCharPos = input.length() - 1;
                if (input.charAt(lastCharPos) == c) {
                    input.deleteCharAt(lastCharPos);
                    return trimChars(input, charsToTrim, fromFront);
                }
            }
        }
        return input;
    }

    /**
	 * @see #isEmpty(String)
	 */
    public static boolean isEmpty(StringBuffer input) {
        if (input == null) {
            return true;
        }
        return isEmpty(input.toString());
    }

    /**
	 * @param input string to be checked
	 * @return <code>true</code> if <code>input == null</code> or <code>
	 *         input.length() == 0</code>, <code>false</code> otherwise
	 */
    public static boolean isEmpty(String input) {
        if (input == null) {
            return true;
        }
        if (input.length() == 0) {
            return true;
        }
        return false;
    }

    /**
	 * Finds and returns longest string in <code>strings</code>.
	 *
	 * @param strings
	 *
	 * @return longest string in <code>strings</code>
	 */
    public static String findLongestString(String[] strings) {
        if (strings == null) {
            return "";
        }
        if (strings.length == 0) {
            return "";
        }
        String result = null;
        int maxLength = -1;
        for (String element : strings) {
            if (element.length() > maxLength) {
                result = element;
                maxLength = element.length();
            }
        }
        return result;
    }

    /**
	 * @param strings strings to be checked
	 * @return length of longest string in <code>strings</code> 
	 */
    public static int getMaxStringLength(String[] strings) {
        return findLongestString(strings).length();
    }

    /**
	 * @see StringUtils#leftPad(String, int, char)
	 *
	 * @param input
	 * @param fillChar
	 * @param targetLength
	 * @return filled string
	 */
    public static String lFillCharsTargetLength(String input, char fillChar, int targetLength) {
        return StringUtils.leftPad(input, targetLength, fillChar);
    }

    /**
	 * @see StringUtils#rightPad(String, int, char)

	 * @param input
	 * @param fillChar
	 * @param targetLength
	 * @return filled string
	 */
    public static String rFillCharsTargetLength(String input, char fillChar, int targetLength) {
        return StringUtils.rightPad(input, targetLength, fillChar);
    }

    public static String firstLetterToLowerCase(String string) {
        return WordUtils.uncapitalize(string);
    }

    public static String firstLetterToUpperCase(String string) {
        return WordUtils.capitalize(string);
    }

    public static String toString(Object o) {
        if (o == null) {
            return null;
        }
        StringBuffer result = new StringBuffer(o.getClass().getName());
        Class<?> clss = o.getClass();
        Method[] methods = clss.getMethods();
        String methodName = null;
        String methodValue = null;
        Map<String, String> map = new HashMap<String, String>();
        Iterator<String> iter = null;
        String[] keys = null;
        int max = -1;
        int counter = 0;
        for (Method method : methods) {
            methodName = method.getName();
            if ((!methodName.equals("getClass")) && methodName.startsWith("get") && (method.getParameterTypes().length == 0)) {
                try {
                    methodValue = "" + method.invoke(o, (Object[]) null);
                } catch (IllegalAccessException e) {
                    System.out.println(e);
                } catch (IllegalArgumentException e) {
                    System.out.println(e);
                } catch (InvocationTargetException e) {
                    System.out.println(e);
                }
                map.put(methodName.substring(3, 4).toLowerCase() + methodName.substring(4), methodValue);
            }
        }
        keys = new String[map.keySet().size()];
        iter = map.keySet().iterator();
        while (iter.hasNext()) {
            keys[counter] = iter.next();
            counter++;
        }
        Arrays.sort(keys, String.CASE_INSENSITIVE_ORDER);
        max = StringUtil.findLongestString(keys).length();
        for (String key : keys) {
            result.append("\n\t").append(key).append(StringUtils.repeat(" ", max - key.length())).append(" [").append(map.get(key)).append("]");
        }
        return result.toString();
    }
}
