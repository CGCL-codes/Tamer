package org.argouml.kernel;

/**
 * The base class for any commands that can be undone by ActionUndo.
 * @author Bob Tarling
 */
public abstract class NonUndoableCommand implements Command {

    public abstract Object execute();

    public void undo() {
    }

    public boolean isUndoable() {
        return false;
    }

    public boolean isRedoable() {
        return false;
    }
}
