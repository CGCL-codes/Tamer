package org.servingMathematics.mqat.util;

import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;
import org.servingMathematics.mqat.io.MqatDocumentWrapper;

/**
 * This class is aim to perform undo and redo operations on DOM document
 *
 * @author <a href="mailto:j.kahovec@imperial.ac.uk">Jakub Kahovec</a>
 * @version 0.1
 */
public class DOMUndoManager extends UndoManager {

    MqatDocumentWrapper mqatDocument;

    public DOMUndoManager(MqatDocumentWrapper mqatDocument) {
        super();
        this.mqatDocument = mqatDocument;
    }

    public void undo() {
        UndoableEdit undoEdit = editToBeUndone();
        if (undoEdit == null) {
            return;
        }
        if (undoEdit instanceof DOMUndoableEdit) {
            DOMUndoableEdit domUndoEdit = (DOMUndoableEdit) undoEdit;
            switch(domUndoEdit.getAction()) {
                case DOMUndoableEdit.INSERT_ELEMENT:
                    DOMHelper.removeNode(domUndoEdit.getNode().getOwnerDocument(), domUndoEdit.getLocation());
                    break;
                case DOMUndoableEdit.REMOVE_ELEMENT:
                    DOMHelper.insertNode(domUndoEdit.getNode(), domUndoEdit.getLocation());
                    break;
                case DOMUndoableEdit.ADD_ATTRIBUTE:
                case DOMUndoableEdit.APPEND_CHILD:
                case DOMUndoableEdit.REMOVE_ATTRIBUTE:
                case DOMUndoableEdit.CHANGE_NODE:
                    DOMHelper.replaceNodeAt(domUndoEdit.getOldNode(), domUndoEdit.getLocation());
                    break;
            }
        }
        super.undo();
    }

    public void redo() {
        UndoableEdit redoEdit = editToBeRedone();
        if (redoEdit == null) {
            return;
        }
        if (redoEdit instanceof DOMUndoableEdit) {
            DOMUndoableEdit domRedoEdit = (DOMUndoableEdit) redoEdit;
            switch(domRedoEdit.getAction()) {
                case DOMUndoableEdit.INSERT_ELEMENT:
                    DOMHelper.insertNode(domRedoEdit.getNode(), domRedoEdit.getLocation());
                    break;
                case DOMUndoableEdit.REMOVE_ELEMENT:
                    DOMHelper.removeNode(domRedoEdit.getNode().getOwnerDocument(), domRedoEdit.getLocation());
                    break;
                case DOMUndoableEdit.APPEND_CHILD:
                case DOMUndoableEdit.ADD_ATTRIBUTE:
                case DOMUndoableEdit.REMOVE_ATTRIBUTE:
                case DOMUndoableEdit.CHANGE_NODE:
                    DOMHelper.replaceNodeAt(domRedoEdit.getNode(), domRedoEdit.getLocation());
                    break;
            }
        }
        super.redo();
    }
}
