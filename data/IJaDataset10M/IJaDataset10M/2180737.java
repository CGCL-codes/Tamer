package gnu.trove.procedure;

/**
 * Interface for procedures that take two parameters of type long and char.
 */
public interface TLongCharProcedure {

    /**
     * Executes this procedure. A false return value indicates that
     * the application executing this procedure should not invoke this
     * procedure again.
     *
     * @param a a <code>long</code> value
     * @param b a <code>char</code> value
     * @return true if additional invocations of the procedure are
     * allowed.
     */
    public boolean execute(long a, char b);
}
