package net.sf.egrep.actions;

import net.sf.egrep.ui.GrepDialog;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;

/**
 * GerpActionDelegate
 * 
 * @author wesley
 */
public class GrepActionDelegate implements IEditorActionDelegate {

    private ITextEditor m_editor;

    public void setActiveEditor(IAction action, IEditorPart targetEditor) {
        if (targetEditor instanceof ITextEditor || targetEditor == null) {
            m_editor = (ITextEditor) targetEditor;
        } else {
            m_editor = null;
        }
    }

    public void run(IAction action) {
        new GrepDialog(m_editor.getSite().getShell()).open();
        final IDocumentProvider dp = ((ITextEditor) m_editor).getDocumentProvider();
        final IDocument doc = dp.getDocument(m_editor.getEditorInput());
        try {
            String text = doc.get(0, doc.getLength());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public void selectionChanged(IAction action, ISelection selection) {
    }
}
