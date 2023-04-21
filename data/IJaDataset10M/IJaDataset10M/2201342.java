package net.sourceforge.jruntimedesigner.undo;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import net.sourceforge.jruntimedesigner.common.IWidgetHolder;
import net.sourceforge.jruntimedesigner.selection.WidgetSelectionManager;

@SuppressWarnings("serial")
public class SetGuideWidgetUndoableEdit extends AbstractUndoableEdit {

    private UndoRedoProgress progress;

    private WidgetSelectionManager selectionManager;

    private IWidgetHolder widget;

    public SetGuideWidgetUndoableEdit(UndoRedoProgress progress, WidgetSelectionManager selectionManager, IWidgetHolder widget) {
        this.progress = progress;
        this.selectionManager = selectionManager;
        this.widget = widget;
    }

    public void undo() throws CannotUndoException {
        progress.start();
        try {
            super.undo();
            selectionManager.unsetGuide(widget);
        } finally {
            progress.stop();
        }
    }

    public void redo() throws CannotRedoException {
        progress.start();
        try {
            super.redo();
            selectionManager.setGuide(widget);
        } finally {
            progress.stop();
        }
    }
}
