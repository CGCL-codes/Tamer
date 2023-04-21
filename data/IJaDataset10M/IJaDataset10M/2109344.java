package javax.swing.text;

import java.util.Vector;
import javax.swing.undo.AbstractUndoableEdit;

abstract class AbstractContentUndoableEdit extends AbstractUndoableEdit {

    private static final String addition = "addition";

    private static final String deletion = "deletion";

    protected final boolean inserted;

    protected final int len;

    protected final int pos;

    protected String text;

    private Vector undoPos;

    public AbstractContentUndoableEdit(final int where, final String chars, final boolean isInsertCommand) throws BadLocationException {
        pos = where;
        text = chars;
        len = text.length();
        inserted = isInsertCommand;
        if (!inserted) {
            undoPos = getPositionsInRange(null, pos, len);
        } else {
            undoPos = new Vector();
        }
    }

    public void die() {
        super.die();
        text = null;
        undoPos = null;
    }

    public String getPresentationName() {
        return inserted ? addition : deletion;
    }

    public void redo() {
        super.redo();
        if (inserted) {
            insertText();
        } else {
            removeText();
        }
    }

    public void undo() {
        super.undo();
        if (inserted) {
            removeText();
        } else {
            insertText();
        }
    }

    protected abstract Vector getPositionsInRange(final Vector positions, final int where, final int length);

    protected abstract void insertItems(final int where, final String chars);

    protected abstract void removeItems(final int where, final int length);

    protected abstract void updateUndoPositions(final Vector undoPositions);

    private void insertText() {
        insertItems(pos, text);
        updateUndoPositions(undoPos);
        undoPos.clear();
    }

    private void removeText() {
        getPositionsInRange(undoPos, pos, len);
        removeItems(pos, len);
    }
}
