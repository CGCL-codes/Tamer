package org.jmlspecs.models;

public class JMLMapException extends IllegalArgumentException {

    /** The value associated with this exception.
     */
    public final JMLType val_;

    /** Initialize this object with the given detail string.
     */
    public JMLMapException() {
        super("finite map error");
        val_ = null;
    }

    /** Initialize this object with the given detail string.
     */
    public JMLMapException(String m) {
        super(m);
        val_ = null;
    }

    /** Initialize this object with the given detail string.
     */
    public JMLMapException(String m, JMLType errval) {
        super(m);
        val_ = errval;
    }
}
