package org.mariella.rcp.problems;

import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IElementFactory;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.mariella.rcp.resources.ResourceOpenedCallback;

public abstract class AbstractEditorProblemResourceOpenHandler implements ProblemResourceOpenHandler {

    @Override
    public void openResource(IWorkbenchWindow window, ProblemResource problemResource, ResourceOpenedCallback cb) {
        EditorProblemResource res = (EditorProblemResource) problemResource;
        String elementFactoryId = res.getElementFactoryId();
        IMemento editorMemento = res.getEditorMemento();
        IElementFactory factory = PlatformUI.getWorkbench().getElementFactory(elementFactoryId);
        IEditorInput input = (IEditorInput) factory.createElement(editorMemento);
        openEditor(window, input, res.getEditorId(), cb);
    }

    protected abstract void openEditor(IWorkbenchWindow window, IEditorInput input, String editorId, ResourceOpenedCallback cb);
}
