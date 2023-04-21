package com.richclientgui.toolbox.samples.duallist;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * @author Carien van Zyl
 */
public class CricketerLabelProvider implements ITableLabelProvider {

    public Image getColumnImage(Object element, int columnIndex) {
        return null;
    }

    public String getColumnText(Object element, int columnIndex) {
        if (!(element instanceof Cricketer)) return "";
        final Cricketer cricketer = (Cricketer) element;
        switch(columnIndex) {
            case CricketerConstants.COL_NAME:
                return cricketer.getName();
            case CricketerConstants.COL_SURNAME:
                return cricketer.getSurname();
            case CricketerConstants.COL_TEAM:
                return cricketer.getTeam().getName();
            default:
                return "";
        }
    }

    public boolean isLabelProperty(Object element, String property) {
        return false;
    }

    public void addListener(ILabelProviderListener listener) {
    }

    public void dispose() {
    }

    public void removeListener(ILabelProviderListener listener) {
    }
}
