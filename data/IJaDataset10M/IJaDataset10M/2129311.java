package matrix.view;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.undo.*;

class JvUndoManager extends UndoManager {

    protected Action undoAction;

    protected Action redoAction;

    public JvUndoManager() {
        this.undoAction = new JvUndoAction(this);
        this.redoAction = new JvRedoAction(this);
        synchronizeActions();
    }

    public Action getUndoAction() {
        return undoAction;
    }

    public Action getRedoAction() {
        return redoAction;
    }

    @Override
    public boolean addEdit(UndoableEdit anEdit) {
        try {
            return super.addEdit(anEdit);
        } finally {
            synchronizeActions();
        }
    }

    @Override
    protected void undoTo(UndoableEdit edit) throws CannotUndoException {
        try {
            super.undoTo(edit);
        } finally {
            synchronizeActions();
        }
    }

    @Override
    protected void redoTo(UndoableEdit edit) throws CannotRedoException {
        try {
            super.redoTo(edit);
        } finally {
            synchronizeActions();
        }
    }

    protected void synchronizeActions() {
        undoAction.setEnabled(canUndo());
        undoAction.putValue(Action.NAME, getUndoPresentationName());
        redoAction.setEnabled(canRedo());
        redoAction.putValue(Action.NAME, getRedoPresentationName());
    }
}
