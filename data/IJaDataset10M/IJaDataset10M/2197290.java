package org.xaware.designer.datatools.connectivity.ui;

import org.eclipse.datatools.connectivity.internal.ui.wizards.NewCPWizardCategoryFilter;
import org.eclipse.jface.viewers.Viewer;
import org.xaware.designer.datatools.connectivity.XACategory;

/**
 * Filter to filter only the providers which are related(selected).
 * 
 * @author Vasu Thadaka
 * 
 */
public class ViewerFilter extends NewCPWizardCategoryFilter {

    /** Default Constructor. */
    public ViewerFilter() {
        super(null);
    }

    /** Viewer instance. */
    public Viewer viewer;

    /** Selected Category */
    public XACategory selectedCategory;

    @Override
    public boolean select(Viewer viewer, Object parentElement, Object element) {
        this.viewer = viewer;
        if (selectedCategory != null) {
            setCategoryID(selectedCategory.getCategoryID());
            return super.select(viewer, parentElement, element);
        }
        return false;
    }

    /** Sets the category selected depending up on that selection will go on. */
    public void setSelectedCategory(XACategory selectedCategory) {
        this.selectedCategory = selectedCategory;
    }

    /** Gets the previously used viewer */
    public Viewer getViewer() {
        return viewer;
    }
}
