package org.bug4j.common;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class that transforms a text string into an array of Strings.
 */
public final class TextToLines {

    private TextToLines() {
    }

    /**
     * Transforms a text string into an array of Strings.
     *
     * @param text input text
     * @return an array of lines
     */
    public static String[] toLineArray(String text) {
        final List<String> ret = new ArrayList<String>();
        final int length = text.length();
        int from = 0;
        int to = from;
        while (to < length) {
            final char c = text.charAt(to);
            if (c == '\r') {
                if (to + 1 < length && text.charAt(to + 1) == '\n') {
                    final String line = text.substring(from, to);
                    ret.add(line);
                    to++;
                    from = to + 1;
                }
            } else if (c == '\n') {
                final String line = text.substring(from, to);
                ret.add(line);
                from = to + 1;
            }
            to++;
        }
        if (from < to) {
            final String line = text.substring(from, to);
            ret.add(line);
        }
        return ret.toArray(new String[ret.size()]);
    }

    /**
     * Transforms a text string into an List of Strings.
     *
     * @param text input text
     * @return an array of lines
     */
    public static List<String> toLineList(String text) {
        final List<String> ret = new ArrayList<String>();
        final int length = text.length();
        int from = 0;
        int to = from;
        while (to < length) {
            final char c = text.charAt(to);
            if (c == '\r') {
                if (to + 1 < length && text.charAt(to + 1) == '\n') {
                    final String line = text.substring(from, to);
                    ret.add(line);
                    to++;
                    from = to + 1;
                }
            } else if (c == '\n') {
                final String line = text.substring(from, to);
                ret.add(line);
                from = to + 1;
            }
            to++;
        }
        if (from < to) {
            final String line = text.substring(from, to);
            ret.add(line);
        }
        return ret;
    }
}
