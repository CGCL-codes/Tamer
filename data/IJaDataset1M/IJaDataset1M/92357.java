package pl.wcislo.sbql4j.tools.javac.util;

/** Throwing an instance of this class causes immediate termination
 *  of the main compiler method.  It is used when some non-recoverable
 *  error has been detected in the compiler environment at runtime.
 *
 *  <p><b>This is NOT part of any API supported by Sun Microsystems.  If
 *  you write code that depends on this, you do so at your own risk.
 *  This code and its internal interfaces are subject to change or
 *  deletion without notice.</b>
 */
public class FatalError extends Error {

    private static final long serialVersionUID = 0;

    /** Construct a <code>FatalError</code> with no detail message.
     */
    public FatalError() {
        super();
    }

    /** Construct a <code>FatalError</code> with the specified detail message.
     *  @param d A diagnostic containing the reason for failure.
     */
    public FatalError(JCDiagnostic d) {
        super(d.toString());
    }

    /** Construct a <code>FatalError</code> with the specified detail message.
     *  @param s An English(!) string describing the failure, typically because
     *           the diagnostic resources are missing.
     */
    public FatalError(String s) {
        super(s);
    }
}
