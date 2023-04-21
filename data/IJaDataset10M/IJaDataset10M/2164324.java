package org.eclipse.mylyn.internal.tasks.ui.search;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.mylyn.tasks.core.AbstractTask;

/**
 * Sorts search results (AbstractQueryHit) by taskId.
 */
public class SearchResultSorterId extends ViewerSorter {

    /**
	 * Returns a negative, zero, or positive number depending on whether the first bug's taskId is less than, equal to,
	 * or greater than the second bug's taskId.
	 * <p>
	 * 
	 * @see org.eclipse.jface.viewers.ViewerSorter#compare(org.eclipse.jface.viewers.Viewer, java.lang.Object,
	 *      java.lang.Object)
	 */
    @Override
    public int compare(Viewer viewer, Object e1, Object e2) {
        try {
            AbstractTask entry1 = (AbstractTask) e1;
            Integer id1 = Integer.parseInt(entry1.getTaskId());
            AbstractTask entry2 = (AbstractTask) e2;
            Integer id2 = Integer.parseInt(entry2.getTaskId());
            if (id1 != null && id2 != null) {
                return id1.compareTo(id2);
            }
        } catch (Exception ignored) {
        }
        return super.compare(viewer, e1, e2);
    }

    /**
	 * Returns the category of the given element. The category is a number used to allocate elements to bins; the bins
	 * are arranged in ascending numeric order. The elements within a bin are arranged via a second level sort
	 * criterion.
	 * <p>
	 * 
	 * @see org.eclipse.jface.viewers.ViewerSorter#category(Object)
	 */
    @Override
    public int category(Object element) {
        try {
            AbstractTask hit = (AbstractTask) element;
            return Integer.parseInt(hit.getTaskId());
        } catch (Exception ignored) {
        }
        return super.category(element);
    }
}
