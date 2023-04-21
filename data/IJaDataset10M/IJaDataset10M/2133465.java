package com.google.code.joto.util;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * This class was adapted from http://jbind.sourceforge.net/ project. Many thanks to its author,
 * Stefan Wachter.
 * @author epere4
 * @author Liliana.nu
 * @author Stefan Wachter
 */
public class StringUtil {

    /**
     * No instantiation.
     */
    private StringUtil() {
    }

    /**
     * Checks if the specified string is a valid java identifier. Null or {@link #isBlank(String)
     * blank} strings are <b>not</b> considered valid java identifiers.
     * @param aString <i>(required)</i>.
     * @return Returns <code>true</code> iff the specified string is a valid java identifier.
     */
    public static boolean isJavaIdentifier(String aString) {
        boolean valid = !isJavaReservedWord(aString) && !isBlank(aString);
        if (valid) {
            valid = Character.isJavaIdentifierStart(aString.charAt(0));
            for (int i = 1; valid && i < aString.length(); i++) {
                char c = aString.charAt(i);
                valid = Character.isJavaIdentifierPart(c);
            }
        }
        return valid;
    }

    /**
     * Checks if the specified string is a valid name of a java package. Null or
     * {@link #isEmpty(String) empty} strings are considered valid java packages, but note that
     * {@link #isBlank(String) blank} string are <b>not</b> considered valid java packages if their
     * length is > 0.
     * @param aString
     * @return Returns <code>true</code> if the specified string is a valid name of a java package.
     */
    public static boolean isJavaPackage(String aString) {
        boolean valid = true;
        if (!isEmpty(aString)) {
            String[] partsOfPackageName = aString.split("\\.", -1);
            for (int i = 0; valid && i < partsOfPackageName.length; i++) {
                valid = isJavaIdentifier(partsOfPackageName[i]);
            }
        }
        return valid;
    }

    /** set with all java reserved words */
    private static final Set<String> javaReservedWords = new HashSet<String>(Arrays.asList("abstract", "continue", "for", "new", "switch", "assert", "default", "goto", "package", "synchronized", "boolean", "do", "if", "private", "this", "break", "double", "implements", "protected", "throw", "byte", "else", "import", "public", "throws", "case", "enum", "instanceof", "return", "transient", "catch", "extends", "int", "short", "try", "char", "final", "interface", "static", "void", "class", "finally", "long", "strictfp", "volatile", "const", "float", "native", "super", "while"));

    /**
     * @param string
     * @return true if the string is a reserved word in java language.
     */
    public static boolean isJavaReservedWord(String string) {
        return javaReservedWords.contains(string);
    }

    /**
     * Tests if a string is empty. A string is considered empty when is null or has a length of
     * zero.
     * @param str
     * @return true if the str is empty according to the above definition.
     */
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    /**
     * Tests if a string is blank. A string is considered blank when is null or has all white space
     * characters according to {@link String#trim()} method.
     * @param str
     * @return true if the str is blank according to the above definition.
     */
    public static boolean isBlank(String str) {
        return str == null || str.trim().length() == 0;
    }

    /**
     * @param srcFolder
     * @param packageName
     * @return
     */
    public static File packageNameToFile(File srcFolder, String packageName) {
        if (!isJavaPackage(packageName)) {
            throw new IllegalArgumentException("Not a valid packageName: " + packageName);
        }
        return new File(srcFolder, packageName == null ? "" : packageName.replace('.', File.separatorChar));
    }

    /**
     * @param packageName
     * @return
     */
    public static File packageNameToFile(String packageName) {
        return packageNameToFile(null, packageName);
    }
}
