package org.dishevelled.codegen;

import java.util.Arrays;
import java.util.List;

/**
 * Static utility methods for the codegen package.
 *
 * @author  Michael Heuer
 * @version $Revision: 1059 $ $Date: 2012-01-03 15:03:02 -0500 (Tue, 03 Jan 2012) $
 */
public final class CodegenUtils {

    /** List of Java primitive names. */
    private static final List<String> PRIMITIVE_NAMES = Arrays.asList(new String[] { "byte", "short", "int", "long", "char", "float", "double", "boolean" });

    /**
     * Return true if the specified name is a Java primitive name.
     *
     * @return true if the specified name is a primitive name
     */
    public static boolean isPrimitive(final String name) {
        return PRIMITIVE_NAMES.contains(name);
    }

    /**
     * Make a lowercase name from the specified name.
     *
     * @param name name
     * @return a lowercase name
     */
    public static String makeLowercase(final String name) {
        return makeDescription(name);
    }

    /**
     * Make an Uppercase name from the specified name.
     *
     * @param name name
     * @return an Uppercase name
     */
    public static String makeUppercase(final String name) {
        return name;
    }

    /**
     * Make a mixedCase name from the specified name.
     *
     * @param name name
     * @return a mixedCase name
     */
    public static String makeMixedCase(final String name) {
        StringBuffer sb = new StringBuffer();
        char ch = name.charAt(0);
        sb.append(Character.toLowerCase(ch));
        sb.append(name.substring(1));
        return sb.toString();
    }

    /**
     * Make a description (all lowercase, words split by spaces) from
     * the specified name.
     *
     * @param name name
     * @return a description
     */
    public static String makeDescription(final String name) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0, size = name.length(); i < size; i++) {
            char ch = name.charAt(i);
            if (Character.isTitleCase(ch) || Character.isUpperCase(ch)) {
                if (i != 0) {
                    sb.append(" ");
                }
                sb.append(Character.toLowerCase(ch));
            } else {
                sb.append(ch);
            }
        }
        return sb.toString();
    }

    /**
     * Make a sentence-case description (first word uppercase, rest lowercase, words split by spaces) from
     * the specified name.
     *
     * @param name name
     * @return a sentence-case description
     */
    public static String makeSentenceCaseDescription(final String name) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0, size = name.length(); i < size; i++) {
            char ch = name.charAt(i);
            if (Character.isTitleCase(ch) || Character.isUpperCase(ch)) {
                if (i == 0) {
                    sb.append(ch);
                } else {
                    sb.append(" ");
                    sb.append(Character.toLowerCase(ch));
                }
            } else {
                sb.append(ch);
            }
        }
        return sb.toString();
    }

    /**
     * Make a lowercase-with-dashes name from the specified name.
     *
     * @param name name
     * @return a lowercase-with-dashes name
     */
    public static String makeLowercaseWithDashes(final String name) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0, size = name.length(); i < size; i++) {
            char ch = name.charAt(i);
            if (Character.isTitleCase(ch) || Character.isUpperCase(ch)) {
                if (i != 0) {
                    sb.append("-");
                }
                sb.append(Character.toLowerCase(ch));
            } else {
                sb.append(ch);
            }
        }
        return sb.toString();
    }

    /**
     * Make an UPPERCASE_WITH_UNDERSCORES name from the specified name.
     *
     * @param name name
     * @return an UPPERCASE_WITH_UNDERSCORES name
     */
    public static String makeUppercaseWithUnderscores(final String name) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0, size = name.length(); i < size; i++) {
            char ch = name.charAt(i);
            if (Character.isTitleCase(ch) || Character.isUpperCase(ch)) {
                if (i != 0) {
                    sb.append("_");
                }
                sb.append(ch);
            } else {
                sb.append(Character.toUpperCase(ch));
            }
        }
        return sb.toString();
    }
}
