package rabbit.html;

/** This is a class that is used to parse a block of HTML code into
 *  separate tokens. This parser uses a recursive descent approach.
 */
public class HTMLParser {

    /** The actual data to parse. */
    protected byte[] pagepart;

    /** The size of the data to parse. */
    protected int length;

    /** The type of the next token. */
    protected int nextToken = START;

    /** Index of the parse. */
    protected int index = 0;

    /** The current tag started here. */
    protected int tagStart = 0;

    /** The current value as a String. */
    protected String stringValue = null;

    /** the current start of string. */
    protected int stringLength = -1;

    /** True if were in a Tag, false otherwise. */
    protected boolean tagmode = false;

    /** The last tag started here. */
    protected int lastTagStart = 0;

    /** The block we have. */
    protected HTMLBlock block;

    /** A pending comment (script or style data). */
    protected Token pendingComment = null;

    /** This indicates the start of a block. */
    public static final int START = 0;

    /** This indicate a String value was found. */
    public static final int STRING = 1;

    /** This is a Single Quoted String a 'string' */
    public static final int SQSTRING = 2;

    /** This is a Double Quoted String a &quot;string&quot; */
    public static final int DQSTRING = 3;

    /** This is the character ''' */
    public static final int SINGELQUOTE = 4;

    /** This is the character '"' */
    public static final int DOUBLEQUOTE = 5;

    /** Less Than '<' */
    public static final int LT = 6;

    /** More Than '>' */
    public static final int MT = 7;

    /** Equals '=' */
    public static final int EQUALS = 8;

    /** A HTML comment &quot;&lt;&#33-- some text --&gt;&quot; */
    public static final int COMMENT = 9;

    /** A HTML script */
    public static final int SCRIPT = 10;

    /** This indicates the end of a block. */
    public static final int END = 100;

    /** Unknown token. */
    public static final int UNKNOWN = 1000;

    /** Create a new HTMLParser */
    public HTMLParser() {
        this.pagepart = null;
    }

    /** Create a new HTMLParser for the given page.
     * @param page the block to parse.
     */
    public HTMLParser(byte[] page) {
        setText(page);
    }

    /** Set the data block to parse.
     * @param page the block to parse.
     */
    public void setText(byte[] page) {
        setText(page, page.length);
    }

    /** Set the data block to parse.
     * @param page the block to parse.
     * @param length the length of the data.
     */
    public void setText(byte[] page, int length) {
        this.pagepart = page;
        this.length = length;
        index = 0;
    }

    /** Set the data to parse.
     * @param page the block to parse.
     */
    public void setText(String page) {
        setText(page.getBytes());
    }

    /** Get a String describing the token.
     * @param token the token type (like STRING).
     * @return a String describing the token (like &quot;STRING&quot;)
     */
    protected String getTokenString(int token) {
        switch(token) {
            case START:
                return "START";
            case STRING:
                return "STRING";
            case SQSTRING:
                return "SQSTRING";
            case DQSTRING:
                return "DQSTRING";
            case SINGELQUOTE:
                return "SINGELQUOTE";
            case DOUBLEQUOTE:
                return "DOUBLEQUOTE";
            case LT:
                return "LT";
            case MT:
                return "MT";
            case EQUALS:
                return "EQUALS";
            case COMMENT:
                return "COMMENT";
            case END:
                return "END";
            case UNKNOWN:
                return "UNKNOWN";
            default:
                return "unknown";
        }
    }

    /** Scan a String from the block.
     * @throws HTMLParseException if an error occurs.
     * @return STRING
     */
    protected int scanString() throws HTMLParseException {
        int endindex = length;
        int startindex = index - 1;
        if (tagmode) {
            loop: while (index < length) {
                switch(pagepart[index]) {
                    case (int) ' ':
                    case (int) '\t':
                    case (int) '\n':
                    case (int) '\r':
                    case (int) '\"':
                    case (int) '\'':
                    case (int) '<':
                    case (int) '>':
                    case (int) '=':
                        endindex = index;
                        break loop;
                    default:
                        index++;
                }
            }
        } else {
            while (index < length) {
                if (pagepart[index] == '<') {
                    endindex = index;
                    break;
                } else index++;
            }
        }
        if (tagmode) {
            try {
                stringValue = new String(pagepart, startindex, (endindex - startindex), "ISO-8859-1");
            } catch (Exception e) {
                throw new HTMLParseException("doh: " + e);
            }
        } else {
            stringLength = (endindex - startindex);
        }
        return STRING;
    }

