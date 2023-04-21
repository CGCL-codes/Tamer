package org.tzi.use.util.cmd;

/** 
 * Signals that an undo for an operation was attempted that cannot be
 * undone.
 *
 * @version     $ProjectVersion: 0.393 $
 * @author      Mark Richters 
 * @see         Command
 */
public class CannotUndoException extends Exception {

    public CannotUndoException() {
        super();
    }

    public CannotUndoException(String s) {
        super(s);
    }
}
