package net.sf.jsequnit.test.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author jacek.ratzinger
 *
 */
public class TestHelper {

    public static String testDir = "target" + "/" + "test-dir";

    public static boolean contains(String fileName, String... matchStrings) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String lineInput;
        while ((lineInput = reader.readLine()) != null) {
            if (stringContainsSubstrings(lineInput, matchStrings)) {
                return true;
            }
        }
        return false;
    }

    private static boolean stringContainsSubstrings(String input, String... subStrings) {
        int index = 0;
        for (String subString : subStrings) {
            if (input.indexOf(subString, index) < 0) {
                return false;
            }
            index = input.indexOf(subString, index) + subString.length();
        }
        return true;
    }

    public static String cleanupParam(String parameterString) {
        String result = parameterString.replaceAll("\n", " ");
        result = result.replaceAll(" {2,}", " ");
        return result.trim();
    }
}
