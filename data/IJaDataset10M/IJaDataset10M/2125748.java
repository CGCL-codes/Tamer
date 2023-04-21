package org.mariella.sample.app.person;

import org.eclipse.ui.IWorkbenchWindow;
import org.mariella.rcp.resources.AbstractVResourceAction;
import org.mariella.rcp.resources.VResourcesPlugin;

public class OpenPersonEditorAction extends AbstractVResourceAction {

    public static final String ID = OpenPersonEditorAction.class.getName();

    public OpenPersonEditorAction(IWorkbenchWindow window) {
        super(window, "Edit Person");
        setId(ID);
    }

    @Override
    public void run() {
        PersonResourceManager rm = VResourcesPlugin.getResourceManagerRegistry().getResourceManager(PersonResourceManager.class);
        rm.openEditor(getWindow(), getSelectedRefHolder().getRef());
    }

    @Override
    protected boolean calculateEnabled() {
        return getSelectedRefHolder() instanceof PersonResourceRefHolder;
    }
}
