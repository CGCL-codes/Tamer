package gap.jac.util;

/** Throwing an instance of
 *  this class causes (silent) termination of the main compiler method.
 *
 *  <p><b>This is NOT part of any API supported by Sun Microsystems.  If
 *  you write code that depends on this, you do so at your own risk.
 *  This code and its internal interfaces are subject to change or
 *  deletion without notice.</b>
 */
public class Abort extends Error {

    private static final long serialVersionUID = 0;

    public Abort(Throwable cause) {
        super(cause);
    }

    public Abort() {
        super();
    }
}
