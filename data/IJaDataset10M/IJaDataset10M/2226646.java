package csa.util.syntax.Syntax.Lexer;

/** 
 * A HTMLToken1 is a token that is returned by a lexer that is lexing a HTML
 * source file.  It has several attributes describing the token:
 * The type of token, the text of the token, the line number on which it
 * occurred, the number of characters into the input at which it started, and
 * similarly, the number of characters into the input at which it ended. <br>
 */
public class HTMLToken1 extends Token {

    public static final int TAG_START = 0x100;

    public static final int TAG_END = 0x101;

    public static final int EQUAL = 0x102;

    public static final int WORD = 0x200;

    public static final int REFERENCE = 0x300;

    public static final int TAG_NAME = 0x400;

    public static final int END_TAG_NAME = 0x401;

    public static final int NAME = 0x500;

    public static final int VALUE = 0x600;

    public static final int CHAR_REF = 0x700;

    public static final int SCRIPT = 0x800;

    public static final int COMMENT = 0xD00;

    public static final int WHITE_SPACE = 0xE00;

    public static final int ERROR_MALFORMED_TAG = 0xF00;

    private int ID;

    private String contents;

    private int lineNumber;

    private int charBegin;

    private int charEnd;

    private int state;

    /**
   * Create a new token.
   * The constructor is typically called by the lexer
   *
   * @param ID the id number of the token
   * @param contents A string representing the text of the token
   * @param lineNumber the line number of the input on which this token started
   * @param charBegin the offset into the input in characters at which this token started
   * @param charEnd the offset into the input in characters at which this token ended
   */
    public HTMLToken1(int ID, String contents, int lineNumber, int charBegin, int charEnd) {
        this(ID, contents, lineNumber, charBegin, charEnd, Token.UNDEFINED_STATE);
    }

    /**
   * Create a new token.
   * The constructor is typically called by the lexer
   *
   * @param ID the id number of the token
   * @param contents A string representing the text of the token
   * @param lineNumber the line number of the input on which this token started
   * @param charBegin the offset into the input in characters at which this token started
   * @param charEnd the offset into the input in characters at which this token ended
   * @param state the state the tokenizer is in after returning this token.
   */
    public HTMLToken1(int ID, String contents, int lineNumber, int charBegin, int charEnd, int state) {
        this.ID = ID;
        this.contents = new String(contents);
        this.lineNumber = lineNumber;
        this.charBegin = charBegin;
        this.charEnd = charEnd;
        this.state = state;
    }

    /**
     * Get an integer representing the state the tokenizer is in after
     * returning this token.
     * Those who are interested in incremental tokenizing for performance
     * reasons will want to use this method to figure out where the tokenizer
     * may be restarted.  The tokenizer starts in Token.INITIAL_STATE, so
     * any time that it reports that it has returned to this state, the
     * tokenizer may be restarted from there.
     */
    public int getState() {
        return state;
    }

    /** 
   * get the ID number of this token
   * 
   * @return the id number of the token
   */
    public int getID() {
        return ID;
    }

    /** 
   * get the contents of this token
   * 
   * @return A string representing the text of the token
   */
    public String getContents() {
        return (new String(contents));
    }

    /** 
   * get the line number of the input on which this token started
   * 
   * @return the line number of the input on which this token started
   */
    public int getLineNumber() {
        return lineNumber;
    }

    /** 
   * get the offset into the input in characters at which this token started
   *
   * @return the offset into the input in characters at which this token started
   */
    public int getCharBegin() {
        return charBegin;
    }

    /** 
   * get the offset into the input in characters at which this token ended
   *
   * @return the offset into the input in characters at which this token ended
   */
    public int getCharEnd() {
        return charEnd;
    }

    /** 
   * Checks this token to see if it is a tag.
   *
   * @return true if this token is a reserved word, false otherwise
   */
    public boolean isSeparator() {
        return ((ID >> 8) == 0x1);
    }

    /** 
   * Checks this token to see if it is a word.
   *
   * @return true if this token is an identifier, false otherwise
   */
    public boolean isWord() {
        return ((ID >> 8) == 0x2);
    }

    /** 
   * Checks this token to see if it is an tag name.
   *
   * @return true if this token is a tag name, false otherwise
   */
    public boolean isTagName() {
        return ((ID >> 8) == 0x4);
    }

    /** 
   * Checks this token to see if it is a name of a name value pair.
   *
   * @return true if this token is a name, false otherwise
   */
    public boolean isName() {
        return ((ID >> 8) == 0x5);
    }

    /** 
   * Checks this token to see if it is a value of a name value pair.
   *
   * @return true if this token is a value, false otherwise
   */
    public boolean isValue() {
        return ((ID >> 8) == 0x6);
    }

    /** 
   * Checks this token to see if it is a character reference.
   *
   * @return true if this token is a character reference, false otherwise
   */
    public boolean isCharacterReference() {
        return ((ID >> 8) == 0x7);
    }

    /** 
   * Checks this token to see if it is an script.
   *
   * @return true if this token is an script, false otherwise
   */
    public boolean isScript() {
        return ((ID >> 8) == 0x8);
    }

    /** 
   * Checks this token to see if it is a comment.
   * 
   * @return true if this token is a comment, false otherwise
   */
    public boolean isComment() {
        return ((ID >> 8) == 0xD);
    }

    /** 
   * Checks this token to see if it is White Space.
   * Usually tabs, line breaks, form feed, spaces, etc.
   * 
   * @return true if this token is White Space, false otherwise
   */
    public boolean isWhiteSpace() {
        return ((ID >> 8) == 0xE);
    }

    /** 
   * Checks this token to see if it is an Error.
   * Unfinished comments, numbers that are too big, unclosed strings, etc.
   * 
   * @return true if this token is an Error, false otherwise
   */
    public boolean isError() {
        return ((ID >> 8) == 0xF);
    }

    /**
	 * A description of this token.  The description should
	 * be appropriate for syntax highlighting.  For example
	 * "comment" is returned for a comment.
     *
	 * @return a description of this token.
	 */
    public String getDescription() {
        if (isSeparator()) {
            return ("separator");
        } else if (isWord()) {
            return ("text");
        } else if (ID == TAG_NAME) {
            return ("tag");
        } else if (ID == END_TAG_NAME) {
            return ("endtag");
        } else if (isName()) {
            return ("name");
        } else if (isCharacterReference()) {
            return ("reference");
        } else if (isValue()) {
            return ("value");
        } else if (isScript()) {
            return ("preprocessor");
        } else if (isComment()) {
            return ("comment");
        } else if (isWhiteSpace()) {
            return ("whitespace");
        } else if (isError()) {
            return ("error");
        } else {
            return ("unknown");
        }
    }

    /**
   * get a String that explains the error, if this token is an error.
   * 
   * @return a  String that explains the error, if this token is an error, null otherwise.
   */
    public String errorString() {
        String s;
        if (isError()) {
            s = "Error on line " + lineNumber + ": ";
            switch(ID) {
                case ERROR_MALFORMED_TAG:
                    s += "Malformed Tag: " + contents;
                    break;
            }
        } else {
            s = null;
        }
        return (s);
    }

    /** 
   * get a representation of this token as a human readable string.
   * The format of this string is subject to change and should only be used
   * for debugging purposes.
   *
   * @return a string representation of this token
   */
    public String toString() {
        return ("Token #" + Integer.toHexString(ID) + ": " + getDescription() + " Line " + lineNumber + " from " + charBegin + " to " + charEnd + " : " + contents);
    }
}
