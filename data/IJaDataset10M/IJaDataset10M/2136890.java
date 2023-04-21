package net.sourceforge.coffea.tools;

/** Exception caused by tools which are not prepare for code processing */
public class UnpreparedToolsException extends Exception {

    /** @see java.io.Serializable */
    private static final long serialVersionUID = -2544599898672943145L;

    /** Exception construction */
    public UnpreparedToolsException() {
        super();
    }

    /**
	 * Construction
	 * @param msg
	 *            Message describing the exception
	 */
    public UnpreparedToolsException(String msg) {
        super(msg);
    }

    @Override
    public String getMessage() {
        return ("The tools needed for code processing have not been properly " + "prepared. " + super.getMessage());
    }
}
