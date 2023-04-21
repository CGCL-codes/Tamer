package org.schemeway.plugins.schemescript.parser;

import org.eclipse.jface.preference.*;
import org.schemeway.plugins.schemescript.preferences.*;

public final class SchemeScannerUtilities {

    private static boolean mBracketsAsParenthesis = true;

    private static boolean mDashInIdentifiers = false;

    public static final int NONE = 0;

    public static final int PARENTHESIS = 1;

    public static final int BRACKET = 2;

    public static final int BRACE = 3;

    public static void initializeScanner(IPreferenceStore store) {
        setBracketsAreParentheses(store.getBoolean(SchemeLexicalExtensionsPreferences.SQUARE_BRACKETS));
        setDashInIdentifiers(store.getBoolean(SchemeLexicalExtensionsPreferences.DASH_IN_IDS));
    }

    public static final int getParenthesisType(char ch) {
        switch(ch) {
            case '(':
            case ')':
                return PARENTHESIS;
            case '[':
            case ']':
                if (mBracketsAsParenthesis) return BRACKET; else return NONE;
            default:
                return NONE;
        }
    }

    public static final boolean bracketsAreParentheses() {
        return mBracketsAsParenthesis;
    }

    public static final boolean dashInIdentifiers() {
        return mDashInIdentifiers;
    }

    public static final void setBracketsAreParentheses(boolean value) {
        mBracketsAsParenthesis = value;
    }

    public static final void setDashInIdentifiers(boolean value) {
        mDashInIdentifiers = value;
    }

    public static final boolean isParenthesis(char ch) {
        return getParenthesisType(ch) != NONE;
    }

    public static final boolean isOpeningParenthesis(char ch) {
        return (ch == '(' || (mBracketsAsParenthesis && isOpeningBracket(ch)));
    }

    public static final boolean isOpeningBracket(char ch) {
        return (ch == '[' || ch == '{');
    }

    public static final boolean isClosingParenthesis(char ch) {
        return (ch == ')' || (mBracketsAsParenthesis && isClosingBracket(ch)));
    }

    public static final boolean isClosingBracket(char ch) {
        return (ch == ']' || ch == '}');
    }

    public static final boolean isPunctuationChar(char ch) {
        return (isParenthesis(ch) || ch == '\'' || ch == ',' || ch == '`');
    }

    public static final boolean isIdentifierPrefixChar(char ch) {
        return (Character.isLetter(ch) || isSpecialInitial(ch));
    }

    public static final boolean isIdentifierPartChar(char ch) {
        return (Character.isDigit(ch) || isIdentifierPrefixChar(ch) || isSpecialSubsequent(ch));
    }

    public static final boolean isSpecialInitial(char ch) {
        return (ch == '!' || ch == '$' || ch == '%' || ch == '&' || ch == '*' || ch == '/' || ch == ':' || ch == '<' || ch == '=' || ch == '>' || ch == '?' || ch == '^' || ch == '_' || ch == '~' || (ch == '#' && mDashInIdentifiers));
    }

    public static final boolean isSpecialSubsequent(char ch) {
        return ch == '-' || ch == '+' || ch == '.' || ch == '@' || (!mBracketsAsParenthesis && isOpeningBracket(ch));
    }

    public static final boolean isWhitespaceChar(char ch) {
        return (ch == ' ' || ch == '\n' || ch == '\t' || ch == '\r');
    }

    public static final boolean isIdentifier(String str) {
        if (str == null || str.equals("")) return false;
        for (int i = 0; i < str.length(); i++) {
            if (!isIdentifierPartChar(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
