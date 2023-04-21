package koala.dynamicjava.tree;

/**
 * This class represents the field access nodes of the syntax tree
 *
 * @author  Stephane Hillion
 * @version 1.0 - 1999/04/24
 */
public abstract class FieldAccess extends PrimaryExpression implements LeftHandSide {

    /**
     * The body property name
     */
    public static final String FIELD_NAME = "fieldName";

    /**
     * The field name
     */
    private String fieldName;

    /**
     * Creates a new field access node
     * @param fln   the field name
     * @param fn    the filename
     * @param bl    the begin line
     * @param bc    the begin column
     * @param el    the end line
     * @param ec    the end column
     * @exception IllegalArgumentException if fln is null
     */
    protected FieldAccess(String fln, String fn, int bl, int bc, int el, int ec) {
        super(fn, bl, bc, el, ec);
        if (fln == null) throw new IllegalArgumentException("fln == null");
        fieldName = fln;
    }

    /**
     * Returns the field name
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * Sets the field name
     */
    public void setFieldName(String s) {
        if (s == null) throw new IllegalArgumentException("s == null");
        firePropertyChange(FIELD_NAME, fieldName, fieldName = s);
    }
}
