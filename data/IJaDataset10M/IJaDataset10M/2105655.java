package org.gzigzag;

/** Tried to move a cursor to a place that can't have cursors on it.
 * @deprecated Cursors can be anywhere, now.
 */
public class ZZInvalidCursorPositionError extends ZZError {

    public static final String rcsid = "$Id: ZZInvalidCursorPositionError.java,v 1.4 2000/10/26 18:09:29 tjl Exp $";

    public ZZInvalidCursorPositionError(String s) {
        super(s);
    }
}
