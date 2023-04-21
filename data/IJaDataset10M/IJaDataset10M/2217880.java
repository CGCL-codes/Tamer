package org.opennms.core.utils;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class StringUtils {

    /**
     * Convenience method for creating arrays of strings suitable for use as
     * command-line parameters when executing an external process.
     * 
     * <p>
     * The default {@link Runtime#exec Runtime.exec}method will split a single
     * string based on spaces, but it does not respect spaces within quotation
     * marks, and it will leave the quotation marks in the resulting substrings.
     * This method solves those problems by replacing all in-quote spaces with
     * the given delimiter, removes the quotes, and then splits the resulting
     * string by the remaining out-of-quote spaces. It then goes through each
     * substring and replaces the delimiters with spaces.
     * </p>
     * 
     * <p>
     * <em>Caveat:</em> This method does not respect escaped quotes! It will
     * simply remove them and leave the stray escape characters.
     * </p>
     * 
     * @param s
     *            the string to split
     * @param delim
     *            a char that does not already exist in <code>s</code>
     * @return An array of strings split by spaces outside of quotes.
     * @throws IllegalArgumentException
     *             If <code>s</code> is null or if <code>delim</code>
     *             already exists in <code>s</code>.
     */
    public static String[] createCommandArray(String s, char delim) {
        if (s == null) {
            throw new IllegalArgumentException("Cannot take null parameters.");
        }
        if (s.indexOf(delim) != -1) {
            throw new IllegalArgumentException("String parameter cannot already contain delimiter character: " + delim);
        }
        char[] chars = s.toCharArray();
        boolean inquote = false;
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '"') {
                inquote = (inquote) ? false : true;
            } else if (inquote && chars[i] == ' ') {
                buffer.append(delim);
            } else {
                buffer.append(chars[i]);
            }
        }
        s = buffer.toString();
        ArrayList<String> arrayList = new ArrayList<String>();
        StringTokenizer tokenizer = new StringTokenizer(s);
        while (tokenizer.hasMoreTokens()) {
            arrayList.add(tokenizer.nextElement().toString());
        }
        String[] list = arrayList.toArray(new String[arrayList.size()]);
        for (int i = 0; i < list.length; i++) {
            list[i] = list[i].replace(delim, ' ');
        }
        return list;
    }

    public static String truncate(String name, int length) {
        if (name.length() <= length) return name;
        return name.substring(0, length);
    }
}
