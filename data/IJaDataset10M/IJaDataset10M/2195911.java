package de.chdev.artools.sql.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import de.chdev.artools.sql.editor.SQLEditor;

/**
 * @author Christoph Heinig
 *
 */
public class ResultCutHandler extends AbstractHandler {

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        IEditorPart activeEditor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        if (activeEditor instanceof SQLEditor) {
            SQLEditor sqlEditor = (SQLEditor) activeEditor;
            TextCellEditor cellEditor = sqlEditor.getTextCellEditor();
            if (cellEditor != null && cellEditor.isActivated() && cellEditor.isCutEnabled()) {
                cellEditor.performCut();
            }
        }
        return null;
    }
}
