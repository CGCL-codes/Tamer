package org.vexi.vexidev.navigator;

import org.eclipse.core.resources.IContainer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.vexi.vexidev.navigator.elements.IVexiResource;
import org.vexi.vexidev.navigator.elements.VexiPackage;

public class VexiModelSorter extends ViewerSorter {

    @SuppressWarnings("unchecked")
    public int compare(Viewer viewer, Object e1, Object e2) {
        if (e1 instanceof IVexiResource && e2 instanceof IVexiResource) {
            IVexiResource resource1 = (IVexiResource) e1;
            IVexiResource resource2 = (IVexiResource) e2;
            int r1 = resource1.getRank();
            int r2 = resource2.getRank();
            if (r1 == IVexiResource.RANK_PACKAGE) {
                assert (r1 == r2);
                VexiPackage p1 = (VexiPackage) resource1;
                VexiPackage p2 = (VexiPackage) resource2;
                p1.getName().compareTo(p2.getName());
            } else if (r1 == r2) {
                return super.compare(viewer, resource1.getActualObject(), resource2.getActualObject());
            } else if (r1 < r2) {
                return -1;
            } else {
                return 1;
            }
        }
        if (e1 instanceof IVexiResource) {
            return -1;
        }
        if (e2 instanceof IVexiResource) {
            return 1;
        }
        if (e1 instanceof IContainer && e2 instanceof IContainer) {
            return super.compare(viewer, e1, e2);
        }
        if (e1 instanceof IContainer) {
            return -1;
        }
        if (e2 instanceof IContainer) {
            return 1;
        }
        return super.compare(viewer, e1, e2);
    }
}
