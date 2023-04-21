package org.easyb.launch.viewerfilters;

import org.easyb.launch.EasybLaunchActivator;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

public class ContainerViewerFilter extends ViewerFilter {

    @Override
    public boolean select(Viewer viewer, Object parentElement, Object element) {
        if (element instanceof IJavaProject) {
            return true;
        }
        try {
            if (element instanceof IPackageFragmentRoot) {
                IPackageFragmentRoot root = (IPackageFragmentRoot) element;
                if (root.isArchive() || !root.hasChildren()) {
                    return false;
                }
                return true;
            }
            if (element instanceof IPackageFragment && ((IPackageFragment) element).hasChildren()) {
                return true;
            }
        } catch (JavaModelException e) {
            EasybLaunchActivator.Log("Unable to filter package for story", e);
            return false;
        }
        return false;
    }
}
