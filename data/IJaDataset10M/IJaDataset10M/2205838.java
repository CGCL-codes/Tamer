package nl.utwente.ewi.stream.pasnql.helpers;

/**
 * A WindowExpression is the left- or righthand side of a window interval. A window expression has a type and a type2. The type is
 * always TIME_BASED or TUPLE_BASED, and the type2 is RELATIVE (NOW, MAX_ID) or ABSOLUTE.
 *
 * @author rein
 */
public class WindowExpression {

    private WindowPredicate.Type type;

    private Type type2;

    private String op;

    private Object value;

    /**
     * Specifies the type2 a window expression can have
     */
    public enum Type {

        RELATIVE, ABSOLUTE
    }

    /**
     * Gets the operator, if any.
     * @return the operator, or null.
     */
    public String getOp() {
        return op;
    }

    /**
     * Sets the operator
     * @param op - the operator
     */
    public void setOp(String op) {
        this.op = op;
    }

    /**
     * Returns the value associated by this expression. This value can be a number or a string.
     *
     * @return the value
     */
    public Object getValue() {
        return value;
    }

    /**
     * Sets the value of this expression
     * @param value - the value
     */
    public void setValue(Object value) {
        this.value = value;
    }

    public WindowPredicate.Type getType() {
        return type;
    }

    public void setType(WindowPredicate.Type type) {
        this.type = type;
    }

    public Type getType2() {
        return type2;
    }

    public void setType2(Type type2) {
        this.type2 = type2;
    }
}
