package gnu.trove.procedure;

/**
 * Interface for procedures with one int parameter.
 */
public interface TIntProcedure {

    /**
     * Executes this procedure. A false return value indicates that
     * the application executing this procedure should not invoke this
     * procedure again.
     *
     * @param value a value of type <code>int</code>
     * @return true if additional invocations of the procedure are
     * allowed.
     */
    public boolean execute(int value);
}
