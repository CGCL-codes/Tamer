package com.beetop.ui.views.busyws;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableItem;

/**
 * A table  viewer that knows how to match files to their associated editor list
 * elements
 */
public class EditorsTableView extends TableViewer {

    /**
	 * Create a new editor list viewer in the given composite with the given SWT
	 * style
	 * 
	 * @see org.eclipse.jface.viewers.TableViewer#TableViewer(Composite, int)
	 */
    public EditorsTableView(Composite parent, int style) {
        super(parent, style);
    }

    /**
	 * @see org.eclipse.jface.viewers.ContentViewer#handleLabelProviderChanged(org.eclipse.jface.viewers.LabelProviderChangedEvent)
	 */
    protected void handleLabelProviderChanged(LabelProviderChangedEvent event) {
        Object[] elements = event.getElements();
        if (elements == null) {
            refresh();
            return;
        }
        for (int i = 0; i < elements.length; i++) {
            Object element = elements[i];
            if (!(element instanceof IFile)) {
                continue;
            }
            TableItem[] children = getTable().getItems();
            for (int j = 0; j < children.length; j++) {
                TableItem item = children[j];
                Object data = item.getData();
                if (data == null) {
                    continue;
                }
                if (element instanceof IFile && data instanceof IAdaptable && element.equals(((IAdaptable) data).getAdapter(IResource.class))) {
                    refresh(data);
                }
            }
        }
    }
}
