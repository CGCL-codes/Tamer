package freemarker.core;

import freemarker.template.utility.SecurityUtilities;

/**
 * This exception is thrown when parse errors are encountered.
 * You can explicitly create objects of this exception type by
 * calling the method generateParseException in the generated
 * parser.
 *
 * You can modify this class to customize your error reporting
 * mechanisms so long as you retain the public fields.
 */
public class ParseException extends java.io.IOException implements FMParserConstants {

    /**
   * This constructor is used by the method "generateParseException"
   * in the generated parser.  Calling this constructor generates
   * a new object of this type with the fields "currentToken",
   * "expectedTokenSequences", and "tokenImage" set.  The boolean
   * flag "specialConstructor" is also set to true to indicate that
   * this constructor was used to create this object.
   * This constructor calls its super class with the empty string
   * to force the "toString" method of parent class "Throwable" to
   * print the error message in the form:
   *     ParseException: <result of getMessage>
   */
    public ParseException(Token currentTokenVal, int[][] expectedTokenSequencesVal, String[] tokenImageVal) {
        super("");
        specialConstructor = true;
        currentToken = currentTokenVal;
        expectedTokenSequences = expectedTokenSequencesVal;
        tokenImage = tokenImageVal;
    }

    /**
   * The following constructors are for use by you for whatever
   * purpose you can think of.  Constructing the exception in this
   * manner makes the exception behave in the normal way - i.e., as
   * documented in the class "Throwable".  The fields "errorToken",
   * "expectedTokenSequences", and "tokenImage" do not contain
   * relevant information.  The JavaCC generated code does not use
   * these constructors.
   */
    protected ParseException() {
        super();
        specialConstructor = false;
    }

    public ParseException(String message, int lineNumber, int columnNumber) {
        super(message);
        specialConstructor = false;
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
    }

    public ParseException(String message, TemplateObject tobj) {
        super(message);
        specialConstructor = false;
        this.lineNumber = tobj.beginLine;
        this.columnNumber = tobj.beginColumn;
    }

    /**
   * This variable determines which constructor was used to create
   * this object and thereby affects the semantics of the
   * "getMessage" method (see below).
   */
    protected boolean specialConstructor;

    /**
   * This is the last token that has been consumed successfully.  If
   * this object has been created due to a parse error, the token
   * following this token will (therefore) be the first error token.
   */
    public Token currentToken;

    public int columnNumber, lineNumber;

    /**
   * Each entry in this array is an array of integers.  Each array
   * of integers represents a sequence of tokens (by their ordinal
   * values) that is expected at this point of the parse.
   */
    public int[][] expectedTokenSequences;

    /**
   * This is a reference to the "tokenImage" array of the generated
   * parser within which the parse error occurred.  This array is
   * defined in the generated ...Constants interface.
   */
    public String[] tokenImage;

    private String templateName = "unknown template";

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    /**
   * This method has the standard behavior when this object has been
   * created using the standard constructors.  Otherwise, it uses
   * "currentToken" and "expectedTokenSequences" to generate a parse
   * error message and returns it.  If this object has been created
   * due to a parse error, and you do not catch it (it gets thrown
   * from the parser), then this method is called during the printing
   * of the final stack trace, and hence the correct error message
   * gets displayed.
   */
    public String getMessage() {
        if (!specialConstructor) {
            return super.getMessage() + " in " + templateName;
        }
        String retval = customGetMessage();
        if (retval != null) {
            return retval;
        }
        String expected = "";
        int maxSize = 0;
        for (int i = 0; i < expectedTokenSequences.length; i++) {
            if (maxSize < expectedTokenSequences[i].length) {
                maxSize = expectedTokenSequences[i].length;
            }
            for (int j = 0; j < expectedTokenSequences[i].length; j++) {
                expected += tokenImage[expectedTokenSequences[i][j]] + " ";
            }
            if (expectedTokenSequences[i][expectedTokenSequences[i].length - 1] != 0) {
                expected += "...";
            }
            expected += eol + "    ";
        }
        retval = "Encountered \"";
        Token tok = currentToken.next;
        for (int i = 0; i < maxSize; i++) {
            if (i != 0) retval += " ";
            if (tok.kind == 0) {
                retval += tokenImage[0];
                break;
            }
            retval += add_escapes(tok.image);
            tok = tok.next;
        }
        retval += "\" at line " + currentToken.next.beginLine + ", column " + currentToken.next.beginColumn;
        retval += " in " + templateName + "." + eol;
        if (expectedTokenSequences.length == 1) {
            retval += "Was expecting:" + eol + "    ";
        } else {
            retval += "Was expecting one of:" + eol + "    ";
        }
        retval += expected;
        return retval;
    }

