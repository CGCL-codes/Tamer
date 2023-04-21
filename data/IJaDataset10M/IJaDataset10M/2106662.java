package seco.langs.python;

import java.util.Comparator;
import java.util.List;
import javax.swing.text.Document;
import org.python.antlr.PythonTree;
import org.python.antlr.ast.Attribute;
import org.python.antlr.ast.Name;

/**
 *
 * @author Tor Norbye
 */
public class PythonUtils {

    public static final String DOT__INIT__ = ".__init__";

    public static final String SOURCES_TYPE_PYTHON = "python";

    private static String prevRootUrl;

    static final String[] PYTHON_KEYWORDS = new String[] { "and", "as", "assert", "break", "class", "continue", "def", "del", "elif", "else", "except", "exec", "finally", "for", "from", "global", "if", "import", "in", "is", "lambda", "not", "or", "pass", "print", "raise", "return", "try", "while", "with", "yield" };

    public static boolean isPythonKeyword(String name) {
        for (String s : PYTHON_KEYWORDS) {
            if (s.equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Return true iff the name is a class name
     * @param name The name
     * @param emptyDefault Whether empty or _ names should be considered a class name or not
     * @return True iff the name looks like a class name
     */
    public static boolean isClassName(String name, boolean emptyDefault) {
        if (name == null || name.length() == 0) {
            return emptyDefault;
        }
        if (name.startsWith("_") && name.length() > 1) {
            return Character.isUpperCase(name.charAt(1));
        }
        return Character.isUpperCase(name.charAt(0));
    }

    /**
     * Return true iff the name is a method name
     * @param name The name
     * @param emptyDefault Whether empty or _ names should be considered a class name or not
     * @return True iff the name looks like a method name
     */
    public static boolean isMethodName(String name, boolean emptyDefault) {
        if (name == null || name.length() == 0) {
            return emptyDefault;
        }
        if (name.startsWith("__") && name.length() > 2) {
            return Character.isLowerCase(name.charAt(2));
        }
        if (name.startsWith("_") && name.length() > 1) {
            return Character.isLowerCase(name.charAt(1));
        }
        return Character.isLowerCase(name.charAt(0));
    }

    public static boolean isValidPythonClassName(String name) {
        if (isPythonKeyword(name)) {
            return false;
        }
        if (name.trim().length() == 0) {
            return false;
        }
        if (!Character.isUpperCase(name.charAt(0))) {
            return false;
        }
        for (int i = 1; i < name.length(); i++) {
            char c = name.charAt(i);
            if (!Character.isJavaIdentifierPart(c)) {
                return false;
            }
        }
        return true;
    }

    /** Is this name a valid operator name? */
    public static boolean isOperator(String name) {
        if (name.length() == 0) {
            return false;
        }
        switch(name.charAt(0)) {
            case '+':
                return name.equals("+") || name.equals("+@");
            case '-':
                return name.equals("-") || name.equals("-@");
            case '*':
                return name.equals("*") || name.equals("**");
            case '<':
                return name.equals("<") || name.equals("<<") || name.equals("<=") || name.equals("<=>");
            case '>':
                return name.equals(">") || name.equals(">>") || name.equals(">=");
            case '=':
                return name.equals("=") || name.equals("==") || name.equals("===") || name.equals("=~");
            case '!':
                return name.equals("!=") || name.equals("!~");
            case '&':
                return name.equals("&") || name.equals("&&");
            case '|':
                return name.equals("|") || name.equals("||");
            case '[':
                return name.equals("[]") || name.equals("[]=");
            case '%':
                return name.equals("%");
            case '/':
                return name.equals("/");
            case '~':
                return name.equals("~");
            case '^':
                return name.equals("^");
            case '`':
                return name.equals("`");
            default:
                return false;
        }
    }

    public static boolean isValidPythonMethodName(String name) {
        if (isPythonKeyword(name)) {
            return false;
        }
        if (name.trim().length() == 0) {
            return false;
        }
        if (isOperator(name)) {
            return true;
        }
        if (Character.isUpperCase(name.charAt(0)) || Character.isWhitespace(name.charAt(0))) {
            return false;
        }
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (!(Character.isLetterOrDigit(c) || c == '_')) {
                return false;
            }
        }
        return true;
    }

    public static boolean isValidPythonIdentifier(String name) {
        if (isPythonKeyword(name)) {
            return false;
        }
        if (name.trim().length() == 0) {
            return false;
        }
        for (int i = 0; i < name.length(); i++) {
            if (Character.isWhitespace(name.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Ruby identifiers should consist of [a-zA-Z0-9_]
     * http://www.headius.com/rubyspec/index.php/Variables
     * <p>
     * This method also accepts the field/global chars
     * since it's unlikely
     */
    public static boolean isSafeIdentifierName(String name, int fromIndex) {
        int i = fromIndex;
        for (; i < name.length(); i++) {
            char c = name.charAt(i);
            if (!(c == '$' || c == '@' || c == ':')) {
                break;
            }
        }
        for (; i < name.length(); i++) {
            char c = name.charAt(i);
            if (!((c >= 'a' && c <= 'z') || (c == '_') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9') || (c == '?') || (c == '=') || (c == '!'))) {
                if (isOperator(name)) {
                    return true;
                }
                return false;
            }
        }
        return true;
    }

    /** @todo Move into GsfUtilities after 6.5 */
    public static int getOffsetByLineCol(String source, int line, int col) {
        int offset = 0;
        for (int i = 0; i < line; i++) {
            offset = source.indexOf('\n', offset);
            if (offset == -1) {
                offset = source.length();
                break;
            }
            offset++;
        }
        if (col > 0) {
            offset += col;
        }
        return offset;
    }

    public static Comparator NAME_NODE_COMPARATOR = new Comparator<Name>() {

        public int compare(Name n1, Name n2) {
            return n1.getInternalId().compareTo(n2.getInternalId());
        }
    };

    public static Comparator ATTRIBUTE_NAME_NODE_COMPARATOR = new Comparator<Object>() {

        @SuppressWarnings("unchecked")
        public int compare(Object n1, Object n2) {
            String s1 = "";
            String s2 = "";
            if (n1 instanceof Name) {
                s1 = ((Name) n1).getInternalId();
            } else if (n1 instanceof Attribute) {
                Attribute a = (Attribute) n1;
                String v = PythonAstUtils.getName(a.getInternalValue());
                if (v != null) {
                    s1 = a.getInternalAttr() + "." + v;
                } else {
                    s1 = a.getInternalAttr();
                }
            }
            if (n2 instanceof Name) {
                s2 = ((Name) n2).getInternalId();
            } else if (n2 instanceof Attribute) {
                Attribute a = (Attribute) n2;
                String v = PythonAstUtils.getName(a.getInternalValue());
                if (v != null) {
                    s2 = a.getInternalAttr() + "." + v;
                } else {
                    s2 = a.getInternalAttr();
                }
            }
            return s1.compareTo(s2);
        }
    };

    public static Comparator NODE_POS_COMPARATOR = new Comparator<PythonTree>() {

        public int compare(PythonTree p1, PythonTree p2) {
            int ret = p1.getCharStartIndex() - p2.getCharStartIndex();
            if (ret != 0) {
                return ret;
            }
            ret = p2.getCharStopIndex() - p1.getCharStopIndex();
            if (ret != 0) {
                return ret;
            }
            return p2.getAntlrType() - p1.getAntlrType();
        }
    };
}
