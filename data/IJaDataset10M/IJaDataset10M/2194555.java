package com.Ostermiller.Syntax.Lexer;

/** 
 * A CToken is a token that is returned by a lexer that is lexing a C
 * source file.  It has several attributes describing the token:
 * The type of token, the text of the token, the line number on which it
 * occurred, the number of characters into the input at which it started, and
 * similarly, the number of characters into the input at which it ended. <br>
 */
public class CToken extends Token {

    public static final int RESERVED_WORD_ABSTRACT = 0x101;

    public static final int RESERVED_WORD_AUTO = 0x102;

    public static final int RESERVED_WORD_BREAK = 0x103;

    public static final int RESERVED_WORD_CASE = 0x104;

    public static final int RESERVED_WORD_CONST = 0x105;

    public static final int RESERVED_WORD_CONTINUE = 0x106;

    public static final int RESERVED_WORD_DEFAULT = 0x107;

    public static final int RESERVED_WORD_DO = 0x108;

    public static final int RESERVED_WORD_ELSE = 0x109;

    public static final int RESERVED_WORD_ENUM = 0x10A;

    public static final int RESERVED_WORD_EXTERN = 0x10B;

    public static final int RESERVED_WORD_FOR = 0x10C;

    public static final int RESERVED_WORD_GOTO = 0x10D;

    public static final int RESERVED_WORD_IF = 0x10E;

    public static final int RESERVED_WORD_REGISTER = 0x10F;

    public static final int RESERVED_WORD_RETURN = 0x110;

    public static final int RESERVED_WORD_SIZEOF = 0x111;

    public static final int RESERVED_WORD_STATIC = 0x112;

    public static final int RESERVED_WORD_STRUCT = 0x113;

    public static final int RESERVED_WORD_SWITCH = 0x114;

    public static final int RESERVED_WORD_TYPEDEF = 0x115;

    public static final int RESERVED_WORD_UNION = 0x116;

    public static final int RESERVED_WORD_VOLATILE = 0x117;

    public static final int RESERVED_WORD_WHILE = 0x118;

    public static final int RESERVED_WORD_CATCH = 0x119;

    public static final int RESERVED_WORD_CLASS = 0x11A;

    public static final int RESERVED_WORD_CONST_CAST = 0x11B;

    public static final int RESERVED_WORD_DELETE = 0x11C;

    public static final int RESERVED_WORD_DYNAMIC_CAST = 0x11D;

    public static final int RESERVED_WORD_FRIEND = 0x11E;

    public static final int RESERVED_WORD_INLINE = 0x11F;

    public static final int RESERVED_WORD_MUTABLE = 0x120;

    public static final int RESERVED_WORD_NAMESPACE = 0x121;

    public static final int RESERVED_WORD_NEW = 0x122;

    public static final int RESERVED_WORD_OPERATOR = 0x123;

    public static final int RESERVED_WORD_OVERLOAD = 0x124;

    public static final int RESERVED_WORD_PRIVATE = 0x125;

    public static final int RESERVED_WORD_PROTECTED = 0x126;

    public static final int RESERVED_WORD_PUBLIC = 0x127;

    public static final int RESERVED_WORD_REINTERPRET_CAST = 0x128;

    public static final int RESERVED_WORD_STATIC_CAST = 0x129;

    public static final int RESERVED_WORD_TEMPLATE = 0x12A;

    public static final int RESERVED_WORD_THIS = 0x12B;

    public static final int RESERVED_WORD_TRY = 0x12C;

    public static final int RESERVED_WORD_VIRTUAL = 0x12D;

    public static final int RESERVED_WORD_BOOL = 0x12E;

    public static final int RESERVED_WORD_CHAR = 0x12F;

    public static final int RESERVED_WORD_DOUBLE = 0x130;

    public static final int RESERVED_WORD_FLOAT = 0x131;

    public static final int RESERVED_WORD_INT = 0x132;

    public static final int RESERVED_WORD_LONG = 0x133;

    public static final int RESERVED_WORD_SHORT = 0x134;

    public static final int RESERVED_WORD_SIGNED = 0x135;

    public static final int RESERVED_WORD_UNSIGNED = 0x136;

    public static final int RESERVED_WORD_VOID = 0x137;

    public static final int RESERVED_WORD_ASM = 0x138;

    public static final int RESERVED_WORD_TYPENAME = 0x139;

    public static final int RESERVED_WORD_EXPLICIT = 0x13A;

    public static final int RESERVED_WORD_USING = 0x13B;

    public static final int RESERVED_WORD_THROW = 0x13C;

    public static final int RESERVED_WORD_WCHAR_T = 0x13D;

    public static final int RESERVED_WORD_TYPEID = 0x13E;

