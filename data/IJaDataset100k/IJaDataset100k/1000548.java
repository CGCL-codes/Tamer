package koala.dynamicjava.tree;

/**
 * This class represents the boolean type nodes of the syntax tree
 *
 * @author  Stephane Hillion
 * @version 1.0 - 1999/04/24
 */
public class BooleanType extends PrimitiveType {

    /**
     * Initializes the type
     */
    public BooleanType() {
        this(null, 0, 0, 0, 0);
    }

    /**
     * Initializes the type
     * @param fn    the filename
     * @param bl    the begin line
     * @param bc    the begin column
     * @param el    the end line
     * @param ec    the end column
     */
    public BooleanType(String fn, int bl, int bc, int el, int ec) {
        super(boolean.class, fn, bl, bc, el, ec);
    }
}
