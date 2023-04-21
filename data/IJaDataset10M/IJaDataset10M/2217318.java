package org.nexopenframework.ide.eclipse.jst.actions;

import java.util.Iterator;
import java.util.Set;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
import org.eclipse.wst.common.project.facet.core.IProjectFacet;
import org.eclipse.wst.common.project.facet.core.IProjectFacetVersion;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;
import org.nexopenframework.ide.eclipse.commons.log.Logger;
import org.nexopenframework.ide.eclipse.jst.datamodel.web.struts2.Struts2FacetInstallConfig;
import org.nexopenframework.ide.eclipse.jst.datamodel.web.struts2.Struts2FacetInstallDelegate;
import org.nexopenframework.ide.eclipse.ui.NexOpenUIActivator;

/**
 * <p>NexOpen Framework</p>
 * 
 * <p>Configuration for Struts2 support</p>
 * 
 * @see IWorkbenchWindowActionDelegate
 * @author Francesc Xavier Magdaleno
 * @version 1.0
 * @since 1.0
 */
public class AddStruts2ActionDelegate implements IWorkbenchWindowActionDelegate {

    /**NexOpen project*/
    private IProject project;

    /***/
    private IWorkbenchWindow window;

    public void dispose() {
    }

    public void init(final IWorkbenchWindow window) {
        this.window = window;
    }

    /**
	 * <p></p>
	 * 
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
    public void run(final IAction action) {
        if (this.project == null) {
            Logger.log(Logger.INFO, "Not project selected in Struts2, just skipping");
            if (this.window != null) {
                MessageDialog.openWarning(this.window.getShell(), "Struts2 Support", "Please, select a project which would like " + "to add Struts 2.0.x capabilities");
            }
            return;
        }
        IFacetedProject fp;
        try {
            fp = ProjectFacetsManager.create(project);
        } catch (CoreException e) {
            throw new RuntimeException(e);
        }
        if (fp == null) {
            return;
        }
        Set facets = fp.getProjectFacets();
        boolean nexopenFacets = false;
        Iterator it_facets = facets.iterator();
        while (it_facets.hasNext()) {
            final IProjectFacetVersion version = (IProjectFacetVersion) it_facets.next();
            final IProjectFacet pf = version.getProjectFacet();
            nexopenFacets = pf.getId().equals("jst.nexopen.ear") || pf.getId().equals("jst.nexopen.web");
            if (nexopenFacets) {
                break;
            }
        }
        if (nexopenFacets) {
            final Struts2FacetInstallDelegate delegate = new Struts2FacetInstallDelegate();
            try {
                if (MessageDialog.openConfirm(this.window.getShell(), "Struts2 Support", "You are going to use the Struts2 capability. Do you want to continue?")) {
                    final Struts2FacetInstallConfig cfg = new Struts2FacetInstallConfig();
                    final NexOpenUIActivator activator = NexOpenUIActivator.getDefault();
                    cfg.setVersion(activator.getStruts2Version());
                    delegate.execute(project, null, cfg, new NullProgressMonitor());
                }
            } catch (final CoreException e) {
                Logger.logException(e);
                ErrorDialog.openError(window.getShell(), "Exception problem", "Problem add struts2 action delegate", e.getStatus());
            }
        } else {
            Logger.log(Logger.INFO, "Not a NexOpen Project, just skipping");
            if (this.window != null) {
                MessageDialog.openWarning(this.window.getShell(), "Struts2 Support", "Not a JEE NexOpen Project");
            }
        }
    }

    public void selectionChanged(final IAction action, final ISelection selection) {
        if (selection instanceof IStructuredSelection) {
            Iterator it = ((IStructuredSelection) selection).iterator();
            while (it.hasNext()) {
                Object item = it.next();
                if (item instanceof IJavaProject) {
                    IJavaProject jp = (IJavaProject) item;
                    this.project = jp.getProject();
                }
            }
        }
    }
}