    public static final int IDENTIFIER = 0x200;

    public static final int LITERAL_BOOLEAN = 0x300;

    public static final int LITERAL_INTEGER_DECIMAL = 0x310;

    public static final int LITERAL_INTEGER_OCTAL = 0x311;

    public static final int LITERAL_INTEGER_HEXIDECIMAL = 0x312;

    public static final int LITERAL_LONG_DECIMAL = 0x320;

    public static final int LITERAL_LONG_OCTAL = 0x321;

    public static final int LITERAL_LONG_HEXIDECIMAL = 0x322;

    public static final int LITERAL_FLOATING_POINT = 0x330;

    public static final int LITERAL_DOUBLE = 0x340;

    public static final int LITERAL_CHARACTER = 0x350;

    public static final int LITERAL_STRING = 0x360;

    public static final int LITERAL_NULL = 0x370;

    public static final int SEPARATOR_LPAREN = 0x400;

    public static final int SEPARATOR_RPAREN = 0x401;

    public static final int SEPARATOR_LBRACE = 0x410;

    public static final int SEPARATOR_RBRACE = 0x411;

    public static final int SEPARATOR_LBRACKET = 0x420;

    public static final int SEPARATOR_RBRACKET = 0x421;

    public static final int SEPARATOR_SEMICOLON = 0x430;

    public static final int SEPARATOR_COMMA = 0x440;

    public static final int SEPARATOR_PERIOD = 0x450;

    public static final int SEPARATOR_ARROW = 0x460;

    public static final int OPERATOR_GREATER_THAN = 0x500;

    public static final int OPERATOR_LESS_THAN = 0x501;

    public static final int OPERATOR_LESS_THAN_OR_EQUAL = 0x502;

    public static final int OPERATOR_GREATER_THAN_OR_EQUAL = 0x503;

    public static final int OPERATOR_EQUAL = 0x504;

    public static final int OPERATOR_NOT_EQUAL = 0x505;

    public static final int OPERATOR_LOGICAL_NOT = 0x510;

    public static final int OPERATOR_LOGICAL_AND = 0x511;

    public static final int OPERATOR_LOGICAL_OR = 0x512;

    public static final int OPERATOR_ADD = 0x520;

    public static final int OPERATOR_SUBTRACT = 0x521;

    public static final int OPERATOR_MULTIPLY = 0x522;

    public static final int OPERATOR_DIVIDE = 0x523;

    public static final int OPERATOR_MOD = 0x524;

    public static final int OPERATOR_BITWISE_COMPLIMENT = 0x530;

    public static final int OPERATOR_BITWISE_AND = 0x531;

    public static final int OPERATOR_BITWISE_OR = 0x532;

    public static final int OPERATOR_BITWISE_XOR = 0x533;

    public static final int OPERATOR_SHIFT_LEFT = 0x540;

    public static final int OPERATOR_SHIFT_RIGHT = 0x541;

    public static final int OPERATOR_ASSIGN = 0x550;

    public static final int OPERATOR_ADD_ASSIGN = 0x560;

    public static final int OPERATOR_SUBTRACT_ASSIGN = 0x561;

    public static final int OPERATOR_MULTIPLY_ASSIGN = 0x562;

    public static final int OPERATOR_DIVIDE_ASSIGN = 0x563;

    public static final int OPERATOR_MOD_ASSIGN = 0x564;

    public static final int OPERATOR_BITWISE_AND_ASSIGN = 0x571;

    public static final int OPERATOR_BITWISE_OR_ASSIGN = 0x572;

    public static final int OPERATOR_BITWISE_XOR_ASSIGN = 0x573;

    public static final int OPERATOR_SHIFT_LEFT_ASSIGN = 0x580;

    public static final int OPERATOR_SHIFT_RIGHT_ASSIGN = 0x581;

    public static final int OPERATOR_INCREMENT = 0x590;

    public static final int OPERATOR_DECREMENT = 0x591;

    public static final int OPERATOR_QUESTION = 0x5A0;

    public static final int OPERATOR_COLON = 0x5A1;

    public static final int PREPROCESSOR_DIRECTIVE = 0XC00;

    public static final int COMMENT_TRADITIONAL = 0xD00;

    public static final int COMMENT_END_OF_LINE = 0xD10;

    public static final int COMMENT_DOCUMENTATION = 0xD20;

    public static final int WHITE_SPACE = 0xE00;

    public static final int ERROR_IDENTIFIER = 0xF00;

    public static final int ERROR_UNCLOSED_STRING = 0xF10;

    public static final int ERROR_MALFORMED_STRING = 0xF11;

    public static final int ERROR_MALFORMED_UNCLOSED_STRING = 0xF12;

