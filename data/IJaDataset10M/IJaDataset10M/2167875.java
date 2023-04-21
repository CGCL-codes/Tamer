package co.edu.unal.ungrid.services.client.applet.document.undo;

import javax.swing.undo.AbstractUndoableEdit;
import co.edu.unal.ungrid.services.client.applet.document.AbstractDocument;

public abstract class UndoableDocumentEdit extends AbstractUndoableEdit {

    public static final long serialVersionUID = 1L;

    public UndoableDocumentEdit(final AbstractDocument doc) {
        assert doc != null;
        m_doc = doc;
    }

    protected AbstractDocument m_doc;
}
