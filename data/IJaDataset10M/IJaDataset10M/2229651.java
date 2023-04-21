package net.sf.jncu.protocol.v2_0.query;

/**
 * Moves the cursor to the next entry in the set of entries referenced by the
 * cursor and returns the entry. Returns nil if the cursor is moved past the
 * last entry.
 * 
 * <pre>
 * 'next'
 * length
 * cursor id
 * </pre>
 * 
 * @author moshew
 */
public class DCursorNext extends DCursor {

    /** <tt>kDCursorNext</tt> */
    public static final String COMMAND = "next";

    /**
	 * Creates a new command.
	 */
    public DCursorNext() {
        super(COMMAND);
    }
}
