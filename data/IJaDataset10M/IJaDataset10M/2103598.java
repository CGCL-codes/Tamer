package net.sourceforge.jtds.jdbc;

import java.sql.*;

public abstract class EscapeProcessor {

    public static final String cvsVersion = "$Id: EscapeProcessor.java,v 1.1 2002-10-14 10:48:59 alin_sinpalean Exp $";

    String input;

    public EscapeProcessor(String sql) {
        input = sql;
    }

    public abstract String expandDBSpecificFunction(String escapeSequence) throws SQLException;

    /**
     * Is the string made up only of digits?
     * <p>
     * <b>Note:</b>  Leading/trailing spaces or signs are not considered digits.
     *
     * @return true if the string has only digits, false otherwise.
     */
    private static boolean validDigits(String str) {
        for (int i = 0; i < str.length(); i++) if (!Character.isDigit(str.charAt(i))) return false;
        return true;
    }

    /**
     * Given a string and an index into that string return the index
     * of the next non-whitespace character.
     *
     * @return index of next non-whitespace character.
     */
    private static int skipWhitespace(String str, int i) {
        while (i < str.length() && Character.isWhitespace(str.charAt(i))) i++;
        return i;
    }

    /**
     * Given a string and an index into that string, advanvance the index
     * iff it is on a quote character.
     *
     * @return the next position in the string iff the current character is a
     *         quote
     */
    private static int skipQuote(String str, int i) {
        if (i < str.length() && (str.charAt(i) == '\'' || str.charAt(i) == '"')) i++;
        return i;
    }

    /**
     * Convert a JDBC SQL escape date sequence into a datestring recognized by SQLServer.
     */
    private static String getDate(String str) throws SQLException {
        int i;
        i = 2;
        i = skipWhitespace(str, i);
        i = skipQuote(str, i);
        if (str.length() - i < 10 || str.charAt(i + 4) != '-' || str.charAt(i + 7) != '-') throw new SQLException("Malformed date");
        String year = str.substring(i, i + 4);
        String month = str.substring(i + 5, i + 5 + 2);
        String day = str.substring(i + 5 + 3, i + 5 + 3 + 2);
        if (!validDigits(year) || !validDigits(month) || !validDigits(day)) throw new SQLException("Malformed date");
        i = i + 10;
        i = skipWhitespace(str, i);
        i = skipQuote(str, i);
        i = skipWhitespace(str, i);
        if (i < str.length()) throw new SQLException("Malformed date");
        return "'" + year + month + day + "'";
    }

    /**
     * Convert a JDBC SQL escape time sequence into a time string recognized by SQLServer.
     */
    private static String getTime(String str) throws SQLException {
        int i;
        i = 2;
        i = skipWhitespace(str, i);
        i = skipQuote(str, i);
        if (str.length() - i < 8 || str.charAt(i + 2) != ':' || str.charAt(i + 5) != ':') throw new SQLException("Malformed time");
        String hour = str.substring(i, i + 2);
        String minute = str.substring(i + 3, i + 3 + 2);
        String second = str.substring(i + 3 + 3, i + 3 + 3 + 2);
        if (!validDigits(hour) || !validDigits(minute) || !validDigits(second)) throw new SQLException("Malformed time");
        i = i + 8;
        i = skipWhitespace(str, i);
        i = skipQuote(str, i);
        i = skipWhitespace(str, i);
        if (i < str.length()) throw new SQLException("Malformed time");
        return "'" + hour + ":" + minute + ":" + second + "'";
    }

    /**
     * Convert a JDBC SQL escape timestamp sequence into a date-time string recognized by SQLServer.
     */
    private static String getTimestamp(String str) throws SQLException {
        int i;
        i = 2;
        i = skipWhitespace(str, i);
        i = skipQuote(str, i);
        if ((str.length() - i) < 19 || str.charAt(i + 4) != '-' || str.charAt(i + 7) != '-') throw new SQLException("Malformed date");
        String year = str.substring(i, i + 4);
        String month = str.substring(i + 5, i + 5 + 2);
        String day = str.substring(i + 5 + 3, i + 5 + 3 + 2);
        if (!validDigits(year) || !validDigits(month) || !validDigits(day)) throw new SQLException("Malformed date");
        i = i + 10;
        if (!Character.isWhitespace(str.charAt(i))) throw new SQLException("Malformed date");
        i = skipWhitespace(str, i);
        if (str.length() - i < 8 || str.charAt(i + 2) != ':' || str.charAt(i + 5) != ':') throw new SQLException("Malformed time");
        String hour = str.substring(i, i + 2);
        String minute = str.substring(i + 3, i + 3 + 2);
        String second = str.substring(i + 3 + 3, i + 3 + 3 + 2);
        String fraction = "000";
        i = i + 8;
        if (str.length() > i && str.charAt(i) == '.') {
            fraction = "";
            i++;
            while (str.length() > i && validDigits(str.substring(i, i + 1))) fraction = fraction + str.substring(i, ++i);
            if (fraction.length() > 3) fraction = fraction.substring(0, 3); else while (fraction.length() < 3) fraction = fraction + "0";
        }
        i = skipWhitespace(str, i);
        i = skipQuote(str, i);
        i = skipWhitespace(str, i);
        if (i < str.length()) {
            throw new SQLException("Malformed date");
        }
        return "'" + year + month + day + " " + hour + ":" + minute + ":" + second + "." + fraction + "'";
    }

