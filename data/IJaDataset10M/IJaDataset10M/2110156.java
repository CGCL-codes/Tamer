package com.wgo.precise.client.ui.view.editor.history;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;
import com.wgo.bpot.domain.common.CommandHistory;
import com.wgo.precise.client.ui.controller.ModelStatus;
import com.wgo.precise.client.ui.controller.RequirementPlugin;

public class HistoryTableLabelProvider implements ITableLabelProvider {

    public void addListener(ILabelProviderListener listener) {
    }

    public void dispose() {
    }

    public boolean isLabelProperty(Object element, String property) {
        if (element instanceof CommandHistory) {
            return true;
        }
        return false;
    }

    public void removeListener(ILabelProviderListener listener) {
    }

    public Image getColumnImage(Object element, int columnIndex) {
        return null;
    }

    public String getColumnText(Object element, int columnIndex) {
        if (element instanceof CommandHistory) {
            return HistoryColumnProperties.values()[columnIndex].getValue((CommandHistory) element);
        }
        RequirementPlugin.log(new ModelStatus(IStatus.WARNING, "Not a valid table row: " + element.getClass()));
        return null;
    }
}