    public static final int ERROR_UNCLOSED_CHARACTER = 0xF20;

    public static final int ERROR_MALFORMED_CHARACTER = 0xF21;

    public static final int ERROR_MALFORMED_UNCLOSED_CHARACTER = 0xF22;

    public static final int ERROR_INTEGER_DECIMIAL_SIZE = 0xF30;

    public static final int ERROR_INTEGER_OCTAL_SIZE = 0xF31;

    public static final int ERROR_INTEGER_HEXIDECIMAL_SIZE = 0xF32;

    public static final int ERROR_LONG_DECIMIAL_SIZE = 0xF33;

    public static final int ERROR_LONG_OCTAL_SIZE = 0xF34;

    public static final int ERROR_LONG_HEXIDECIMAL_SIZE = 0xF35;

    public static final int ERROR_FLOAT_SIZE = 0xF36;

    public static final int ERROR_DOUBLE_SIZE = 0xF37;

    public static final int ERROR_FLOAT = 0xF38;

    public static final int ERROR_UNCLOSED_COMMENT = 0xF40;

    public static final int ERROR_MALFORMED_PREPROCESSOR_DIRECTIVE = 0xF50;

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
    public CToken(int ID, String contents, int lineNumber, int charBegin, int charEnd) {
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
    public CToken(int ID, String contents, int lineNumber, int charBegin, int charEnd, int state) {
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
   * Checks this token to see if it is a reserved word.
   *
   * @return true if this token is a reserved word, false otherwise
   */
    public boolean isReservedWord() {
        return ((ID >> 8) == 0x1);
    }

    /** 
   * Checks this token to see if it is an identifier.
   *
   * @return true if this token is an identifier, false otherwise
   */
    public boolean isIdentifier() {
        return ((ID >> 8) == 0x2);
    }

    /** 
   * Checks this token to see if it is a literal.
   *
   * @return true if this token is a literal, false otherwise
   */
    public boolean isLiteral() {
        return ((ID >> 8) == 0x3);
    }

    /** 
   * Checks this token to see if it is a Separator.
   *
   * @return true if this token is a Separator, false otherwise
   */
    public boolean isSeparator() {
        return ((ID >> 8) == 0x4);
    }

    /** 
   * Checks this token to see if it is a Operator.
   *
   * @return true if this token is a Operator, false otherwise
   */
    public boolean isOperator() {
        return ((ID >> 8) == 0x5);
    }

    /** 
   * Checks this token to see if it should be handled by the preprocessor.
   * 
   * @return true if this token should be handled by the preprocessor, false otherwise
   */
    public boolean isPreProcessor() {
        return ((ID >> 8) == 0xC);
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
        if (isReservedWord()) {
            return ("reservedWord");
        } else if (isIdentifier()) {
            return ("identifier");
        } else if (isLiteral()) {
            return ("literal");
        } else if (isSeparator()) {
            return ("separator");
        } else if (isOperator()) {
            return ("operator");
        } else if (isComment()) {
            return ("comment");
        } else if (isPreProcessor()) {
            return ("preprocessor");
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
                case ERROR_IDENTIFIER:
                    s += "Unrecognized Identifier: " + contents;
                    break;
                case ERROR_UNCLOSED_STRING:
                    s += "'\"' expected after " + contents;
                    break;
                case ERROR_MALFORMED_STRING:
                case ERROR_MALFORMED_UNCLOSED_STRING:
                    s += "Illegal character in " + contents;
                    break;
                case ERROR_UNCLOSED_CHARACTER:
                    s += "\"'\" expected after " + contents;
                    break;
                case ERROR_MALFORMED_CHARACTER:
                case ERROR_MALFORMED_UNCLOSED_CHARACTER:
                    s += "Illegal character in " + contents;
                    break;
                case ERROR_INTEGER_DECIMIAL_SIZE:
                case ERROR_INTEGER_OCTAL_SIZE:
                case ERROR_FLOAT:
                    s += "Illegal character in " + contents;
                    break;
                case ERROR_INTEGER_HEXIDECIMAL_SIZE:
                case ERROR_LONG_DECIMIAL_SIZE:
                case ERROR_LONG_OCTAL_SIZE:
                case ERROR_LONG_HEXIDECIMAL_SIZE:
                case ERROR_FLOAT_SIZE:
                case ERROR_DOUBLE_SIZE:
                    s += "Literal out of bounds: " + contents;
                    break;
                case ERROR_UNCLOSED_COMMENT:
                    s += "*/ expected after " + contents;
                    break;
                case ERROR_MALFORMED_PREPROCESSOR_DIRECTIVE:
                    s += "Unrecognized preprocessor command " + contents;
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
