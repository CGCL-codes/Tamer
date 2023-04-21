package gnu.trove.procedure;

/**
 * Interface for procedures that take two parameters of type Object and double.
 */
public interface TObjectDoubleProcedure<K> {

    /**
     * Executes this procedure. A false return value indicates that
     * the application executing this procedure should not invoke this
     * procedure again.
     *
     * @param a an <code>Object</code> value
     * @param b a <code>double</code> value
     * @return true if additional invocations of the procedure are
     * allowed.
     */
    public boolean execute(K a, double b);
}
