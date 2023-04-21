package com.ibm.icu.dev.tool.localeconverter;

import java.io.*;
import java.util.*;

/**
 * An escape transition parses a POSIX escape sequence.  An escape
 * sequence can be a hex, octal, or decimal constant, or an escaped
 * character.  The resultant value is ALWAYS on byte long.  Longer
 * escaped values (ie.\xFF63) overflow and are truncated.  An escape
 * character followed by an EOL sequence is silently eaten.
 */
public class EscapeTransition extends ComplexTransition {

    public static final EscapeTransition GLOBAL = new EscapeTransition(SUCCESS);

    public static final char DEFAULT_ESCAPE_CHAR = '\\';

    public static char ESCAPE_CHAR = DEFAULT_ESCAPE_CHAR;

    private static final int DECIMAL = 1;

    private static final int OCTAL = 2;

    private static final int HEX = 3;

    private static final int ESCAPE = 4;

    private static final int EOL = 5;

    private static final String OCTAL_CHARS = "01234567";

    private static final String DECIMAL_CHARS = "0123456789";

    private static final String HEX_CHARS = "0123456789abcdefABCDEF";

    /** Set the escape character to the default */
    public static synchronized char setDefaultEscapeChar() {
        return setEscapeChar(DEFAULT_ESCAPE_CHAR);
    }

    /** Set the escape character */
    public static synchronized char setEscapeChar(char c) {
        char result = ESCAPE_CHAR;
        ESCAPE_CHAR = c;
        theStates = null;
        return result;
    }

    public EscapeTransition(int success) {
        super(success);
    }

    public boolean accepts(int c) {
        return ESCAPE_CHAR == (char) c;
    }

    /** Convert the accepted text into the appropriate unicode character */
    protected void handleSuccess(Lex parser, StringBuffer output) throws IOException {
        switch(parser.getState()) {
            case DECIMAL:
                output.append((char) parser.dataAsNumber(10));
                break;
            case OCTAL:
                output.append((char) parser.dataAsNumber(8));
                break;
            case HEX:
                output.append((char) parser.dataAsNumber(16));
                break;
            case ESCAPE:
                parser.appendDataTo(output);
                break;
            case EOL:
                break;
            default:
                throw new Lex.ParseException("Internal error parsing escape sequence");
        }
    }

    /** return the states for this transaction */
    protected Lex.Transition[][] getStates() {
        synchronized (getClass()) {
            if (theStates == null) {
                theStates = new Lex.Transition[][] { { new Lex.CharTransition(ESCAPE_CHAR, Lex.IGNORE_CONSUME, -1), new Lex.ParseExceptionTransition("illegal escape character") }, { new Lex.EOFTransition(OCTAL), new Lex.CharTransition('d', Lex.IGNORE_CONSUME, -3), new Lex.CharTransition('x', Lex.IGNORE_CONSUME, -2), new Lex.StringTransition(OCTAL_CHARS, Lex.ACCUMULATE_CONSUME, -4), new Lex.CharTransition(ESCAPE_CHAR, Lex.ACCUMULATE_CONSUME, ESCAPE), new EOLTransition(EOL), new Lex.DefaultTransition(Lex.ACCUMULATE_CONSUME, ESCAPE) }, { new Lex.EOFTransition(HEX), new Lex.StringTransition(HEX_CHARS, Lex.ACCUMULATE_CONSUME, -2), new Lex.DefaultTransition(Lex.IGNORE_PUTBACK, HEX) }, { new Lex.EOFTransition(DECIMAL), new Lex.StringTransition(DECIMAL_CHARS, Lex.ACCUMULATE_CONSUME, -3), new Lex.DefaultTransition(Lex.IGNORE_PUTBACK, DECIMAL) }, { new Lex.EOFTransition(OCTAL), new Lex.StringTransition(OCTAL_CHARS, Lex.ACCUMULATE_CONSUME, -4), new Lex.DefaultTransition(Lex.IGNORE_PUTBACK, OCTAL) } };
            }
        }
        return theStates;
    }

    private static Lex.Transition[][] theStates = null;

    public static void main(String args[]) {
        try {
            Lex.Transition[][] states = { { new EscapeTransition(SUCCESS), new Lex.EOFTransition(), new Lex.ParseExceptionTransition("bad test input") } };
            String text = "\\d100\\xAf\\\\\\777\\\n\\123\\x2028\\x2029";
            StringReader sr = new StringReader(text);
            PushbackReader pr = new PushbackReader(sr);
            Lex parser = new Lex(states, pr);
            int s = parser.nextToken();
            while (s == SUCCESS) {
                System.out.println(parser.getState());
                s = parser.nextToken();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
