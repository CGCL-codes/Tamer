package org.deft.operation.tempfile2ecorediag;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorActionBarContributor;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IKeyBindingService;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

@SuppressWarnings("deprecation")
public class DummyWorkbenchPartSite implements IWorkbenchPartSite, IEditorSite {

    @Override
    public String getId() {
        return null;
    }

    @Override
    public IKeyBindingService getKeyBindingService() {
        return null;
    }

    @Override
    public IWorkbenchPart getPart() {
        return null;
    }

    @Override
    public String getPluginId() {
        return null;
    }

    @Override
    public String getRegisteredName() {
        return null;
    }

    @Override
    public void registerContextMenu(MenuManager menuManager, ISelectionProvider selectionProvider) {
    }

    @Override
    public void registerContextMenu(String menuId, MenuManager menuManager, ISelectionProvider selectionProvider) {
    }

    @Override
    public IWorkbenchPage getPage() {
        return null;
    }

    @Override
    public ISelectionProvider getSelectionProvider() {
        return null;
    }

    private Shell shell_getShell;

    @Override
    public Shell getShell() {
        Display.getDefault().syncExec(new Runnable() {

            @Override
            public void run() {
                shell_getShell = Display.getDefault().getActiveShell();
            }
        });
        return shell_getShell;
    }

    /** this field is only accessed by synchronized method getWorkbenchWindow */
    private IWorkbenchWindow workbenchWindow_getWorkbenchWindow;

    @Override
    public synchronized IWorkbenchWindow getWorkbenchWindow() {
        Display display = Display.getDefault();
        display.syncExec(new Runnable() {

            public void run() {
                workbenchWindow_getWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
            }
        });
        return workbenchWindow_getWorkbenchWindow;
    }

    @Override
    public void setSelectionProvider(ISelectionProvider provider) {
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Object getAdapter(Class adapter) {
        return null;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Object getService(Class api) {
        return null;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean hasService(Class api) {
        return false;
    }

    @Override
    public IEditorActionBarContributor getActionBarContributor() {
        return null;
    }

    @Override
    public IActionBars getActionBars() {
        return null;
    }

    @Override
    public void registerContextMenu(MenuManager menuManager, ISelectionProvider selectionProvider, boolean includeEditorInput) {
    }

    @Override
    public void registerContextMenu(String menuId, MenuManager menuManager, ISelectionProvider selectionProvider, boolean includeEditorInput) {
    }
}
