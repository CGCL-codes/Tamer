package it.chesslab.commons;

import java.util.StringTokenizer;

/**
 * Classe di utilita' per la gestione delle stringhe
 * @author Romano Ghetti
 */
public abstract class StringLib {

    /**  */
    public static final String EMPTY_STRING = "";

    public static final String NEW_LINE = System.getProperty("line.separator");

    public static final String LF = "\n";

    public static final String CR = "\r";

    /**  */
    public static final boolean isNullOrEmptyString(String string) {
        return string == null || string.length() == 0;
    }

    /**  */
    public static String getFirstToken(String text, char separator) {
        return StringLib.getFirstToken(text, Character.toString(separator));
    }

    /**  */
    public static String getFirstToken(String text, char[] separators) {
        return StringLib.getFirstToken(text, new String(separators));
    }

    /**  */
    public static String getFirstToken(String text, String separators) {
        StringTokenizer tok = new StringTokenizer(text, separators, false);
        if (tok.hasMoreTokens()) {
            return tok.nextToken();
        } else {
            return null;
        }
    }

    /**  */
    public static String getLastToken(String text, char separator) {
        return StringLib.getLastToken(text, Character.toString(separator));
    }

    /**  */
    public static String getLastToken(String text, char[] separators) {
        return StringLib.getLastToken(text, new String(separators));
    }

    /**  */
    public static String getLastToken(String text, String separators) {
        StringTokenizer tok = new StringTokenizer(text, separators, false);
        String token = null;
        while (tok.hasMoreTokens()) {
            token = tok.nextToken();
        }
        return token;
    }

    /**  */
    public static String[] getTokens(String text, char separator) {
        return StringLib.getTokens(text, Character.toString(separator));
    }

    /**  */
    public static String[] getTokens(String text, char[] separators) {
        return StringLib.getTokens(text, new String(separators));
    }

    /**  */
    public static String[] getTokens(String text, String separators) {
        StringTokenizer tok = new StringTokenizer(text, separators, false);
        String[] tokens = new String[tok.countTokens()];
        for (int i = 0; tok.hasMoreTokens(); i++) {
            tokens[i] = tok.nextToken();
        }
        return tokens;
    }

    /**  */
    public static String getConcatenation(String[] elements, char separator) {
        return StringLib.getConcatenation(elements, Character.toString(separator));
    }

    /**  */
    public static String getConcatenation(String[] elements, String separator) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < elements.length; i++) {
            if (i > 0) {
                builder.append(separator);
            }
            builder.append(elements[i]);
        }
        return builder.toString();
    }
}