    public int getLineNumber() {
        return currentToken != null ? currentToken.next.beginLine : lineNumber;
    }

    public int getColumnNumber() {
        return currentToken != null ? currentToken.next.beginColumn : columnNumber;
    }

    private String customGetMessage() {
        Token nextToken = currentToken.next;
        int kind = nextToken.kind;
        if (kind == EOF) {
            StringBuffer buf = new StringBuffer("Unexpected end of file reached.\n");
            for (int i = 0; i < expectedTokenSequences.length; i++) {
                int[] sequence = expectedTokenSequences[i];
                switch(sequence[0]) {
                    case END_FOREACH:
                        buf.append("Unclosed foreach directive.\n");
                        break;
                    case END_LIST:
                        buf.append("Unclosed list directive.\n");
                        break;
                    case END_SWITCH:
                        buf.append("Unclosed switch directive.\n");
                        break;
                    case END_IF:
                        buf.append("Unclosed if directive.\n");
                        break;
                    case END_COMPRESS:
                        buf.append("Unclosed compress directive.\n");
                        break;
                    case END_MACRO:
                        buf.append("Unclosed macro directive.\n");
                        break;
                    case END_FUNCTION:
                        buf.append("Unclosed function directive.\n");
                        break;
                    case END_TRANSFORM:
                        buf.append("Unclosed transform directive.\n");
                        break;
                    case END_ESCAPE:
                        buf.append("Unclosed escape directive.\n");
                        break;
                    case END_NOESCAPE:
                        buf.append("Unclosed noescape directive.\n");
                        break;
                }
            }
            return buf.toString();
        }
        if (kind == END_IF || kind == ELSE_IF || kind == ELSE) {
            return "Found unexpected directive: " + nextToken + " on line " + nextToken.beginLine + ", column " + nextToken.beginColumn + "\nCheck whether you have a well-formed if-else block.";
        }
        return null;
    }

    /**
   * The end of line string for this machine.
   */
    protected String eol = SecurityUtilities.getSystemProperty("line.separator", "\n");

    /**
   * Used to convert raw characters to their escaped version
   * when these raw version cannot be used as part of an ASCII
   * string literal.
   */
    protected String add_escapes(String str) {
        StringBuffer retval = new StringBuffer();
        char ch;
        for (int i = 0; i < str.length(); i++) {
            switch(str.charAt(i)) {
                case 0:
                    continue;
                case '\b':
                    retval.append("\\b");
                    continue;
                case '\t':
                    retval.append("\\t");
                    continue;
                case '\n':
                    retval.append("\\n");
                    continue;
                case '\f':
                    retval.append("\\f");
                    continue;
                case '\r':
                    retval.append("\\r");
                    continue;
                case '\"':
                    retval.append("\\\"");
                    continue;
                case '\'':
                    retval.append("\\\'");
                    continue;
                case '\\':
                    retval.append("\\\\");
                    continue;
                default:
                    if ((ch = str.charAt(i)) < 0x20 || ch > 0x7e) {
                        String s = "0000" + Integer.toString(ch, 16);
                        retval.append("\\u" + s.substring(s.length() - 4, s.length()));
                    } else {
                        retval.append(ch);
                    }
                    continue;
            }
        }
        return retval.toString();
    }
}
