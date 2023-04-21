package org.nexopenframework.ide.eclipse.services.views;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.ViewPart;
import org.nexopenframework.ide.eclipse.commons.log.Logger;
import org.nexopenframework.ide.eclipse.services.model.CompilationUnitHolder;

/**
 * <p>NexOpen Framework</p>
 * 
 * <p>Implementation of the {@link IDoubleClickListener} for opening
 * the java files related to service components to the Java editor</p>
 * 
 * @see IDoubleClickListener
 * @author Francesc Xavier Magdaleno
 * @version 1.0
 * @since 1.0
 */
final class ServiceComponentsListener implements IDoubleClickListener {

    /**The Service Explorer view*/
    private ViewPart view;

    ServiceComponentsListener(ViewPart view) {
        this.view = view;
    }

    /**
	 * <p></p>
	 * 
	 * @see org.eclipse.jface.viewers.IDoubleClickListener#doubleClick(org.eclipse.jface.viewers.DoubleClickEvent)
	 */
    public void doubleClick(DoubleClickEvent event) {
        IStructuredSelection selection = (IStructuredSelection) event.getSelection();
        final Object obj = selection.getFirstElement();
        if (obj instanceof CompilationUnitHolder) {
            final ICompilationUnit cu = ((CompilationUnitHolder) obj).cu;
            final IProject prj = cu.getJavaProject().getProject();
            IPath ipath = cu.getPath();
            String path = ipath.toString();
            String prjName = prj.getName();
            if (path.indexOf(prjName) > -1) {
                path = path.substring(path.indexOf(prjName) + prjName.length());
            }
            IFile file = prj.getFile(path);
            try {
                IDE.openEditor(this.view.getSite().getPage(), file);
            } catch (PartInitException e) {
                Logger.logException(e);
            }
        }
    }
}
