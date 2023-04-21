package org.plazmaforge.studio.dbmanager.editors;

import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.part.EditorActionBarContributor;

public class SQLEditorActionBarContributor extends EditorActionBarContributor {

    private CursorPositionContrib _cursorPosition;

    public SQLEditorActionBarContributor() {
        super();
        _cursorPosition = new CursorPositionContrib();
    }

    @Override
    public void contributeToStatusLine(IStatusLineManager statusLineManager) {
        super.contributeToStatusLine(statusLineManager);
        statusLineManager.add(_cursorPosition);
    }

    @Override
    public void setActiveEditor(IEditorPart targetEditor) {
        super.setActiveEditor(targetEditor);
        ((SQLEditor) targetEditor).updateCursorPosition();
    }
}
