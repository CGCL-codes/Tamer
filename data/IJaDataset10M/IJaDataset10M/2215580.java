package ircam.jmax.editors.qlist;

import java.util.*;
import java.text.*;
import ircam.jmax.*;
import ircam.fts.client.*;

public class QListUnparse {

    static final int lex_float_start = 0;

    static final int lex_float_in_value = 1;

    static final int lex_float_in_sign = 2;

    static final int lex_float_after_point = 3;

    static final int lex_float_end = 4;

    private static DecimalFormat formatter;

    static {
        formatter = new DecimalFormat("0.######;-0.######");
        formatter.setGroupingUsed(false);
        formatter.setDecimalSeparatorAlwaysShown(true);
        DecimalFormatSymbols formsym = new DecimalFormatSymbols();
        formsym.setDecimalSeparator('.');
        formatter.setDecimalFormatSymbols(formsym);
    }

    /** Identify digits */
    private static final boolean isDigit(char c) {
        return ((c == '0') || (c == '1') || (c == '2') || (c == '3') || (c == '4') || (c == '5') || (c == '6') || (c == '7') || (c == '8') || (c == '9'));
    }

    /** identify  the sign char */
    private static final boolean isSign(char c) {
        return ((c == '-') || (c == '+'));
    }

    /** Identify decimal point, and so a float representation */
    private static final boolean isDecimalPoint(char c) {
        return (c == '.');
    }

    private static final boolean wantASpaceBefore(Object value) {
        if (value instanceof String) {
            String keywords[] = { "+", "-", "*", "/", "%", "&&", "&", "||", "|", "==", "=", "!=", "!", ">=", "^", ">>", ">", "<<", "<=", "<", "?", "::", ":" };
            for (int i = 0; i < keywords.length; i++) if (keywords[i].equals((String) value)) return true;
            return false;
        } else return false;
    }

    private static final boolean wantASpaceBefore(FtsAtom value) {
        if (value.isString() || value.isSymbol()) {
            String stringVal = (value.isString()) ? value.stringValue : value.symbolValue.toString();
            String keywords[] = { "+", "-", "*", "/", "%", "&&", "&", "||", "|", "==", "!=", "!", ">=", "^", ">>", ">", "<<", "<=", "<", "?", "::", ":" };
            for (int i = 0; i < keywords.length; i++) if (keywords[i].equals(stringVal)) return true;
            return false;
        } else return false;
    }

    private static final boolean dontWantASpaceBefore(Object value) {
        if (value instanceof String) {
            String keywords[] = { ")", "[", "]", "}", ",", ";", ".", "=" };
            for (int i = 0; i < keywords.length; i++) if (keywords[i].equals((String) value)) return true;
            return false;
        } else return false;
    }

    private static final boolean dontWantASpaceBefore(FtsAtom value) {
        if (value.isString() || value.isSymbol()) {
            String stringVal = (value.isString()) ? value.stringValue : value.symbolValue.toString();
            String keywords[] = { ")", "[", "]", "}", ",", ";", ".", "=" };
            for (int i = 0; i < keywords.length; i++) if (keywords[i].equals(stringVal)) return true;
            return false;
        } else return false;
    }

    private static final boolean wantASpaceAfter(Object value) {
        if (value instanceof String) {
            String keywords[] = { "+", "-", "*", "/", "%", ",", "&&", "&", "||", "|", "==", "=", "!=", "!", ">=", ">>", ">", "<<", "<=", "<", "?", "::", ":", "^", ";" };
            for (int i = 0; i < keywords.length; i++) if (keywords[i].equals((String) value)) return true;
            return false;
        } else return false;
    }

    private static final boolean wantASpaceAfter(FtsAtom value) {
        if (value.isString() || value.isSymbol()) {
            String stringVal = (value.isString()) ? value.stringValue : value.symbolValue.toString();
            String keywords[] = { "+", "-", "*", "/", "%", ",", "&&", "&", "||", "|", "==", "!=", "!", ">=", ">>", ">", "<<", "<=", "<", "?", "::", ":", "^", ";" };
            for (int i = 0; i < keywords.length; i++) if (keywords[i].equals(stringVal)) return true;
            return false;
        } else return false;
    }

    private static final boolean dontWantASpaceAfter(Object value) {
        if (value instanceof String) {
            String keywords[] = { "(", "[", "{", "$", "'", ".", "=" };
            for (int i = 0; i < keywords.length; i++) if (keywords[i].equals((String) value)) return true;
            return false;
        } else return false;
    }

