package net.sf.jood.ui.panels.download;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

public class DownloadCategoryFilter extends ViewerFilter {

    private String category = DownloadObject.ALL;

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public boolean select(Viewer viewer, Object parentElement, Object element) {
        if (category.equals(DownloadObject.ALL)) return true;
        DownloadObject downloadObject = (DownloadObject) element;
        if (downloadObject.getCategory().equals(this.category)) return true;
        return false;
    }
}
