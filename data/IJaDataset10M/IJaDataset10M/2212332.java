package org.easyb.launch;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.internal.ui.packageview.PackageFragmentRootContainer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class EasybLaunchActivator extends AbstractUIPlugin {

    public static final String PLUGIN_ID = "org.easyb.launch.ui";

    private static EasybLaunchActivator plugin;

    /**
	 * The constructor
	 */
    public EasybLaunchActivator() {
    }

    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
    }

    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }

    /**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
    public static EasybLaunchActivator getDefault() {
        return plugin;
    }

    /**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
    public static ImageDescriptor getImageDescriptor(String path) {
        return imageDescriptorFromPlugin(PLUGIN_ID, path);
    }

    public static void Log(String message, Exception ex) {
        getDefault().getLog().log(new Status(IStatus.ERROR, PLUGIN_ID, 0, message, ex));
    }

    public static void Log(String message) {
        getDefault().getLog().log(new Status(IStatus.ERROR, PLUGIN_ID, message));
    }

    public static void Log(IStatus status) {
        getDefault().getLog().log(status);
    }

    public static IPreferenceStore getLaunchPreferenceStore() {
        return getDefault().getPreferenceStore();
    }

    public IProject getSelectedProject() {
        ISelectionService selectionService = getWorkbench().getActiveWorkbenchWindow().getSelectionService();
        ISelection selection = selectionService.getSelection();
        if (selection instanceof IStructuredSelection) {
            Object element = ((IStructuredSelection) selection).getFirstElement();
            IProject project = null;
            if (element instanceof IResource) {
                project = ((IResource) element).getProject();
            } else if (element instanceof PackageFragmentRootContainer) {
                IJavaProject jProject = ((PackageFragmentRootContainer) element).getJavaProject();
                project = jProject.getProject();
            } else if (element instanceof IJavaElement) {
                IJavaProject jProject = ((IJavaElement) element).getJavaProject();
                project = jProject.getProject();
            }
            return project;
        }
        return null;
    }
}
