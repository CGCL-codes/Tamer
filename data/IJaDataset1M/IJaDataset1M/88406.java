package q_impress.pmi.lib.decorators;

import q_impress.pmi.lib.PmiException;

/**
 * An exception modeling errors concerning invalid contexts.
 * @author Mauro Luigi Drago
 *
 */
public class InvalidContextException extends PmiException {

    private static final long serialVersionUID = 4526783831610089848L;

    public InvalidContextException() {
    }

    public InvalidContextException(String arg0) {
        super(arg0);
    }

    public InvalidContextException(Throwable arg0) {
        super(arg0);
    }

    public InvalidContextException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }
}
