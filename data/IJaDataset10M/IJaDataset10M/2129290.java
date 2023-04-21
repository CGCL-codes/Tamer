package org.openscience.jchempaint.controller.undoredo;

/**
 * This interface is implemented by all the actual edit events.
 * 
 * @cdk.module control
 */
public interface IUndoRedoable {

    public void redo();

    public void undo();

    public boolean canRedo();

    public boolean canUndo();
}
