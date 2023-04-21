package org.maven.ide.eclipse.ext.actions;

import java.util.Iterator;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.maven.ide.eclipse.ext.Maven2Plugin;
import org.nexopenframework.ide.eclipse.commons.log.Logger;

/**
 * <p>NexOpen Framework</p>
 * 
 * <p>Downloads the source related to a given artifact of <code>jar</code> type</p>
 * 
 * @see org.maven.ide.eclipse.ext.Maven2Plugin#downloadSources(IProject, org.eclipse.core.runtime.IPath)
 * @see org.eclipse.ui.IObjectActionDelegate
 * @author Francesc Xavier Magdaleno
 * @version 1.0
 * @since 1.0
 */
public class DownloadSourcesAction implements IObjectActionDelegate {

    /**Eclipse {@link IStructuredSelection}*/
    private IStructuredSelection selection;

    public void setActivePart(final IAction action, final IWorkbenchPart targetPart) {
    }

    /**
	 * <p>Selects a jar artifact into the Maven2 container and dowloads the sources if available</p>
	 * 
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
    public void run(final IAction action) {
        for (final Iterator it = selection.iterator(); it.hasNext(); ) {
            Object element = it.next();
            try {
                if (element instanceof IProject) {
                    final IProject project = (IProject) element;
                    if (project.isAccessible() && project.hasNature(Maven2Plugin.NATURE_ID)) {
                        Maven2Plugin.downloadSources(project, null);
                    }
                } else if (element instanceof IPackageFragmentRoot) {
                    final IPackageFragmentRoot fragment = (IPackageFragmentRoot) element;
                    final IProject project = fragment.getJavaProject().getProject();
                    if (project.isAccessible() && fragment.isArchive()) {
                        Maven2Plugin.downloadSources(project, fragment.getPath());
                    }
                }
            } catch (final CoreException e) {
                Maven2Plugin.getDefault().getConsole().logError("Core Exception donwloading resources :: " + e);
                Logger.getLog().error("Java Model Exception", e);
            }
        }
    }

    public void selectionChanged(final IAction action, final ISelection selection) {
        if (selection instanceof IStructuredSelection) {
            this.selection = (IStructuredSelection) selection;
        }
    }
}
