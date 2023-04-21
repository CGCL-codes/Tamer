package net.sf.zip.internal;

import java.io.File;
import net.sf.zip.internal.model.IArchive;
import net.sf.zip.internal.model.IArchiveContainer;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorRegistry;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

public class ShowEntryAction implements IObjectActionDelegate {

    private ISelection selection;

    private IWorkbenchPart targetPart;

    public void setActivePart(IAction action, IWorkbenchPart targetPart) {
        this.targetPart = targetPart;
    }

    public void run(IAction action) {
        if (!(selection instanceof IStructuredSelection)) return;
        Object[] sel = ((IStructuredSelection) selection).toArray();
        for (int i = 0; i < sel.length; i++) {
            Object object = sel[i];
            if (object instanceof IArchiveContainer) continue;
            if (object instanceof IArchive) {
                try {
                    String path = ((IArchive) object).create(ZipPlugin.getRootFolder());
                    File file = new File(path);
                    targetPart.getSite().getPage().openEditor(new ExternalFileEditorInput(file), IEditorRegistry.SYSTEM_EXTERNAL_EDITOR_ID);
                    file.deleteOnExit();
                } catch (CoreException e) {
                    ZipPlugin.logError(e);
                }
            }
        }
    }

    public void selectionChanged(IAction action, ISelection selection) {
        this.selection = selection;
    }
}
