package org.antlride.internal.ui.editor;

import org.eclipse.dltk.internal.ui.editor.SourceModuleEditorActionContributor;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.ide.IDEActionFactory;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.ITextEditorActionConstants;

public class ModuleEditorActionContributor extends SourceModuleEditorActionContributor {

    @Override
    public void setActiveEditor(IEditorPart part) {
        if (part instanceof MultiPageEditorPart) {
            part = (IEditorPart) part.getAdapter(GrammarEditor.class);
        }
        super.setActiveEditor(part);
        IActionBars actionBars = getActionBars();
        if (actionBars != null) {
            ITextEditor editor = (part instanceof ITextEditor) ? (ITextEditor) part : null;
            actionBars.setGlobalActionHandler(ActionFactory.DELETE.getId(), getAction(editor, ITextEditorActionConstants.DELETE));
            actionBars.setGlobalActionHandler(ActionFactory.UNDO.getId(), getAction(editor, ITextEditorActionConstants.UNDO));
            actionBars.setGlobalActionHandler(ActionFactory.REDO.getId(), getAction(editor, ITextEditorActionConstants.REDO));
            actionBars.setGlobalActionHandler(ActionFactory.FIND.getId(), getAction(editor, ITextEditorActionConstants.FIND));
            actionBars.setGlobalActionHandler(IDEActionFactory.BOOKMARK.getId(), getAction(editor, IDEActionFactory.BOOKMARK.getId()));
            actionBars.setGlobalActionHandler(ActionFactory.CUT.getId(), null);
            actionBars.setGlobalActionHandler(ActionFactory.COPY.getId(), null);
            actionBars.setGlobalActionHandler(ActionFactory.PASTE.getId(), null);
            actionBars.setGlobalActionHandler(ActionFactory.SELECT_ALL.getId(), null);
            actionBars.updateActionBars();
        }
    }
}
