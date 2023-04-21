package DE.FhG.IGD.semoa.shell;

/**
 *
 *
 * @author Roger Hartmann
 * @version "$Id: ASTStringStatement.java 668 2002-07-01 12:53:04Z jpeters $"
 */
public class ASTStringStatement extends SimpleNode {

    protected String text;

    protected int type = TYPE_NORMAL;

    public static String[] TYPES = { "normal", "quoted", "single quoted" };

    public static int TYPE_NORMAL = 0;

    public static int TYPE_QUOTED = 1;

    public static int TYPE_S_QUOTED = 2;

    private boolean seperated_;

    public ASTStringStatement(int id) {
        super(id);
    }

    public String toString() {
        return ShellParserTreeConstants.jjtNodeName[id] + " \"" + text + "\"";
    }

    public Object eval(ShellParser p) {
        StringBuffer strbuf;
        int n;
        int i;
        if (type == TYPE_QUOTED) {
            strbuf = new StringBuffer(text.substring(text.indexOf("\"") + 1, text.lastIndexOf("\"")));
            return strbuf.toString();
        } else if (type == TYPE_S_QUOTED) {
            strbuf = new StringBuffer(text.substring(text.indexOf("'") + 1, text.lastIndexOf("'")));
            return strbuf.toString();
        } else {
            return trim(true);
        }
    }

    public String prettyPrint() {
        StringBuffer strbuf;
        if (type == TYPE_QUOTED) {
            strbuf = new StringBuffer(text.substring(text.indexOf("\""), text.lastIndexOf("\"") + 1));
            return strbuf.toString();
        } else if (type == TYPE_S_QUOTED) {
            strbuf = new StringBuffer(text.substring(text.indexOf("'"), text.lastIndexOf("'") + 1));
            return strbuf.toString();
        } else {
            return trim(false);
        }
    }

    public boolean seperated() {
        StringBuffer strbuf;
        if (type == TYPE_QUOTED || type == TYPE_S_QUOTED) {
            strbuf = new StringBuffer(text);
            if (space_.indexOf(strbuf.charAt(text.length() - 1)) != -1) {
                seperated_ = true;
            } else {
                seperated_ = false;
            }
        } else {
            trim(false);
        }
        return seperated_;
    }

    /**
     * Trims the string represented by this class, sets the class 
     * variable <code>seperated_</code> according to its structure and 
     * returns the trimmed string without modifying the class variable
     * <code>text</code>:
     * <p>
     * <ul>
     * <li> If <code>evaluate</code> is <code>true</code> escaped 
     *   characters are replaced first (<code>\&lt;CHAR&gt;</code> 
     *   is transformed to <code>&lt;CHAR&gt;</code>).
     * <li> If the string then ends with a space <code>seperated_</code>
     *   is set to <code>true</code>.
     * <li> Leading and ending spaces are removed, afterwards.
     * </ul>
     * <p> 
     * Example 1 (evaluate=true): <code>[ a b\\c\ ]</code> will be 
     * transformed to <code>[a b\c ]</code> and <code>seperated_</code> 
     * will be set to <code>false</code>.
     * <p>
     * Example 2 (evaluate=false): <code>[ d e\\f\  ]</code> will be 
     * transformed to <code>[d e\\f\ ]</code> and <code>seperated_</code>
     * will be set to <code>true</code>.
     *
     * @param evaluate Flag to control escape character replacement.
     * @return The trimmed string.
     */
    private String trim(boolean evaluate) {
        StringBuffer strbuf;
        boolean leadingSpaces;
        String result;
        int end;
        int n;
        int i;
        strbuf = new StringBuffer(text);
        n = text.length();
        end = -1;
        leadingSpaces = true;
        for (i = 0; i < n; i++) {
            if (leadingSpaces) {
                if (space_.indexOf(strbuf.charAt(i)) != -1) {
                    strbuf.deleteCharAt(i);
                    n--;
                    continue;
                } else {
                    leadingSpaces = false;
                }
            }
            if (space_.indexOf(strbuf.charAt(i)) == -1) {
                end = i;
            }
            if (strbuf.charAt(i) == '\\') {
                if (evaluate) {
                    strbuf.deleteCharAt(i);
                    n--;
                } else {
                    if (end != -1) {
                        end++;
                    }
                    i++;
                }
            }
        }
        end++;
        result = strbuf.toString();
        if (end >= 0 && end < n) {
            result = result.substring(0, end);
            seperated_ = true;
        } else {
            seperated_ = false;
        }
        return result;
    }
}