    private static final boolean dontWantASpaceAfter(FtsAtom value) {
        if (value.isString() || value.isSymbol()) {
            String stringVal = (value.isString()) ? value.stringValue : value.symbolValue.toString();
            String keywords[] = { "(", "[", "{", "$", "'", ".", "=" };
            for (int i = 0; i < keywords.length; i++) if (keywords[i].equals(stringVal)) return true;
            return false;
        } else return false;
    }

    private static final boolean isAKeyword(String value) {
        String keywords[] = { "(", ")", "[", "]", "{", "}", ",", "::", ":", "$", ".", ";", "'", "==", "!=", "<=", ">=", "=" };
        for (int i = 0; i < keywords.length; i++) if (keywords[i].equals((String) value)) return true;
        return false;
    }

    private static final boolean isAnInt(String value) {
        int i = 0;
        if (isSign(value.charAt(i))) if (value.length() == 1) return false; else i++;
        while (i < value.length()) if (!isDigit(value.charAt(i++))) return false;
        return true;
    }

    private static final boolean isAFloat(String value) {
        int status;
        int i = 0;
        status = lex_float_start;
        while (status != lex_float_end) {
            switch(status) {
                case lex_float_start:
                    if (i >= value.length()) return false; else if (isDigit(value.charAt(i))) status = lex_float_in_value; else if (isSign(value.charAt(i))) status = lex_float_in_sign; else if (isDecimalPoint(value.charAt(i))) status = lex_float_after_point; else return false;
                    break;
                case lex_float_in_sign:
                    if (i >= value.length()) return false; else if (isDigit(value.charAt(i))) status = lex_float_in_value; else if (isDecimalPoint(value.charAt(i))) status = lex_float_after_point; else return false;
                    break;
                case lex_float_in_value:
                    if (i >= value.length()) return false; else if (isDigit(value.charAt(i))) status = lex_float_in_value; else if (isDecimalPoint(value.charAt(i))) status = lex_float_after_point; else return false;
                    break;
                case lex_float_after_point:
                    if (i >= value.length()) return true; else if (isDigit(value.charAt(i))) status = lex_float_after_point; else return false;
                    break;
            }
            i++;
        }
        return true;
    }

    private static final boolean includeStartToken(String s) {
        char chars[] = { '$', ',', '(', ')', '[', ']', '{', '}', ':', ';', '\'', '\t', ' ' };
        for (int i = 0; i < chars.length; i++) if (s.indexOf(chars[i]) != -1) return true;
        return false;
    }

    /** Unparse an object description from a FTS message.  */
    static String removeZeroAtEnd(String buff) {
        while (buff.endsWith("0")) {
            buff = buff.substring(0, buff.length() - 1);
        }
        return buff;
    }

    public static String unparseDescription(MaxVector values) {
        boolean doNewLine = false;
        boolean addBlank = false;
        boolean noNewLine = false;
        Object value1 = null;
        Object value2 = null;
        Enumeration en = values.elements();
        StringBuffer descr = new StringBuffer();
        if (!en.hasMoreElements()) return "";
        value2 = en.nextElement();
        value1 = value2;
        while (value1 != null) {
            if (doNewLine) descr.append("\n"); else if (addBlank) descr.append(" ");
            doNewLine = false;
            if (!en.hasMoreElements()) value2 = null; else value2 = en.nextElement();
            if (value1 instanceof Float) descr.append(removeZeroAtEnd(formatter.format(value1))); else if (value1 instanceof Integer) descr.append(value1); else if (value1 instanceof String) {
                if (isAnInt((String) value1) || isAFloat((String) value1) || ((!isAKeyword((String) value1)) && includeStartToken((String) value1))) {
                    descr.append("\"");
                    descr.append(value1);
                    descr.append("\"");
                } else descr.append(value1);
                if (value1.equals("'")) noNewLine = true; else noNewLine = false;
            } else descr.append(value1);
            if (wantASpaceAfter(value1)) addBlank = true; else if (dontWantASpaceAfter(value1)) addBlank = false; else if (value2 != null) {
                if (wantASpaceBefore(value2)) addBlank = true; else if (dontWantASpaceBefore(value2)) addBlank = false; else addBlank = true;
            }
            value1 = value2;
        }
        return descr.toString();
    }
}
