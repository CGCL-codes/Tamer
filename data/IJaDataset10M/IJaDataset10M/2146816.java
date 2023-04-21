package org.liris.schemerger.ui.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.liris.schemerger.ui.editors.RequestEditor;
import org.liris.schemerger.ui.editors.RequestEditorInput;

public class NewRequestHandler extends AbstractHandler implements IHandler {

    public Object execute(ExecutionEvent event) throws ExecutionException {
        IEditorInput input = new RequestEditorInput();
        try {
            IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(input, RequestEditor.ID);
            ((RequestEditor) editor).setDirty(true);
        } catch (PartInitException e) {
            e.printStackTrace();
        }
        return null;
    }
}
