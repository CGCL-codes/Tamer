package org.retro.lisp;

import java.io.InputStream;
import java.io.IOException;
import java.util.Vector;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;

class EndOfListException extends Exception {
}

public class Parser {

    Scanner _scanner;

    public Parser(InputStream in) {
        Reader _rx = new BufferedReader(new InputStreamReader(in));
        _scanner = new Scanner(_rx);
    }

    public List parse() throws ParseException {
        List sexps = new List();
        try {
            Object val;
            while ((val = parseSExp()) != null) sexps.addElement(val);
            return sexps;
        } catch (IOException ioe) {
            throw new ParseException("IOException: " + ioe, _scanner.lineno(), "");
        } catch (EndOfListException eole) {
            throw new ParseException("Unbalanced close parenthesis.", _scanner.lineno(), ")");
        }
    }

    Object parseSExp() throws IOException, ParseException, EndOfListException {
        int token = _scanner.nextToken();
        if (token == '\'') {
            return parseValue(true);
        } else {
            _scanner.pushBack();
            return parseValue(false);
        }
    }

    Object parseValue(boolean quoted) throws IOException, ParseException, EndOfListException {
        int token = _scanner.nextToken();
        switch(token) {
            case Scanner.TT_EOF:
                return null;
            case Scanner.TT_NUMBER:
                if (quoted) return String.valueOf(_scanner.nval); else return new Integer((int) _scanner.nval);
            case Scanner.TT_WORD:
                if (_scanner.sval.equals("nil")) {
                    return new Nil();
                } else if (quoted) {
                    return _scanner.sval;
                } else {
                    return new Name(_scanner.sval);
                }
            case '"':
                return _scanner.sval;
            case '(':
                {
                    Vector list = (quoted ? new List() : new Vector());
                    try {
                        Object sexp;
                        while ((sexp = parseSExp()) != null) list.addElement(sexp);
                        throw new ParseException("Premature end of sexp.", _scanner.lineno(), "");
                    } catch (EndOfListException e) {
                        return list;
                    }
                }
            case ')':
                throw new EndOfListException();
            case '+':
                return new Name("add");
            case '-':
                return new Name("sub");
            case '*':
                return new Name("mul");
            case '/':
                return new Name("div");
            case '=':
                return new Name("eq");
            case '<':
                {
                    int nt = _scanner.nextToken();
                    if (nt == '=') {
                        return new Name("lte");
                    } else {
                        _scanner.pushBack();
                        return new Name("lt");
                    }
                }
            case '>':
                {
                    int nt = _scanner.nextToken();
                    if (nt == '=') {
                        return new Name("gte");
                    } else {
                        _scanner.pushBack();
                        return new Name("gt");
                    }
                }
            default:
                throw new ParseException("Invalid token.", _scanner.lineno(), String.valueOf((char) token));
        }
    }
}
