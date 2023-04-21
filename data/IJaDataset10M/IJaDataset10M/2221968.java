package com.dayspringtech.daylisp;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PushbackReader;
import java.io.Reader;

/**
 * Scanner.java
 *
 * @author Matthew Denson - ae6up at users.sourceforge.net
 */
public class Scanner {

    static final String EOF = "#!EOF";

    static final String RTPAR = "#!)";

    static final String LTPAR = "#!(";

    static final String DOT = "#!.";

    static final String QUOTE = "#!'";

    static final String QUASIQUOTE = "#!`";

    static final String UNQUOTE = "#!,";

    static final String UNQUOTESPLICE = "#!,@";

    static final String FUNCQUOTE = "#!#'";

    static final String VECTSTART = "#!#(";

    private boolean isPushedToken = false;

    private Object pushedToken = null;

    private PushbackReader in = null;

    private boolean stdio = false;

    /**
     * Create a scanner which uses Console IO with System.in.
     */
    public Scanner() {
        stdio = true;
        this.in = new PushbackReader(new InputStreamReader(System.in), 10);
    }

    public Scanner(Reader in) {
        if (in == null) throw new NullPointerException("'in' cannot be null");
        this.in = new PushbackReader(in, 10);
    }

    public void close() {
        try {
            if (!stdio && in != null) in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    ParseException parseError(String s) {
        try {
            while (in.ready()) in.read();
        } catch (IOException ioe) {
        }
        return new ParseException(s);
    }

    Object nextToken() throws ParseException {
        int c;
        if (isPushedToken) {
            isPushedToken = false;
            return pushedToken;
        }
        while (Character.isWhitespace((char) (c = getCh()))) {
        }
        int nextChar = lookChar();
        switch(c) {
            case '(':
                return LTPAR;
            case -1:
                return EOF;
            case '\'':
                return QUOTE;
            case '`':
                return QUASIQUOTE;
            case ',':
                if (nextChar == '@') {
                    getCh();
                    return UNQUOTESPLICE;
                } else return UNQUOTE;
            case ')':
                return RTPAR;
            case '#':
                if (nextChar == '\'') {
                    getCh();
                    return FUNCQUOTE;
                } else if (nextChar == '|') {
                    scanBlockQuote();
                    return nextToken();
                } else if (nextChar == '\\') {
                    getCh();
                    String s = scanCharacterName();
                    if (s.equalsIgnoreCase("space")) return ' '; else if (s.equalsIgnoreCase("newline")) return '\n'; else {
                        if (s.length() == 1) {
                            return s.charAt(0);
                        } else {
                            return null;
                        }
                    }
                } else if (nextChar == '(') {
                    getCh();
                    return VECTSTART;
                } else if (nextChar == 'b' || nextChar == 'B' || nextChar == 'o' || nextChar == 'O' || nextChar == 'x' || nextChar == 'X') {
                    pushChar(c);
                    return scanRadixInt();
                } else {
                    pushChar(c);
                    return scanSymbol();
                }
            case '"':
                return scanString();
            case '.':
                return DOT;
            case ';':
                while (c != -1 && c != '\n' && c != '\r') c = getCh();
                return nextToken();
            default:
                if ((Character.isDigit((char) c) && !((char) c == '1' && ((nextChar == '-') || (nextChar == '+')))) || ((c == '+' || c == '-') && (Character.isDigit((char) nextChar) || (nextChar == '.')))) {
                    pushChar(c);
                    return scanNumber();
                } else {
                    pushChar(c);
                    return scanSymbol();
                }
        }
    }

    private Object scanNumber() throws ParseException {
        boolean isLong = true;
        long sign = 1;
        long lval = 0;
        double val;
        double f = 0.0;
        double k = 1.0;
        long eSign = 1;
        long exp = 0;
        if (lookChar() == '-') {
            getCh();
            sign = -1;
        }
        if (lookChar() == '+') {
            getCh();
            sign = 1;
        }
        int count = 0;
        while (Character.isDigit((char) lookChar())) {
            lval = (10 * lval) + Character.digit((char) getCh(), 10);
            count++;
        }
        if (count == 0) throw parseError("Number must have at least one digit before the decimal point");
        if (lookChar() == '.') {
            isLong = false;
            getCh();
            count = 0;
            while (Character.isDigit((char) lookChar())) {
                k *= 10;
                f = (10 * f) + Character.digit((char) getCh(), 10);
                count++;
            }
            if (count == 0) throw parseError("Number must have at least one digit after the decimal point");
        }
        if (lookChar() == 'e' || lookChar() == 'E') {
            isLong = false;
            getCh();
            if (lookChar() == '-') {
                getCh();
                eSign = -1;
            }
            if (lookChar() == '+') {
                getCh();
                eSign = 1;
            }
            count = 0;
            while (Character.isDigit((char) lookChar())) {
                exp = (10 * exp) + Character.digit((char) getCh(), 10);
                count++;
            }
            if (count == 0) throw parseError("Number must have at least one digit after the 'E'");
            exp *= eSign;
        }
        if (!isLong) {
            val = sign * (lval + (f / k)) * Math.pow(10, exp);
            return new Double(val);
        } else return new Long(sign * lval);
    }

    private Object scanRadixInt() throws ParseException {
        long sign = 1;
        long lval = 0;
        int c;
        int radix = 10;
        if (lookChar() == '#') {
            getCh();
            c = getCh();
            switch(c) {
                case 'b':
                case 'B':
                    radix = 2;
                    break;
                case 'o':
                case 'O':
                    radix = 8;
                    break;
                case 'x':
                case 'X':
                    radix = 16;
                    break;
                default:
                    throw parseError("Unknown radix identifier " + c);
            }
        }
        if (lookChar() == '-') {
            getCh();
            sign = -1;
        }
        if (lookChar() == '+') {
            getCh();
            sign = 1;
        }
        int count = 0;
        switch(radix) {
            case 10:
                while ('0' <= lookChar() && lookChar() <= '9') {
                    lval = (10 * lval) + Character.digit((char) getCh(), 10);
                    count++;
                }
                break;
            case 2:
                while ('0' <= lookChar() && lookChar() <= '1') {
                    lval = (2 * lval) + Character.digit((char) getCh(), 2);
                    count++;
                }
                break;
            case 8:
                while ('0' <= lookChar() && lookChar() <= '7') {
                    lval = (8 * lval) + Character.digit((char) getCh(), 8);
                    count++;
                }
                break;
            case 16:
                while (('0' <= lookChar() && lookChar() <= '9') || ('a' <= lookChar() && lookChar() <= 'f') || ('A' <= lookChar() && lookChar() <= 'F')) {
                    lval = (16 * lval) + Character.digit((char) getCh(), 16);
                    count++;
                }
                break;
        }
        if (count == 0) throw parseError("Number must have at least one digit before the decimal point");
        return sign * lval;
    }

    private Object scanSymbol() {
        StringBuffer buf = new StringBuffer();
        int c;
        if ((char) lookChar() == '|') {
            getCh();
            while (!Character.isWhitespace((char) (c = lookChar())) && c != -1 && c != '.' && c != '(' && c != ')') {
                if (c == '\\') getCh(); else if (c == '|') break;
                buf.append((char) getCh());
            }
            getCh();
        } else {
            while (!Character.isWhitespace((char) (c = lookChar())) && c != -1 && c != '.' && c != '(' && c != ')' && c != ';' && c != '#') buf.append(Character.toUpperCase((char) getCh()));
        }
        return Symbol.getSymbol(buf.toString());
    }

    private String scanCharacterName() {
        StringBuffer buf = new StringBuffer();
        int c;
        while (!Character.isWhitespace((char) (c = lookChar())) && c != -1 && c != '.' && c != '(' && c != ')') buf.append((char) getCh());
        return buf.toString();
    }

    private Object scanString() {
        StringBuffer buf = new StringBuffer();
        int c;
        while ((c = lookChar()) != '"' && c != -1) {
            if (c == '\\') getCh();
            buf.append((char) getCh());
        }
        getCh();
        return new LispString(buf.toString());
    }

    @SuppressWarnings({ "UnusedAssignment" })
    private void scanBlockQuote() throws ParseException {
        int c = getCh();
        while ((c = getCh()) != -1) {
            if (c == '|') {
                if (lookChar() == '#') {
                    getCh();
                    return;
                }
            } else if (c == '#') {
                if (lookChar() == '|') {
                    scanBlockQuote();
                }
            }
        }
    }

    void pushToken(Object t) {
        isPushedToken = true;
        pushedToken = t;
    }

    private void pushChar(char c) {
        try {
            in.unread(c);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void pushChar(int c) {
        pushChar((char) c);
    }

    boolean isReady() throws DomainException {
        try {
            return in.ready();
        } catch (IOException e) {
            throw new DomainException("Not a stream.");
        }
    }

    int getCh() {
        int c = -1;
        try {
            c = in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return c;
    }

    int lookChar() {
        int ret = getCh();
        if (ret != -1) pushChar((char) ret);
        return ret;
    }
}
