package net.sourceforge.processdash.tool.diff.engine;

import java.util.StringTokenizer;

public class WhitespaceCompareString {

    /** The original string */
    private String s;

    /**
     * A cached version of the normalized string used for whitespace-agnostic
     * comparison. Two WhitespaceCompareString objects are equal if their w
     * components are equal.
     */
    private String w;

    WhitespaceCompareString(String s) {
        this.s = s;
        this.w = null;
    }

    public String toString() {
        return s;
    }

    public int hashCode() {
        return getWhite().hashCode();
    }

    public boolean equals(Object o) {
        if (o instanceof WhitespaceCompareString) {
            WhitespaceCompareString that = (WhitespaceCompareString) o;
            return this.getWhite().equals(that.getWhite());
        } else {
            return false;
        }
    }

    private String getWhite() {
        if (w == null) w = canonicalizeWhitespace(s);
        return w;
    }

    public static String canonicalizeWhitespace(String str) {
        String result = str.trim();
        if (whitespaceIsCanonical(result)) return result;
        StringBuilder buf = new StringBuilder();
        StringTokenizer tok = new StringTokenizer(result, WHITESPACE);
        while (tok.hasMoreTokens()) buf.append(" ").append(tok.nextToken());
        result = buf.toString();
        if (result.length() > 0) result = result.substring(1);
        return result;
    }

    public static boolean whitespaceIsCanonical(String str) {
        for (int i = WHITESPACE.length(); i-- > 1; ) if (str.indexOf(WHITESPACE.charAt(i)) != -1) return false;
        return (str.indexOf("  ") == -1);
    }

    private static final String WHITESPACE = " \t\r\n\f";
}
