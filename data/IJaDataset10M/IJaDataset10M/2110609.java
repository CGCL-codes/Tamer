package javax.mail.internet;

import java.util.*;

/**
 * This class tokenizes RFC822 and MIME headers into the basic
 * symbols specified by RFC822 and MIME. <p>
 *
 * This class handles folded headers (ie headers with embedded
 * CRLF SPACE sequences). The folds are removed in the returned
 * tokens. 
 *
 * @version 1.11, 07/05/04
 * @author  John Mani
 */
public class HeaderTokenizer {

    /**
     * The Token class represents tokens returned by the 
     * HeaderTokenizer.
     */
    public static class Token {

        private int type;

        private String value;

        /**
	 * Token type indicating an ATOM.
	 */
        public static final int ATOM = -1;

        /**
	 * Token type indicating a quoted string. The value 
	 * field contains the string without the quotes.
 	 */
        public static final int QUOTEDSTRING = -2;

        /**
	 * Token type indicating a comment. The value field 
	 * contains the comment string without the comment 
	 * start and end symbols.
	 */
        public static final int COMMENT = -3;

        /**
	 * Token type indicating end of input.
	 */
        public static final int EOF = -4;

        /**
	 * Constructor.
	 * @param	type	Token type
	 * @param	value	Token value
	 */
        public Token(int type, String value) {
            this.type = type;
            this.value = value;
        }

        /**
	 * Return the type of the token. If the token represents a
	 * delimiter or a control character, the type is that character
	 * itself, converted to an integer. Otherwise, it's value is 
	 * one of the following:
	 * <ul>
	 * <li><code>ATOM</code> A sequence of ASCII characters 
	 *	delimited by either SPACE, CTL, "(", <"> or the 
	 *	specified SPECIALS
	 * <li><code>QUOTEDSTRING</code> A sequence of ASCII characters
	 *	within quotes
	 * <li><code>COMMENT</code> A sequence of ASCII characters 
	 *	within "(" and ")".
	 * <li><code>EOF</code> End of header
	 * </ul>
	 */
        public int getType() {
            return type;
        }

        /**
	 * Returns the value of the token just read. When the current
	 * token is a quoted string, this field contains the body of the
	 * string, without the quotes. When the current token is a comment,
	 * this field contains the body of the comment.
	 *
	 * @return	token value
	 */
        public String getValue() {
            return value;
        }
    }

    private String string;

    private boolean skipComments;

    private String delimiters;

    private int currentPos;

    private int maxPos;

    private int nextPos;

    private int peekPos;

    /**
     * RFC822 specials
     */
    public static final String RFC822 = "()<>@,;:\\\"\t .[]";

    /**
     * MIME specials
     */
    public static final String MIME = "()<>@,;:\\\"\t []/?=";

    private static final Token EOFToken = new Token(Token.EOF, null);

    /**
     * Constructor that takes a rfc822 style header.
     *
     * @param	header	The rfc822 header to be tokenized
     * @param	delimiters      Set of delimiter characters 
     *				to be used to delimit ATOMS. These
     *				are usually <code>RFC822</code> or 
     *				<code>MIME</code>
     * @param   skipComments  If true, comments are skipped and
     *				not returned as tokens
     */
    public HeaderTokenizer(String header, String delimiters, boolean skipComments) {
        string = (header == null) ? "" : header;
        this.skipComments = skipComments;
        this.delimiters = delimiters;
        currentPos = nextPos = peekPos = 0;
        maxPos = string.length();
    }

    /**
     * Constructor. Comments are ignored and not returned as tokens
     *
     * @param	header  The header that is tokenized
     * @param	delimiters  The delimiters to be used
     */
    public HeaderTokenizer(String header, String delimiters) {
        this(header, delimiters, true);
    }

    /**
     * Constructor. The RFC822 defined delimiters - RFC822 - are
     * used to delimit ATOMS. Also comments are skipped and not
     * returned as tokens
     */
    public HeaderTokenizer(String header) {
        this(header, RFC822);
    }

