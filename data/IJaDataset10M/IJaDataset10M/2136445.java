package koala.dynamicjava.tree;

/**
 * This class represents the integer literal nodes of the syntax tree
 *
 * @author  Stephane Hillion
 * @version 1.0 - 1999/04/24
 */
public class IntegerLiteral extends Literal {

    /**
     * Initializes a literal
     * @param rep the representation of the literal
     */
    public IntegerLiteral(String rep) {
        this(rep, null, 0, 0, 0, 0);
    }

    /**
     * Initializes a literal
     * @param rep the representation of the literal
     * @param fn  the filename
     * @param bl  the begin line
     * @param bc  the begin column
     * @param el  the end line
     * @param ec  the end column
     */
    public IntegerLiteral(String rep, String fn, int bl, int bc, int el, int ec) {
        super(rep, parse(rep), int.class, fn, bl, bc, el, ec);
    }

    /**
     * Parses the representation of an integer
     */
    private static Integer parse(String s) {
        if (s.startsWith("0x")) {
            return parseHexadecimal(s.substring(2, s.length()));
        } else if (s.startsWith("0")) {
            return parseOctal(s);
        } else {
            return Integer.valueOf(s);
        }
    }

    /**
     * Parses an hexadecimal number
     */
    private static Integer parseHexadecimal(String s) {
        int value = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = Character.toLowerCase(s.charAt(i));
            if ((value >>> 28) != 0) {
                throw new NumberFormatException(s);
            }
            value = (value << 4) + c + ((c >= 'a' && c <= 'f') ? 10 - 'a' : -'0');
        }
        return new Integer(value);
    }

    /**
     * Parses an octal number
     */
    private static Integer parseOctal(String s) {
        int value = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if ((value >>> 29) != 0) {
                throw new NumberFormatException(s);
            }
            value = (value << 3) + c - '0';
        }
        return new Integer(value);
    }
}
