package org.mariella.rcp.databinding;

import org.eclipse.jface.viewers.AbstractListViewer;
import org.mariella.rcp.databinding.internal.InternalBindingContext;
import org.mariella.rcp.databinding.internal.ListViewerController;
import org.mariella.rcp.databinding.internal.VListViewerObservableList;

public class ListViewerLabelDecoratorExtension implements VBindingDomainExtension {

    String propertyPath;

    ListViewerLabelDecoratorCallback labelDecoratorCallback;

    public ListViewerLabelDecoratorExtension(String propertyPath, ListViewerLabelDecoratorCallback labelDecoratorCallback) {
        this.propertyPath = propertyPath;
        this.labelDecoratorCallback = labelDecoratorCallback;
    }

    public void install(VBinding binding) {
        ListViewerController controller = ((InternalBindingContext) binding.getBindingContext()).getMainContext().listViewerControllerMap.get(getListViewer(binding));
        controller.install(this, binding);
    }

    private AbstractListViewer getListViewer(VBinding binding) {
        return ((VListViewerObservableList) binding.getBinding().getTarget()).getListViewer();
    }

    public String getPropertyPath() {
        return propertyPath;
    }

    public ListViewerLabelDecoratorCallback getLabelDecoratorCallback() {
        return labelDecoratorCallback;
    }
}
