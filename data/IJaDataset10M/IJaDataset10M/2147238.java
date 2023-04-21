package ostf.gui.undo;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;
import org.w3c.dom.Document;
import ostf.gui.frame.AdminConsole;

public class XmlUndoManager extends UndoManager {

    private static final long serialVersionUID = -5602069538528923028L;

    private static XmlUndoManager manager = null;

    public static XmlUndoManager getInstance() {
        if (manager == null) manager = new XmlUndoManager();
        return manager;
    }

    public boolean addEdit(UndoableEdit anEdit) {
        boolean result = super.addEdit(anEdit);
        AdminConsole.getInstance().updateEditMenu();
        return result;
    }

    public void undo() throws CannotUndoException {
        super.undo();
        AdminConsole.getInstance().updateEditMenu();
    }

    public void redo() throws CannotRedoException {
        super.redo();
        AdminConsole.getInstance().updateEditMenu();
    }

    /**
     * Send die to each subedit, in the reverse of the order that
     * they were added. Then remove these edits from the UndoManager.
     * @param document a document
     */
    public void dieEditIfMatch(Document document) {
        int size = edits.size();
        for (int i = size - 1; i >= 0; i--) {
            if (edits.elementAt(i) instanceof XmlUndoableEdit) {
                XmlUndoableEdit e = (XmlUndoableEdit) edits.elementAt(i);
                if (e.dieIfMatch(document)) {
                    this.trimEdits(i, i);
                }
            }
        }
        AdminConsole.getInstance().updateEditMenu();
    }
}
