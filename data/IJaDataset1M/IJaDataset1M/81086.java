package org.ibex.js;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PushbackReader;
import java.io.Reader;

/** Lexes a stream of characters into a stream of Tokens */
class Lexer implements Tokens {

    /** for debugging */
    public static void main(String[] s) throws IOException {
        Lexer l = new Lexer(new InputStreamReader(System.in), "stdin", 0);
        int tok = 0;
        while ((tok = l.getToken()) != -1) System.out.println(codeToString[tok]);
    }

    /** the token that was just parsed */
    protected int op;

    /** the most recently parsed token, <i>regardless of pushbacks</i> */
    protected int mostRecentlyReadToken;

    /** if the token just parsed was a NUMBER, this is the numeric value */
    protected Number number = null;

    /** if the token just parsed was a NAME or STRING, this is the string value */
    protected String string = null;

    /** the line number of the most recently <i>lexed</i> token */
    protected int line = 0;

    /** the line number of the most recently <i>parsed</i> token */
    protected int parserLine = 0;

    /** the column number of the current token */
    protected int col = 0;

    /** the name of the source code file being lexed */
    protected String sourceName;

    private SmartReader in;

    public Lexer(Reader r, String sourceName, int line) throws IOException {
        this.sourceName = sourceName;
        this.line = line;
        this.parserLine = line;
        in = new SmartReader(r);
    }

    private static boolean isAlpha(int c) {
        return ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'));
    }

    private static boolean isDigit(int c) {
        return (c >= '0' && c <= '9');
    }

    private static int xDigitToInt(int c) {
        if ('0' <= c && c <= '9') return c - '0'; else if ('a' <= c && c <= 'f') return c - ('a' - 10); else if ('A' <= c && c <= 'F') return c - ('A' - 10); else return -1;
    }

    private int getKeyword(String name) throws IOException {
        int key = SW.get(name);
        switch(key) {
            case SW.if_:
                return IF;
            case SW.lt:
                return LT;
            case SW.gt:
                return GT;
            case SW.in:
                return IN;
            case SW.do_:
                return DO;
            case SW.and:
                return AND;
            case SW.or:
                return OR;
            case SW.for_:
                return FOR;
            case SW.case_:
                return CASE;
            case SW.try_:
                return TRY;
            case SW.var:
                return VAR;
            case SW.else_:
                return ELSE;
            case SW.null_:
                return NULL;
            case SW.true_:
                return TRUE;
            case SW.break_:
                return BREAK;
            case SW.while_:
                return WHILE;
            case SW.false_:
                return FALSE;
            case SW.throw_:
                return THROW;
            case SW.catch_:
                return CATCH;
            case SW.return_:
                return RETURN;
            case SW.assert_:
                return ASSERT;
            case SW.switch_:
                return SWITCH;
            case SW.typeof:
                return TYPEOF;
            case SW.default_:
                return DEFAULT;
            case SW.finally_:
                return FINALLY;
            case SW.continue_:
                return CONTINUE;
            case SW.function:
                return FUNCTION;
            case SW.int_:
            case SW.new_:
            case SW.byte_:
            case SW.char_:
            case SW.enum_:
            case SW.goto_:
            case SW.long_:
            case SW.with:
            case SW.void_:
            case SW.class_:
            case SW.const_:
            case SW.final_:
            case SW.super_:
            case SW.delete:
            case SW.throws_:
            case SW.double_:
            case SW.public_:
            case SW.package_:
            case SW.boolean_:
            case SW.private_:
            case SW.extends_:
            case SW.abstract_:
            case SW.debugger:
            case SW.volatile_:
            case SW.interface_:
            case SW.protected_:
            case SW.transient_:
            case SW.implements_:
            case SW.instanceof_:
            case SW.synchronized_:
                return RESERVED;
            default:
                return -1;
        }
    }

    private int getIdentifier(int c) throws IOException {
        in.startString();
        while (Character.isJavaIdentifierPart((char) (c = in.read()))) ;
        in.unread();
        String str = in.getString();
        int result = getKeyword(str);
        if (result == RESERVED) throw new LexerException("The reserved word \"" + str + "\" is not permitted in Vexi scripts");
        if (result != -1) return result;
        this.string = str.intern();
        return NAME;
    }

