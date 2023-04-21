package org.emftext.language.office.resource.office.ui;

public class OfficeOutlinePageTreeViewer extends org.eclipse.jface.viewers.TreeViewer {

    boolean suppressNotifications = false;

    public OfficeOutlinePageTreeViewer(org.eclipse.swt.widgets.Composite parent, int style) {
        super(parent, style);
    }

    public void setSelection(org.eclipse.jface.viewers.ISelection selection, boolean reveal) {
        if (selection instanceof org.emftext.language.office.resource.office.ui.OfficeEObjectSelection) {
            suppressNotifications = true;
            super.setSelection(selection, reveal);
            suppressNotifications = false;
        }
    }

    public void refresh(Object element, boolean updateLabels) {
        super.refresh(element, updateLabels);
        expandToLevel(getAutoExpandLevel());
    }

    public void refresh(Object element) {
        super.refresh(element);
        expandToLevel(getAutoExpandLevel());
    }

    public void refresh() {
        super.refresh();
        expandToLevel(getAutoExpandLevel());
    }

    public void refresh(boolean updateLabels) {
        super.refresh(updateLabels);
        expandToLevel(getAutoExpandLevel());
    }

    protected void fireSelectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent event) {
        if (suppressNotifications == true) return;
        super.fireSelectionChanged(event);
    }
}
