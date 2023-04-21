package com.ivis.xprocess.ui.views.sorters;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import com.ivis.xprocess.core.Category;
import com.ivis.xprocess.ui.datawrappers.IElementWrapper;
import com.ivis.xprocess.ui.datawrappers.process.CategoryWrapper;

public class CategoryViewerSorter extends ViewerSorter {

    @Override
    public int compare(Viewer viewer, Object e1, Object e2) {
        if (!(e1 instanceof IElementWrapper) || !(e2 instanceof IElementWrapper)) {
            return super.compare(viewer, e1, e2);
        }
        IElementWrapper a1 = (IElementWrapper) e1;
        IElementWrapper a2 = (IElementWrapper) e2;
        if (a1 instanceof CategoryWrapper && a2 instanceof CategoryWrapper) {
            Category categoryA1 = (Category) ((CategoryWrapper) a1).getElement();
            Category categoryA2 = (Category) ((CategoryWrapper) a2).getElement();
            if (categoryA1.getUuid().equals(categoryA1.getCategoryType().getUncategorizedCategory().getUuid())) {
                return 1;
            }
            if (categoryA2.getUuid().equals(categoryA2.getCategoryType().getUncategorizedCategory().getUuid())) {
                return 0;
            }
            return a1.getLabel().compareTo(a2.getLabel());
        }
        return 1;
    }
}
