package co.edu.unal.ungrid.services.client.applet.subtraction.view;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import co.edu.unal.ungrid.transformation.Transform;

public class ViewUndoableEdit extends AbstractUndoableEdit {

    public static final long serialVersionUID = 1L;

    public ViewUndoableEdit(final UndoableView view, final Transform tUndo, final Transform tRedo) {
        m_view = view;
        m_tUndo = tUndo;
        m_tRedo = tRedo;
    }

    public void undo() throws CannotUndoException {
        m_view.copyTransform(m_tUndo);
    }

    public void redo() throws CannotRedoException {
        m_view.copyTransform(m_tRedo);
    }

    public boolean canUndo() {
        return (m_view != null && m_tUndo != null);
    }

    public boolean canRedo() {
        return (m_view != null && m_tUndo != null);
    }

    public String getPresentationName() {
        return "Transform";
    }

    private UndoableView m_view;

    private Transform m_tUndo;

    private Transform m_tRedo;
}
