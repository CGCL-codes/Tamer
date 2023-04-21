package org.icepdf.ri.common;

import org.icepdf.core.Memento;
import org.icepdf.core.util.Defs;
import java.util.ArrayList;

/**
 * Undo caretaker implementation for the Viewer RI.  Currently only annotation
 * can be manipulate but this class can easily handle any class that implements
 * the Memento interfce.
 *
 * @since 4.0
 */
public class UndoCaretaker {

    private static int maxHistorySize;

    static {
        maxHistorySize = Defs.sysPropertyInt("org.icepdf.ri.viewer.undo.size", 25);
    }

    private ArrayList<Memento> mementoStateHistory;

    private int cursor;

    public UndoCaretaker() {
        mementoStateHistory = new ArrayList<Memento>(maxHistorySize);
        cursor = 0;
    }

    /**
     * Undo the last state change.  Only possible if there are items in the
     * undo history list.
     */
    public void undo() {
        if (isUndo()) {
            cursor = cursor - 1;
            Memento tmp = mementoStateHistory.get(cursor);
            tmp.restore();
        }
    }

    /**
     * Gets the status of the undo command.
     *
     * @return true if an undo command is possible, false if undo can not be done.
     */
    public boolean isUndo() {
        return mementoStateHistory.size() > 0 && cursor > 0;
    }

    /**
     * Redo the last state change.  ONly possible if there have been previous
     * undo call.
     */
    public void redo() {
        if (isRedo()) {
            cursor = cursor + 1;
            Memento tmp = mementoStateHistory.get(cursor);
            tmp.restore();
        }
    }

    /**
     * Gets the status of the redo command.
     *
     * @return true if an redo command is possible, false if the redo can not be done.
     */
    public boolean isRedo() {
        return cursor + 1 < mementoStateHistory.size();
    }

    /**
     * Adds the give states to the history list.
     *
     * @param previousState previous state
     * @param newState new state. 
     */
    public void addState(Memento previousState, Memento newState) {
        if (cursor >= maxHistorySize) {
            mementoStateHistory.remove(0);
            mementoStateHistory.remove(1);
            cursor = mementoStateHistory.size() - 1;
        }
        if (isRedo()) {
            for (int i = cursor + 1, max = mementoStateHistory.size(); i < max; i++) {
                mementoStateHistory.remove(cursor + 1);
            }
        }
        if (mementoStateHistory.size() == 0) {
            mementoStateHistory.add(previousState);
            mementoStateHistory.add(newState);
            cursor = 1;
        } else {
            mementoStateHistory.set(cursor, previousState);
            mementoStateHistory.add(newState);
            cursor++;
        }
    }
}
