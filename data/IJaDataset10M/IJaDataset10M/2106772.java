package org.apache.myfaces.trinidadinternal.el;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * converts a EL expression into tokens.
 */
public class Tokenizer implements Iterator<Tokenizer.Token> {

    /**
   * Creates a new Tokenizer
   * @param expression the expression to tokenize
   */
    public Tokenizer(String expression) {
        _exp = expression;
        _curToken = _next();
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

    public boolean hasNext() {
        return _curToken != null;
    }

    public Tokenizer.Token next() {
        if (_curToken == null) throw new NoSuchElementException();
        Token tok = _curToken;
        _curToken = _next();
        return tok;
    }

    private Token _next() {
        int size = _exp.length();
        if (_curIndex >= size) return null;
        final Token tok;
        if (_state == _STATE_INIT) {
            int loc = _exp.indexOf("#{", _curIndex);
            if (loc < 0) {
                loc = size;
            }
            _state = _STATE_EXP_START;
            if (loc > _curIndex) {
                tok = new Token(TEXT_TYPE, _curIndex, loc);
                _curIndex = loc;
            } else {
                tok = _next();
            }
        } else if (_state == _STATE_EXP_START) {
            int end = _curIndex + 2;
            tok = new Token(EXP_START_TYPE, _curIndex, end);
            _curIndex = end;
            _state = _STATE_EXP;
        } else if (_state == _STATE_EXP) {
            int start = _curIndex;
            char ch = _exp.charAt(_curIndex++);
            if (Character.isWhitespace(ch)) tok = _readWhiteSpace(start); else if (Character.isDigit(ch)) tok = _readNumber(start); else if (Character.isJavaIdentifierStart(ch)) tok = _readVar(start); else if ('}' == ch) {
                tok = new Token(EXP_END_TYPE, start, _curIndex);
                _state = _STATE_INIT;
            } else if (('"' == ch) || ('\'' == ch)) tok = _readQuoted(start, ch); else tok = new Token(OPER_TYPE, start, _curIndex);
        } else throw new AssertionError("invalid state:" + _state);
        return tok;
    }

    /**
   * creates a quote token.
   * @param start the start index of the quotation
   * @param quote the quote character that delimits this quotation.
   */
    private Token _readQuoted(int start, char quote) {
        for (int end = start + 1; ; ) {
            end = _exp.indexOf(quote, end);
            if (end >= 0) {
                end++;
                if (_exp.charAt(end - 2) == '\\') {
                    continue;
                }
                _curIndex = end;
                return new Token(QUOTED_TYPE, start, end);
            } else throw _fatal("Cannot find matching quote", start);
        }
    }

    private RuntimeException _fatal(String message, int charPos) {
        return new IllegalArgumentException(message + ". character position:" + charPos);
    }

    /**
   * Creates a token for variables
   * @param start the starting index of a variable usage
   */
    private Token _readVar(int start) {
        for (int end = start + 1; ; ) {
            end = _getLastLocOfAlpha(end);
            if (_exp.charAt(end) == '.') end++; else {
                _curIndex = end;
                break;
            }
        }
        return new Token(VAR_TYPE, start, _curIndex);
    }

    private int _getLastLocOfAlpha(int start) {
        while (Character.isJavaIdentifierPart(_exp.charAt(start))) start++;
        return start;
    }

    /**
   * reads a number token.
   * @param start the starting index of a number
   */
    private Token _readNumber(int start) {
        int end = _getLastLocOfDigit(start + 1);
        if (_exp.charAt(end) == '.') end = _getLastLocOfDigit(end + 1);
        _curIndex = end;
        return new Token(NUMBER_TYPE, start, end);
    }

    private int _getLastLocOfDigit(int start) {
        while (Character.isDigit(_exp.charAt(start))) start++;
        return start;
    }

    /**
   * creates a whitespace token
   * @param start the starting index of the first whitespace character
   */
    private Token _readWhiteSpace(int start) {
        while (Character.isWhitespace(_exp.charAt(_curIndex))) {
            _curIndex++;
        }
        return new Token(WHITE_SPACE_TYPE, start, _curIndex);
    }

    private final String _exp;

    private int _curIndex = 0;

    private int _state = _STATE_INIT;

    private Token _curToken = null;

    public final class Token {

        public final int type;

        private final int _start, _end;

        private Token(int type, int start, int end) {
            this.type = type;
            _start = start;
            _end = end;
        }

        public String getText() {
            return _exp.substring(_start, _end);
        }
    }

    private static final int _STATE_INIT = 100;

    private static final int _STATE_EXP_START = 200;

    private static final int _STATE_EXP = 300;

    /**
   * Identifies some plain text
   */
    public static final int TEXT_TYPE = 100;

    /**
   * Identifies the expression start string.
   * This string is two charaters long and is: "#{"
   */
    public static final int EXP_START_TYPE = 200;

    /**
   * Identifies the expression end character.
   * This is character is "}"
   */
    public static final int EXP_END_TYPE = 300;

    /**
   * Identifies a series of whitespace characters
   */
    public static final int WHITE_SPACE_TYPE = 400;

    /**
   * Identifies a quotation. 
   * Eg: "quotation", 'quotation', 'he said, "foo!"'  ,
   * "it's it". 
   * More examples:   "foo \" bar" ,  'foo \' bar'
   */
    public static final int QUOTED_TYPE = 500;

    /**
   * Identifies a variable usage.
   * Eg: "row", "row.name", "foo.bar.baz"
   */
    public static final int VAR_TYPE = 600;

    /**
   * identifies a number
   * eg:  "123", "321.321"
   */
    public static final int NUMBER_TYPE = 700;

    /**
   * Identifies an operator.
   * eg: ">", "[", "+", "?"
   */
    public static final int OPER_TYPE = 800;
}
