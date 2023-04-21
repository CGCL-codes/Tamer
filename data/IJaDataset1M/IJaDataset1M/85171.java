package com.ivis.xprocess.ui.view.providers;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;
import com.ivis.xprocess.core.Pattern;

public class PatternViewerLabelProvider implements ITableLabelProvider {

    public Image getColumnImage(Object element, int columnIndex) {
        return null;
    }

    public String getColumnText(Object element, int columnIndex) {
        if (element instanceof Pattern) {
            Pattern pattern = (Pattern) element;
            if (columnIndex == 0) {
                return pattern.getContainedIn().getLabel();
            }
            if (columnIndex == 1) {
                return pattern.getLabel();
            }
        }
        return "";
    }

    public void addListener(ILabelProviderListener listener) {
    }

    public void dispose() {
    }

    public boolean isLabelProperty(Object element, String property) {
        return false;
    }

    public void removeListener(ILabelProviderListener listener) {
    }
}
