package ru.whale.parser.lexer;

import ru.whale.parser.SyntaxErrorException;
import ru.whale.parser.lexer.Lexem.LexemType;
import java.util.List;

/**
 * Class that providing lexical analyse of string.
 *
 */
public class OldWhaleLexer {

    public static final int EOF = -1;

    private int position = -1;

    private int line = -1;

    private List<String> lines;

    private String lastLine;

    private Lexem l = null;

    private int lastChar;

    public OldWhaleLexer(List<String> lines) {
        this.lines = lines;
        lastLine = "";
        getChar();
    }

    public Lexem getLexem() {
        Lexem tmp = lookLexem();
        l = readNextLexem();
        return tmp;
    }

    private Lexem readNextLexem() {
        skipWhites();
        int ch = lookChar();
        int start = position;
        if (ch == EOF) {
            return new Lexem(LexemType.EOF, "", start, line);
        }
        if (Character.isDigit(ch)) {
            return new Lexem(LexemType.NUMBER, getNumber(), start, line);
        }
        if (ch == '"') {
            return new Lexem(LexemType.STRING, getString(), start, line);
        }
        if (ch == '\'') {
            return new Lexem(LexemType.CHARACTER, getString(), start, line);
        }
        if (Character.isLetter(ch)) {
            String name = getName();
            return new Lexem(LexemType.NAME, name, start, line);
        }
        return getOperator();
    }

    private void skipWhites() {
        while (Character.isWhitespace((char) lookChar())) {
            getChar();
        }
    }

    private Lexem getOperator() {
        int start = position;
        char ch = (char) getChar();
        switch(ch) {
            case '+':
            case '-':
            case '*':
            case '/':
            case '%':
            case '|':
            case '&':
                if (ch == '&' && lookChar() == '&') {
                    getChar();
                    return new Lexem(LexemType.OPERATOR, "&&", start, line);
                }
            case '!':
            case '<':
            case '>':
                if (lookChar() == '=') {
                    getChar();
                    return new Lexem(LexemType.OPERATOR, Character.toString(ch) + "=", start, line);
                }
                return new Lexem(LexemType.OPERATOR, Character.toString(ch), start, line);
            case '=':
                if (lookChar() == '>') {
                    getChar();
                    return new Lexem(LexemType.RIGHT_ARROW, Character.toString(ch), start, line);
                } else if (lookChar() == '=') {
                    getChar();
                    return new Lexem(LexemType.OPERATOR, "==", start, line);
                } else {
                    return new Lexem(LexemType.OPERATOR, Character.toString(ch), start, line);
                }
            case '(':
                return new Lexem(LexemType.OPENING_BRACKET, Character.toString(ch), start, line);
            case ')':
                return new Lexem(LexemType.CLOSING_BRACKET, Character.toString(ch), start, line);
            case '{':
                return new Lexem(LexemType.OPENING_B_BRACKET, Character.toString(ch), start, line);
            case '}':
                return new Lexem(LexemType.CLOSING_B_BRACKET, Character.toString(ch), start, line);
            case '[':
                return new Lexem(LexemType.OPENING_SQUARE_BRACKET, Character.toString(ch), start, line);
            case ']':
                return new Lexem(LexemType.CLOSING_SQUARE_BRACKET, Character.toString(ch), start, line);
            case '.':
                if (lookChar() == '.') {
                    getChar();
                    return new Lexem(LexemType.OPERATOR, "..", start, line);
                }
                return new Lexem(LexemType.DOT, Character.toString(ch), start, line);
            case ',':
                return new Lexem(LexemType.COMMA, Character.toString(ch), start, line);
            case '~':
                return new Lexem(LexemType.TILDA, "~", start, line);
            case ':':
                return new Lexem(LexemType.COLON, Character.toString(ch), start, line);
            case ';':
                return new Lexem(LexemType.SEMICOLON, Character.toString(ch), start, line);
            case '\n':
                while (lookChar() == '\n') {
                    getChar();
                }
                return new Lexem(LexemType.NEW_LINE, Character.toString(ch), start, line);
        }
        return new Lexem(LexemType.ERROR, "", start, line);
    }

    private String getString() {
        int ch = getChar();
        ch = getChar();
        StringBuffer result = new StringBuffer();
        while (ch != '"' && ch != '\n') {
            result.append(ch);
            ch = getChar();
        }
        return result.toString();
    }

    private String getName() {
        StringBuffer result = new StringBuffer();
        int ch = lookChar();
        while (Character.isJavaIdentifierPart(ch)) {
            result.append((char) getChar());
            ch = lookChar();
        }
        return result.toString();
    }

    private String getNumber() {
        StringBuffer result = new StringBuffer();
        int ch = lookChar();
        while (Character.isDigit(ch)) {
            result.append((char) getChar());
            ch = lookChar();
        }
        return result.toString();
    }

    private int lookChar() {
        return lastChar;
    }

    private int getChar() {
        int ch = lookChar();
        if (position < lastLine.length() - 1) {
            lastChar = lastLine.charAt(++position);
        } else if (line < lines.size() - 1) {
            lastLine = lines.get(++line);
            position = 0;
            lastChar = lastLine.charAt(0);
        } else {
            lastChar = EOF;
        }
        return ch;
    }

    public int getPosition() {
        return position;
    }

    public Lexem lookLexem() {
        if (l == null) {
            l = readNextLexem();
        }
        return l;
    }

    public Lexem getLexem(String str) throws SyntaxErrorException {
        Lexem l = getLexem();
        if (!(l.val.equals(str))) {
            throw new SyntaxErrorException("[" + str + "] expected, but was [" + l.val + "].", l, this);
        }
        return l;
    }

    public Lexem getLexem(LexemType type) throws SyntaxErrorException {
        Lexem l = getLexem();
        if (l.type != type) {
            throw new SyntaxErrorException(type.toString() + " expected, but was [" + l.val + "].", l, this);
        }
        return l;
    }
}