    public String expandEscape(String escapeSequence) throws SQLException {
        String str = new String(escapeSequence);
        String result = null;
        str = str.trim();
        if (str.startsWith("fn ")) {
            str = str.substring(3);
            result = expandCommonFunction(str);
            if (result == null) result = expandDBSpecificFunction(str);
            if (result == null) result = str;
        } else if (str.startsWith("call ") || (str.startsWith("?=") && str.substring(2).trim().startsWith("call "))) {
            boolean returnsVal = str.startsWith("?=");
            str = str.substring(str.indexOf("call") + 4).trim();
            int pPos = str.indexOf('(');
            if (pPos >= 0) {
                if (str.charAt(str.length() - 1) != ')') throw new SQLException("Malformed procedure call: " + str);
                result = "exec " + (returnsVal ? "?=" : "") + str.substring(0, pPos) + " " + str.substring(pPos + 1, str.length() - 1);
            } else result = "exec " + (returnsVal ? "?=" : "") + str;
        } else if (str.startsWith("d ")) result = getDate(str); else if (str.startsWith("t ")) result = getTime(str); else if (str.startsWith("ts ")) result = getTimestamp(str); else if (str.startsWith("oj ")) result = str.substring(3, str.length() - 1).trim(); else throw new SQLException("Unrecognized escape sequence: " + escapeSequence);
        return result;
    }

    /**
     * Expand functions that are common to both SQLServer and Sybase
     */
    public String expandCommonFunction(String str) throws SQLException {
        String result = null;
        int pPos = str.indexOf('(');
        if (pPos < 0) throw new SQLException("Malformed function escape: " + str);
        String fName = str.substring(0, pPos).trim();
        if (fName.equalsIgnoreCase("user")) result = "user_name" + str.substring(pPos); else if (fName.equalsIgnoreCase("now")) result = "getdate" + str.substring(pPos);
        return result;
    }

    public String nativeString() throws SQLException {
        return nativeString(input, '\\');
    }

    private String nativeString(String sql, char escapeCharacter) throws SQLException {
        StringBuffer result = new StringBuffer(sql.length());
        String escape = "";
        int i;
        final int normal = 0;
        final int inString = 1;
        final int inStringWithBackquote = 2;
        final int inEscape = 3;
        final int inEscapeInString = 4;
        final int inEscapeInStringWithBackquote = 5;
        int state = normal;
        char ch;
        int escapeStartedAt = -1;
        i = 0;
        while (i < sql.length()) {
            ch = sql.charAt(i);
            switch(state) {
                case normal:
                    {
                        if (ch == '{') {
                            escapeStartedAt = i;
                            state = inEscape;
                            escape = "";
                        } else {
                            result.append(ch);
                            if (ch == '\'') state = inString;
                        }
                        break;
                    }
                case inString:
                case inStringWithBackquote:
                    {
                        if ((i + 1) < sql.length() && ch == escapeCharacter && (sql.charAt(i + 1) == '_' || sql.charAt(i + 1) == '%')) {
                            i++;
                            ch = sql.charAt(i);
                            result.append('\\');
                            result.append(ch);
                        } else {
                            result.append(ch);
                            if (state == inStringWithBackquote) {
                                state = inString;
                            } else {
                                if (ch == '\\') state = inStringWithBackquote;
                                if (ch == '\'') state = normal;
                            }
                        }
                        break;
                    }
                case inEscape:
                    {
                        if (ch == '}') {
                            if (escape.startsWith("escape ")) {
                                char c;
                                if (i + 1 != sql.length()) throw new SQLException("Malformed statement. Escape clause must " + "be at the end of the query.");
                                c = findEscapeCharacter(sql.substring(escapeStartedAt));
                                result.delete(0, result.length());
                                result.append(nativeString(sql.substring(0, escapeStartedAt), c));
                                state = normal;
                            } else {
                                state = normal;
                                result.append(expandEscape(escape));
                                escapeStartedAt = -1;
                            }
                        } else {
                            escape = escape + ch;
                            if (ch == '\'') state = inEscapeInString;
                        }
                        break;
                    }
                case inEscapeInString:
                case inEscapeInStringWithBackquote:
                    {
                        escape = escape + ch;
                        if (state == inEscapeInStringWithBackquote) state = inEscapeInString; else {
                            if (ch == '\\') state = inEscapeInStringWithBackquote;
                            if (ch == '\'') state = inEscape;
                        }
                        break;
                    }
                default:
                    throw new SQLException("Internal error.  Unknown state in FSM");
            }
            i++;
        }
        if (state != normal && state != inString) throw new SQLException("Syntax error in SQL escape syntax");
        return result.toString();
    }

    static char findEscapeCharacter(String original_str) throws SQLException {
        String str = new String(original_str);
        str = str.trim();
        if (str.charAt(0) != '{' || str.charAt(str.length() - 1) != '}' || str.length() < 12) throw new SQLException("Internal Error");
        str = str.substring(1, str.length() - 1);
        str = str.trim();
        if (!str.startsWith("escape")) throw new SQLException("Internal Error");
        str = str.substring(6);
        str = str.trim();
        if (str.length() != 3 || str.charAt(0) != '\'' || str.charAt(2) != '\'') throw new SQLException("Malformed escape clause: |" + original_str + "|");
        return str.charAt(1);
    }
}