    private int getNumber(int c) throws IOException {
        int base = 10;
        in.startString();
        double dval = Double.NaN;
        long longval = 0;
        boolean isInteger = true;
        if (c == '0') {
            if (Character.toLowerCase((char) (c = in.read())) == 'x') {
                base = 16;
                in.startString();
            } else if (isDigit(c)) base = 8;
        }
        while (0 <= xDigitToInt(c) && !(base < 16 && isAlpha(c))) c = in.read();
        if (base == 10 && (c == '.' || c == 'e' || c == 'E')) {
            isInteger = false;
            if (c == '.') do {
                c = in.read();
            } while (isDigit(c));
            if (c == 'e' || c == 'E') {
                c = in.read();
                if (c == '+' || c == '-') c = in.read();
                if (!isDigit(c)) throw new LexerException("float listeral did not have an exponent value");
                do {
                    c = in.read();
                } while (isDigit(c));
            }
        }
        in.unread();
        String numString = in.getString();
        try {
            if (base == 10 && !isInteger) {
                dval = (Double.valueOf(numString)).doubleValue();
            } else {
                if (isInteger) {
                    longval = Long.parseLong(numString, base);
                    dval = (double) longval;
                } else {
                    dval = Double.parseDouble(numString);
                    longval = (long) dval;
                    if (longval == dval) isInteger = true;
                }
            }
        } catch (NumberFormatException ex) {
            throw new LexerException("invalid numeric literal: \"" + numString + "\"");
        }
        if (!isInteger) this.number = JS.N(dval); else this.number = JS.N(longval);
        return NUMBER;
    }

    private int getString(int c) throws IOException {
        StringBuffer stringBuf = null;
        int quoteChar = c;
        c = in.read();
        in.startString();
        while (c != quoteChar) {
            if (c == '\n' || c == -1) throw new LexerException("unterminated string literal");
            if (c == '\\') {
                if (stringBuf == null) {
                    in.unread();
                    stringBuf = new StringBuffer(in.getString());
                    in.read();
                }
                switch(c = in.read()) {
                    case 'b':
                        c = '\b';
                        break;
                    case 'f':
                        c = '\f';
                        break;
                    case 'n':
                        c = '\n';
                        break;
                    case 'r':
                        c = '\r';
                        break;
                    case 't':
                        c = '\t';
                        break;
                    case 'v':
                        c = '';
                        break;
                    case '\\':
                        c = '\\';
                        break;
                    case 'u':
                        {
                            int v = 0;
                            for (int i = 0; i < 4; i++) {
                                int ci = in.read();
                                if (!((ci >= '0' && ci <= '9') || (ci >= 'a' && ci <= 'f') || (ci >= 'A' && ci <= 'F'))) throw new LexerException("illegal character '" + ((char) c) + "' in \\u unicode escape sequence");
                                v = (v << 8) | Integer.parseInt(ci + "", 16);
                            }
                            c = (char) v;
                            break;
                        }
                    default:
                        break;
                }
            }
            if (stringBuf != null) stringBuf.append((char) c);
            c = in.read();
        }
        if (stringBuf != null) this.string = stringBuf.toString().intern(); else {
            in.unread();
            this.string = in.getString().intern();
            in.read();
        }
        return STRING;
    }

    private int _getToken() throws IOException {
        int c;
        do {
            c = in.read();
        } while (c == ' ' || c == '	' || c == '' || c == '' || c == '\n');
        if (c == -1) return -1;
        if (c == '\\' || Character.isJavaIdentifierStart((char) c)) return getIdentifier(c);
        if (isDigit(c) || (c == '.' && isDigit(in.peek()))) return getNumber(c);
        if (c == '"' || c == '\'') return getString(c);
        switch(c) {
            case ';':
                return SEMI;
            case '[':
                return LB;
            case ']':
                return RB;
            case '{':
                return LC;
            case '}':
                return RC;
            case '(':
                return LP;
            case ')':
                return RP;
            case ',':
                return COMMA;
            case '?':
                return HOOK;
            case ':':
                return !in.match(':') ? COLON : in.match('=') ? GRAMMAR : le(":: is not a valid token");
            case '.':
                return DOT;
            case '|':
                return in.match('|') ? OR : (in.match('=') ? ASSIGN_BITOR : BITOR);
            case '^':
                return in.match('=') ? ASSIGN_BITXOR : BITXOR;
            case '&':
                return in.match('&') ? AND : in.match('=') ? ASSIGN_BITAND : BITAND;
            case '=':
                return !in.match('=') ? ASSIGN : in.match('=') ? SHEQ : EQ;
            case '!':
                return !in.match('=') ? BANG : in.match('=') ? SHNE : NE;
            case '%':
                return in.match('=') ? ASSIGN_MOD : MOD;
            case '~':
                return BITNOT;
            case '+':
                return in.match('=') ? ASSIGN_ADD : in.match('+') ? (in.match('=') ? ADD_TRAP : INC) : ADD;
            case '-':
                return in.match('=') ? ASSIGN_SUB : in.match('-') ? (in.match('=') ? DEL_TRAP : DEC) : SUB;
            case '*':
                return in.match('=') ? ASSIGN_MUL : MUL;
            case '<':
                return !in.match('<') ? (in.match('=') ? LE : LT) : in.match('=') ? ASSIGN_LSH : LSH;
            case '>':
                return !in.match('>') ? (in.match('=') ? GE : GT) : in.match('>') ? (in.match('=') ? ASSIGN_URSH : URSH) : (in.match('=') ? ASSIGN_RSH : RSH);
            case '/':
                if (in.match('=')) return ASSIGN_DIV;
                if (in.match('/')) {
                    while ((c = in.read()) != -1 && c != '\n') ;
                    in.unread();
                    return getToken();
                }
                if (!in.match('*')) return DIV;
                while ((c = in.read()) != -1 && !(c == '*' && in.match('/'))) {
                    if (c == '\n' || c != '/' || !in.match('*')) continue;
                    if (in.match('/')) return getToken();
                    throw new LexerException("nested comments are not permitted");
                }
                if (c == -1) throw new LexerException("unterminated comment");
                return getToken();
            default:
                throw new LexerException("illegal character: \'" + ((char) c) + "\'");
        }
    }

