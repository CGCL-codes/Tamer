package jolie.net.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import jolie.lang.parse.Scanner;
import jolie.lang.parse.Scanner.Token;
import jolie.lang.parse.Scanner.TokenType;

public class HttpScanner {

    private final InputStream stream;

    private int state;

    private int currInt;

    private char ch;

    private static final int OVERFLOW_NET = 8192;

    public HttpScanner(InputStream stream, URI source) throws IOException {
        this.stream = stream;
        readChar();
    }

    public String readLine() throws IOException {
        StringBuilder buffer = new StringBuilder();
        readChar();
        while (!Scanner.isNewLineChar(ch)) {
            buffer.append(ch);
            readChar();
        }
        return buffer.toString();
    }

    public String readWord() throws IOException {
        return readWord(true);
    }

    public String readWord(boolean readChar) throws IOException {
        StringBuilder buffer = new StringBuilder();
        if (readChar) {
            readChar();
        }
        do {
            buffer.append(ch);
            readChar();
        } while (!Scanner.isSeparator(ch));
        return buffer.toString();
    }

    public void eatSeparators() throws IOException {
        while (Scanner.isSeparator(ch)) {
            readChar();
        }
    }

    public void eatSeparatorsUntilEOF() throws IOException {
        while (Scanner.isSeparator(ch) && stream.available() > 0) {
            readChar();
        }
    }

    public char currentCharacter() {
        return ch;
    }

    public InputStream inputStream() {
        return stream;
    }

    public final void readChar() throws IOException {
        currInt = stream.read();
        ch = (char) currInt;
    }

    public Token getToken() throws IOException {
        state = 1;
        StringBuilder builder = new StringBuilder();
        builder.append(ch);
        int i;
        String tmp;
        while (ch != -1 && Scanner.isSeparator(ch)) {
            readChar();
            builder.append(ch);
            tmp = builder.toString();
            if ((i = tmp.indexOf('\n', 0)) < tmp.indexOf('\n', i + 1)) {
                return new Token(TokenType.EOF);
            }
        }
        if (ch == -1) return new Token(TokenType.EOF);
        boolean stopOneChar = false;
        Token retval = null;
        builder = new StringBuilder();
        while (ch != -1 && retval == null) {
            switch(state) {
                case 1:
                    if (Character.isLetter(ch)) state = 2; else if (Character.isDigit(ch)) state = 3; else if (ch == '"') state = 4; else if (ch == '+') state = 5; else if (ch == '=') state = 6; else if (ch == '|') state = 7; else if (ch == '&') state = 8; else if (ch == '<') state = 9; else if (ch == '>') state = 10; else if (ch == '!') state = 11; else if (ch == '/') state = 12; else if (ch == '-') state = 14; else {
                        if (ch == '(') retval = new Token(TokenType.LPAREN); else if (ch == ')') retval = new Token(TokenType.RPAREN); else if (ch == '[') retval = new Token(TokenType.LSQUARE); else if (ch == ']') retval = new Token(TokenType.RSQUARE); else if (ch == '{') retval = new Token(TokenType.LCURLY); else if (ch == '}') retval = new Token(TokenType.RCURLY); else if (ch == '*') retval = new Token(TokenType.ASTERISK); else if (ch == '@') retval = new Token(TokenType.AT); else if (ch == ':') retval = new Token(TokenType.COLON); else if (ch == ',') retval = new Token(TokenType.COMMA); else if (ch == ';') retval = new Token(TokenType.SEQUENCE); else if (ch == '.') retval = new Token(TokenType.DOT); else if (ch == '/') retval = new Token(TokenType.DIVIDE);
                        readChar();
                    }
                    break;
                case 2:
                    if (!Character.isLetterOrDigit(ch) && ch != '_' && ch != '-' && ch != '+') {
                        retval = new Token(TokenType.ID, builder.toString());
                    }
                    break;
                case 3:
                    if (!Character.isDigit(ch)) retval = new Token(TokenType.INT, builder.toString());
                    break;
                case 4:
                    if (ch == '"') {
                        retval = new Token(TokenType.STRING, builder.substring(1));
                        readChar();
                    } else if (ch == '\\') {
                        readChar();
                        if (ch == '\\') builder.append('\\'); else if (ch == 'n') builder.append('\n'); else if (ch == 't') builder.append('\t'); else if (ch == '"') builder.append('"'); else throw new IOException("malformed string: bad \\ usage");
                        stopOneChar = true;
                        readChar();
                    }
                    break;
                case 5:
                    if (ch == '+') {
                        retval = new Token(TokenType.INCREMENT);
                        readChar();
                    } else retval = new Token(TokenType.PLUS);
                    break;
                case 6:
                    if (ch == '=') {
                        retval = new Token(TokenType.EQUAL);
                        readChar();
                    } else retval = new Token(TokenType.ASSIGN);
                    break;
                case 7:
                    if (ch == '|') {
                        retval = new Token(TokenType.OR);
                        readChar();
                    } else retval = new Token(TokenType.PARALLEL);
                    break;
                case 8:
                    if (ch == '&') {
                        retval = new Token(TokenType.AND);
                        readChar();
                    }
                    break;
                case 9:
                    if (ch == '=') {
                        retval = new Token(TokenType.MINOR_OR_EQUAL);
                        readChar();
                    } else retval = new Token(TokenType.LANGLE);
                    break;
                case 10:
                    if (ch == '=') {
                        retval = new Token(TokenType.MAJOR_OR_EQUAL);
                        readChar();
                    } else retval = new Token(TokenType.RANGLE);
                    break;
                case 11:
                    if (ch == '=') {
                        retval = new Token(TokenType.NOT_EQUAL);
                        readChar();
                    } else retval = new Token(TokenType.NOT);
                    break;
                case 12:
                    retval = new Token(TokenType.DIVIDE);
                    break;
                case 13:
                    if (ch == '*') {
                        readChar();
                        stopOneChar = true;
                        if (ch == '/') {
                            readChar();
                            retval = getToken();
                        }
                    }
                    break;
                case 14:
                    if (Character.isDigit(ch)) state = 3; else retval = new Token(TokenType.MINUS);
                    break;
                case 15:
                    if (ch == '\n') {
                        readChar();
                        retval = getToken();
                    }
                    break;
                default:
                    retval = new Token(TokenType.ERROR);
                    break;
            }
            if (retval == null) {
                if (stopOneChar) {
                    stopOneChar = false;
                } else {
                    if (builder.length() > OVERFLOW_NET) {
                        throw new IOException("Token length exceeds maximum allowed limit (" + OVERFLOW_NET + " bytes). First 10 characters: " + builder.toString().substring(0, 10) + " Last 10 characters: " + builder.toString().substring(builder.length() - 10, builder.length()));
                    }
                    builder.append(ch);
                    readChar();
                }
            }
        }
        if (retval == null) retval = new Token(TokenType.ERROR);
        return retval;
    }
}
