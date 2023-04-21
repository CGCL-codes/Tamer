package com.jswiff.swfrecords.actions;

/**
 * <p>
 * Ends the drag operation in progress, if any.
 * </p>
 * 
 * <p>
 * Performed stack operations: none
 * </p>
 * 
 * <p>
 * ActionScript equivalents: <code>stopDrag()</code>,
 * <code>MovieClip.stopDrag()</code>
 * </p>
 *
 * @since SWF 5
 */
public final class EndDrag extends Action {

    /**
   * Creates a new EndDrag action.
   */
    public EndDrag() {
        code = ActionConstants.END_DRAG;
    }

    /**
   * Returns a short description of this action.
   *
   * @return <code>"EndDrag"</code>
   */
    public String toString() {
        return "EndDrag";
    }
}
