package tefkat.plugin.wip.explorer;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

public class HideNonTefkatProjects extends ViewerFilter {

    public boolean select(Viewer viewer, Object parentElement, Object element) {
        return true;
    }
}
