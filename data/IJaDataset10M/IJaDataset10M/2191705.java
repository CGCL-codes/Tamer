package jacol;

/**
 * This interface describes lisp interpreters, whether synchronous or asynchronous.
 *
 * @author Jason Lowdermilk <a href="mailto:lowdermilk@users.sourceforge.net">Email</a>
 * @version 0.20 beta
 */
public interface LispInterpreter {

    /**
     * Evaluate some lisp code. This may block depending on whether or not the interpreter is synchronous.
     *
     * @param lispCode a String representing a lisp statement.
     * @return if the lisp interpreter is synchronous, a String will be returned containing the response from
     *         the lisp server. Otherwise null will be returned.
     */
    public String eval(String lispCode);

    /**
     * Instruct JACOL to exit. This should be the last thing called from the java program. If it is omitted,
     * JACOL will continue running since there are other threads. If System.exit() is called directly, the
     * lisp server will continue running as an orphaned process. 
     */
    public void exit();
}
