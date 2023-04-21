package org.vizzini.game.action;

/**
 * Provides an undo action in the game framework. An undo action instance
 * signals the ActionManager to undo the last action.
 *
 * <p>This class participates in the Command pattern as described in Pattern In
 * Java Volume 1, Mark Grand, 1998.</p>
 *
 * @author   Jeffrey M. Thompson
 * @version  v0.4
 * @since    v0.2
 */
public class UndoAction extends DefaultAction implements IUndo {

    /** Serial version UID. */
    private static final long serialVersionUID = 1L;

    /**
     * This implementation of doIt does not actually do anything. The logic for
     * undo is in the ActionManager class.
     *
     * @return  true if successful and can be undone.
     *
     * @since   v0.2
     */
    @Override
    public boolean doIt() {
        throw new NoSuchMethodError();
    }

    /**
     * This implementation of undoIt does not actually do anything. Undo actions
     * are not undone. Instead a redo action is issued.
     *
     * @return  true if the undo was successful.
     *
     * @since   v0.2
     */
    @Override
    public boolean undoIt() {
        throw new NoSuchMethodError();
    }
}
