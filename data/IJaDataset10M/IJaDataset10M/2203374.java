package nz.org.venice.prefs;

/**
 * A representation of an expression that can be referenced by name. A stored expression
 * is saved in the Preferences data so that the user does not have to re-type the
 * expression.
 */
public class StoredExpression {

    /** Name of the stored expression. */
    public String name;

    /** The stored expression. */
    public String expression;

    /**
     * Create a new stored expression.
     *
     * @param name the name of the expression.
     * @param expression the expression to store.
     */
    public StoredExpression(String name, String expression) {
        this.name = name;
        this.expression = expression;
    }
}