    /** Scan a quoted tring from the block. The first character is
     *  treated as the quotation character.
     * @throws HTMLParseException if an error occurs.
     * @return SQSTRING, DQSTRING or UNKNOWN (for strange quotes).
     */
    protected int scanQuotedString() throws HTMLParseException {
        int endindex = -1;
        int startindex = index - 1;
        byte start = pagepart[startindex];
        while (index < length) {
            if (pagepart[index++] == start) {
                endindex = index;
                break;
            }
        }
        if (endindex == -1) {
            block.setRest(lastTagStart);
            return END;
        }
        int l = (endindex < length ? endindex : length) - startindex;
        try {
            stringValue = new String(pagepart, startindex, l, "ISO-8859-1");
        } catch (Exception e) {
            throw new HTMLParseException("doh: " + e);
        }
        switch(start) {
            case (int) '\'':
                return SQSTRING;
            case (int) '"':
                return DQSTRING;
            default:
                return UNKNOWN;
        }
    }

    /** Is this tag a comment?
     * @return true if the block(at current index) starts with !--,
     *  false otherwise.
     */
    protected boolean isComment() {
        if (index + 3 >= length) return false;
        return (pagepart[index] == '!' && pagepart[index + 1] == '-' && pagepart[index + 2] == '-');
    }

    /** Scan a comment from the block, that is the string up to and
     *	including &quot;-->&quot;.
     * @return COMMENT or END.
     */
    protected int scanComment() throws HTMLParseException {
        int startvalue = index - 1;
        int i = -1;
        int j = index;
        while (j + 2 < length) {
            if (pagepart[j] == '-' && pagepart[j + 1] == '-' && pagepart[j + 2] == '>') {
                i = j;
                break;
            }
            j++;
        }
        if (i > -1) {
            index = i + 2;
            nextToken = MT;
            match(MT);
            stringLength = index - startvalue;
            return COMMENT;
        }
        block.setRest(startvalue);
        return END;
    }

    /** Match the token with next token and scan the (new)next token.
     * @param token the token to match.
     * @return the next token.
     */
    protected int match(int token) throws HTMLParseException {
        int ts;
        if (nextToken != token) throw new HTMLParseException("Token: " + getTokenString(token) + " != " + getTokenString(nextToken));
        if (pendingComment != null) {
            nextToken = LT;
            pendingComment = null;
            return SCRIPT;
        }
        while (index < length) {
            tagStart = index;
            stringValue = null;
            switch(pagepart[index++]) {
                case (int) ' ':
                case (int) '\t':
                case (int) '\n':
                case (int) '\r':
                    continue;
                case (int) '<':
                    ts = tagStart;
                    if (isComment()) {
                        nextToken = scanComment();
                        tagStart = ts;
                        return nextToken;
                    } else return nextToken = LT;
                case (int) '>':
                    return nextToken = MT;
                case (int) '=':
                    stringValue = "=";
                    return nextToken = EQUALS;
                case (int) '"':
                    if (tagmode) return nextToken = scanQuotedString();
                case (int) '\'':
                    if (tagmode) return nextToken = scanQuotedString();
                default:
                    return nextToken = scanString();
            }
        }
        return nextToken = END;
    }

    /** Scan a value from the block.
     * @return the value or null.
     */
    protected String value() throws HTMLParseException {
        while (nextToken == EQUALS) {
            match(EQUALS);
            if (nextToken == STRING || nextToken == SQSTRING || nextToken == DQSTRING) {
                String val = stringValue;
                match(nextToken);
                return val;
            }
            return "";
        }
        return null;
    }

    protected void setPendingComment(Token comment) {
        nextToken = SCRIPT;
        this.pendingComment = comment;
        tagStart = pendingComment.getStartIndex();
        stringLength = pendingComment.getLength();
    }

