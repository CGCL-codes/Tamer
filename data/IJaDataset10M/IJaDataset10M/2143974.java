package org.nomadpim.core.ui.test;

import junit.framework.TestCase;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.nomadpim.core.entity.IEntity;
import org.nomadpim.core.ui.editor.EntityEditorInput;

public abstract class AbstractEntityEditorTest extends TestCase {

    private IWorkbenchPage getPage() {
        return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
    }

    protected IEditorPart openEditor(IEntity spaceObject, String editorID) throws PartInitException {
        return getPage().openEditor(new EntityEditorInput(spaceObject.getKey()), editorID);
    }
}
