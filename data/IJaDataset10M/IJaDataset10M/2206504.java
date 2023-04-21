package net.sourceforge.wildlife.core.parser;

/**
 * @author Jean Barata
 */
class Utility {

    public static void _assert(boolean expr) {
        if (false == expr) {
            throw (new Error("Error: Assertion failed."));
        }
    }

    private static final String errorMsg[] = { "Error: Unmatched end-of-comment punctuation.", "Error: Unmatched start-of-comment punctuation.", "Error: Unclosed string.", "Error: Illegal character." };

    public static final int E_ENDCOMMENT = 0;

    public static final int E_STARTCOMMENT = 1;

    public static final int E_UNCLOSEDSTR = 2;

    public static final int E_UNMATCHED = 3;

    public static void error(int code) {
        System.out.println(errorMsg[code]);
    }
}
