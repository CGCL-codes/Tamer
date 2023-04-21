package org.argouml.kernel;

import java.beans.PropertyChangeListener;

/**
 * Stores Commands that have been executed and allows them to be undone
 * and redone. Commands represent single operations on the ArgoUML model.
 * A single user interaction may generate several Commands. Undo/redo
 * works an a user interaction and so can undo/redo several commands in one
 * call.
 * 
 * @author Bob Tarling
 */
public interface UndoManager {

    /**
     * Adds a new command to the undo stack.
     * @param command the command.
     */
    public abstract void addCommand(Command command);

    /**
     * Execute a command and add it to the undo stack.
     * 
     * @param command the command.
     * @return any resulting object the command creates
     */
    public abstract Object execute(Command command);

    /**
     * Set the maximum number of interactions the stack can hold.
     * @param max the maximum chain count
     */
    public abstract void setUndoMax(int max);

    /**
     * Undo the top user interaction on the undo stack and move
     * it to the redo stack.
     */
    public abstract void undo();

    /**
     * Redo the top user interaction on the redo stack and move
     * it to the undo stack.
     */
    public abstract void redo();

    /**
     * Instructs the UndoManager that a new user interaction is about to take
     * place. All commands received until the next call to startInteraction will
     * form a single undoable unit.
     * 
     * @param label the label for this interaction to build the undo/redo label
     */
    public abstract void startInteraction(String label);

    /**
     * Add a new PropertyChangeListener for undo/redo events. Allow a listener
     * to detect when the undo or redo stack changes availability. No guarantees
     * are made about which thread the event will be delivered on, so any
     * specific thread requirements (e.g. Swing/AWT thread requirements) must be
     * dealt with by the
     * {@link PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)}
     * method.
     * 
     * @param listener a PropertyChangeListener
     */
    public abstract void addPropertyChangeListener(PropertyChangeListener listener);

    /**
     * Remove the given listener.
     * @param listener a PropertyChangeListener
     */
    public abstract void removePropertyChangeListener(PropertyChangeListener listener);
}