    /** Scan an argument list from the block.
     * @param tag the Tag that have the arguments.
     */
    protected void arglist(Tag tag) throws HTMLParseException {
        String key = null;
        boolean eq = false;
        while (true) {
            switch(nextToken) {
                case MT:
                    tagmode = false;
                    if (tag.getLowerCaseType() != null && (tag.getLowerCaseType().equals("script") || tag.getLowerCaseType().equals("style"))) {
                        Token text = scanCommentUntilEnd(tag.getLowerCaseType());
                        if (text != null) {
                            setPendingComment(text);
                        } else {
                            tagmode = false;
                            return;
                        }
                    } else {
                        match(MT);
                    }
                    return;
                case STRING:
                    key = stringValue;
                    match(STRING);
                    String value = value();
                    tag.addArg(key, value, false);
                    break;
                case END:
                    return;
                case DQSTRING:
                    String ttype = tag.getType();
                    if (ttype != null && ttype.charAt(0) == '!') {
                        tag.addArg(stringValue, null, false);
                        match(nextToken);
                    } else {
                        index -= stringValue.length();
                        pagepart[index] = (byte) ' ';
                        match(nextToken);
                        tag.getToken().setChanged(true);
                    }
                    break;
                case LT:
                    String type = tag.getLowerCaseType();
                    if (type == null || (type.equals("table") && stringValue == null)) {
                        tagmode = false;
                        return;
                    }
                default:
                    if (stringValue != null) tag.addArg(stringValue, null, false);
                    match(nextToken);
            }
        }
    }

    protected boolean sameTag(String tag, int j) {
        byte[] b = tag.getBytes();
        int i = -1;
        for (i = 0; i < b.length && i < length; i++) {
            if (b[i] != pagepart[j + i] && b[i] != pagepart[j + i] + 32) return false;
        }
        return i == b.length;
    }

    protected Token scanCommentUntilEnd(String tag) throws HTMLParseException {
        int len = tag.length();
        int startvalue = index;
        int i = -1;
        int j = index;
        while (j + 1 + len < length) {
            if (pagepart[j] == '<' && pagepart[j + 1] == '/' && sameTag(tag, j + 2)) {
                i = j;
                break;
            }
            j++;
        }
        if (i > -1) {
            stringLength = j - startvalue;
            index = j;
            Token text = new Token(pagepart, Token.COMMENT, startvalue, stringLength);
            return text;
        }
        block.setRest(lastTagStart);
        return null;
    }

    /** Scan a tag from the block.
     * @param ltagStart the index of the last tag started.
     */
    protected void tag(int ltagStart) throws HTMLParseException {
        Tag tag = new Tag();
        Token token = new Token(tag, false);
        switch(nextToken) {
            case STRING:
                tag.setType(stringValue);
                match(STRING);
                arglist(tag);
                if (tagmode) {
                    block.setRest(lastTagStart);
                } else {
                    if (block.restSize() == 0) {
                        token.setStartIndex(ltagStart);
                        block.addToken(token);
                    }
                }
                break;
            case MT:
                tagmode = false;
                match(MT);
                break;
            case END:
                block.setRest(lastTagStart);
                tagmode = false;
                return;
            default:
                arglist(tag);
        }
    }

    /** Scan a page from the block.
     */
    protected void page() throws HTMLParseException {
        while (block.restSize() == 0) {
            switch(nextToken) {
                case END:
                    return;
                case LT:
                    lastTagStart = tagStart;
                    tagmode = true;
                    match(LT);
                    tag(lastTagStart);
                    break;
                case COMMENT:
                    block.addToken(new Token(pagepart, Token.COMMENT, tagStart, stringLength));
                    match(COMMENT);
                    break;
                case SCRIPT:
                    block.addToken(new Token(pagepart, Token.SCRIPT, tagStart, stringLength));
                    match(SCRIPT);
                    break;
                case STRING:
                default:
                    block.addToken(new Token(pagepart, Token.TEXT, tagStart, stringLength));
                    match(nextToken);
            }
        }
    }

    /** Get a HTMLBlock from the pagepart given.
     */
    public HTMLBlock parse() throws HTMLParseException {
        block = new HTMLBlock(pagepart, length);
        nextToken = START;
        match(START);
        page();
        return block;
    }
}
