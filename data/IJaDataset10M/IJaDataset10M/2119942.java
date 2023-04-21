package com.huyderman.css;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CSSparser {

    private static final String COMMENT = "\\/\\*[^*]*\\*+([^/*][^*]*\\*+)*\\/";

    private static final String CDO = "<!--";

    private static final String CDC = "-->";

    public CSSparser(String string) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(string));
            String s = reader.readLine();
            String css = "";
            while (s != null) {
                css += s + "\n";
                s = reader.readLine();
            }
            reader.close();
            Matcher matcher = Pattern.compile(COMMENT).matcher(css);
            css = matcher.replaceAll("");
            css = css.trim();
            int pos = 0;
            while (pos < css.length()) {
                if (css.startsWith(CDO, pos)) {
                    pos += CDO.length();
                    while (!css.startsWith(CDC, pos)) {
                        pos++;
                    }
                } else if (isWhiteSpace(css.charAt(pos))) {
                    pos = skipWhiteSpace(css, pos);
                } else {
                    if (css.charAt(pos) == '@') {
                        pos++;
                        String atKeyWord = readIdent(css, pos);
                        pos += atKeyWord.length();
                        int startPos = pos;
                        while (pos < css.length() && css.charAt(pos) != '{' && css.charAt(pos) != ';') pos++;
                        int endPos = pos;
                        parseItems(css.substring(startPos, endPos));
                        if (css.charAt(pos) == '{') {
                        } else pos++;
                    } else {
                        pos++;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
         * @param string
         */
    private void parseItems(String string) {
        int pos = 0;
        while (pos < string.length()) {
            pos = skipWhiteSpace(string, pos);
            String ident = readIdent(string, pos);
            if (ident != null) {
                pos += ident.length();
                if (pos < string.length() && string.charAt(pos) == '(') {
                } else {
                }
            }
            pos = skipWhiteSpace(string, pos);
        }
    }

    /**
         * @param css
         * @param pos
         * @return
         */
    private String readIdent(String css, int pos) {
        if (css.charAt(pos) == '-' || isNmStart(css, pos)) {
            int identStart = pos, identEnd = pos;
            while (identEnd < css.length() && isNmChar(css, identEnd)) {
                identEnd++;
            }
            return css.substring(identStart, identEnd);
        }
        return null;
    }

    /**
         * @param string
         * @param pos
         * @return
         */
    private boolean isNmChar(String string, int pos) {
        return (string.charAt(pos) == '_' || string.charAt(pos) == '-' || Character.isLetterOrDigit(string.charAt(pos)) || string.charAt(pos) > 177);
    }

    private boolean isNmStart(String string, int pos) {
        return (string.charAt(pos) == '-' || Character.isLetter(string.charAt(pos)) || string.charAt(pos) > 177);
    }

    /**
         * @param css
         * @param pos
         * @return
         */
    private int skipWhiteSpace(String css, int pos) {
        while (pos < css.length() && isWhiteSpace(css.charAt(pos))) {
            pos++;
        }
        return pos;
    }

    /**
         * Checks if a given character is white space as defined in the CSS
         * syntax
         * 
         * @param c
         *                character to check
         * @return true if given character is a whitespace character
         */
    private boolean isWhiteSpace(final char c) {
        return c == ' ' || c == '\t' || c == '\r' || c == '\n' || c == '\f';
    }
}