    public String getErrorLine() throws IOException {
        return in.getErrorLine();
    }

    private int le(String s) throws LexerException {
        if (true) throw new LexerException(s);
        return 0;
    }

    /** a Reader that tracks line numbers and can push back tokens */
    private class SmartReader {

        PushbackReader reader = null;

        int lastread = -1;

        public SmartReader(Reader r) {
            reader = new PushbackReader(r);
        }

        public void unread() throws IOException {
            unread((char) lastread);
        }

        public void unread(char c) throws IOException {
            reader.unread(c);
            if (c == '\n') col = -1; else col--;
            if (accumulator != null) accumulator.setLength(accumulator.length() - 1);
            if (errorLine.length() > 0) errorLine.setLength(errorLine.length() - 1);
        }

        public boolean match(char c) throws IOException {
            if (peek() == c) {
                reader.read();
                return true;
            } else return false;
        }

        public int peek() throws IOException {
            int peeked = reader.read();
            if (peeked != -1) reader.unread((char) peeked);
            return peeked;
        }

        public int read() throws IOException {
            lastread = reader.read();
            if (accumulator != null) accumulator.append((char) lastread);
            if (errorLine.length() > 0 && errorLine.charAt(errorLine.length() - 1) == '\n') errorLine.setLength(0);
            if (lastread != -1) errorLine.append((char) lastread);
            if (lastread != '\n' && lastread != '\r') col++;
            if (lastread == '\n') {
                if (col != -1) parserLine = ++line;
                col = 0;
            }
            return lastread;
        }

        StringBuffer accumulator = null;

        public void startString() {
            accumulator = new StringBuffer();
            accumulator.append((char) lastread);
        }

        public String getString() throws IOException {
            String ret = accumulator.toString().intern();
            accumulator = null;
            return ret;
        }

        StringBuffer errorLine = new StringBuffer();

        String getErrorLine() throws IOException {
            StringBuffer tmpLine = new StringBuffer(errorLine.toString());
            int pos = tmpLine.length();
            int newread = ' ';
            while (true) {
                newread = reader.read();
                if (newread == -1 || newread == '\n') break;
                tmpLine.append((char) newread);
            }
            return tmpLine.toString();
        }
    }

    private int pushBackDepth = 0;

    private int[] pushBackInts = new int[10];

    private Object[] pushBackObjects = new Object[10];

    /** push back a token */
    public final void pushBackToken(int op, Object obj) {
        if (pushBackDepth >= pushBackInts.length - 1) {
            int[] newInts = new int[pushBackInts.length * 2];
            System.arraycopy(pushBackInts, 0, newInts, 0, pushBackInts.length);
            pushBackInts = newInts;
            Object[] newObjects = new Object[pushBackObjects.length * 2];
            System.arraycopy(pushBackObjects, 0, newObjects, 0, pushBackObjects.length);
            pushBackObjects = newObjects;
        }
        pushBackInts[pushBackDepth] = op;
        pushBackObjects[pushBackDepth] = obj;
        pushBackDepth++;
    }

    /** push back the most recently read token */
    public final void pushBackToken() {
        pushBackToken(op, number != null ? (Object) number : (Object) string);
    }

    /** read a token but leave it in the stream */
    public final int peekToken() throws IOException {
        int ret = getToken();
        pushBackToken();
        return ret;
    }

    /** read a token */
    public final int getToken() throws IOException {
        number = null;
        string = null;
        if (pushBackDepth == 0) {
            mostRecentlyReadToken = op;
            return op = _getToken();
        }
        pushBackDepth--;
        op = pushBackInts[pushBackDepth];
        if (pushBackObjects[pushBackDepth] != null) {
            number = pushBackObjects[pushBackDepth] instanceof Number ? (Number) pushBackObjects[pushBackDepth] : null;
            string = pushBackObjects[pushBackDepth] instanceof String ? (String) pushBackObjects[pushBackDepth] : null;
        }
        return op;
    }

    class LexerException extends IOException {

        public LexerException(String s) {
            super(sourceName + ":" + line + "," + col + ": " + s);
        }
    }
}
