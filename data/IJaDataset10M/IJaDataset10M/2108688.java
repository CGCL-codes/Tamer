package com.dayspringtech.daylisp;

import java.io.Reader;

/**
 * InputStream.java
 *
 * @author Matthew Denson - ae6up at users.sourceforge.net
 */
public class InputStream extends Scanner {

    public InputStream() {
        super();
    }

    public InputStream(Reader in) {
        super(in);
    }

    public Object readChar() {
        int c = -1;
        try {
            if (isReady()) c = getCh();
        } catch (DomainException e) {
            e.printStackTrace();
        }
        if (c == -1) return Scanner.EOF; else return (char) c;
    }

    public Object peekChar() {
        int c = lookChar();
        if (c == -1) return Scanner.EOF; else return (char) c;
    }

    public Object read() throws ParseException {
        Object token = nextToken();
        if (token == EOF) return token; else if (token == LTPAR) return readTail(); else if (token == VECTSTART) {
            Object obj = readTail();
            try {
                return new GeneralVector((Cons) obj);
            } catch (ClassCastException cce) {
                throw parseError("Vector must be followed by a list of tokens between parens.");
            }
        } else if (token == RTPAR) {
            System.err.println("Extra ) ignored.");
            return read();
        } else if (token == QUOTE) return Cons.list(Symbol.QUOTE, read()); else if (token == QUASIQUOTE) return Cons.list(Symbol.QUASIQUOTE, read()); else if (token == UNQUOTE) return Cons.list(Symbol.UNQUOTE, read()); else if (token == UNQUOTESPLICE) return Cons.list(Symbol.UNQUOTE_SPLICING, read()); else if (token == FUNCQUOTE) return Cons.list(Symbol.FUNCTION, read()); else if (token == DOT) {
            System.err.println("Extra . ignored.");
            return read();
        } else return token;
    }

    public boolean isEOF() {
        try {
            Object token = nextToken();
            pushToken(token);
            return (token == EOF);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isEOF(Object token) {
        return (token == Scanner.EOF);
    }

    private Object readTail() throws ParseException {
        Object token = nextToken();
        if (token == EOF) throw parseError("Encountered unexpected EOF during read."); else if (token == RTPAR) return null; else if (token == DOT) {
            Object result = read();
            token = nextToken();
            if (token != RTPAR) throw parseError("Where's the ')'? Got " + token + " after .");
            return result;
        } else {
            pushToken(token);
            return Cons.cons(read(), readTail());
        }
    }
}