    /**
     * Parses the next token from this String. <p>
     *
     * Clients sit in a loop calling next() to parse successive
     * tokens until an EOF Token is returned.
     *
     * @return		the next Token
     * @exception	ParseException if the parse fails
     */
    public Token next() throws ParseException {
        Token tk;
        currentPos = nextPos;
        tk = getNext();
        nextPos = peekPos = currentPos;
        return tk;
    }

    /**
     * Peek at the next token, without actually removing the token
     * from the parse stream. Invoking this method multiple times
     * will return successive tokens, until <code>next()</code> is
     * called. <p>
     *
     * @return		the next Token
     * @exception	ParseException if the parse fails
     */
    public Token peek() throws ParseException {
        Token tk;
        currentPos = peekPos;
        tk = getNext();
        peekPos = currentPos;
        return tk;
    }

    /**
     * Return the rest of the Header.
     *
     * @return String	rest of header. null is returned if we are
     *			already at end of header
     */
    public String getRemainder() {
        return string.substring(nextPos);
    }

    private Token getNext() throws ParseException {
        if (currentPos >= maxPos) return EOFToken;
        if (skipWhiteSpace() == Token.EOF) return EOFToken;
        char c;
        int start;
        boolean filter = false;
        c = string.charAt(currentPos);
        while (c == '(') {
            int nesting;
            for (start = ++currentPos, nesting = 1; nesting > 0 && currentPos < maxPos; currentPos++) {
                c = string.charAt(currentPos);
                if (c == '\\') {
                    currentPos++;
                    filter = true;
                } else if (c == '\r') filter = true; else if (c == '(') nesting++; else if (c == ')') nesting--;
            }
            if (nesting != 0) throw new ParseException("Unbalanced comments");
            if (!skipComments) {
                String s;
                if (filter) s = filterToken(string, start, currentPos - 1); else s = string.substring(start, currentPos - 1);
                return new Token(Token.COMMENT, s);
            }
            if (skipWhiteSpace() == Token.EOF) return EOFToken;
            c = string.charAt(currentPos);
        }
        if (c == '"') {
            for (start = ++currentPos; currentPos < maxPos; currentPos++) {
                c = string.charAt(currentPos);
                if (c == '\\') {
                    currentPos++;
                    filter = true;
                } else if (c == '\r') filter = true; else if (c == '"') {
                    currentPos++;
                    String s;
                    if (filter) s = filterToken(string, start, currentPos - 1); else s = string.substring(start, currentPos - 1);
                    return new Token(Token.QUOTEDSTRING, s);
                }
            }
            throw new ParseException("Unbalanced quoted string");
        }
        if (c < 040 || c >= 0177 || delimiters.indexOf(c) >= 0) {
            currentPos++;
            char ch[] = new char[1];
            ch[0] = c;
            return new Token((int) c, new String(ch));
        }
        for (start = currentPos; currentPos < maxPos; currentPos++) {
            c = string.charAt(currentPos);
            if (c < 040 || c >= 0177 || c == '(' || c == ' ' || c == '"' || delimiters.indexOf(c) >= 0) break;
        }
        return new Token(Token.ATOM, string.substring(start, currentPos));
    }

    private int skipWhiteSpace() {
        char c;
        for (; currentPos < maxPos; currentPos++) if (((c = string.charAt(currentPos)) != ' ') && (c != '\t') && (c != '\r') && (c != '\n')) return currentPos;
        return Token.EOF;
    }

    private static String filterToken(String s, int start, int end) {
        StringBuffer sb = new StringBuffer();
        char c;
        boolean gotEscape = false;
        boolean gotCR = false;
        for (int i = start; i < end; i++) {
            c = s.charAt(i);
            if (c == '\n' && gotCR) {
                gotCR = false;
                continue;
            }
            gotCR = false;
            if (!gotEscape) {
                if (c == '\\') gotEscape = true; else if (c == '\r') gotCR = true; else sb.append(c);
            } else {
                sb.append(c);
                gotEscape = false;
            }
        }
        return sb.toString();
    }
}
