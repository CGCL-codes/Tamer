package jp.gr.java_conf.ykhr.eclipse.plugin.warninger;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.IWorkbenchWindow;

public class ResourceChecker implements IStartup, IResourceChangeListener {

    public void resourceChanged(IResourceChangeEvent event) {
        IResourceDelta delta = event.getDelta();
        if (delta != null) {
            final IWorkbenchWindow window = getWorkbenchWindow();
            try {
                delta.accept(new IResourceDeltaVisitor() {

                    public boolean visit(IResourceDelta delta) throws CoreException {
                        IResource resource = delta.getResource();
                        if (resource instanceof IFile) {
                            IFile file = (IFile) resource;
                            if (isTarge(file, window)) {
                                if (check(file)) {
                                    alert(file, window);
                                }
                            }
                            return false;
                        }
                        return true;
                    }
                });
            } catch (CoreException e) {
                Activator.logError(e);
            }
        }
    }

    private IWorkbenchWindow getWorkbenchWindow() {
        WindowRunnable runnable = new WindowRunnable();
        Display.getDefault().syncExec(runnable);
        return runnable.getWorkbenchWindow();
    }

    private boolean check(IFile file) throws CoreException {
        IMarker[] markers = file.findMarkers(null, true, IResource.DEPTH_INFINITE);
        for (IMarker marker : markers) {
            if (marker.getAttribute(IMarker.SEVERITY, 0) >= IMarker.SEVERITY_WARNING) {
                return true;
            }
        }
        return false;
    }

    private void alert(IFile file, IWorkbenchWindow window) {
        Display.getDefault().asyncExec(new Runnable() {

            public void run() {
                MessageDialog.openError(Activator.getShell(), "error", "error");
            }
        });
    }

    private boolean isTarge(IFile file, IWorkbenchWindow window) throws CoreException {
        IEditorPart activeEditor = window.getActivePage().getActiveEditor();
        if (activeEditor == null) {
            return false;
        }
        IResource res = (IResource) activeEditor.getEditorInput().getAdapter(IResource.class);
        IProject project = file.getProject();
        return project.hasNature(WarningerNature.NATURE_ID) && "java".equalsIgnoreCase(file.getFileExtension()) && file.equals(res);
    }

    public void earlyStartup() {
        ResourcesPlugin.getWorkspace().addResourceChangeListener(this, IResourceChangeEvent.POST_BUILD);
    }

    private static class WindowRunnable implements Runnable {

        private IWorkbenchWindow window = null;

        public void run() {
            window = Activator.getActiveWorkbenchWindow();
        }

        public IWorkbenchWindow getWorkbenchWindow() {
            return window;
        }
    }
}
