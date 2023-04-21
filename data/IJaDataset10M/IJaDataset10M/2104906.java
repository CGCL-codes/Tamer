package net.sf.jncu.protocol.v2_0.query;

/**
 * Disposes the cursor and returns a <tt>kDRes</tt> with a {@code 0} or error.
 * 
 * <pre>
 * 'cfre'
 * length
 * cursor id
 * </pre>
 * 
 * @author moshew
 */
public class DCursorFree extends DCursor {

    /** <tt>kDCursorFree</tt> */
    public static final String COMMAND = "cfre";

    /**
	 * Creates a new command.
	 */
    public DCursorFree() {
        super(COMMAND);
    }
}
