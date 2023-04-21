package de.glossmaker.undo;

/**
 * 
 * @author Markus Flingelli
 *
 * @param <E>
 */
public interface IUndoableStack<E> {

    int undoSize();

    int redoSize();

    E popUndo();

    E popRedo();

    void pushUndo(E item);

    void pushRedo(E item);

    void resetUndo();

    void resetRedo();

    void reset();
}
