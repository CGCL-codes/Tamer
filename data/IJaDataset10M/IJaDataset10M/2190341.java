package com.ezware.common;

import java.io.PrintWriter;
import java.io.StringWriter;

public class Strings {

    private Strings() {
    }

    ;

    /**
	 * Check if string is empty
	 * @param s
	 * @return true if string is null or empty (white spaces are not counted)
	 */
    public static final boolean isEmpty(String s) {
        return s == null || s.trim().length() == 0;
    }

    /**
	 * Returns safe string - never null
	 * @param s
	 * @return
	 */
    public static final String safe(String s) {
        return isEmpty(s) ? "" : s;
    }

    /**
	 * Capitalize every word in the string
	 * @param s
	 * @return
	 */
    public static final String capitalize(String s) {
        if (isEmpty(s)) return s;
        StringBuilder sb = new StringBuilder();
        for (String w : s.split(" ")) {
            sb.append(capitalizeWord(w));
            sb.append(' ');
        }
        return sb.toString().trim();
    }

    /**
	 * Capitalizes given word
	 * @param word
	 * @return
	 */
    private static final String capitalizeWord(String word) {
        if (isEmpty(word)) return word;
        String capital = word.substring(0, 1).toUpperCase();
        return (word.length() == 1) ? capital : capital + word.substring(1).toLowerCase();
    }

    /**
	 * Converts exception stack trace as string
	 * @param ex
	 * @return
	 */
    public static final String stackStraceAsString(Throwable ex) {
        StringWriter sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }
}
