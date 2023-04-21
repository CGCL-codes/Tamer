package gnu.trove;

/**
 * Interface for procedures that take two parameters of type int and Object.
 *
 * Created: Mon Nov  5 22:03:30 2001
 *
 * @author Eric D. Friedman
 * @version $Id: P2OProcedure.template,v 1.1 2006/11/10 23:28:00 robeden Exp $
 */
public interface TIntObjectProcedure<T> {

    /**
     * Executes this procedure. A false return value indicates that
     * the application executing this procedure should not invoke this
     * procedure again.
     *
     * @param a a <code>int</code> value
     * @param b an <code>Object</code> value
     * @return true if additional invocations of the procedure are
     * allowed.
     */
    public boolean execute(int a, T b);
}
