package nl.hupa.hulpmiddelen;

import java.io.InputStream;
import java.io.IOException;
import java.util.Hashtable;

/**
 *  Used by the parsers to break a file into Tokens.
 * Again, another quick to market class to extract some information.
 * 
 */
public class Lex {

    InputStream is;

    char[] buffer = new char[3];

    int count = -1;

    Token[] token = new Token[3];

    int tokenCount = -1;

    static Hashtable<String, String> keywords;

    static {
        keywords = new Hashtable<String, String>(20);
        keywords.put("cull", "");
        keywords.put("nopicmip", "");
        keywords.put("nomipmaps", "");
        keywords.put("rgbgen", "");
        keywords.put("blendfunc", "");
        keywords.put("alphafunc", "");
        keywords.put("depthfunc", "");
        keywords.put("depthwrite", "");
        keywords.put("map", "");
        keywords.put("clampmap", "");
        keywords.put("animmap", "");
        keywords.put("tcmod", "");
        keywords.put("tcgen", "");
        keywords.put("alphagen", "");
        keywords.put("foggen", "");
        keywords.put("tesssize", "");
        keywords.put("qereditorimage", "");
        keywords.put("q3map_surfacelight", "");
        keywords.put("q3map_lightimage", "");
        keywords.put("q3map_lightsubdivide", "");
        keywords.put("q3map_globaltexture", "");
        keywords.put("q3map_sun", "");
        keywords.put("light", "");
        keywords.put("surfaceparm", "");
        keywords.put("fogparms", "");
        keywords.put("skyparms", "");
        keywords.put("nomarks", "");
        keywords.put("cloudparms", "");
        keywords.put("deformvertexes", "");
    }

    /**
	 *Constructor for the Lex object
	 *
	 * @param  is  Description of the Parameter
	 */
    public Lex(InputStream is) {
        this.is = is;
    }

    /**
	 *  Description of the Method
	 *
	 * @return    Description of the Return Value
	 */
    public char peek() {
        if (count < 0) {
            try {
                buffer[++count] = (char) is.read();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return buffer[count];
    }

    /**
	 *  Gets the char attribute of the Lex object
	 *
	 * @return    The char value
	 */
    public char getChar() {
        if (count >= 0) {
            return buffer[count--];
        }
        try {
            return (char) is.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (char) 0;
    }

    /**
	 *  Description of the Method
	 *
	 * @return    Description of the Return Value
	 */
    public boolean available() {
        try {
            return count < 0 ? is.available() > 0 : true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
	 *  Description of the Method
	 *
	 * @return    Description of the Return Value
	 */
    public Token peekToken() {
        if (tokenCount < 0) {
            token[++tokenCount] = getTokenInternal();
        }
        return token[tokenCount];
    }

    /**
	 *  Gets the token attribute of the Lex object
	 *
	 * @return    The token value
	 */
    public Token getToken() {
        if (tokenCount < 0) {
            return getTokenInternal();
        }
        return token[tokenCount--];
    }

    /**
	 *  Gets the tokenInternal attribute of the Lex object
	 *
	 * @return    The tokenInternal value
	 */
    private Token getTokenInternal() {
        Token t = new Token();
        char ch;
        while (true) {
            while (Character.isWhitespace(ch = getChar())) {
                ;
            }
            if (ch == '/' && peek() == '/') {
                for (; available() && ch != '\n'; ch = getChar()) {
                    ;
                }
            } else {
                break;
            }
        }
        if (!available()) {
            t.type = Token.EOF;
            return t;
        }
        switch(ch) {
            case '{':
                t.type = Token.LBRACE;
                t.text = "{";
                return t;
            case '}':
                t.type = Token.RBRACE;
                t.text = "}";
                return t;
            case '(':
                t.type = Token.LPAREN;
                t.text = "(";
                return t;
            case ')':
                t.type = Token.RPAREN;
                t.text = ")";
                return t;
            case '$':
                t.type = Token.DOLLAR;
                t.text = "$";
                return t;
            case '*':
                t.type = Token.ASTERISK;
                t.text = "*";
                return t;
            case '"':
                t.type = Token.QUOTE;
                t.text = "\"";
                return t;
        }
        if (Character.isDigit(ch) || ch == '.' || ch == '-' || ch == '+') {
            if (!Character.isDigit(peek())) {
                t.type = Token.DASH;
                t.text = "-";
                return t;
            }
            t.type = Token.NUMBER;
            String result = "" + ch;
            while (Character.isDigit(peek()) || peek() == '.') {
                result = result + getChar();
            }
            t.number = Float.parseFloat(result);
            return t;
        }
        if (Character.isLetter(ch) || ch == '_') {
            t.text = "" + ch;
            while (Character.isLetterOrDigit(peek()) || peek() == '_' || peek() == '.' || peek() == '/') {
                if (peek() == '/') {
                    t.type = Token.PATH;
                }
                t.text = t.text + getChar();
            }
            if (t.type == Token.PATH) {
                return t;
            }
            if (keywords.containsKey(t.text.toLowerCase())) {
                t.type = Token.KEYWORD;
            } else {
                t.type = Token.STRING;
            }
            return t;
        }
        t.type = Token.UNKNOWN;
        return t;
    }
}
